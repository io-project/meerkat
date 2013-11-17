package meerkat.modules.core;

import meerkat.modules.encryption.IDecryptionImplementation;
import meerkat.modules.import_export.IImportImplementation;
import meerkat.modules.serialization.IDeserializationImplementation;

import java.io.IOException;
import java.nio.channels.Pipe;
import java.nio.channels.spi.SelectorProvider;

/**
 * @author Maciej Poleski
 */
class DecryptionImplementationProvider implements IDecryptionImplementationProvider<DecryptionImplementationPack, IState, Void> {
    @Override
    public DecryptionImplementationPack getImplementationPackFromDecryptionPipeline(DecryptionPipeline pipeline, IImportImplementation importImplementation) {
        IDecryptionImplementation decryptionImplementation = pipeline.getEncryptionPlugin().getDecryptionImplementation();
        IDeserializationImplementation deserializationImplementation = pipeline.getSerializationPlugin().getDeserializationImplementation();
        return new DecryptionImplementationPack(importImplementation, decryptionImplementation, deserializationImplementation);
    }

    @Override
    public IState getPrepareImplementationState(final DecryptionJobTemplate<DecryptionImplementationPack, Void> parent, final DecryptionImplementationPack implementationPack, final Thread importThread, final Pipe importDecryptPipe) {
        return parent.new BranchingState() {
            @Override
            public IState start() {
                if (!implementationPack.decryptionImplementation.prepare(parent.getDialogBuilderFactory()) || parent.isAborted()) {
                    abort();
                    return getCurrentState();
                }
                if (!implementationPack.deserializationImplementation.prepare(parent.getDialogBuilderFactory()) || parent.isAborted()) {
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

    IState getWorkerState(final DecryptionImplementationPack implementationPack, final DecryptionJobTemplate<DecryptionImplementationPack, Void> parent, final Thread importThread, final Pipe importDecryptPipe) {
        return parent.new BranchingState() {
            Thread decryptThread;
            Pipe decryptDeserialPipe;
            Thread deserialThread;

            @Override
            public IState start() {
                try {
                    synchronized (this) {
                        decryptThread = new Thread(wrapRunnable(implementationPack.decryptionImplementation), "Decryption Thread");
                        decryptDeserialPipe = SelectorProvider.provider().openPipe();
                        deserialThread = new Thread(wrapRunnable(implementationPack.deserializationImplementation), "Deserialization Thread");

                        implementationPack.decryptionImplementation.setInputChannel(importDecryptPipe.source());
                        implementationPack.decryptionImplementation.setOutputChannel(decryptDeserialPipe.sink());
                        implementationPack.deserializationImplementation.setInputChannel(decryptDeserialPipe.source());

                        decryptThread.start();
                        deserialThread.start();
                    }

                    importThread.join();
                    importDecryptPipe.sink().close();
                    decryptThread.join();
                    importDecryptPipe.source().close();
                    decryptDeserialPipe.sink().close();
                    deserialThread.join();
                    decryptDeserialPipe.source().close();

                    initializeNextState(parent.new FinishedState());

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

/**
 * @author Maciej Poleski
 */
class DecryptionImplementationPack {
    final IImportImplementation importImplementation;
    final IDecryptionImplementation decryptionImplementation;
    final IDeserializationImplementation deserializationImplementation;

    DecryptionImplementationPack(IImportImplementation importImplementation, IDecryptionImplementation decryptionImplementation, IDeserializationImplementation deserializationImplementation) {
        this.importImplementation = importImplementation;
        this.decryptionImplementation = decryptionImplementation;
        this.deserializationImplementation = deserializationImplementation;
    }
}