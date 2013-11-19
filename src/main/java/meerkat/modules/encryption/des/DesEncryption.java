package meerkat.modules.encryption.des;

import meerkat.modules.encryption.IEncryptionImplementation;
import meerkat.modules.gui.IDialog;
import meerkat.modules.gui.IDialogBuilder;
import meerkat.modules.gui.IDialogBuilderFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.nio.ByteBuffer;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;


public class DesEncryption implements IEncryptionImplementation {

    ReadableByteChannel readChannel = null;
    WritableByteChannel writeChannel = null;
    private SecretKey key = null;

    @Override
    public boolean prepare(IDialogBuilderFactory<?> dialogBuilderFactory) {
        IDialogBuilder dialogBuilder = dialogBuilderFactory.newDialogBuilder();
        String label_password = "Haslo";
        String label_description = "Podaj haslo chroniace kodowanie:";
        IDialog dialog = dialogBuilder
                .addLabel(label_description)
                .addPasswordEdit(label_password)
                .build();
        if (dialog.exec()) {
            try {
                key = DesAddition.makeKeyFromPassword(new String(dialog.getPasswordValue(label_password)).getBytes());
                return true;
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
        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

        // initializing cipher...
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

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
