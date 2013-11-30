package meerkat.modules;

/**
 * Pluginy muszą implementować ten interfejs.
 * <p/>
 * Tak naprawdę dopiero klasy implementujące interfejsy pochodne mogą być rejestrowane jako pluginy. Tutaj jest
 * zebrana wspólna funkcjonalność.
 *
 * @author Maciej Poleski
 */
public interface IPlugin {

    /**
     * Zwraca nazwę pluginu do wiadomości użytkownika.
     * Można ją wykorzystać w GUI. Nie należy jej przetwarzać maszynowo (opierać na niej logiki).
     *
     * @return Czytelna dla człowieka, zwięzła nazwa pluginu.
     */
    String getUserVisibleName();

    /**
     * Zwraca unikatowy identyfikator pluginu.
     * <p/>
     * Przykładowo identyfikatorem może być pełna nazwa pakietu w którym znajduje się implementacja danego pluginu.
     * <p/>
     * Te dane będą wykorzystane w celu zidentyfikowania wykorzystanego algorytmu w celu automatycznego wyboru
     * implementacji odwracającej (serializacja->deserializacja, szyfrowanie->deszyfrowanie).
     *
     * @return Unikalny identyfikator tego pluginu.
     */
    String getUniquePluginId();

}
