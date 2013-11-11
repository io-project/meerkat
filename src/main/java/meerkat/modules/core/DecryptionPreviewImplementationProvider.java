package meerkat.modules.core;

import meerkat.modules.encryption.IDecryptionImplementation;
import meerkat.modules.import_export.IImportImplementation;
import meerkat.modules.serialization.IDeserializationPreviewImplementation;

import javax.swing.tree.TreeModel;
import java.io.IOException;
import java.nio.channels.Pipe;
import java.nio.channels.spi.SelectorProvider;

/**
 * @author Maciej Poleski
 */
class DecryptionPreviewImplementationProvider implements IDecryptionImplementationProvider<DecryptionPreviewImplementationPack, IState, TreeModel> {
    @Override
    public DecryptionPreviewImplementationPack getImplementationPackFromDecryptionPipeline(DecryptionPipeline pipeline, IImportImplementation importImplementation) {
        IDecryptionImplementation decryptionImplementation = pipeline.getEncryptionPlugin().getDecryptionImplementation();
        IDeserializationPreviewImplementation deserializationPreviewImplementation = pipeline.getSerializationPlugin().getDeserializationPreviewImplementation();
        return new DecryptionPreviewImplementationPack(importImplementation, decryptionImplementation, deserializationPreviewImplementation);
    }

    @Override
    public IState getPrepareImplementationState(final DecryptionJobTemplate<DecryptionPreviewImplementationPack, TreeModel> parent, final DecryptionPreviewImplementationPack implementationPack, final Thread importThread, final Pipe importDecryptPipe) {
        return parent.new BranchingState() {
            @Override
            public IState start() {
                if (!implementationPack.decryptionImplementation.prepare(parent.getDialogBuilderFactory()) || parent.isAborted()) {
                    abort();
                    return getCurrentState();
                }
                if (!implementationPack.deserializationPreviewImplementation.prepare(parent.getDialogBuilderFactory()) || parent.isAborted()) {
                    abort();
                    return getCurrentState();
                }
                initializeNextState(getWorkerState(implementationPack, parent, importThread, importDecryptPipe));
                return getCurrentState();
            }

            @Override
            public void abort() {
                initializeNextState(parent.new AbortedState());
            }

            @Override
            public IJob.State getState() {
                return IJob.State.PREPARING;
            }
        };
    }

    private IState getWorkerState(final DecryptionPreviewImplementationPack implementationPack, final DecryptionJobTemplate<DecryptionPreviewImplementationPack, TreeModel> parent, final Thread importThread, final Pipe importDecryptPipe) {
        return parent.new BranchingState() {
            Thread decryptThread;
            Pipe decryptDeserialPipe;
            Thread deserialThread;

            @Override
            public IState start() {
                try {
                    synchronized (this) {
                        decryptThread = new Thread(wrapRunnable(implementationPack.decryptionImplementation));
                        decryptDeserialPipe = SelectorProvider.provider().openPipe();
                        deserialThread = new Thread(wrapRunnable(implementationPack.deserializationPreviewImplementation));

                        implementationPack.decryptionImplementation.setInputChannel(importDecryptPipe.source());
                        implementationPack.decryptionImplementation.setOutputChannel(decryptDeserialPipe.sink());
                        implementationPack.deserializationPreviewImplementation.setInputChannel(decryptDeserialPipe.source());
                        final Object that = this;
                        implementationPack.deserializationPreviewImplementation.setResultCallback(new IResultCallback<TreeModel>() {
                            @Override
                            public void setResult(TreeModel result) {
                                parent.result = result;
                                initializeNextState(parent.new FinishedState());
                                that.notify();
                            }
                        });

                        decryptThread.start();
                        deserialThread.start();
                    }

                    importThread.join();
                    decryptThread.join();
                    deserialThread.join();

                    synchronized (this) {
                        while (getCurrentState() == null) {
                            wait();
                        }
                        abort();   // Trzeba posprzątać
                    }

                } catch (IOException e) {
                    abortBecauseOfFailure(e);
                } catch (InterruptedException e) {
                    // Znaczy że zadanie zostało anulowane
                }
                return getCurrentState();
            }

            @Override
            public void abort() {
                synchronized (this) {
                    initializeNextState(parent.new AbortedState());
                    deserialThread.interrupt();
                    decryptThread.interrupt();
                    importThread.interrupt();
                }
            }

            @Override
            public IJob.State getState() {
                return IJob.State.WORKING;
            }
        };
    }
}

class DecryptionPreviewImplementationPack {
    final IImportImplementation importImplementation;
    final IDecryptionImplementation decryptionImplementation;
    final IDeserializationPreviewImplementation deserializationPreviewImplementation;

    DecryptionPreviewImplementationPack(IImportImplementation importImplementation, IDecryptionImplementation decryptionImplementation, IDeserializationPreviewImplementation deserializationPreviewImplementation) {
        this.importImplementation = importImplementation;
        this.decryptionImplementation = decryptionImplementation;
        this.deserializationPreviewImplementation = deserializationPreviewImplementation;
    }
}