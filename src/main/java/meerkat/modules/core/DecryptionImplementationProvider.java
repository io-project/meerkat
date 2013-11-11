package meerkat.modules.core;

import meerkat.modules.encryption.IDecryptionImplementation;
import meerkat.modules.import_export.IImportImplementation;
import meerkat.modules.serialization.IDeserializationImplementation;

import java.nio.channels.Pipe;

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

    public IState getWorkerState(DecryptionImplementationPack implementationPack, DecryptionJobTemplate<DecryptionImplementationPack, Void> parent, Thread importThread, Pipe importDecryptPipe) {
        return parent.new BranchingState() {
            @Override
            public IState start() {
                return null;
            }

            @Override
            public void abort() {

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