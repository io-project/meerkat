package projekt.modules.plausible_deniability;

/**
 * Pluginy oferujące funkcjonalność niszczenia danych muszą dostarczyć implementacje logiki za pomocą tego interfejsu.
 *
 * @author Maciej Poleski
 */
public interface IOverrideImplementation {

    /* Tutaj powinien pojawić się interfejs z którego będą korzystać pozostałe moduły w celu wykorzystania
     * funkcjonalności bezpiecznego niszczenia danych. Należy pamiętać o tym, że dane można zniszczyć dopiero po
     * upewnieniu się, że proces szyfrowania i eksportu zakończył się pomyślnie.
     */
}
