package meerkat.modules.gui.simple;

import meerkat.modules.core.ICore;
import meerkat.modules.gui.IGuiImplementation;
import meerkat.modules.gui.IGuiPlugin;

/**
 *
 * @author Tomasz Nocek
 */
public class GuiPlugin implements IGuiPlugin {

    @Override
    public IGuiImplementation getImplementation(ICore core) {
        return new GuiImplementation(core);
    }
}
