package meerkat.modules.encryption;

import meerkat.modules.IPrepare;

import java.nio.channels.InterruptibleChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Pluginy oferujące funkcjonalność deszyfrowania muszą dostarczyć implementacje logiki za pomocą tego interfejsu.
 *
 * @author Maciej Poleski
 */
public interface IDecryptionImplementation extends IPrepare, meerkat.modules.Runnable {

    /* Tutaj powinien pojawić się interfejs z którego będą korzystać pozostałe moduły w celu wykorzystania
     * funkcjonalności szyfrowania. Należy pamiętać o tym, że przy deszyfrowaniu czasem chcemy uzyskać wejściowe
     * dane, a czasem jedynie ich część wystarczającą do wyświetlenia drzewa plików.
     */

    /**
     * Ta metoda zostanie uruchomiona przed implementacją w celu dostarczenia kanału wejściowego.
     *
     * @param channel Kanał wejściowy - plugin musi deszyfrować jego zawartość
     * @param <T>     Typ kanału który jest odczytywalny i przerywalny.
     */
    <T extends ReadableByteChannel & InterruptibleChannel> void setInputChannel(T channel);

    /**
     * Ta metoda zostanie uruchomiona przed implementacją w celu dostarczenia kanału wyjściowego.
     *
     * @param channel Kanał wyjściowy - plugin musi umieścić w nim output
     * @param <T>     Typ kanału który jest zapisywalny i przerywalny.
     */
    <T extends WritableByteChannel & InterruptibleChannel> void setOutputChannel(T channel);
}
