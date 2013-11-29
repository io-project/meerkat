package meerkat.modules.serialization.standardSerialization;

import meerkat.modules.gui.IDialog;
import meerkat.modules.gui.IDialogBuilder;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.gui.IFileValidator;
import meerkat.modules.serialization.ISerializationImplementation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @author Jakub Ciesla
 */
public class StandardSerializationImplementation implements
        ISerializationImplementation {

    private WritableByteChannel outputChannel;
    public String path;
    private final DirectoryTreeBuilder directoryTreeBuilder = new DirectoryTreeBuilder();
    private final FileSender fileSender = new FileSender();

    //Uzytkownik musi wybrac sciezke serializowanego pliku.
    @Override
    public boolean prepare(IDialogBuilderFactory<?> dialogBuilderFactory) {

    	//potrzebny validator jeszcze
        
        IDialogBuilder builder = dialogBuilderFactory.newDialogBuilder();
        builder.addLabel("Standard serialization:")
               .addSeparator()
               .addLabel("Podaj ścieżkę katalogu:")
               .addDirectoryChooser("dc");
        IDialog d = builder.build();
        
        if(d.exec()) {
        	path = d.getDirectoryValue("dc").getAbsolutePath();
            return true;
        }
        return false;
    }

    @Override
    public void run() throws Exception {
        DirectoryNode directoryNode = directoryTreeBuilder.buildTree(path);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        out = new ObjectOutputStream(bos);
        out.writeObject(directoryNode);
        byte[] directoryTreeBytes = bos.toByteArray();

        ByteBuffer buf = ByteBuffer.allocate(directoryTreeBytes.length + 100);
        buf.clear();
        buf.putInt(directoryTreeBytes.length);

        buf.put(directoryTreeBytes);

        buf.flip();

        while (buf.hasRemaining()) {
            outputChannel.write(buf);
        }
        
        //teraz nalezy wyslac zawartosc plikow
        directoryNode.DFSReadFiles(fileSender, outputChannel);

    }

    @Override
    public <T extends WritableByteChannel & InterruptibleChannel> void setOutputChannel(
            T channel) {
        this.outputChannel = channel;

    }

}
