package meerkat.modules.import_export;

import meerkat.modules.IPrepare;

import java.nio.channels.InterruptibleChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * Pluginy oferujące funkcjonalność eksportowania muszą dostarczyć implementacje logiki za pomocą tego interfejsu.
 *
 * @author Maciej Poleski
 */
public interface IExportImplementation extends IPrepare, meerkat.modules.Runnable {
    /**
     * Ta metoda zostanie uruchomiona przed implementacją w celu dostarczenia kanału wejściowego.
     *
     * @param channel Kanał wejściowy - plugin musi wyeksportować jego zawartość
     * @param <T>     Typ kanału który jest odczytywalny i przerywalny.
     */
    <T extends ReadableByteChannel & InterruptibleChannel> void setInputChannel(T channel);
}
