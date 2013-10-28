package meerkat.modules.gui;

/**
 * Fabryka budowniczych okien dialogowych. Implementacja powinna być state-less.
 *
 * @author Maciej Poleski
 */
public interface IDialogBuilderFactory {

    /**
     * Tworzy i zwraca nowego budowniczego okien dialogowych.
     */
    IDialogBuilder newDialogBuilder();
}
