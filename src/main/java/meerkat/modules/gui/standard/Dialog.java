package meerkat.modules.gui.standard;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import javax.swing.JPanel;
import meerkat.modules.gui.IDialog;

/**
 *
 * @author Tomasz Nocek
 */
public final class Dialog implements IDialog {

    private final DialogBuilder dialogBuilder;
    private final Semaphore semaphore;
    private boolean state;
    
    public Dialog(DialogBuilder dialogBuilder) {
        this.dialogBuilder = dialogBuilder;
        semaphore = new Semaphore(1);
        state = acquire();
    }
    
    void okClicked() {
        if(validate()) {
            semaphore.release();
        }
    }
    
    void cancelClicked() {
        state = false;
        semaphore.release();
    }
    
    boolean acquire() {
        try {
            semaphore.acquire();
        } catch (InterruptedException ex) {
            return false;
        }
        return true;
    }
    
    boolean validate() {
        Map<String,Object> fields = new HashMap<>();
        
        for(DialogDirectoryField d : dialogBuilder.directoryFields) {
            fields.put(d.label, d.getValue());
            if(d.validate() == false) return false;
        }
        
        for(DialogFileField d : dialogBuilder.fileFields) {
            fields.put(d.label, d.getValue());
            if(d.validate() == false) return false; 
        }
        
        for(DialogPasswordField d : dialogBuilder.passwordFields) {
            fields.put(d.label, d.getValue());
            if(d.validate() == false) return false; 
        }
        
        for(DialogLineEditField d : dialogBuilder.lineEditFields) {
            fields.put(d.label, d.getValue());
            if(d.validate() == false) return false; 
        }
        if(dialogBuilder.validator != null) return dialogBuilder.validator.validate(fields);
        return true;
    }
    
    @Override
    public boolean exec() {
        dialogBuilder.displayContents();
        acquire();
        dialogBuilder.removeContents();
        return state;
    }
    
    @Override
    public String getLineEditValue(String label) {
        for(DialogLineEditField d : dialogBuilder.lineEditFields) {
            if(d.label.equals(label)) return d.getValue();
        }
        return null;
    }

    @Override
    public char[] getPasswordValue(String label) {
        for(DialogPasswordField d : dialogBuilder.passwordFields) {
            if(d.label.equals(label)) return d.getValue();
        }
        return null;
    }

    @Override
    public File getFileValue(String label) {
        for(DialogFileField d : dialogBuilder.fileFields) {
            if(d.label.equals(label)) return d.getValue();
        }
        return null;
    }

    @Override
    public File getDirectoryValue(String label) {
        for(DialogDirectoryField d : dialogBuilder.directoryFields) {
            if(d.label.equals(label)) return d.getValue();
        }
        return null;
    }
    
}
