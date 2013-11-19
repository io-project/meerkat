package meerkat.modules.encryption.none;

import meerkat.modules.encryption.IEncryptionImplementation;
import meerkat.modules.gui.IDialogBuilderFactory;

import java.nio.ByteBuffer;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @author Tomasz Nocek
 */
public class NoEncryptionImplementation implements IEncryptionImplementation {

    private ReadableByteChannel inputChannel = null;
    private WritableByteChannel outputChannel = null;


    @Override
    public <T extends ReadableByteChannel & InterruptibleChannel> void setInputChannel(T channel) {
        inputChannel = channel;
    }

    @Override
    public <T extends WritableByteChannel & InterruptibleChannel> void setOutputChannel(T channel) {
        outputChannel = channel;
    }

    @Override
    public boolean prepare(IDialogBuilderFactory<?> dialogBuilderFactory) {
        return true;
    }

    @Override
    public void run() throws Exception {

        ByteBuffer buffer = ByteBuffer.allocate(32768);
        int bytesRead = inputChannel.read(buffer);

        while (bytesRead != -1) {
            buffer.flip();
            while (buffer.hasRemaining()) outputChannel.write(buffer);
            buffer.clear();
            bytesRead = inputChannel.read(buffer);
        }
    }

}
