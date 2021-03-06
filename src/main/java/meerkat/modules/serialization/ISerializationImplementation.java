package meerkat.modules.serialization;

import meerkat.modules.IPrepare;

import java.nio.channels.InterruptibleChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Pluginy oferujące funkcjonalność serializacji muszą dostarczyć implementacje logiki szyfrowania za pomocą tego
 * interfejsu.
 *
 * @author Maciej Poleski
 */
public interface ISerializationImplementation extends IPrepare, meerkat.modules.Runnable {
    /**
     * Ta metoda zostanie uruchomiona przed implementacją w celu dostarczenia kanału wyjściowego.
     *
     * @param channel Kanał wyjściowy - plugin musi umieścić w nim output
     * @param <T>     Typ kanału który jest zapisywalny i przerywalny.
     */
    <T extends WritableByteChannel & InterruptibleChannel> void setOutputChannel(T channel);
}
