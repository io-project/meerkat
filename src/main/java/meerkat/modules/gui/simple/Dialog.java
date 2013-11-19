package meerkat.modules.gui.simple;

import java.io.File;
import java.util.concurrent.Semaphore;
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
    
    void cancel() {
        state = false;
    }
    
    boolean acquire() {
        try {
            semaphore.acquire();
        } catch (InterruptedException ex) {
            return false;
        }
        return true;
    }
    
    void release() {
        semaphore.release();
    }
    
    @Override
    public boolean exec() {
        dialogBuilder.displayDialog();
        acquire();
        dialogBuilder.removeDialog();
        return state;
    }

    @Override
    public String getLineEditValue(String label) {
        String result = "";
        for(DialogField d : dialogBuilder.lineEditFields) {
            if(d.getLabel().equals(label)) result = d.getValue();
        }
        return result;
    }

    @Override
    public char[] getPasswordValue(String label) {
        String result = "";
        for(DialogField d : dialogBuilder.passwordFields) {
            if(d.getLabel().equals(label)) result = d.getValue();
        }
        return result.toCharArray();
    }

    @Override
    public File getFileValue(String label) {
        String result = "";
        for(DialogField d : dialogBuilder.fileFields) {
            if(d.getLabel().equals(label)) result = d.getValue();
        }
        return new File(result);
    }

    @Override
    public File getDirectoryValue(String label) {
        String result = "";
        for(DialogField d : dialogBuilder.directoryFields) {
            if(d.getLabel().equals(label)) result = d.getValue();
        }
        return new File(result);
    }
    
}
