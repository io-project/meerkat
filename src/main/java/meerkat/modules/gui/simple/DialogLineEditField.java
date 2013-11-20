package meerkat.modules.gui.simple;

import javax.swing.JTextField;
import meerkat.modules.gui.ILineEditValidator;

/**
 *
 * @author Tomasz Nocek
 */
public class DialogLineEditField extends DialogField {
    
    public DialogLineEditField(String label, JTextField textField, ILineEditValidator validator) {
        super(label, textField, validator);
    }
            
    public String getValue() {
        return textField.getText();
    }

    public boolean validate() {
        if(validator == null) return true;
        return ((ILineEditValidator) validator).validate(label, getValue());
    }
}
