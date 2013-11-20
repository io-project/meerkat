package meerkat.modules.gui.simple;

import java.io.File;
import javax.swing.JTextField;
import meerkat.modules.gui.IFileValidator;

/**
 *
 * @author Tomasz Nocek
 */
public class DialogFileField extends DialogField {

    public DialogFileField(String label, JTextField textField, IFileValidator validator) {
        super(label, textField, validator);
    }
            
    public File getValue() {
        File file = new File(textField.getText());
        return file;
    }

    public boolean validate() {
        if(validator == null) return true;
        return ((IFileValidator) validator).validate(label, getValue());
    }
    
}
