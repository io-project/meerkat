package meerkat.modules.import_export;

import java.nio.channels.InterruptibleChannel;
import java.nio.channels.WritableByteChannel;
import meerkat.modules.IPrepare;

/**
 * Pluginy oferujące funkcjonalność importowania muszą dostarczyć implementacje logiki za pomocą tego interfejsu.
 *
 * @author Maciej Poleski
 * @author Tomasz Nocek
 */
public interface IImportImplementation extends IPrepare, meerkat.modules.Runnable {
    
    /**
     * Ta metoda zostanie uruchomiona przed implementacją w celu dostarczenia kanału wyjściowego.
     *
     * @param channel Kanał wyjściowy - plugin musi wpisać do niego zawartość pliku
     * @param <T>     Typ kanału który jest zapisywalny i przerywalny.
     */
    <T extends WritableByteChannel & InterruptibleChannel> void setOutputChannel(T channel);
}
