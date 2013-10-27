package meerkat.modules.gui;

/**
 * Pluginy GUI muszą dostarczyć funkcjonalność implementując ten interfejs.
 *
 * @author Maciej Poleski
 */
public interface IGuiImplementation {

    /**
     * Tworzy funkcjonalny interfejs użytkownika w oparciu o dane udostępnione przez moduł Core.
     */
    void start();
}
