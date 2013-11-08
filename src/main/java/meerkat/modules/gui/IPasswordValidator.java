package meerkat.modules.gui;

/**
 * Klasa może zaimplementować ten interfejs aby dostarczyć funkcjonalność weryfikacji wartości pola Password.
 *
 * @author Maciej Poleski
 */
public interface IPasswordValidator extends IFieldValidator {
    /**
     * Sprawdza czy wartość pola Password jest akceptowalna.
     *
     * @param label Etykieta pola Password.
     * @param value Wartość pola Password.
     * @return true - jest ok, false w przeciwnym wypadku.
     */
    boolean validate(String label, char[] value);
}
