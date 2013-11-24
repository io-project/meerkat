package meerkat.modules.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.tree.TreeModel;
import java.util.concurrent.Callable;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Maciej Poleski
 */
public class DecryptionPreviewJobTest {

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
        String filename = "__builtin_filename_decryptionPreview";
        final TreeModel treeModel = createMock(TreeModel.class);
        replay(treeModel);
        final Throwable[] r = {null};
        final EncryptionPipeline encryptionPipeline = new EncryptionPipeline(pluginsProvider.getSerializationPlugin(new Callable<Byte>() {
            byte r = 54;

            @Override
            public Byte call() throws Exception {
                return r++;
            }
        }, 10000, filename, treeModel), pluginsProvider.getEncryptionPlugin(), pluginsProvider.getImportExportPlugin(), pluginsProvider.getOverridePlugin());
        final boolean[] running = {false};
        EncryptionJob job = new EncryptionJob(encryptionPipeline, null, null, new IResultHandler<Void>() {
            @Override
            public void handleResult(Void result) {

                IJob decryptionJob = new DecryptionJobTemplate<>(encryptionPipeline.getImportExportPlugin(), null, null, pluginsProvider, new DecryptionPreviewImplementationProvider(), new IResultHandler<TreeModel>() {
                    @Override
                    public void handleResult(TreeModel result) {
                        synchronized (DecryptionPreviewJobTest.this) {
                            assertEquals(treeModel, result);
                            running[0] = false;
                            DecryptionPreviewJobTest.this.notify();
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
        if (r[0] != null)
            throw r[0];
        verify(treeModel);
    }
}
