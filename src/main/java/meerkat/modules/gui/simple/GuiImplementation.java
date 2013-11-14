package meerkat.modules.gui.simple;

import meerkat.modules.core.ICore;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.gui.IGuiImplementation;

/**
 *
 * @author Tomasz Nocek
 */
public class GuiImplementation implements IGuiImplementation {

    private final ICore core;
    
    public GuiImplementation(ICore core) {
        this.core = core;
    }

    @Override
    public void start() {
        setLookAndFeel();
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UI(core).setVisible(true);
            }
        });
    }

    @Override
    public IDialogBuilderFactory getDialogBuilderFactory() {
         return new DialogBuilderFactory();
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
