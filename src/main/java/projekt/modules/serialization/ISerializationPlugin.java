package projekt.modules.serialization;

import projekt.modules.IPlugin;

/**
 * Pluginy obsługujące proces serializacji muszą implementować ten interfejs.
 *
 * @author Maciej Poleski
 */
public interface ISerializationPlugin extends IPlugin {

    /**
     * Zwraca implementacje funkcjonalności serializacji.
     */
    ISerializationImplementation getSerializationImplementation();

    /**
     * Zwraca implementacje funkcjonalności pełnej deserializacji (przywracania struktury plików we wskazanej
     * lokalizacji.
     */
    IDeserializationImplementation getDeserializationImplementation();

    /**
     * Zwraca implementacje funkcjonalności podglądu zaszyfrowanych danych (dostarcza dane do wyświetlenia w GUI).
     */
    IDeserializationPreviewImplementation getDeserializationPreviewImplementation();
}
