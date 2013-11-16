package meerkat.modules.import_export.dropbox;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.ReadableByteChannel;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxWriteMode;

import meerkat.modules.gui.IDialog;
import meerkat.modules.gui.IDialogBuilder;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.import_export.IExportImplementation;

/**
 * Export do Dropboxa
 * 
 * @author
 */
public class DropboxExport implements IExportImplementation {

	private DbxClient client = null; // TODO
	private Channel inputChannel = null;
	private String targetPath = null;

	private boolean removeFile(String path) {
		File f = new File(path);
		boolean success = true;
		if (f.exists())
			success = f.delete();
		return success;
	}

	@Override
	public <T extends ReadableByteChannel & InterruptibleChannel> void setInputChannel(
			T channel) {
		this.inputChannel = channel;
	}
	
	private IDialog buildDirectoryDialog(IDialogBuilderFactory dialogBuilderFactory) {
        // metoda tworzy okienko do wybrania pliku
    	IDialogBuilder idb = dialogBuilderFactory.newDialogBuilder();
    	idb.addDirectoryChooser("Choose a directory: ");
    	return idb.build();
    }

	@Override
	public boolean prepare(IDialogBuilderFactory dialogBuilderFactory) {
		IDialog idb = buildDirectoryDialog(dialogBuilderFactory);
    	if(idb.exec()) {
    		targetPath = idb.getLineEditValue("Choose a directory: ");
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

			if (!targetPath.startsWith("/")) // sciezka musi sie zaczynac od /
				targetPath = "/" + targetPath;
			
			DbxEntry.File uploadedFile = client.chunkedUploadFinish(targetPath,
					DbxWriteMode.add(), uploadId);
		} catch (ClosedChannelException e) {
			throw e;
		} finally {
			inputStream.close();
		}
	}
}
