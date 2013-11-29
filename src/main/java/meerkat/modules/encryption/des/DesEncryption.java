package meerkat.modules.encryption.des;

import meerkat.modules.encryption.IEncryptionImplementation;
import meerkat.modules.gui.IDialog;
import meerkat.modules.gui.IDialogBuilder;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.gui.IPasswordValidator;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import java.nio.ByteBuffer;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;


public class DesEncryption implements IEncryptionImplementation {

    ReadableByteChannel readChannel = null;
    WritableByteChannel writeChannel = null;
    private SecretKey secretKey = null;
    private DesAddition desAddition = new DesAddition();

    public DesAddition getDesAddition() {
        return desAddition;
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
            	return desAddition.makeSecretKeyFromPassword(dialog.getPasswordValue(label_password));
            } catch (Exception e) {
                IDialog d = dialogBuilderFactory.newDialogBuilder().addLabel("DES alghoritm not found.").build();
                d.exec();
            }
        }
        return false;
    }

    @Override
    public void run() throws Exception {
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        ByteBuffer writeBuffer = ByteBuffer.allocate(1032);

        secretKey = desAddition.getSecretKey();
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(new byte[8]));

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
