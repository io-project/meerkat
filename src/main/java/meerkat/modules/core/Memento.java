package meerkat.modules.core;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * Tutaj zapisane są informacje o wykorzystanych pluginach podczas szyfrowania na potrzeby deszyfrowania.
 *
 * @author Maciej Poleski
 */

class Memento implements Serializable {
    private final String serializationPluginId;
    private final String encryptionPluginId;

    Memento(EncryptionPipeline pipeline) {
        serializationPluginId = pipeline.getSerializationPlugin().getUniquePluginId();
        encryptionPluginId = pipeline.getEncryptionPlugin().getUniquePluginId();
    }

    /**
     * Zamienia zestaw identyfikatorów w blok bajtów (na potrzeby eksportu).
     *
     * @return Bufor zawierający komplet danych.
     * @throws java.io.IOException Jeżeli serializacja się nie powiedzie (nie powinno mieć miejsca).
     */
    ByteBuffer mementoToByteBuffer() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(this);
        return ByteBuffer.wrap(baos.toByteArray());
    }

    /**
     * Zamienia blok bajtów na zestaw identyfikatorów (na potrzeby importu).
     *
     * @param byteBuffer Bufor zawierający komplet danych.
     * @return Memento zbudowane z bloku bajtów.
     * @throws IOException Jeżeli coś się nie uda, np. blok bajtów jest błędny/niekompletny.
     */
    static Memento byteBufferToMemento(ByteBuffer byteBuffer) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(byteBuffer.array());
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
}