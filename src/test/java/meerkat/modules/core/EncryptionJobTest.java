package meerkat.modules.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Callable;

/**
 * @author Maciej Poleski
 */
public class EncryptionJobTest {

    private PluginsProvider pluginsProvider;

    @Before
    public void setUp() throws Exception {
        pluginsProvider = new PluginsProvider();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void test1() throws Throwable {
        final Throwable[] throwable = {null};
        EncryptionPipeline encryptionPipeline = new EncryptionPipeline(pluginsProvider.getSerializationPlugin(new Callable<Byte>() {
            @Override
            public Byte call() throws Exception {
                return 7;
            }
        }, 10000), pluginsProvider.getEncryptionPlugin(), pluginsProvider.getImportExportPlugin(), pluginsProvider.getOverridePlugin());
        final boolean[] running = {false};
        EncryptionJob job = new EncryptionJob(encryptionPipeline, null, null, new IResultHandler<Void>() {
            @Override
            public void handleResult(Void result) {
                synchronized (EncryptionJobTest.this) {
                    running[0] = false;
                    EncryptionJobTest.this.notify();
                }
            }

            @Override
            public void handleException(Throwable t) {
                throwable[0] = t;
                handleResult(null);
            }
        });
        running[0] = true;
        job.start();
        synchronized (this) {
            while (running[0]) {
                wait();
            }
        }
        if (throwable[0] != null)
            throw throwable[0];
    }
}
