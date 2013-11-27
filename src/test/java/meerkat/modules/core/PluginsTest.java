package meerkat.modules.core;

import meerkat.modules.IPlugin;
import meerkat.modules.encryption.IDecryptionImplementation;
import meerkat.modules.encryption.IEncryptionImplementation;
import meerkat.modules.encryption.IEncryptionPlugin;
import meerkat.modules.import_export.IExportImplementation;
import meerkat.modules.import_export.IImportExportPlugin;
import meerkat.modules.import_export.IImportImplementation;
import meerkat.modules.plausible_deniability.IOverrideImplementation;
import meerkat.modules.plausible_deniability.IOverridePlugin;
import meerkat.modules.serialization.IDeserializationImplementation;
import meerkat.modules.serialization.IDeserializationPreviewImplementation;
import meerkat.modules.serialization.ISerializationImplementation;
import meerkat.modules.serialization.ISerializationPlugin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author Maciej Poleski
 */
public class PluginsTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetHealthStatusIPlugin1() throws Exception {
        IPlugin plugin = new IPlugin() {
            @Override
            public String getUserVisibleName() {
                return null;
            }

            @Override
            public String getUniquePluginId() {
                return null;
            }
        };
        PluginHealthStatus status = Plugins.getHealthStatus(plugin);
        assertFalse(status.isReady());
        assertSame(plugin, status.plugin);
        assertEquals(2, status.getMessages().size());
        assertTrue(status.getMessages().contains(PluginHealthStatus.PLUGIN_HAS_NO_ID));
        assertTrue(status.getMessages().contains(PluginHealthStatus.PLUGIN_HAS_NO_NAME));
        assertThat(status.getMessages(), everyItem(anyOf(sameInstance(PluginHealthStatus.PLUGIN_HAS_NO_NAME), sameInstance(PluginHealthStatus.PLUGIN_HAS_NO_ID))));
    }

    @Test
    public void testGetHealthStatusIPlugin2() throws Exception {
        IPlugin plugin = new IPlugin() {
            @Override
            public String getUserVisibleName() {
                return "";
            }

            @Override
            public String getUniquePluginId() {
                return "";
            }
        };
        PluginHealthStatus status = Plugins.getHealthStatus(plugin);
        assertFalse(status.isReady());
        assertSame(plugin, status.plugin);
        assertEquals(2, status.getMessages().size());
        assertTrue(status.getMessages().contains(PluginHealthStatus.PLUGIN_HAS_EMPTY_ID));
        assertTrue(status.getMessages().contains(PluginHealthStatus.PLUGIN_HAS_EMPTY_NAME));
        assertThat(status.getMessages(), everyItem(anyOf(sameInstance(PluginHealthStatus.PLUGIN_HAS_EMPTY_NAME), sameInstance(PluginHealthStatus.PLUGIN_HAS_EMPTY_ID))));
    }

    @Test
    public void testGetHealthStatusISerializationPlugin() throws Exception {
        ISerializationPlugin plugin = new ISerializationPlugin() {
            @Override
            public ISerializationImplementation getSerializationImplementation() {
                return null;
            }

            @Override
            public IDeserializationImplementation getDeserializationImplementation() {
                return null;
            }

            @Override
            public IDeserializationPreviewImplementation getDeserializationPreviewImplementation() {
                return null;
            }

            @Override
            public String getUserVisibleName() {
                return null;
            }

            @Override
            public String getUniquePluginId() {
                return null;
            }
        };
        PluginHealthStatus status = Plugins.getHealthStatus(plugin);
        assertFalse(status.isReady());
        assertSame(plugin, status.plugin);
        assertEquals(5, status.getMessages().size());
        assertThat(status.getMessages(), hasItems(PluginHealthStatus.PLUGIN_HAS_NO_ID, PluginHealthStatus.PLUGIN_HAS_NO_NAME));
    }

    @Test
    public void testGetHealthStatusIEncryptionPlugin() throws Exception {
        IEncryptionPlugin plugin = new IEncryptionPlugin() {
            @Override
            public IEncryptionImplementation getEncryptionImplementation() {
                return null;
            }

            @Override
            public IDecryptionImplementation getDecryptionImplementation() {
                return null;
            }

            @Override
            public String getUserVisibleName() {
                return null;
            }

            @Override
            public String getUniquePluginId() {
                return null;
            }
        };
        PluginHealthStatus status = Plugins.getHealthStatus(plugin);
        assertFalse(status.isReady());
        assertSame(plugin, status.plugin);
        assertEquals(4, status.getMessages().size());
        assertThat(status.getMessages(), hasItems(PluginHealthStatus.PLUGIN_HAS_NO_ID, PluginHealthStatus.PLUGIN_HAS_NO_NAME));
    }

    @Test
    public void testGetHealthStatusIImportExportPlugin() throws Exception {
        IImportExportPlugin plugin = new IImportExportPlugin() {
            @Override
            public IImportImplementation getImportImplementation() {
                return null;
            }

            @Override
            public IExportImplementation getExportImplementation() {
                return null;
            }

            @Override
            public String getUserVisibleName() {
                return null;
            }

            @Override
            public String getUniquePluginId() {
                return null;
            }
        };
        PluginHealthStatus status = Plugins.getHealthStatus(plugin);
        assertFalse(status.isReady());
        assertSame(plugin, status.plugin);
        assertEquals(4, status.getMessages().size());
        assertThat(status.getMessages(), hasItems(PluginHealthStatus.PLUGIN_HAS_NO_ID, PluginHealthStatus.PLUGIN_HAS_NO_NAME));
    }

    @Test
    public void testGetHealthStatusIOverridePlugin() throws Exception {
        IOverridePlugin plugin = new IOverridePlugin() {
            @Override
            public IOverrideImplementation getOverrideImplementation() {
                return null;
            }

            @Override
            public String getUserVisibleName() {
                return null;
            }

            @Override
            public String getUniquePluginId() {
                return null;
            }
        };
        PluginHealthStatus status = Plugins.getHealthStatus(plugin);
        assertFalse(status.isReady());
        assertSame(plugin, status.plugin);
        assertEquals(3, status.getMessages().size());
        assertThat(status.getMessages(), hasItems(PluginHealthStatus.PLUGIN_HAS_NO_ID, PluginHealthStatus.PLUGIN_HAS_NO_NAME));
    }
}
