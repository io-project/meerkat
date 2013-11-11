package meerkat.modules.core;

import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.import_export.IImportExportPlugin;

/**
 * Ta klasa obsługuje zadanie deszyfrowania.
 * <p/>
 * To nie jest część API.
 *
 * @author Maciej Poleski
 */
class DecryptionJobTemplate<T> extends JobWithStates {

    private DecryptionPipeline pipeline;
    private T implementationPack;
    private final IDialogBuilderFactory dialogBuilderFactory;
    private final IAbortedStateFactory abortedStateFactory = new IAbortedStateFactory() {
        @Override
        public IState newAbortedState() {
            return new AbortedState();
        }
    };

    /**
     * Tworzy nowe zadanie deszyfrowania. Po stworzeniu znajduje się ono w stanie State.READY
     *
     * @param importPlugin         Plugin importu wybrany przez użytkownika.
     * @param handler              Handler na który będzie zgłaszana zmiana stanu zadania
     * @param dialogBuilderFactory Fabryka budowniczych okien dialogowych na potrzeby pluginów.
     */
    public DecryptionJobTemplate(IImportExportPlugin importPlugin, IJobObserver handler, IDialogBuilderFactory dialogBuilderFactory) {
        super(handler);
        this.dialogBuilderFactory = dialogBuilderFactory;
        this.currentState = new ReadyState(importPlugin);
    }

    @Override
    public void abort() {
        abort(abortedStateFactory);
    }


    private class ReadyState implements IState {
        private final IImportExportPlugin importExportPlugin;

        private ReadyState(IImportExportPlugin importExportPlugin) {
            this.importExportPlugin = importExportPlugin;
        }

        @Override
        public IState start() {
            return new PreparingImportState(importExportPlugin);
        }

        @Override
        public void abort() {
            // Nie ma nic do zrobienia
        }

        @Override
        public State getState() {
            return State.READY;
        }
    }

    private class PreparingImportState extends BranchingState {
        private final IImportExportPlugin importExportPlugin;

        public PreparingImportState(IImportExportPlugin importExportPlugin) {
            this.importExportPlugin = importExportPlugin;
        }

        @Override
        public IState start() {

            return null;
        }

        @Override
        public void abort() {

        }

        @Override
        public State getState() {
            return null;
        }
    }
}
