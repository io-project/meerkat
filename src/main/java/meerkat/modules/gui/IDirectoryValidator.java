package meerkat.modules.gui;

import java.io.File;

/**
 * Klasa może zaimplementować ten interfejs aby dostarczyć funkcjonalność weryfikacji wartości pola DirectoryChooser.
 *
 * @author Maciej Poleski
 */
public interface IDirectoryValidator extends IFieldValidator {
    /**
     * Sprawdza czy wartość pola DirectoryChooser jest akceptowalna.
     *
     * @param label Etykieta pola DirectoryChooser.
     * @param value Wartość pola DirectoryChooser.
     * @return true - jest ok, false w przeciwnym wypadku.
     */
    boolean validate(String label, File value);
}
