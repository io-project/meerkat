package meerkat.modules.core;

import meerkat.modules.PluginNotFoundException;
import meerkat.modules.encryption.IEncryptionPlugin;
import meerkat.modules.import_export.IImportExportPlugin;
import meerkat.modules.serialization.ISerializationPlugin;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * Tutaj zapisane są informacje o wykorzystanych pluginach podczas szyfrowania na potrzeby deszyfrowania.
 *
 * @author Maciej Poleski
 */

final class Memento implements Serializable {
    private final String serializationPluginId;
    private final String encryptionPluginId;

    Memento(EncryptionPipeline pipeline) {
        serializationPluginId = pipeline.getSerializationPlugin().getUniquePluginId();
        encryptionPluginId = pipeline.getEncryptionPlugin().getUniquePluginId();
    }

    /**
     * Zamienia blok bajtów na zestaw identyfikatorów (na potrzeby importu).
     *
     * @param byteBuffer Bufor zawierający komplet danych. Pozycja musi być ustawiona na początku bloku danych.
     * @param length     Długość danych z których ma zostać rekonstruowany Memento
     * @return Memento zbudowane z bloku bajtów.
     * @throws IOException Jeżeli coś się nie uda, np. blok bajtów jest błędny/niekompletny.
     */
    static Memento byteBufferToMemento(ByteBuffer byteBuffer, int length) throws IOException {
        byte[] array = new byte[length];
        byteBuffer.get(array, 0, length);
        ByteArrayInputStream bais = new ByteArrayInputStream(array);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Memento result = null;
        try {
            result = (Memento) ois.readObject();
        } catch (ClassNotFoundException ignored) {
            // Absolutnie niemożliwe
            assert false;
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Memento memento = (Memento) o;

        if (!encryptionPluginId.equals(memento.encryptionPluginId)) return false;
        if (!serializationPluginId.equals(memento.serializationPluginId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = serializationPluginId.hashCode();
        result = 31 * result + encryptionPluginId.hashCode();
        return result;
    }

    /**
     * Zamienia zestaw identyfikatorów w blok bajtów (na potrzeby eksportu).
     *
     * @return Bufor zawierający komplet danych. Pozycja będzie ustawiona na 0, a limit i capacity na wielkość bufora.
     * @throws java.io.IOException Jeżeli serializacja się nie powiedzie (nie powinno mieć miejsca).
     */
    ByteBuffer mementoToByteBuffer() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(this);
        return ByteBuffer.wrap(baos.toByteArray());
    }

    DecryptionPipeline getDecryptionPipeline(IImportExportPlugin importExportPlugin, IPluginManager pluginManager) throws PluginNotFoundException {
        IEncryptionPlugin encryptionPlugin = pluginManager.getEncryptionPluginForId(encryptionPluginId);
        ISerializationPlugin serializationPlugin = pluginManager.getSerializationPluginForId(serializationPluginId);
        return new DecryptionPipeline(serializationPlugin, encryptionPlugin, importExportPlugin);
    }
}