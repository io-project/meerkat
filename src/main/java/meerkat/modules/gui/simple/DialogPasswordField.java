package meerkat.modules.gui.simple;

import javax.swing.JTextField;
import meerkat.modules.gui.IPasswordValidator;

/**
 *
 * @author Tomasz Nocek
 */
public class DialogPasswordField extends DialogField {
    
    public DialogPasswordField(String label, JTextField textField, IPasswordValidator validator) {
        super(label, textField, validator);
    }
            
    public char[] getValue() {
        return textField.getText().toCharArray();
    }

    public boolean validate() {
        if(validator == null) return true;
        return ((IPasswordValidator) validator).validate(label, getValue());
    }
}
