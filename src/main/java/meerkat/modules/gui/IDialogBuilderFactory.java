package meerkat.modules.gui;

/**
 * Fabryka budowniczych okien dialogowych. Implementacja powinna być state-less.
 *
 * @param <T> typ budowniczego okna dialogowego zwracanego przez newDialogBuilder().
 * @author Maciej Poleski
 */
public interface IDialogBuilderFactory<T extends IDialogBuilder<T>> {

    /**
     * Tworzy i zwraca nowego budowniczego okien dialogowych.
     */
    T newDialogBuilder();
}
