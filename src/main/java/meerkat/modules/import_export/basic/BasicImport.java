package meerkat.modules.import_export.basic;

import java.io.File;
import meerkat.modules.gui.IDialog;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.import_export.IImportImplementation;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import meerkat.modules.gui.IFileValidator;

/**
 * Podstawowy plugin importu - wczytanie z pliku.
 *
 * @author Tomasz Nocek
 */
public class BasicImport implements IImportImplementation {

    private Channel outputChannel = null;
    private File inputFile = null;

    @Override
    public <T extends WritableByteChannel & InterruptibleChannel> void setOutputChannel(T channel) {
        this.outputChannel = channel;
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
        if(d.exec()) {
            inputFile = d.getFileValue("Podaj ścieżkę pliku:");
            return true;
        }
        return false;
    }

    @Override
    public void run() throws Exception {

        if(inputFile == null || outputChannel == null) throw new NullPointerException();

        RandomAccessFile file = new RandomAccessFile(inputFile, "r");

        try {
            FileChannel fileChannel = file.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(32768);

            int bytesRead = fileChannel.read(buffer);

            while (bytesRead != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) ((WritableByteChannel) outputChannel).write(buffer);
                buffer.clear();
                bytesRead = fileChannel.read(buffer);
            }
        } catch (ClosedChannelException e) {
            // Plik jest w trakcie wczytywania, ale operacja zostaje przerwana
            throw e;
        } finally {
            file.close();
        }
    }
}