package meerkat.modules.gui.simple;

import javax.swing.JTextField;
import meerkat.modules.gui.IFieldValidator;

/**
 *
 * @author Tomasz Nocek
 */
public class DialogField {
    
    private final String label;
    private final JTextField textField;
    private final IFieldValidator fieldValidator;
    
    public DialogField(String label, JTextField textField) {
        this.label = label;
        this.textField = textField;
        this.fieldValidator = null;
    }
    
    public DialogField(String label, JTextField textField, IFieldValidator validator) {
        this.label = label;
        this.textField = textField;
        this.fieldValidator = validator;
    }
    
    public String getLabel() {
        return label;
    }
    
    public String getValue() {
        return textField.getText();
    }
}
