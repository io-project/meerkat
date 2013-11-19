package meerkat.modules.gui.simple;

import javax.swing.JPanel;
import meerkat.modules.core.ICore;
import meerkat.modules.gui.IDialogBuilder;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.gui.IGuiImplementation;

/**
 *
 * @author Tomasz Nocek
 */
public class GuiImplementation implements IGuiImplementation {

    private final UI ui;

    public GuiImplementation(ICore core) {
        setLookAndFeel();
        this.ui = new UI(core);
    }
    
    @Override
    public void start() {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ui.setVisible(true);
            }
        });
    }

    @Override
    public IDialogBuilderFactory getDialogBuilderFactory() {
        return new IDialogBuilderFactory() {
            
            @Override
            public IDialogBuilder newDialogBuilder() {
                return new DialogBuilder(ui);
            }
            
        };
    }

    private void setLookAndFeel() {
         /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }
}
