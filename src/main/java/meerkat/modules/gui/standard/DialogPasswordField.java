package meerkat.modules.gui.standard;

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
        if( !((IPasswordValidator) validator).validate(label, getValue()) ) {
            super.setInvalid();
            return false;
        }
        return true;
    }
}
