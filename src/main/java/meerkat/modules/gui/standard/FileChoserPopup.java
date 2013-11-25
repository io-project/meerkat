package meerkat.modules.gui.standard;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JFrame;

/**
 *
 * @author Tomasz Nocek
 */
public class FileChoserPopup {
    
    private final JTextField jFTF;
    
    public FileChoserPopup(JTextField jFTF) {
        this.jFTF = jFTF;
    }
    
    public void build(boolean enableFiles) {
        final JFrame frame = new JFrame("FileChooser");
        final Container contentPane = frame.getContentPane();

        final JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setControlButtonsAreShown(true);
        if(!enableFiles) fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setApproveButtonText("ok");
        contentPane.add(fileChooser, BorderLayout.CENTER);
        
        fileChooser.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if(JFileChooser.APPROVE_SELECTION.equals(evt.getActionCommand())) {
                    jFTF.setText(fileChooser.getSelectedFile().getPath());
                    frame.setVisible(false);
                } else if (JFileChooser.CANCEL_SELECTION.equals(evt.getActionCommand())) {
                    frame.setVisible(false);
                }
            }
        });
        
        frame.pack();
        frame.setVisible(true);
    }
}
