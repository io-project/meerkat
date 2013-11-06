package meerkat.modules.gui;

import java.io.File;

/**
 * Interfejs udostępniany przez okno dialogowe stworzone przez buildera (IDialogBuilder). Gdy klient jest gotowy
 * powinien wywołać metodę exec(). Metoda ta kończy działanie po zakończeniu interakcji użytkownika z oknem dialogowym.
 * Jeżeli dane zostały zaakceptowane (przez użytkownika i validatory), to można uzyskać do nich dostęp wywołując
 * odpowiednie metody get...Value(String label) i podając im jako parametr etykiete podaną przy umieszczaniu danego pola
 * przez Buildera w wywołaniu add...(String label, ...).
 *
 * @author Maciej Poleski
 */
public interface IDialog {
    /**
     * Wyświetla okno dialogowe jako okno modalne aplikacji. Funkcja kończy się po zakończeniu interakcji użytkownika
     * z oknem dialogowym.
     *
     * @return true jeżeli użytkownik i validatory zaakceptowały dane, false w przeciwnym przypadku.
     */
    boolean exec();

    /**
     * @param label Etykieta
     * @return Zwraca napis wprowadzony przez użytkownika w polu LineEdit o etykiecie label
     */
    String getLineEditValue(String label);

    /**
     * @param label Etykieta
     * @return Zwraca hasło wprowadzone przez użytkownika w polu PasswordEdit o etykiecie label
     */
    char[] getPasswordValue(String label);

    /**
     * @param label Etykieta
     * @return Zwraca plik wprowadzony przez użytkownika w polu FileChooser o etykiecie label
     */
    File getFileValue(String label);

    /**
     * @param label Etykieta
     * @return Zwraca katalog wprowadzony przez użytkownika w polu DirectoryChooser o etykiecie label
     */
    File getDirectoryValue(String label);
}
