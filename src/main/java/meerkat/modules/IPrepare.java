package meerkat.modules;

import meerkat.modules.gui.IDialogBuilderFactory;

/**
 * Wszystkie klasy obsługujące przygotowanie implementują ten interfejs.
 *
 * @author Maciej Poleski
 */
public interface IPrepare {
    /**
     * Ta funkcja będzie wywołana przed uruchomieniem faktycznej implementacji. Pozwala ona zdobyć dodatkowe
     * informacje od użytkownika (interaktywnie). Funkcja musi poczekać aż użytkownik zakończy interakcje
     * i zwrócić odpowiedni rezultat.
     *
     * @param dialogBuilderFactory Fabryka budowniczych okien dialogowych do wykorzystania w celu zdobycia informacji.
     * @return true - Jeżeli przygotowanie zakończyło się powodzeniem, false - jeżeli proces został anulowany
     */
    boolean prepare(IDialogBuilderFactory<?> dialogBuilderFactory);
}
