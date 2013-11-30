package meerkat.modules.gui;

/**
 * Pluginy GUI muszą dostarczyć funkcjonalność implementując ten interfejs.
 *
 * @author Maciej Poleski
 */
public interface IGuiImplementation {

    /**
     * Tworzy funkcjonalny interfejs użytkownika w oparciu o dane udostępnione przez moduł Core. Funkcja może
     * natychmiast się zakończyć lub czekać aż do zakończenia pracy aplikacji przez użytkownika.
     */
    void start();

    /**
     * Zwraca fabrykę budowniczych okien dialogowych.
     *
     * @return Fabryka budowniczych okien dialogowych. Zostanie ona dostarczona do wszystkich pluginów i wykorzystana
     *         przez nie do uzyskania dodatkowych informacji od użytkownika.
     */
    IDialogBuilderFactory<?> getDialogBuilderFactory();
}
