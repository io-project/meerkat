package meerkat.modules.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
     * Zamienia zestaw identyfikatorów w blok bajtów (na potrzeby eksportu)
     *
     * @param memento Obiekt zainicjalizowany wybranym EncryptionPipeline.
     * @return Bufor zawierający komplet danych
     * @throws IOException Jeżeli serializacja się nie powiedzie (nie powinno mieć miejsca)
     */
    static ByteBuffer getMementoByteBuffer(Memento memento) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(memento);
        return ByteBuffer.wrap(baos.toByteArray());
    }
}