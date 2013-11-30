package meerkat.modules.core;

/**
 * Klasa może zaimplementować interfejs IJobObserver jeżeli chce być informowana o zmianach w stanie zadań. Należy
 * pamiętać że nie wolno opierać logiki aplikacji na tej informacji. Już w chwili uruchomienia {@code update()} może
 * być ona nieaktualna.
 *
 * @author Maciej Poleski
 */
public interface IJobObserver {
    /**
     * Ta metoda jest wywoływana gdy stan zadania ulegnie (prawdopodobnie) zmianie. Nie wolno opierać logiki aplikacji
     * na tej informacji. Już w chwili wywołania tej metody informacja może być nieaktualna. Metoda może być również
     * wywołana bez widocznej dla klienta zmiany w stanie obiektu (spurious wake-up).
     *
     * @param job   Zadanie którego stan uległ zmianie.
     * @param state Nowy stan zadania. Być może już nieaktualny.
     */
    void update(IJob job, IJob.State state);
}
