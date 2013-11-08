package meerkat.modules.gui;

import java.io.File;

/**
 * Klasa może zaimplementować ten interfejs aby dostarczyć funkcjonalność weryfikacji wartości pola FileChooser.
 *
 * @author Maciej Poleski
 */
public interface IFileValidator extends IFieldValidator {
    /**
     * Sprawdza czy wartość pola FileChooser jest akceptowalna.
     *
     * @param label Etykieta pola FileChooser.
     * @param value Wartość pola FileChooser.
     * @return true - jest ok, false w przeciwnym wypadku.
     */
    boolean validate(String label, File value);
}
