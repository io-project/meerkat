package meerkat.modules.gui.standard;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import meerkat.modules.gui.IFieldValidator;

/**
 *
 * @author Tomasz Nocek
 */
public class DialogField {
   
    final String label;
    final JTextField textField;
    final IFieldValidator validator;
    final Border border;
    final Border invalidBorder;
    
    DialogField(String label, JTextField textField, IFieldValidator validator) {
        this.label = label;
        this.textField = textField;
        this.validator = validator;
        this.border = textField.getBorder();
        this.invalidBorder = BorderFactory.createLineBorder(new Color(200,10,10), 1);
        setListener();
    }
    
    public void setInvalid() {
        textField.setBorder(invalidBorder);
    }
    
    private void setListener(){
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                textField.setBorder(border);
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                textField.setBorder(border);
            }
            @Override
            public void changedUpdate(DocumentEvent e) {}
        });
    }
}
