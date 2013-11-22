package meerkat.modules.gui.simple;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.Border;
import meerkat.modules.core.ICore;
import meerkat.modules.gui.IDialogBuilder;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.gui.IGuiImplementation;
import sun.swing.SwingLazyValue;

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
        try {
            SwingLazyValue font12 = new SwingLazyValue(
                "javax.swing.plaf.FontUIResource", null,
                new Object[] {Font.SANS_SERIF, Font.PLAIN, 12 }
            );
            SwingLazyValue font13 = new SwingLazyValue(
                "javax.swing.plaf.FontUIResource", null,
                new Object[] {Font.SANS_SERIF, Font.PLAIN, 13 }
            );
            Color fontColor1 = new Color(200,200,200);
            Color fontColor2 = new Color(160,160,160);
            Color backgroundColor1 = new Color(32,32,32);
            Color backgroundColor2 = new Color(35,35,35);
            Color buttonColor = new Color(13,13,13);
            Color borderColor = new Color(100,100,100);
            Border border = BorderFactory.createLineBorder(borderColor, 1);
            
            Object[] defaults = {
                "Panel.background", backgroundColor1,
                "Label.font", font13,
                "Label.foreground", fontColor1,
                "ComboBox.background", backgroundColor1,
                "ComboBox.font", font12,
                "ComboBox.foreground", fontColor2,
                "ComboBox.selectionBackground", backgroundColor1,
                "ComboBox.selectionForeground", fontColor1,
                "ComboBox.border", border,
                "ComboBox.selectionBorderColor", borderColor,
                "Button.background", buttonColor,
                "Button.font", font12,
                "PasswordField.border", border,
                "PasswordField.font", font12,
                "PasswordField.foreground", fontColor2,
                "PasswordField.background", backgroundColor2,
                "TextField.border", border,
                "TextField.font", font12,
                "TextField.foreground", fontColor2,
                "TextField.background", backgroundColor2,
                
            };
            
            UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
            UIManager.getLookAndFeelDefaults().putDefaults(defaults);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
