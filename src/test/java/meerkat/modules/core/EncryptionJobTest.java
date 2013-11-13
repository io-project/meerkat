package meerkat.modules.core;

import meerkat.modules.encryption.IDecryptionImplementation;
import meerkat.modules.encryption.IEncryptionImplementation;
import meerkat.modules.encryption.IEncryptionPlugin;
import meerkat.modules.gui.IDialogBuilderFactory;
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

import java.nio.ByteBuffer;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Maciej Poleski
 */
public class EncryptionJobTest {

    EncryptionJob encryptionJob;

    static ISerializationPlugin getSerializationPlugin(final Callable<Byte> dataSource, final int dataLength) {
        return new ISerializationPlugin() {
            @Override
            public ISerializationImplementation getSerializationImplementation() {
                return new ISerializationImplementation() {
                    WritableByteChannel channel;

                    @Override
                    public <T extends WritableByteChannel & InterruptibleChannel> void setOutputChannel(T channel) {
                        this.channel = channel;
                    }

                    @Override
                    public boolean prepare(IDialogBuilderFactory dialogBuilderFactory) {
                        return true;
                    }

                    @Override
                    public void run() throws Exception {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(dataLength);
                        for (int i = 0; i < dataLength; ++i) {
                            byteBuffer.put(dataSource.call());
                        }
                        channel.write(byteBuffer);
                        channel.close();
                    }
                };
            }

            @Override
            public IDeserializationImplementation getDeserializationImplementation() {
                return new IDeserializationImplementation() {
                    @Override
                    public <T extends ReadableByteChannel & InterruptibleChannel> void setInputChannel(T channel) {
                    }

                    @Override
                    public boolean prepare(IDialogBuilderFactory dialogBuilderFactory) {
                        return true;
                    }

                    @Override
                    public void run() throws Exception {
                    }
                };
            }

            @Override
            public IDeserializationPreviewImplementation getDeserializationPreviewImplementation() {
                return null;
            }

            @Override
            public String getUserVisibleName() {
                return null; // We mean it
            }

            @Override
            public String getUniquePluginId() {
                return "__builtin__testing_serialization1";
            }
        };
    }

    IEncryptionPlugin getEncryptionPlugin() {
        return new IEncryptionPlugin() {
            @Override
            public IEncryptionImplementation getEncryptionImplementation() {
                return new IEncryptionImplementation() {
                    ReadableByteChannel input;
                    WritableByteChannel output;

                    @Override
                    public <T extends ReadableByteChannel & InterruptibleChannel> void setInputChannel(T channel) {
                        input = channel;
                    }

                    @Override
                    public <T extends WritableByteChannel & InterruptibleChannel> void setOutputChannel(T channel) {
                        output = channel;
                    }

                    @Override
                    public boolean prepare(IDialogBuilderFactory dialogBuilderFactory) {
                        return true;
                    }

                    @Override
                    public void run() throws Exception {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        while (true) {
                            int n;
                            if ((n = input.read(byteBuffer)) == -1)
                                break;
                            byteBuffer.flip();
                            assertEquals(n, output.write(byteBuffer));
                            byteBuffer.clear();
                        }
                        input.close();
                        output.close();
                    }
                };
            }

            @Override
            public IDecryptionImplementation getDecryptionImplementation() {
                return null;  // TODO
            }

            @Override
            public String getUserVisibleName() {
                return null;
            }

            @Override
            public String getUniquePluginId() {
                return "__builtin_testing_encryption1";
            }
        };
    }

    IImportExportPlugin getImportExportPlugin() {
        return new IImportExportPlugin() {
            @Override
            public IImportImplementation getImportImplementation() {
                return null;
            }

            @Override
            public IExportImplementation getExportImplementation() {
                return new IExportImplementation() {
                    ReadableByteChannel channel;

                    @Override
                    public <T extends ReadableByteChannel & InterruptibleChannel> void setInputChannel(T channel) {
                        this.channel = channel;
                    }

                    @Override
                    public boolean prepare(IDialogBuilderFactory dialogBuilderFactory) {
                        return true;
                    }

                    @Override
                    public void run() throws Exception {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        while (true) {
                            int n;
                            if ((n = channel.read(byteBuffer)) == -1)
                                break;
                            byteBuffer.flip();
                            byteBuffer.clear();
                        }
                    }
                };
            }

            @Override
            public String getUserVisibleName() {
                return null;
            }

            @Override
            public String getUniquePluginId() {
                return "__builtin_testing_importexport1";
            }
        };
    }

    IOverridePlugin getOverridePlugin() {
        return new IOverridePlugin() {
            @Override
            public IOverrideImplementation getOverrideImplementation() {
                return new IOverrideImplementation() {
                    @Override
                    public boolean prepare(IDialogBuilderFactory dialogBuilderFactory) {
                        return true;
                    }

                    @Override
                    public void run() throws Exception {
                    }
                };
            }

            @Override
            public String getUserVisibleName() {
                return null;
            }

            @Override
            public String getUniquePluginId() {
                return "__builtin_testing_override";
            }
        };
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void test1() throws Exception {
        EncryptionPipeline encryptionPipeline = new EncryptionPipeline(getSerializationPlugin(new Callable<Byte>() {
            @Override
            public Byte call() throws Exception {
                return 7;
            }
        }, 10000), getEncryptionPlugin(), getImportExportPlugin(), getOverridePlugin());
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
