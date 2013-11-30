package meerkat.modules.core;

import meerkat.modules.import_export.IImportImplementation;

import java.nio.channels.Pipe;

/**
 * Ma za zadanie standaryzować metodę uzyskiwania odpowiednika ImplementationPack i odpowiedniej implementacji
 * wykonującej zadanie deszyfrowania (lub udawania). Pozwala na uzyskanie metody tworzącej ImplementationPack i
 * odpowiedniej implementacji stanu obiektu DecryptionJobTemplate korzystającej z odpowiedniego ImplementationPack
 * i kontekstu.
 * <p/>
 * To nie jest część API. To nieistotny detal implementacyjny.
 *
 * @param <T> typ ImplementationPack.
 * @param <S> typ implementacji stanu wewnętrznego obiektu DecryptionJobTemplate.
 * @param <U> typ rezultatu zadania przy którego wykonaniu będzie brać udział implementacja tego interfejsu.
 * @author Maciej Poleski
 */
interface IDecryptionImplementationProvider<T, S, U> {

    /**
     * Tworzy zestaw implementacji na podstawie dostarczonych danych.
     *
     * @param pipeline             Zrekonstryowany pipeline (będą z niego wybrane implementacje)
     * @param importImplementation Implementacja funkcjonalności importu (zostanie wykorzystana podana zamiast
     *                             tworzenia nowej na podstawie podanego pipeline).
     * @return Zestaw implementacji gotowy do wykorzystania przez stan produkowany przez ten interfejs.
     */
    T getImplementationPackFromDecryptionPipeline(DecryptionPipeline pipeline, IImportImplementation importImplementation);

    /**
     * Tworzy stan odpowiedzialny za przygotowanie implementacji (i dalszą obsługą procesu). Może to oznaczać
     * efektownie interakcje z użytkownikiem (ale wyłącznie za pośrednictwem innych modułów).
     *
     * @param parent             Instancja niekompletnego szablonu obsługującego proces deszyfrowania która potrzebuje brakującej
     *                           implementacji.
     * @param implementationPack Zestaw implementacji wykorzystany do deszyfrowania.
     * @param importThread       Wątek na którym odbywa się importowanie.
     * @param importDecryptPipe  Pipe służący do komunikacji między modułem importującym a deszyfrującym.
     * @return Stan odpowiedzialny za przygotowanie implementacji w oparciu o podane w parametrach dane i inne
     *         informacje uzyskane za pośrednictwem innych modułów.
     */
    S getPrepareImplementationState(DecryptionJobTemplate<T, U> parent, T implementationPack, Thread importThread, Pipe importDecryptPipe);
}
