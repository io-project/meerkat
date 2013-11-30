package meerkat.modules.core;

/**
 * Interfejs stanu wewnętrznego zadań.
 * <p/>
 * To nie jest część API. To nieistotny detal implementacyjny.
 *
 * @author Maciej Poleski
 */
interface IState {
    /**
     * Uruchamia logikę odpowiedzialną za obsługę tego stanu. Wywołanie blokujące.
     *
     * @return Stan do którego należy przejść (this - koniec)
     */
    IState start();

    /**
     * Służy do anulowania zadania znajdującego się w tym stanie. Może (ale nie musi) wpłynąć na rezultat funkcji
     * {@code start()}.
     */
    void abort();

    /**
     * Przedstawia się w formie zrozumiałej dla obserwatora.
     *
     * @return Najlepsze dopasowanie tego stanu do któregoś z pól typu wyliczeniowego {@code IJob.State}.
     */
    IJob.State getState();
}

/**
 * Interfejs znacznikowy. Zadanie znajdujące się w tym stanie można anulować.
 * <p/>
 * To nie jest część API. To nieistotny detal implementacyjny.
 */
interface IAbortableState {
}