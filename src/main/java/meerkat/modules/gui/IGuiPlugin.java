package meerkat.modules.gui;

import meerkat.modules.core.ICore;

/**
 * Pluginy dostarczające GUI muszą implementować ten interfejs
 *
 * @author Maciej Poleski
 */
public interface IGuiPlugin {

    /**
     * Zwraca implementacje logiki GUI.
     *
     * @param core Implementacja ICore zdolna do kierowania pozostałymi modułami.
     */
    IGuiImplementation getImplementation(ICore core);
}
