package meerkat.modules.gui.standard;

import java.io.File;
import javax.swing.JTextField;
import meerkat.modules.gui.IDirectoryValidator;

/**
 *
 * @author Tomasz Nocek
 */
public class DialogDirectoryField extends DialogField {

    public DialogDirectoryField(String label, JTextField textField, IDirectoryValidator validator) {
        super(label, textField, validator);
    }
            
    public File getValue() {
        File file = new File(textField.getText());
        return file;
    }

    public boolean validate() {
        if(validator == null) return true;
        if( !((IDirectoryValidator) validator).validate(label, getValue()) ) {
            super.setInvalid();
            return false;
        }
        return true;
    }
    
}
