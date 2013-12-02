package meerkat.modules.core;

import meerkat.modules.IPlugin;
import meerkat.modules.PluginNotFoundException;
import meerkat.modules.Runnable;
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

import javax.swing.tree.TreeModel;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;

class PluginsProvider implements IPluginManager {

    private final List<ISerializationPlugin> serializationPluginList = new ArrayList<>();
    private final List<IEncryptionPlugin> encryptionPluginList = new ArrayList<>();
    private final List<IImportExportPlugin> importExportPluginList = new ArrayList<>();
    private final List<IOverridePlugin> overridePluginList = new ArrayList<>();
    private int serializationPluginsCount = 0;
    private int encryptionPluginsCount = 0;
    private int importExportPluginsCount = 0;
    private int overridePluginsCount = 0;

    ISerializationPlugin getSerializationPlugin(final Callable<Byte> dataSource, final int dataLength, final String fileName, final TreeModel treeModel) {
        return getSerializationPlugin(dataSource, dataLength, fileName, treeModel, null);
    }

    ISerializationPlugin getSerializationPlugin(final Callable<Byte> dataSource, final int dataLength, final String fileName, final TreeModel treeModel, final Runnable injectionToDeserialization) {
        ISerializationPlugin result = new ISerializationPlugin() {
            final ArrayList<Byte> serializedData = new ArrayList<>();
            final String pluginId = "__builtin__testing_serialization" + serializationPluginsCount++;

            @Override
            public ISerializationImplementation getSerializationImplementation() {
                return new ISerializationImplementation() {
                    WritableByteChannel channel;

                    @Override
                    public <T extends WritableByteChannel & InterruptibleChannel> void setOutputChannel(T channel) {
                        this.channel = channel;
                    }

                    @Override
                    public boolean prepare(IDialogBuilderFactory<?> dialogBuilderFactory) {
                        return true;
                    }

                    @Override
                    public void run() throws Exception {
                        ObjectOutputStream oos = new ObjectOutputStream(Channels.newOutputStream(channel));
                        oos.writeObject(fileName);
                        ByteBuffer byteBuffer = ByteBuffer.allocate(dataLength);
                        for (int i = 0; i < dataLength; ++i) {
                            byte d = dataSource.call();
                            serializedData.add(d);
                            byteBuffer.put(d);
                        }
                        byteBuffer.flip();
                        channel.write(byteBuffer);
                    }
                };
            }

            @Override
            public IDeserializationImplementation getDeserializationImplementation() {
                return new IDeserializationImplementation() {
                    ReadableByteChannel channel;

                    @Override
                    public <T extends ReadableByteChannel & InterruptibleChannel> void setInputChannel(T channel) {
                        this.channel = channel;
                    }

                    @Override
                    public boolean prepare(IDialogBuilderFactory<?> dialogBuilderFactory) {
                        return true;
                    }

                    @Override
                    public void run() throws Exception {
                        ObjectInputStream ois = new ObjectInputStream(Channels.newInputStream(channel));
                        assertEquals(fileName, ois.readObject());
                        int i = 0;
                        int j;
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int n;
                        if (injectionToDeserialization != null)
                            injectionToDeserialization.run();
                        while ((n = channel.read(buffer)) != -1) {
                            buffer.flip();
                            for (j = 0; j < n; ++i, ++j) {
                                byte d = buffer.get();
                                assertEquals(d, (byte) serializedData.get(i));
                            }
                            buffer.clear();
                        }
                        serializedData.clear();
                    }
                };
            }

            @Override
            public IDeserializationPreviewImplementation getDeserializationPreviewImplementation() {
                return new IDeserializationPreviewImplementation() {
                    ReadableByteChannel channel;
                    private IResultCallback<TreeModel> resultCallback;

                    @Override
                    public <T extends ReadableByteChannel & InterruptibleChannel> void setInputChannel(T channel) {
                        this.channel = channel;
                    }

                    @Override
                    public void setResultCallback(IResultCallback<TreeModel> resultCallback) {
                        this.resultCallback = resultCallback;
                    }

                    @Override
                    public boolean prepare(IDialogBuilderFactory<?> dialogBuilderFactory) {
                        return true;
                    }

                    @Override
                    public void run() throws Exception {
                        ObjectInputStream ois = new ObjectInputStream(Channels.newInputStream(channel));
                        assertEquals(fileName, ois.readObject());
                        resultCallback.setResult(treeModel);
                    }
                };
            }

            @Override
            public String getUserVisibleName() {
                return null; // We mean it
            }

            @Override
            public String getUniquePluginId() {
                return pluginId;
            }
        };
        serializationPluginList.add(result);
        return result;
    }

