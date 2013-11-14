package meerkat.modules.serialization.singleFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.ReadableByteChannel;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.serialization.IDeserializationImplementation;

/**
 *
 * @author Tomasz Nocek
 */
public class SingleFileDeserializationImplementation implements IDeserializationImplementation {
    
    private Channel inputChannel = null;
    private String filePath = null;
    
    private boolean removeFile(String path) {
        File f = new File(path);
        boolean success = true;
        if(f.exists()) success = f.delete();
        return success;
    }
    
    @Override
    public <T extends ReadableByteChannel & InterruptibleChannel> void setInputChannel(T channel) {
        this.inputChannel = channel;
    }

    @Override
    public boolean prepare(IDialogBuilderFactory dialogBuilderFactory) {
        // TODO implement 
        // metoda prepare powinna dostarczać dane od użytkownika dotyczące pliku wyjściowego
        // tymczasowa ścieżka pliku:
        filePath = "/home/tomasz/Desktop/deserialized";
        return true;
    }
    
    @Override
    public void run() throws Exception {
        
        if(filePath == null || inputChannel == null) throw new NullPointerException();
        
        // throws FileNotFoundException or Permission denied
        RandomAccessFile file = new RandomAccessFile(filePath, "rw");
        
        try {
            FileChannel fileChannel = file.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(32768);

            int bytesRead = ((ReadableByteChannel)inputChannel).read(buffer);

            while(bytesRead != -1) {
                buffer.flip();
                while(buffer.hasRemaining()) fileChannel.write(buffer);
                buffer.clear();
                bytesRead = ((ReadableByteChannel)inputChannel).read(buffer);
            }
        }
        catch(ClosedChannelException e) {
            // Plik jest stworzony (niekompletny), ale operacja zostaje przerwana
            removeFile(filePath);
            throw e;
        }
    }
}
