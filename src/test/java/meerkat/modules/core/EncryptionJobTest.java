package meerkat.modules.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Callable;

import static org.junit.Assert.assertTrue;

/**
 * @author Maciej Poleski
 */
public class EncryptionJobTest {

    private final PluginsProvider pluginsProvider = new PluginsProvider();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void test1() throws Exception {
        EncryptionPipeline encryptionPipeline = new EncryptionPipeline(PluginsProvider.getSerializationPlugin(new Callable<Byte>() {
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
                assertTrue(t.getMessage(), false);
            }
        });
        running[0] = true;
        job.start();
        synchronized (this) {
            while (running[0]) {
                wait();
            }
        }
    }

    interface IDataChecker<T> {
        boolean check(T data);
    }
}
