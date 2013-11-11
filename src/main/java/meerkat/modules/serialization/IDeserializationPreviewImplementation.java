package meerkat.modules.serialization;

import meerkat.modules.IPrepare;
import meerkat.modules.core.IResultCallback;

import java.nio.channels.InterruptibleChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * Pluginy oferujące funkcjonalność podgląda zserializowanych danych muszą dostarczyć implementacje logiki za pomocą
 * tego interfejsu.
 *
 * @author Maciej Poleski
 */
public interface IDeserializationPreviewImplementation extends IPrepare, meerkat.modules.Runnable {

    /* Tutaj powinien pojawić się interfejs z którego będą korzystać pozostałe moduły w celu wykorzystania
     * funkcjonalności serializacji. Należy pamiętać o tym, że przy deserializacji czasem chcemy uzyskać wejściowe
     * drzewo plików, a czasem jedynie dane na potrzeby wyświetlenia podglądu.
     */

    /**
     * Ta metoda zostanie uruchomiona przed implementacją w celu dostarczenia kanału wejściowego.
     *
     * @param channel Kanał wejściowy - plugin musi czytać z niego input.
     * @param <T>     Typ kanału który jest odczytywalny i przerywalny.
     */
    <T extends ReadableByteChannel & InterruptibleChannel> void setInputChannel(T channel);

    /**
     * Ta metoda zostanie wywołana przed implementacją w celu dostarczenia wywołania zwrotnego na potrzeby przekazania
     * rezultatu operacji.
     *
     * @param resultCallback Callback który powinien zostać wywołany po zakończeniu operacji z argumentem będącym
     *                       jej rezultatem.
     */
    void setResultCallback(IResultCallback resultCallback);
}
