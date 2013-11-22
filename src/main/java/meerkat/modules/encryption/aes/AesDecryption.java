package meerkat.modules.encryption.aes;

import meerkat.modules.encryption.IDecryptionImplementation;
import meerkat.modules.gui.IDialog;
import meerkat.modules.gui.IDialogBuilder;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.gui.IPasswordValidator;

import java.nio.ByteBuffer;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AesDecryption implements IDecryptionImplementation {

    ReadableByteChannel readChannel = null;
    WritableByteChannel writeChannel = null;
    SecretKeySpec secretKey = null;
    private AesAddition aesAddition = new AesAddition();

    public AesAddition getXorAddition() {
        return aesAddition;
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
            return aesAddition.makeSecretKeyFromPassword(dialog.getPasswordValue(label_password));
        }
        return false;
    }

    @Override
    public void run() throws Exception {
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
        
        secretKey = aesAddition.getSecretKey();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        
        while (readChannel.read(readBuffer) != -1) {
            readBuffer.flip();
            cipher.doFinal(readBuffer, writeBuffer);
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

