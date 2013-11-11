package meerkat.modules.core;

import meerkat.modules.import_export.IImportImplementation;

import java.nio.channels.Pipe;

/**
 * Ma za zadanie standaryzować metodę uzyskiwania odpowiednika ImplementationPack i odpowiedniej implementacji
 * wykonującej zadanie deszyfrowania (lub udawania). Pozwala na uzyskanie metody tworzącej ImplementationPack i
 * odpowiedniej implementacji stanu obiektu DecryptionJobTemplate korzystającej z odpowiedniego ImplementationPack i kontekstu.
 *
 * @param <T> typ ImplementationPack
 * @param <S> typ implementacji stanu obiektu DecryptionJobTemplate
 * @param <U> typ rezultatu
 * @author Maciej Poleski
 */
interface IDecryptionImplementationProvider<T, S, U> {

    T getImplementationPackFromDecryptionPipeline(DecryptionPipeline pipeline, IImportImplementation importImplementation);

    S getPrepareImplementationState(DecryptionJobTemplate<T, U> parent, T implementationPack, Thread importThread, Pipe importDecryptPipe);
}

// Pewne elementy można współdzielić. Np. interfejs stanu obiektów DecryptionJobTemplate tak aby można być stworzyć klasy
// konkretne parametryzowane typami T i S jak wyżej. (Typ T może być znany przez konkretną klasę np.
// DeserializationPreviewJob).