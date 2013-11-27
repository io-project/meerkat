package meerkat.modules.core;

import meerkat.modules.IPlugin;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.api.easymock.PowerMock;

import java.util.Arrays;
import java.util.Collections;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Maciej Poleski
 */
public class PluginHealthStatusTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    PluginHealthStatus pluginHealthStatus;
    IPlugin plugin;

    @Before
    public void setUp() throws Exception {
        plugin = createMock(IPlugin.class);
        replay(plugin);
        pluginHealthStatus = new PluginHealthStatus(plugin);
    }

    @After
    public void tearDown() throws Exception {
        verify(plugin);
    }

    @Test
    public void testPluginField() throws Exception {
        assertSame(plugin, pluginHealthStatus.plugin);
    }

    @Test
    public void testIsReady() throws Exception {
        assertTrue(pluginHealthStatus.isReady());
        pluginHealthStatus.messages.add(PluginHealthStatus.PLUGIN_HAS_NO_NAME);
        assertFalse(pluginHealthStatus.isReady());
    }

    @Test
    public void testGetMessagesBasic() throws Exception {
        assertEquals(Collections.<String>emptyList(), pluginHealthStatus.getMessages());
        pluginHealthStatus.messages.add(PluginHealthStatus.PLUGIN_HAS_NO_ID);
        assertEquals(Collections.singletonList(PluginHealthStatus.PLUGIN_HAS_NO_ID), pluginHealthStatus.getMessages());
        assertSame(PluginHealthStatus.PLUGIN_HAS_NO_ID, pluginHealthStatus.getMessages().get(0));
        String someString = PowerMock.createMock(String.class);
        PowerMock.replay(someString);
        pluginHealthStatus.messages.add(someString);
        assertEquals(Arrays.asList(PluginHealthStatus.PLUGIN_HAS_NO_ID, someString), pluginHealthStatus.getMessages());
        assertSame(someString, pluginHealthStatus.getMessages().get(1));
        PowerMock.verify(someString);
    }

    @Test
    public void testGetMessagesExceptions() throws Exception {
        expectedException.expect(UnsupportedOperationException.class);
        pluginHealthStatus.getMessages().add("");
    }
}
