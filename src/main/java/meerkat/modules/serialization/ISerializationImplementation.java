package meerkat.modules.serialization;

import meerkat.modules.IPrepare;

/**
 * Pluginy oferujące funkcjonalność serializacji muszą dostarczyć implementacje logiki szyfrowania za pomocą tego
 * interfejsu.
 *
 * @author Maciej Poleski
 */
public interface ISerializationImplementation extends IPrepare {

    /* Tutaj powinien pojawić się interfejs z którego będą korzystać pozostałe moduły w celu wykorzystania
     * funkcjonalności serializacji. Należy pamiętać o tym, że przy deserializacji czasem chcemy uzyskać wejściowe
     * drzewo plików, a czasem jedynie dane na potrzeby wyświetlenia podglądu.
     */
}
