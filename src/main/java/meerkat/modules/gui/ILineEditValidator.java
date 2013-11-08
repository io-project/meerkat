package meerkat.modules.gui;

/**
 * Klasa może zaimplementować ten interfejs aby dostarczyć funkcjonalność weryfikacji wartości pola LineEdit.
 *
 * @author Maciej Poleski
 */
public interface ILineEditValidator extends IFieldValidator {
    /**
     * Sprawdza czy wartość pola LineEdit jest akceptowalna.
     *
     * @param label Etykieta pola LineEdit.
     * @param value Wartość pola LineEdit.
     * @return true - jest ok, false w przeciwnym wypadku.
     */
    boolean validate(String label, String value);
}
