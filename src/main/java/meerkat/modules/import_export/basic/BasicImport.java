package meerkat.modules.import_export.basic;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.WritableByteChannel;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.import_export.IImportImplementation;

/**
 * Podstawowy plugin importu - wczytanie z pliku.
 * 
 * @author Tomasz Nocek
 */
public class BasicImport implements IImportImplementation {

    private Channel outputChannel = null;
    private String filePath = null;
    
    @Override
    public <T extends WritableByteChannel & InterruptibleChannel> void setOutputChannel(T channel) {
        this.outputChannel = channel;
    }

    @Override
    public boolean prepare(IDialogBuilderFactory dialogBuilderFactory) {
        // TODO implement 
        // metoda prepare powinna dostarczać dane od użytkownika dotyczące pliku wejściowego
        // tymczasowa ścieżka pliku:
        filePath = "./exported";
        return true;
    }

    @Override
    public void run() throws Exception {
        
        if(filePath == null || outputChannel == null) throw new NullPointerException();
        
        // throws FileNotFoundException or Permission denied
        RandomAccessFile file = new RandomAccessFile(filePath, "r");
        
        try {
            FileChannel fileChannel = file.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(32768);

            int bytesRead = fileChannel.read(buffer);

            while(bytesRead != -1) {
                buffer.flip();
                while(buffer.hasRemaining()) ((WritableByteChannel)outputChannel).write(buffer);
                buffer.clear();
                bytesRead = fileChannel.read(buffer);
            }
        }
        catch(ClosedChannelException e) {
            // Plik jest w trakcie wczytywania, ale operacja zostaje przerwana
            throw e;
        }
        finally {
            file.close();
        }
    }
}