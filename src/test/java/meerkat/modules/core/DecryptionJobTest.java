package meerkat.modules.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Callable;

import static org.junit.Assert.assertTrue;

/**
 * @author Maciej Poleski
 */
public class DecryptionJobTest {

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
        final Throwable[] r = {null};
        final EncryptionPipeline encryptionPipeline = new EncryptionPipeline(pluginsProvider.getSerializationPlugin(new Callable<Byte>() {
            byte r = 4;

            @Override
            public Byte call() throws Exception {
                return r++;
            }
        }, 10000), pluginsProvider.getEncryptionPlugin(), pluginsProvider.getImportExportPlugin(), pluginsProvider.getOverridePlugin());
        final boolean[] running = {false};
        EncryptionJob job = new EncryptionJob(encryptionPipeline, null, null, new IResultHandler<Void>() {
            @Override
            public void handleResult(Void result) {

                DecryptionJobTemplate decryptionJob = new DecryptionJobTemplate<>(encryptionPipeline.getImportExportPlugin(), null, null, pluginsProvider, new DecryptionImplementationProvider(), new IResultHandler<Void>() {
                    @Override
                    public void handleResult(Void result) {
                        synchronized (DecryptionJobTest.this) {
                            running[0] = false;
                            DecryptionJobTest.this.notify();
                        }
                    }

                    @Override
                    public void handleException(Throwable t) {
                        r[0] = t;
                        handleResult(null);
                    }
                });
                decryptionJob.start();
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
        if (r[0] != null)
            throw r[0];
    }
}
