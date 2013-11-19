package meerkat.modules.serialization.singleFile;

import meerkat.modules.gui.IDialog;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.serialization.ISerializationImplementation;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @author Tomasz Nocek
 */
public class SingleFileSerializationImplementation implements ISerializationImplementation {

    private WritableByteChannel outputChannel = null;
    private String filePath = null;

    @Override
    public <T extends WritableByteChannel & InterruptibleChannel> void setOutputChannel(T channel) {
        outputChannel = channel;
    }

    @Override
    public boolean prepare(IDialogBuilderFactory<?> dialogBuilderFactory) {
        IDialog d = dialogBuilderFactory.newDialogBuilder().addFileChooser("Podaj ścieżkę pliku:").build();
        if (d.exec()) {
            filePath = d.getFileValue("Podaj ścieżkę pliku:").getAbsolutePath();
            return true;
        }
        return false;
    }

    @Override
    public void run() throws Exception {
        if (filePath == null || outputChannel == null) throw new NullPointerException();
        // throws FileNotFoundException or Permission denied
        RandomAccessFile file = new RandomAccessFile(filePath, "r");

        FileChannel fileChannel = file.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(32768);
        int bytesRead = fileChannel.read(buffer);

        while (bytesRead != -1) {
            buffer.flip();
            while (buffer.hasRemaining()) outputChannel.write(buffer);
            buffer.clear();
            bytesRead = fileChannel.read(buffer);
        }
    }
}
