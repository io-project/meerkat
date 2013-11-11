package meerkat.modules.import_export;

import meerkat.modules.IPrepare;

import java.nio.channels.InterruptibleChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Pluginy oferujące funkcjonalność importowania muszą dostarczyć implementacje logiki za pomocą tego interfejsu.
 *
 * @author Maciej Poleski
 */
public interface IImportImplementation extends IPrepare, meerkat.modules.Runnable {
    /**
     * Ta metoda zostanie uruchomiona przed implementacją w celu dostarczenia kanału wyjściowego.
     *
     * @param channel Kanał wyjściowy - plugin musi zapisać do niego importowane dane.
     * @param <T>     Typ kanału który jest zapisywalny i przerywalny.
     */
    <T extends WritableByteChannel & InterruptibleChannel> void setOutputChannel(T channel);
}
