package meerkat.modules.core;

import meerkat.modules.encryption.IEncryptionPlugin;
import meerkat.modules.serialization.ISerializationPlugin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Maciej Poleski
 */
public class MementoTest {

    final String encryptionPluginId1 = "Encryption Plugin 1";
    final String encryptionPluginId2 = "Encryption Plugin 2";
    final String serializationPluginId1 = "Serialization Plugin 1";
    final String serializationPluginId2 = "Serialization Plugin 2";
    Memento memento1;
    Memento memento2;

    public static Memento newMemento(String encryptionPluginId, String serializationPluginId) {
        EncryptionPipeline encryptionPipeline1 = createMock(EncryptionPipeline.class);

        IEncryptionPlugin encryptionPlugin1 = createMock(IEncryptionPlugin.class);

        expect(encryptionPlugin1.getUniquePluginId()).andReturn(encryptionPluginId);

        ISerializationPlugin serializationPlugin1 = createMock(ISerializationPlugin.class);

        expect(serializationPlugin1.getUniquePluginId()).andReturn(serializationPluginId);

        expect(encryptionPipeline1.getEncryptionPlugin()).andReturn(encryptionPlugin1);
        expect(encryptionPipeline1.getSerializationPlugin()).andReturn(serializationPlugin1);

        replay(encryptionPlugin1, serializationPlugin1);
        replay(encryptionPipeline1);

        Memento memento = new Memento(encryptionPipeline1);

        verify(encryptionPlugin1, serializationPlugin1);
        verify(encryptionPipeline1);
        return memento;
    }

    @Before
    public void setUp() throws Exception {
        memento1 = newMemento(encryptionPluginId1, serializationPluginId1);
        memento2 = newMemento(encryptionPluginId2, serializationPluginId2);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testByteBufferToMemento() throws Exception {

    }

    @Test
    public void testEquals() throws Exception {
        assertTrue(memento1.equals(memento1));
        assertTrue(memento2.equals(memento2));
        assertFalse(memento1.equals(memento2));
        assertFalse(memento2.equals(memento1));
        Memento memento1new = newMemento(encryptionPluginId1, serializationPluginId1);
        assertTrue(memento1.equals(memento1new));
        assertTrue(memento1new.equals(memento1));
        assertFalse(memento2.equals(memento1new));
        Memento memento3new = newMemento(encryptionPluginId1, serializationPluginId2);
        assertFalse(memento1.equals(memento3new));
        assertFalse(memento2.equals(memento3new));
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(memento1.hashCode(), memento1.hashCode());
        assertEquals(memento2.hashCode(), memento2.hashCode());
        Memento memento1new = newMemento(encryptionPluginId1, serializationPluginId1);
        assertEquals(memento1.hashCode(), memento1new.hashCode());
        Memento memento2new = newMemento(encryptionPluginId2, serializationPluginId2);
        assertEquals(memento2.hashCode(), memento2new.hashCode());
    }

    @Test
    public void testMementoToByteBuffer() throws Exception {
        ByteBuffer buffer11 = memento1.mementoToByteBuffer();
        ByteBuffer buffer12 = memento1.mementoToByteBuffer();

        ByteBuffer buffer21 = memento2.mementoToByteBuffer();
        ByteBuffer buffer22 = memento2.mementoToByteBuffer();

        assertArrayEquals(buffer11.array(), buffer12.array());
        assertArrayEquals(buffer21.array(), buffer22.array());

        assertEquals(memento1, Memento.byteBufferToMemento(buffer11));
        assertEquals(memento1, Memento.byteBufferToMemento(buffer12));
        assertEquals(memento2, Memento.byteBufferToMemento(buffer21));
        assertEquals(memento2, Memento.byteBufferToMemento(buffer22));

        Memento memento13 = newMemento(encryptionPluginId1, serializationPluginId1);
        assertEquals(memento13, Memento.byteBufferToMemento(memento13.mementoToByteBuffer()));
        assertEquals(memento1, Memento.byteBufferToMemento(memento13.mementoToByteBuffer()));

    }

    @Test
    public void testGetDecryptionPipeline() throws Exception {

    }
}
