package meerkat.modules.gui.simple;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
import javax.swing.JSeparator;
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
    
    final private UI ui;
    final private JPanel panel;
    final private GroupLayout layout;
    final private ParallelGroup hGroup;
    final private SequentialGroup vGroup;
    final LinkedList<DialogPasswordField> passwordFields;
    final LinkedList<DialogFileField> fileFields;
    final LinkedList<DialogDirectoryField> directoryFields;
    final LinkedList<DialogLineEditField> lineEditFields;
    IDialogValidator validator;
            
    public DialogBuilder(UI ui) {
        this.ui = ui;
        panel = ui.getDialogPanel();
        layout = new GroupLayout(panel);
        hGroup = layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);
        vGroup = layout.createSequentialGroup().addGap(50, 50, 50);
        lineEditFields = new LinkedList<>();
        fileFields = new LinkedList<>();
        directoryFields = new LinkedList<>();
        passwordFields = new LinkedList<>();
        validator = null;
    }
    
    void removeDialog() {
        panel.removeAll();
        panel.updateUI();
        ui.enableComponents();
    }
    
    void displayDialog() {
        panel.setLayout(layout);
        ui.disableComponents();
    }
    
    @Override
    public IDialogBuilder addSeparator() {
        JSeparator s = new JSeparator();
        hGroup.addComponent(s, PREFERRED_SIZE, DEFAULT_SIZE, DEFAULT_SIZE);
        vGroup.addComponent(s, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
        .addGap(30, 30, 30);
        
        return this;
    }
    
    @Override
    public IDialogBuilder addLabel(String label) {
        JLabel l = new JLabel(label);
        hGroup.addGroup(layout.createSequentialGroup()
            .addComponent(l)
            .addGap(0, 0, Short.MAX_VALUE)
        );
                
        vGroup.addComponent(l).addGap(5, 5, 5);
        return this;
    }
    
    @Override
    public IDialogBuilder addHyperLink(String label, final String url) {
        JLabel l = new JLabel(label);
        l.setText("<html><a href=\"\">" + label + "</a></html>");
		l.setCursor(new Cursor(Cursor.HAND_CURSOR));
		l.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
                                    Desktop.getDesktop().browse(new URI(url));
				} catch (URISyntaxException | IOException ex) {
					// It looks like there's a problem
				}
			}
		});
		
        hGroup.addGroup(layout.createSequentialGroup()
            .addComponent(l)
            .addGap(0, 0, Short.MAX_VALUE)
        );
                
        vGroup.addComponent(l).addGap(5, 5, 5);
        return this;
    }

    @Override
    public IDialogBuilder addLineEdit(String label, ILineEditValidator validator) {
        JTextField x = new JTextField();
        hGroup.addComponent(x, PREFERRED_SIZE, DEFAULT_SIZE, DEFAULT_SIZE);
                    
        vGroup.addComponent(x, 25,25,25)
        .addGap(25, 25, 25);
        
        lineEditFields.add(new DialogLineEditField(label,x,validator));
        return this;
    }

    @Override
    public IDialogBuilder addPasswordEdit(String label, IPasswordValidator validator) {
        JPasswordField x = new JPasswordField();
        hGroup.addComponent(x, PREFERRED_SIZE, DEFAULT_SIZE, DEFAULT_SIZE);
                    
        vGroup.addComponent(x, 25,25,25)
        .addGap(25, 25, 25);
        
        passwordFields.add(new DialogPasswordField(label,x,validator));
        return this;
    }

    @Override
    public IDialogBuilder addFileChooser(String label, IFileValidator validator) {
        
        final JTextField x = new JTextField();
        final JButton b = new JButton("select");
        b.setFocusable(false);
        
        b.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                new FileChoserPopup(x).build(true);
            }
        });
        
        hGroup.addGroup(layout.createSequentialGroup()
            .addComponent(x, PREFERRED_SIZE, DEFAULT_SIZE, DEFAULT_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED,10,10)
            .addComponent(b)
        );
                    
        vGroup.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(x, 25,25,25)
            .addComponent(b)
        ).addGap(25, 25, 25);
        
        fileFields.add(new DialogFileField(label,x,validator));
        return this;
    }

    @Override
    public IDialogBuilder addDirectoryChooser(String label, IDirectoryValidator validator) {
    
        final JTextField x = new JTextField();
        final JButton b = new JButton("select");
        b.setFocusable(false);
        
        b.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                new FileChoserPopup(x).build(false);
            }
        });
        
        hGroup.addGroup(layout.createSequentialGroup()
            .addComponent(x, PREFERRED_SIZE, DEFAULT_SIZE, DEFAULT_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED,10,10)
            .addComponent(b)
        );
                    
        vGroup.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(x, 25,25,25)
            .addComponent(b)
        ).addGap(25, 25, 25);
                
        directoryFields.add(new DialogDirectoryField(label,x,validator));
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
        this.validator = validator;
        return this;
    }

    private void setHorizontalGroup() {
        
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addGroup(hGroup)
                .addGap(100, 100, 100)
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
        JButton b1 = new JButton("accept");
        JButton b2 = new JButton("cancel");
        b1.setFocusable(false);
        b2.setFocusable(false);
        
        b1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.okClicked();
            }
        });
        
        b2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.cancelClicked();
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
        ).addGap(30, 30, 30);
        
    }
    
    @Override
    public IDialog build() {
        
        Dialog dialog = new Dialog(this);
        
        initButtons(dialog);
        setHorizontalGroup();
        setVerticalGroup();
        
        return dialog;
    }
    
}
