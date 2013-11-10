package meerkat.modules.core;

/**
 * Ma za zadanie standaryzować metodę uzyskiwania odpowiednika ImplementationPack i odpowiedniej implementacji
 * wykonującej zadanie deszyfrowania (lub udawania). Pozwala na uzyskanie metody tworzącej ImplementationPack i
 * odpowiedniej implementacji stanu obiektu DecryptionJob korzystającej z odpowiedniego ImplementationPack i kontekstu.
 *
 * @param <T> typ ImplementationPack
 * @param <S> typ implementacji stanu obiektu DecryptionJob
 * @author Maciej Poleski
 */
interface IDecryptionImplementationProvider<T, S> {

    T getImplementationPackFromDecryptionPipeline(DecryptionPipeline pipeline);

    S getPrepareImplementationState(T implementationPack);

    S getWorkerState(T implementationPack);
}

// Pewne elementy można współdzielić. Np. interfejs stanu obiektów DecryptionJob tak aby można być stworzyć klasy
// konkretne parametryzowane typami T i S jak wyżej. (Typ T może być znany przez konkretną klasę np.
// DeserializationPreviewJob).