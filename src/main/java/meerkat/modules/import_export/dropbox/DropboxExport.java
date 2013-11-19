package meerkat.modules.import_export.dropbox;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxWriteMode;
import meerkat.modules.gui.IDialog;
import meerkat.modules.gui.IDialogBuilder;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.gui.ILineEditValidator;
import meerkat.modules.import_export.IExportImplementation;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.*;

/**
 * Export do Dropboxa
 *
 * @author
 */
public class DropboxExport implements IExportImplementation {

    class CodeValidator implements ILineEditValidator {
        @Override
        public boolean validate(String label, String value) {
            if ((client = DropboxClient.authorize(value)) == null)
                return false;
            return true;
        }
    }

    private DbxClient client = null;
    private Channel inputChannel = null;
    private String targetPath = null;

    @Override
    public <T extends ReadableByteChannel & InterruptibleChannel> void setInputChannel(
            T channel) {
        this.inputChannel = channel;
    }

    private IDialog buildUrlDialog(IDialogBuilderFactory dialogBuilderFactory,
                                   String url) {
        // metoda tworzy okienko do wpisania kodu
        // w celu zalogowania się na Dropboxa
        IDialogBuilder idb = dialogBuilderFactory.newDialogBuilder();
        idb.addLabel("Open this site: ").addLabel(url)
                .addLineEdit("Enter a code: ", new CodeValidator());
        return idb.build();
    }

    private IDialog buildDirectoryDialog(
            IDialogBuilderFactory dialogBuilderFactory) {
        // metoda tworzy okienko do wybrania pliku
        IDialogBuilder idb = dialogBuilderFactory.newDialogBuilder();
        idb.addDirectoryChooser("Choose a directory: ");
        return idb.build();
    }

    @Override
    public boolean prepare(IDialogBuilderFactory<?> dialogBuilderFactory) {
        String response = DropboxClient.connect();
        while (response != DropboxClient.CONNECTED) {
            IDialog idb = buildUrlDialog(dialogBuilderFactory, response);
            idb.exec();
            response = DropboxClient.connect();
        }

        IDialog idb = buildDirectoryDialog(dialogBuilderFactory);
        if (idb.exec()) {
            targetPath = idb.getLineEditValue("Choose a directory: ");
            if (!targetPath.startsWith("/")) // sciezka musi sie zaczynac od /
                targetPath = "/" + targetPath;
            return true;
        }
        return false;
    }

    @Override
    public void run() throws Exception {

        if (targetPath == null || inputChannel == null)
            throw new NullPointerException();

        InputStream inputStream = null;

        try {
            inputStream = Channels
                    .newInputStream((ReadableByteChannel) inputChannel);
            ByteBuffer buffer = ByteBuffer.allocate(32768);

            int bytesRead = ((ReadableByteChannel) inputChannel).read(buffer);
            long uploadOffset = bytesRead, offset;
            String uploadId = null;

            while (bytesRead != -1) {
                buffer.flip();
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);
                if (uploadId == null) {
                    uploadId = client.chunkedUploadFirst(data);
                } else {
                    offset = client.chunkedUploadAppend(uploadId, uploadOffset,
                            data);
                    while (offset != -1L) { // w offset jest prawidlowy offset,
                        // jesli -1 to OK
                        uploadOffset = offset;
                        offset = client.chunkedUploadAppend(uploadId,
                                uploadOffset, buffer.array());
                    }
                }
                buffer.clear();
                bytesRead = ((ReadableByteChannel) inputChannel).read(buffer);
                uploadOffset += bytesRead;
            }

            client.chunkedUploadFinish(targetPath, DbxWriteMode.add(), uploadId);
        } catch (ClosedChannelException e) {
            throw e;
        } finally {
            inputStream.close();
        }
    }
}
