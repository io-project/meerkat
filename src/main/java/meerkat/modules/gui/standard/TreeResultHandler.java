package meerkat.modules.gui.standard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import meerkat.modules.core.IResultHandler;
import javax.swing.tree.TreeModel;

/**
 *
 * @author Tomasz Nocek
 */
public class TreeResultHandler implements IResultHandler<TreeModel> {

    private final UI ui ;
    
    public TreeResultHandler(UI ui) {
        this.ui = ui;
    }
    
    @Override
    public void handleResult(TreeModel result) {
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
        db.addLabel("operation finished successfully").addSeparator();
        db.addTree(result);
        db.initButton(b1);
        db.buildAndShow();
    }

    @Override
    public void handleException(Throwable t) {
        t.printStackTrace();
    }
}
