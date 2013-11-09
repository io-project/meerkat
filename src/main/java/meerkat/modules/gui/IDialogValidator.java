package meerkat.modules.gui;

import java.util.Map;

/**
 * Validator służący do sprawdzenia wszystkich danych wprowadzonych przez użytkownika do okna dialogowego.
 * Używany po validacji poszczególnych pól dedykowanymi validatorami.
 *
 * @author Maciej Poleski
 */
public interface IDialogValidator {
    /**
     * Sprawdza czy wartość wszystkich pól jest akceptowalna. Można założyć że validatory poszczególnych pól zostały
     * uruchomione i wszystkie zaakceptowały.
     *
     * @param fields Mapa pól. Kluczem jest etykieta. Wartością jest obiekt typu powiązanego z danym polem tak jak w
     *               validatorach poszczególnych pól.
     * @return true - jest ok, false w przeciwnym wypadku.
     */
    boolean validate(Map<String, ?> fields);    // Jeżeli ktoś ma lepszy pomysł - jestem otwarty na sugestie
}
