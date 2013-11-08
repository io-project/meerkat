package meerkat.modules.core;

/**
 * Klasa może zaimplementować interfejs IJobObserver jeżeli chce być informowana o zmianach w stanie zadań.
 *
 * @author Maciej Poleski
 */
public interface IJobObserver {
    /**
     * Ta metoda jest wywoływana gdy stan zadania ulegnie zmianie.
     *
     * @param job   Zadanie którego stan uległ zmianie.
     * @param state Nowy stan zadania.
     */
    void update(IJob job, IJob.State state);
}
