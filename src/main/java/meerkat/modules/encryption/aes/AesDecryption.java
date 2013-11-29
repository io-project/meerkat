package meerkat.modules.encryption.aes;

import meerkat.modules.IncorrectPasswordException;
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
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class AesDecryption implements IDecryptionImplementation {

    ReadableByteChannel readChannel = null;
    WritableByteChannel writeChannel = null;
    SecretKey secretKey = null;
    private AesAddition aesAddition = new AesAddition();

    public AesAddition getAesAddition() {
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
            try {
				return aesAddition.makeSecretKeyFromPassword(dialog.getPasswordValue(label_password));
			} catch (Exception e) {
				IDialog d = dialogBuilderFactory.newDialogBuilder().addLabel("AES alghoritm not found.").build();
                d.exec();
			}
        }
        return false;
    }

    @Override
    public void run() throws Exception {
        ByteBuffer readBuffer = ByteBuffer.allocate(1040);
        ByteBuffer writeBuffer = ByteBuffer.allocate(1040);
        
        secretKey = aesAddition.getSecretKey();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(new byte[16]));
        
        while (readChannel.read(readBuffer) != -1) {
            readBuffer.flip();
            try{
            	cipher.doFinal(readBuffer, writeBuffer);
            }catch(Exception e){
            	throw new IncorrectPasswordException();
            }
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

