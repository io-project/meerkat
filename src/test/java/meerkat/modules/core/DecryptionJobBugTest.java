package meerkat.modules.core;

import meerkat.modules.Runnable;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.concurrent.Callable;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertTrue;

/**
 * @author Maciej Poleski
 */
public class DecryptionJobBugTest {

    private PluginsProvider pluginsProvider;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        pluginsProvider = new PluginsProvider();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test(timeout = 3000)
    public void test1() throws Throwable {
        final Throwable[] r = {null};
        final Exception exceptionInDeserialization = new Exception();
        final EncryptionPipeline encryptionPipeline = new EncryptionPipeline(
                pluginsProvider.getSerializationPlugin(
                        new Callable<Byte>() {
                            byte r = 46;

                            @Override
                            public Byte call() throws Exception {
                                return r++;
                            }
                        },
                        10000,
                        "filename",
                        null,
                        new Runnable() {
                            @Override
                            public void run() throws Exception {
                                throw exceptionInDeserialization;
                            }
                        }
                ),
                pluginsProvider.getEncryptionPlugin(),
                pluginsProvider.getImportExportPlugin(true),
                pluginsProvider.getOverridePlugin());
        final boolean[] running = {false};
        EncryptionJob job = new EncryptionJob(encryptionPipeline, null, null, new IResultHandler<Void>() {
            @Override
            public void handleResult(Void result) {

                IJob decryptionJob = new DecryptionJobTemplate<>(encryptionPipeline.getImportExportPlugin(), null, null, pluginsProvider, new DecryptionImplementationProvider(), new IResultHandler<Void>() {
                    @Override
                    public void handleResult(Void result) {
                        synchronized (DecryptionJobBugTest.this) {
                            running[0] = false;
                            DecryptionJobBugTest.this.notify();
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
                t.printStackTrace();
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
        expectedException.expect(sameInstance(exceptionInDeserialization));
        if (r[0] != null)
            throw r[0];
    }
}
