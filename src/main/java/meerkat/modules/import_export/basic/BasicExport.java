package meerkat.modules.import_export.basic;

import meerkat.modules.gui.IDialog;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.import_export.IExportImplementation;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import meerkat.modules.gui.IDialogBuilder;
import meerkat.modules.gui.IFileValidator;

/**
 * Podstawowy plugin exportu - zapis do pliku.
 *
 * @author Tomasz Nocek
 */
public class BasicExport implements IExportImplementation {

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
                if(value.isDirectory()) return false;
                if(value.canWrite()) return true;
                try {
                    if(value.createNewFile()) return true;
                } catch (IOException ex) {}
                return false;
            }
        };
        
        IDialogBuilder builder = dialogBuilderFactory.newDialogBuilder();
        builder.addLabel("basic export:")
               .addSeparator()
               .addLabel("podaj ścieżkę pliku:")
               .addFileChooser("fc",v);
        IDialog d = builder.build();
        
        if(d.exec()) {
            outputFile = d.getFileValue("fc");
            return true;
        }
        return false;
    }

    @Override
    public void run() throws Exception {

        if(outputFile == null || inputChannel == null) throw new NullPointerException();

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
        } finally {
            file.close();
        }
    }
}

