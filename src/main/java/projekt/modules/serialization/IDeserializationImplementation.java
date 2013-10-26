package projekt.modules.serialization;

/**
 * Pluginy oferujące funkcjonalność deserializacji muszą dostarczyć implementacje logiki za pomocą tego interfejsu.
 *
 * @author Maciej Poleski
 */
public interface IDeserializationImplementation {

    /* Tutaj powinien pojawić się interfejs z którego będą korzystać pozostałe moduły w celu wykorzystania
     * funkcjonalności serializacji. Należy pamiętać o tym, że przy deserializacji czasem chcemy uzyskać wejściowe
     * drzewo plików, a czasem jedynie dane na potrzeby wyświetlenia podglądu.
     */
}
