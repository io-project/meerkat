package meerkat.modules.serialization.singleFile;

import java.io.File;
import meerkat.modules.gui.IDialog;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.serialization.ISerializationImplementation;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.WritableByteChannel;
import meerkat.modules.gui.IFileValidator;

/**
 * @author Tomasz Nocek
 */
public class SingleFileSerializationImplementation implements ISerializationImplementation {

    private WritableByteChannel outputChannel = null;
    private File inputFile = null;

    @Override
    public <T extends WritableByteChannel & InterruptibleChannel> void setOutputChannel(T channel) {
        outputChannel = channel;
    }

    @Override
    public boolean prepare(IDialogBuilderFactory<?> dialogBuilderFactory) {
        
        IFileValidator v = new IFileValidator() {
            @Override
            public boolean validate(String label, File value) {
                return  value.exists() && value.isFile() && value.canRead();
            }
        };
        
        IDialog d = dialogBuilderFactory.newDialogBuilder().addFileChooser("Podaj ścieżkę pliku:",v).build();
        while(d.exec()) {
            if(d.validate()) {
                inputFile = d.getFileValue("Podaj ścieżkę pliku:");
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() throws Exception {
        if(inputFile == null || outputChannel == null) throw new NullPointerException();

        RandomAccessFile file = new RandomAccessFile(inputFile, "r");
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
