package meerkat.modules.core;

import meerkat.modules.NoGuiPluginRegisteredException;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertTrue;

/**
 * @author Maciej Poleski
 */
public class CoreTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @After
    public void tearDown() throws Exception {
        System.setErr(null);
    }

    @Test
    public void testSimpleStartup() throws Exception {
        Core core = new Core();
        PluginsProvider provider = new PluginsProvider();
        core.registerPlugin(provider.getEncryptionPlugin());
        core.registerPlugin(provider.getImportExportPlugin());
        core.registerPlugin(provider.getOverridePlugin());
        core.registerPlugin(provider.getSerializationPlugin(null, 0, null, null));
        assertTrue(core.getEncryptionPlugins().isEmpty());
        assertTrue(core.getImportExportPlugins().isEmpty());
        assertTrue(core.getOverridePlugins().isEmpty());
        assertTrue(core.getSerializationPlugins().isEmpty());

        System.setErr(new PrintStream(new ByteArrayOutputStream()));
        expectedException.expect(NoGuiPluginRegisteredException.class);
        core.start();
    }
}
