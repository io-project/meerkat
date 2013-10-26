package projekt.modules.encryption;

/**
 * Pluginy oferujące funkcjonalność szyfrowania muszą dostarczyć implementacje logiki za pomocą tego interfejsu.
 *
 * @author Maciej Poleski
 */
public interface IEncryptionImplementation {

    /* Tutaj powinien pojawić się interfejs z którego będą korzystać pozostałe moduły w celu wykorzystania
     * funkcjonalności szyfrowania. Należy pamiętać o tym, że przy deszyfrowaniu czasem chcemy uzyskać wejściowe
     * dane, a czasem jedynie ich część wystarczającą do wyświetlenia drzewa plików.
     */
}
