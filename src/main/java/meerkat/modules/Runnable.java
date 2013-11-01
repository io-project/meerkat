package meerkat.modules;

/**
 * Implementacje pluginów dostarczają faktyczną funkcjonalność za pośrednictwem tego interfejsu.
 *
 * @author Maciej Poleski
 */
public interface Runnable {

    /**
     * Tutaj powinna zostać zaimplementowana główna logika każdego pluginu.
     *
     * @throws Exception Jeżeli nie uda się wykonać zadania - należy rzucić wyjątek.
     */
    void run() throws Exception;
}
