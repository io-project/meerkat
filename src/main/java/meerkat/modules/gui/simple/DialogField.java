package meerkat.modules.gui.simple;

import javax.swing.JTextField;
import meerkat.modules.gui.IFieldValidator;

/**
 *
 * @author Tomasz Nocek
 */
public class DialogField {
   
    final String label;
    final JTextField textField;
    final IFieldValidator validator;

    DialogField(String label, JTextField textField, IFieldValidator validator) {
        this.label = label;
        this.textField = textField;
        this.validator = validator;
    }
}
