package meerkat.modules.serialization.singleFile;

import meerkat.modules.gui.IDialog;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.serialization.IDeserializationImplementation;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.*;

/**
 * @author Tomasz Nocek
 */
public class SingleFileDeserializationImplementation implements IDeserializationImplementation {

    private Channel inputChannel = null;
    private String filePath = null;

    private boolean removeFile(String path) {
        File f = new File(path);
        boolean success = true;
        if (f.exists()) success = f.delete();
        return success;
    }

    @Override
    public <T extends ReadableByteChannel & InterruptibleChannel> void setInputChannel(T channel) {
        this.inputChannel = channel;
    }

    @Override
    public boolean prepare(IDialogBuilderFactory<?> dialogBuilderFactory) {
        IDialog d = dialogBuilderFactory.newDialogBuilder().addFileChooser("Podaj ścieżkę zapisu:").build();
        if (d.exec()) {
            filePath = d.getFileValue("Podaj ścieżkę zapisu:").getAbsolutePath();
            return true;
        }
        return false;
    }

    @Override
    public void run() throws Exception {

        if (filePath == null || inputChannel == null) throw new NullPointerException();

        // throws FileNotFoundException or Permission denied
        RandomAccessFile file = new RandomAccessFile(filePath, "rw");

        try {
            FileChannel fileChannel = file.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(32768);

            int bytesRead = ((ReadableByteChannel) inputChannel).read(buffer);

            while (bytesRead != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) fileChannel.write(buffer);
                buffer.clear();
                bytesRead = ((ReadableByteChannel) inputChannel).read(buffer);
            }
        } catch (ClosedChannelException e) {
            // Plik jest stworzony (niekompletny), ale operacja zostaje przerwana
            removeFile(filePath);
            throw e;
        }
    }
}
