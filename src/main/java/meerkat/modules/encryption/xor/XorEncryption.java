package meerkat.modules.encryption.xor;

import meerkat.modules.encryption.IEncryptionImplementation;
import meerkat.modules.gui.IDialog;
import meerkat.modules.gui.IDialogBuilder;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.gui.IPasswordValidator;

import java.nio.ByteBuffer;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;


public class XorEncryption implements IEncryptionImplementation {

    ReadableByteChannel readChannel = null;
    WritableByteChannel writeChannel = null;
    byte[] hashCode = null;
    private XorAddition xorAddition = new XorAddition();

    public XorAddition getXorAddition() {
        return xorAddition;
    }

    @Override
    public boolean prepare(IDialogBuilderFactory<?> dialogBuilderFactory) {
        IDialogBuilder<?> dialogBuilder = dialogBuilderFactory.newDialogBuilder();
        String label_password = "Haslo";
        String label_description = "Podaj haslo chroniace kodowanie (1-8 znakow):";
        IPasswordValidator passwordValidator = new IPasswordValidator(){
        	@Override
        	public boolean validate(String label, char[] value){
        		return value != null && value.length > 0 && value.length < 9;
        	}
        };
        IDialog dialog = dialogBuilder
                .addLabel(label_description)
                .addPasswordEdit(label_password, passwordValidator)
                .build();
        if (dialog.exec()) {
            return xorAddition.makeHashArrayFromPassword(dialog.getPasswordValue(label_password));
        }
        return false;
    }

    private void code(ByteBuffer readBuffer, int read, ByteBuffer writeBuffer) {
        for (int i = 0; i < read; i++) {
            byte b = readBuffer.get();
            byte xorByte = (byte) (b ^ hashCode[i % hashCode.length]);
            writeBuffer.put(xorByte);
        }
    }

    @Override
    public void run() throws Exception {
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

        int read = -1;
        hashCode = xorAddition.getHashCodeArray();
        while ((read = readChannel.read(readBuffer)) != -1) {
            readBuffer.flip();
            code(readBuffer, read, writeBuffer);
            writeBuffer.flip();
            writeChannel.write(writeBuffer);
            readBuffer.clear();
            writeBuffer.clear();
        }
    }

    @Override
    public <T extends ReadableByteChannel & InterruptibleChannel> void setInputChannel(
            T channel) {
        readChannel = channel;
    }

    @Override
    public <T extends WritableByteChannel & InterruptibleChannel> void setOutputChannel(
            T channel) {
        writeChannel = channel;
    }

}
