package meerkat.modules.plausible_deniability.none;

import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.plausible_deniability.IOverrideImplementation;

/**
 * @author Tomasz Nocek
 */
public class NoOverrideImplementation implements IOverrideImplementation {

    @Override
    public boolean prepare(IDialogBuilderFactory<?> dialogBuilderFactory) {
        return true;
    }

    @Override
    public void run() throws Exception {
        // do nothing
    }

}
