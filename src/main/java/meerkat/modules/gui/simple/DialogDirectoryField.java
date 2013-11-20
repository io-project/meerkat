package meerkat.modules.gui.simple;

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
        return ((IDirectoryValidator) validator).validate(label, getValue());
    }
    
}
