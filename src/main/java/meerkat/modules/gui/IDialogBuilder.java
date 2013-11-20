package meerkat.modules.gui;

/**
 * GUI musi dostarczyć fabrykę obiektów implementujących ten interfejs. Służy on do budowania prostych okien
 * dialogowych na potrzeby interaktywnych pluginów. Do poszczególnych pól oraz do całego okna można dodawać validatory
 * wywołanie funkcji IDialog.exec() może zakończyć się powodzeniem tylko gdy wszystkie validatory zaakceptują dane
 * wprowadzone przez użytkownika.
 *
 * @author Maciej Poleski
 */
public interface IDialogBuilder<T extends IDialogBuilder<T>> {
    /**
     * Dodaje etykiete tekstową.
     *
     * @param label Treść etykiety
     * @return Ten sam obiekt
     */
    T addLabel(String label);
    
    /**
     * Dodaje hiperlacze.
     *
     * @param label Nazwa wyswietlana hiperlacza
     * @param url	Adres, na ktory prowadzi odsylacz
     * @return Ten sam obiekt
     */
    T addHyperLink(String label, String url);

    /**
     * Dodaje pole umożliwiające wprowadzenie jednej linii tekstu.
     *
     * @param label     Etykieta tekstowa odnosząca się do tego pola tekstowego.
     * @param validator Validator wykorzystany do oceny poprawności wprowadzonego przez użytkownika napisu.
     * @return Ten sam obiekt
     */
    T addLineEdit(String label, ILineEditValidator validator);

    /**
     * Dodaje pole umożliwiające wprowadzenie hasła.
     *
     * @param label     Etykieta tekstowa odnosząca się do tego pola na hasło.
     * @param validator Validator wykorzystany do oceny poprawności wprowadzonego przez użytkownika hasła.
     * @return Ten sam obiekt
     */
    T addPasswordEdit(String label, IPasswordValidator validator);

    /**
     * Dodaje pole umożliwiające wybór pliku.
     *
     * @param label     Etykieta tekstowa odnosząca się do tego pola na wybór pliku.
     * @param validator Validator wykorzystany do oceny poprawności wprowadzonego przez użytkownika pliku.
     * @return Ten sam obiekt
     */
    T addFileChooser(String label, IFileValidator validator);

    /**
     * Dodaje pole umożliwiające wybór katalogu.
     *
     * @param label     Etykieta tekstowa odnosząca się do tego pola na wybór katalogu.
     * @param validator Validator wykorzystany do oceny poprawności wprowadzonego przez użytkownika katalogu.
     * @return Ten sam obiekt
     */
    T addDirectoryChooser(String label, IDirectoryValidator validator);

    /**
     * Dodaje pole umożliwiające wprowadzenie jednej linii tekstu.
     *
     * @param label Etykieta tekstowa odnosząca się do tego pola tekstowego.
     * @return Ten sam obiekt
     */
    T addLineEdit(String label);

    /**
     * Dodaje pole umożliwiające wprowadzenie hasła.
     *
     * @param label Etykieta tekstowa odnosząca się do tego pola na hasło.
     * @return Ten sam obiekt
     */
    T addPasswordEdit(String label);

    /**
     * Dodaje pole umożliwiające wybór pliku.
     *
     * @param label Etykieta tekstowa odnosząca się do tego pola na wybór pliku.
     * @return Ten sam obiekt
     */
    T addFileChooser(String label);

    /**
     * Dodaje pole umożliwiające wybór katalogu.
     *
     * @param label Etykieta tekstowa odnosząca się do tego pola na wybór katalogu.
     * @return Ten sam obiekt
     */
    T addDirectoryChooser(String label);

    /**
     * Ustawia globalny validator tego okna dialogowego. Zostanie on uruchomiony po uruchomieniu validatorów dla
     * poszczególnych pól (i zaakceptowaniu ich wszystkich).
     *
     * @param validator Validator wykorzystany do oceny poprawności wprowadzonych przez użytkownika danych.
     * @return Ten sam obiekt
     */
    T setValidator(IDialogValidator validator);

    /**
     * Tworzy i zwraca nowy obiekt okna dialogowego zbudowany na podstawie wprowadzonych wcześniej danych.
     *
     * @return Obiekt okna dialogowego zbudowany na podstawie dostarczonych informacji.
     */
    IDialog build();
}
