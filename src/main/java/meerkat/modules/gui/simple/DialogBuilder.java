package meerkat.modules.gui.simple;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.GroupLayout;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import meerkat.modules.gui.IDialog;
import meerkat.modules.gui.IDialogBuilder;
import meerkat.modules.gui.IDialogValidator;
import meerkat.modules.gui.IDirectoryValidator;
import meerkat.modules.gui.IFileValidator;
import meerkat.modules.gui.ILineEditValidator;
import meerkat.modules.gui.IPasswordValidator;

/**
 *
 * @author Tomasz Nocek
 */
public class DialogBuilder implements IDialogBuilder {
    
    final private JPanel panel;
    final private GroupLayout layout;
    final private ParallelGroup hGroup;
    final private SequentialGroup vGroup;
    final LinkedList<DialogField> passwordFields;
    final LinkedList<DialogField> fileFields;
    final LinkedList<DialogField> directoryFields;
    final LinkedList<DialogField> lineEditFields;  
            
    public DialogBuilder(JPanel panel) {
        this.panel = panel;
        layout = new GroupLayout(panel);
        hGroup = layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);
        vGroup = layout.createSequentialGroup().addContainerGap();
        lineEditFields = new LinkedList<>();
        fileFields = new LinkedList<>();
        directoryFields = new LinkedList<>();
        passwordFields = new LinkedList<>();
    }
    
    void clearPanel() {
        this.panel.removeAll(); 
        this.panel.updateUI();
    }
    
    @Override
    public IDialogBuilder addLabel(String label) {
        JLabel l = new JLabel(label);
        hGroup.addGroup(layout.createSequentialGroup()
            .addComponent(l)
            .addGap(0, 0, Short.MAX_VALUE)
        );
                
        vGroup.addComponent(l).addGap(18, 18, 18);
        return this;
    }

    @Override
    public IDialogBuilder addLineEdit(String label, ILineEditValidator validator) {
        JLabel l = new JLabel(label);
        JTextField x = new JTextField();
        hGroup.addGroup(layout.createSequentialGroup()
            .addComponent(l)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED,30,30)
            .addComponent(x, PREFERRED_SIZE, DEFAULT_SIZE, DEFAULT_SIZE)
        );
                    
        vGroup.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(l)
            .addComponent(x, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        ).addGap(18, 18, 18);
        
        lineEditFields.add(new DialogField(label,x,validator));
        return this;
    }

    @Override
    public IDialogBuilder addPasswordEdit(String label, IPasswordValidator validator) {
        JLabel l = new JLabel(label);
        JPasswordField x = new JPasswordField();
        hGroup.addGroup(layout.createSequentialGroup()
            .addComponent(l)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED,30,30)
            .addComponent(x, PREFERRED_SIZE, DEFAULT_SIZE, DEFAULT_SIZE)
        );
                    
        vGroup.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(l)
            .addComponent(x, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        ).addGap(18, 18, 18);
        
        passwordFields.add(new DialogField(label,x,validator));
        return this;
    }

    @Override
    public IDialogBuilder addFileChooser(String label, IFileValidator validator) {
        final JLabel l = new JLabel(label);
        final JTextField x = new JTextField();
        final JButton b = new JButton("wybierz");
        b.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                new FileChoserPopup(x).build(true);
            }
        });
        
        hGroup.addGroup(layout.createSequentialGroup()
            .addComponent(l)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED,30,30)
            .addComponent(x, PREFERRED_SIZE, DEFAULT_SIZE, DEFAULT_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED,10,10)
            .addComponent(b)
        );
                    
        vGroup.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(l)
            .addComponent(x, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(b)
        ).addGap(18, 18, 18);
        
        fileFields.add(new DialogField(label,x,validator));
        return this;
    }

    @Override
    public IDialogBuilder addDirectoryChooser(String label, IDirectoryValidator validator) {
        final JLabel l = new JLabel(label);
        final JTextField x = new JTextField();
        final JButton b = new JButton("wybierz");
        b.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                new FileChoserPopup(x).build(false);
            }
        });
        
        hGroup.addGroup(layout.createSequentialGroup()
            .addComponent(l)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED,30,30)
            .addComponent(x, PREFERRED_SIZE, DEFAULT_SIZE, DEFAULT_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED,10,10)
            .addComponent(b)
        );
                    
        vGroup.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(l)
            .addComponent(x, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(b)
        ).addGap(18, 18, 18);
                
        directoryFields.add(new DialogField(label,x,validator));
        return this;
    }

    @Override
    public IDialogBuilder addLineEdit(String label) {
        return addLineEdit(label, null);
    }

    @Override
    public IDialogBuilder addPasswordEdit(String label) {
        return addPasswordEdit(label, null);
    }

    @Override
    public IDialogBuilder addFileChooser(String label) {
        return addFileChooser(label, null);
    }

    @Override
    public IDialogBuilder addDirectoryChooser(String label) {
        return addDirectoryChooser(label, null);
    }

    @Override
    public IDialogBuilder setValidator(IDialogValidator validator) {
        return this;
    }

    private void setHorizontalGroup() {
        
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(hGroup)
                .addContainerGap()
            )
        );
    }
    
    private void setVerticalGroup() {
        
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, vGroup)
        );
    }
    
    private void initButtons(final Dialog dialog) {
        JButton b1 = new JButton("ok");
        JButton b2 = new JButton("anuluj");
        
        b1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.release();
            }
        });
        
        b2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.release();
                dialog.cancel();
            }
        });
        
        hGroup.addGroup(
                layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(b1)
                .addGap(18, 18, 18)
                .addComponent(b2)
                .addGap(0, 0, Short.MAX_VALUE)
        );
        
        vGroup.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(b1)
                .addComponent(b2)
        ).addContainerGap();
        
    }
    
    @Override
    public IDialog build() {
        panel.setLayout(layout);
        Dialog dialog = new Dialog(this);
        
        initButtons(dialog);
        setHorizontalGroup();
        setVerticalGroup();
        
        return dialog;
    }
    
}
