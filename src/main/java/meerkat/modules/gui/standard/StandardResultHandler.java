package meerkat.modules.gui.standard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import meerkat.modules.core.IResultHandler;

/**
 *
 * @author Tomasz Nocek
 */
public class StandardResultHandler<Void> implements IResultHandler<Void> {

    private final UI ui ;
    
    public StandardResultHandler(UI ui) {
        this.ui = ui;
    }
    
    @Override
    public void handleResult(Void result) {
        try {
            ui.getSemaphore().acquire();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        ui.clearDialogPanel();
        JButton b1 = new JButton("continue");
        b1.setFocusable(false);
        b1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                ui.enableComponents();
                ui.clearDialogPanel();
                ui.getSemaphore().release();
            }
        });

        DialogBuilder db = new DialogBuilder(ui);
        db.addLabel("operation finished successfully").addSeparator();
        db.initButton(b1);
        db.buildAndShow();
    }

    @Override
    public void handleException(Throwable t) {
        try {
            ui.getSemaphore().acquire();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        ui.clearDialogPanel();
        JButton b1 = new JButton("continue");
        b1.setFocusable(false);
        b1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                ui.enableComponents();
                ui.clearDialogPanel();
                ui.getSemaphore().release();
            }
        });

        DialogBuilder db = new DialogBuilder(ui);
        db.addLabel("operation failed").addSeparator().addLabel(t.toString());
        db.initButton(b1);
        db.buildAndShow();
    }

}
