package meerkat.modules.gui.standard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import meerkat.modules.core.IJob;
import meerkat.modules.core.IJobObserver;

/**
 *
 * @author Tomasz Nocek
 */
public class JobObserver implements IJobObserver {

    private final UI ui;
    
    public JobObserver(UI ui) {
        this.ui = ui;
    }
    
    @Override
    public void update(final IJob job, IJob.State state) {
        
        if(state.equals(state.PREPARING)) {
            ui.disableComponents();
        }
        
        if(state.equals(state.WORKING)) {
            JButton b1 = new JButton("abort");
            b1.setFocusable(false);
            b1.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    job.abort();
                    ui.clearDialogPanel();
                }
            });
            
            DialogBuilder db = new DialogBuilder(ui);
            db.addLabel("operation in progress").addSeparator();
            db.addProgressBar();
            db.initButton(b1);
            db.buildAndShow();
        }
        
        if(state.equals(state.FINISHED)) {
           
        }
        
        if(state.equals(state.ABORTED)) {
            ui.enableComponents();
        }
        if(state.equals(state.FAILED)) {
            ui.clearDialogPanel();
            
            JButton b1 = new JButton("continue");
            b1.setFocusable(false);
            b1.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    ui.enableComponents();
                    ui.clearDialogPanel();
                }
            });
            
            DialogBuilder db = new DialogBuilder(ui);
            db.addLabel("operation failed").addSeparator();
            db.initButton(b1);
            db.buildAndShow();
        }
    
    }
    
}