    IEncryptionPlugin getEncryptionPlugin() {
        IEncryptionPlugin result = new IEncryptionPlugin() {
            final String pluginId = "__builtin_testing_encryption" + encryptionPluginsCount++;

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
                    public boolean prepare(IDialogBuilderFactory<?> dialogBuilderFactory) {
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
                    }
                };
            }

            @Override
            public IDecryptionImplementation getDecryptionImplementation() {
                return new IDecryptionImplementation() {
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
                    public boolean prepare(IDialogBuilderFactory<?> dialogBuilderFactory) {
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
                    }
                };
            }

            @Override
            public String getUserVisibleName() {
                return null;
            }

            @Override
            public String getUniquePluginId() {
                return pluginId;
            }
        };
        encryptionPluginList.add(result);
        return result;
    }

    IImportExportPlugin getImportExportPlugin() {
        return getImportExportPlugin(false);
    }

    IImportExportPlugin getImportExportPlugin(final boolean hangInImport) {
        IImportExportPlugin result = new IImportExportPlugin() {
            final ArrayList<Byte> exportedData = new ArrayList<>();
            final String pluginId = "__builtin_testing_importexport" + importExportPluginsCount++;

            @Override
            public IImportImplementation getImportImplementation() {
                return new IImportImplementation() {
                    private WritableByteChannel channel;

                    @Override
                    public <T extends WritableByteChannel & InterruptibleChannel> void setOutputChannel(T channel) {
                        this.channel = channel;
                    }

                    @Override
                    public boolean prepare(IDialogBuilderFactory<?> dialogBuilderFactory) {
                        return true;
                    }

                    @Override
                    public void run() throws Exception {
                        byte[] d = new byte[exportedData.size()];
                        for (int i = 0; i < d.length; ++i) {
                            d[i] = exportedData.get(i);
                        }
                        ByteBuffer byteBuffer = ByteBuffer.wrap(d);
                        assertEquals(d.length, channel.write(byteBuffer));
                        exportedData.clear();
                        if (hangInImport) {
                            synchronized (this) {
                                wait();
                            }
                        }
                    }
                };
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
                    public boolean prepare(IDialogBuilderFactory<?> dialogBuilderFactory) {
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
                            byte[] d = new byte[n];
                            byteBuffer.get(d);
                            for (int i = 0; i < n; ++i) {
                                exportedData.add(d[i]);
                            }
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
                return pluginId;
            }
        };
        importExportPluginList.add(result);
        return result;
    }

    IOverridePlugin getOverridePlugin() {
        IOverridePlugin result = new IOverridePlugin() {
            final String pluginId = "__builtin_testing_override" + overridePluginsCount++;

            @Override
            public IOverrideImplementation getOverrideImplementation() {
                return new IOverrideImplementation() {
                    @Override
                    public boolean prepare(IDialogBuilderFactory<?> dialogBuilderFactory) {
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
                return pluginId;
            }
        };
        overridePluginList.add(result);
        return result;
    }

    private <T extends IPlugin> T getPluginForId(String id, List<T> plugins) throws PluginNotFoundException {
        for (T p : plugins) {
            if (p.getUniquePluginId().equals(id)) {
                return p;
            }
        }
        throw new PluginNotFoundException(id);
    }

    @Override
    public ISerializationPlugin getSerializationPluginForId(String id) throws PluginNotFoundException {
        return getPluginForId(id, serializationPluginList);
    }

    @Override
    public IEncryptionPlugin getEncryptionPluginForId(String id) throws PluginNotFoundException {
        return getPluginForId(id, encryptionPluginList);
    }
}