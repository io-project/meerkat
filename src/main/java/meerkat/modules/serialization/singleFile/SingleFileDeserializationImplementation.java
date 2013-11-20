package meerkat.modules.serialization.singleFile;

import meerkat.modules.gui.IDialog;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.serialization.IDeserializationImplementation;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import meerkat.modules.gui.IFileValidator;

/**
 * @author Tomasz Nocek
 */
public class SingleFileDeserializationImplementation implements IDeserializationImplementation {

    private Channel inputChannel = null;
    private File outputFile = null;

    private boolean removeFile(File f) {
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
        
        IFileValidator v = new IFileValidator() {
            @Override
            public boolean validate(String label, File value) {
                if(value.canWrite()) return true;
                try {
                    if(value.createNewFile()) return true;
                } catch (IOException ex) {}
                return false;
            }
        };
        
        IDialog d = dialogBuilderFactory.newDialogBuilder().addFileChooser("Podaj ścieżkę pliku:",v).build();
        while(d.exec()) {
            if(d.validate()) {
                outputFile = d.getFileValue("Podaj ścieżkę pliku:");
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() throws Exception {

        if (outputFile == null || inputChannel == null) throw new NullPointerException();

        RandomAccessFile file = new RandomAccessFile(outputFile, "rw");

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
            removeFile(outputFile);
            throw e;
        }
    }
}
