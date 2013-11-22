package meerkat.modules.gui.simple;

import java.awt.CardLayout;
import javax.swing.ImageIcon;
import javax.swing.tree.TreeModel;
import meerkat.modules.core.EncryptionPipeline;
import meerkat.modules.core.ICore;
import meerkat.modules.core.IJob;
import meerkat.modules.core.IJobObserver;
import meerkat.modules.core.IResultHandler;
import meerkat.modules.encryption.IEncryptionPlugin;
import meerkat.modules.import_export.IImportExportPlugin;
import meerkat.modules.plausible_deniability.IOverridePlugin;
import meerkat.modules.serialization.ISerializationPlugin;

/**
 *
 * @author Tomasz Nocek
 */
public class UI extends javax.swing.JFrame {

    private final ICore core;
    
    public UI(ICore core) {
        ImageIcon icon  = new ImageIcon("meerkat.png","");
        setIconImage(icon.getImage());
        
        this.core = core;
        initComponents();
        initComboBoxes();
    }
    
    javax.swing.JPanel getDialogPanel() {
        return jPanel4;
    }
    
    void disableComponents(){
        jLabel8.setEnabled(false);
        jLabel10.setEnabled(false);
        jLabel12.setEnabled(false);
    }
    
    void enableComponents(){
        jLabel8.setEnabled(true);
        jLabel10.setEnabled(true);
        jLabel12.setEnabled(true);
    }
    
    private void initComboBoxes() {
        
        for(ISerializationPlugin p : core.getSerializationPlugins()) {
            jComboBox1.addItem(p.getUserVisibleName());
        }
        for(IEncryptionPlugin p : core.getEncryptionPlugins()) {
            jComboBox2.addItem(p.getUserVisibleName());
        }
        for(IImportExportPlugin p : core.getImportExportPlugins()) {
            jComboBox3.addItem(p.getUserVisibleName());
            jComboBox8.addItem(p.getUserVisibleName());
            jComboBox6.addItem(p.getUserVisibleName());
        }
        for(IOverridePlugin p : core.getOverridePlugins()) {
            jComboBox4.addItem(p.getUserVisibleName());
        }
    }
    
    private void startEncryption() {
        
        ISerializationPlugin p1 = null;
        IEncryptionPlugin p2 = null;
        IImportExportPlugin p3 = null;
        IOverridePlugin p4 = null;
        
        for(ISerializationPlugin p : core.getSerializationPlugins()) {
            if(jComboBox1.getSelectedItem().equals(p.getUserVisibleName())) p1=p;
        }
        for(IEncryptionPlugin p : core.getEncryptionPlugins()) {
           if(jComboBox2.getSelectedItem().equals(p.getUserVisibleName())) p2=p;
        }
        for(IImportExportPlugin p : core.getImportExportPlugins()) {
            if(jComboBox3.getSelectedItem().equals(p.getUserVisibleName())) p3=p;
        }
        for(IOverridePlugin p : core.getOverridePlugins()) {
            if(jComboBox4.getSelectedItem().equals(p.getUserVisibleName())) p4=p;
        }
        
        EncryptionPipeline pipeline = new EncryptionPipeline(p1,p2,p3,p4);
        IJobObserver observer = new IJobObserver() {
            @Override
            public void update(IJob job, IJob.State state) {
                // TODO implement
            }
        };
        IResultHandler<Void> resultHandler = new IResultHandler(){

            @Override
            public void handleResult(Object result) {
                // TODO implement
            }
            @Override
            public void handleException(Throwable t) {
                // TODO implement
                t.printStackTrace();
            }
        };
        
        core.prepareEncryptionJob(pipeline, observer, resultHandler).start();
    }
    
    private void startDecryption() {
        
        IImportExportPlugin p8 = null;
        for(IImportExportPlugin p : core.getImportExportPlugins()) {
            if(jComboBox8.getSelectedItem().equals(p.getUserVisibleName())) p8=p;
        }
        
        IJobObserver observer = new IJobObserver() {
            @Override
            public void update(IJob job, IJob.State state) {
                // TODO implement
            }
        };
        IResultHandler<Void> resultHandler = new IResultHandler(){

            @Override
            public void handleResult(Object result) {
                // TODO implement
            }
            @Override
            public void handleException(Throwable t) {
                // TODO implement
                t.printStackTrace();
            }
        };
        
        core.prepareDecryptionJob(p8, observer, resultHandler).start();
    }
    
    private void startPreview() {
        IImportExportPlugin p6 = null;
        for(IImportExportPlugin p : core.getImportExportPlugins()) {
            if(jComboBox6.getSelectedItem().equals(p.getUserVisibleName())) p6=p;
        }
        
        IJobObserver observer = new IJobObserver() {
            @Override
            public void update(IJob job, IJob.State state) {
                // TODO implement
            }
        };
        IResultHandler<TreeModel> resultHandler = new IResultHandler(){

            @Override
            public void handleResult(Object result) {
                // TODO implement
            }
            @Override
            public void handleException(Throwable t) {
                // TODO implement
                t.printStackTrace();
            }
        };
        
        core.prepareDecryptionPreviewJob(p6, observer, resultHandler);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox3 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jComboBox4 = new javax.swing.JComboBox();
        jComboBox7 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jComboBox5 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jComboBox8 = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jComboBox6 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jComboBox9 = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("meerkat");
        setMinimumSize(new java.awt.Dimension(800, 500));

        jPanel3.setName("cards"); // NOI18N
        jPanel3.setLayout(new java.awt.CardLayout());

        jComboBox1.setFocusable(false);
        jComboBox1.setInheritsPopupMenu(true);
        jComboBox1.setPreferredSize(new java.awt.Dimension(180, 28));

        jComboBox3.setFocusable(false);
        jComboBox3.setPreferredSize(new java.awt.Dimension(180, 28));

        jComboBox2.setFocusable(false);
        jComboBox2.setPreferredSize(new java.awt.Dimension(180, 28));

        jComboBox4.setFocusable(false);
        jComboBox4.setPreferredSize(new java.awt.Dimension(180, 28));

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "encryption", "decryption", "preview" }));
        jComboBox7.setFocusable(false);
        jComboBox7.setPreferredSize(new java.awt.Dimension(190, 28));
        jComboBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox7ActionPerformed(evt);
            }
        });

        jLabel4.setText("serialization plugin");

        jLabel5.setText("encryption plugin");

        jLabel1.setText("export plugin");

        jLabel6.setText("override plugin");

        jLabel8.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel8.setText("start encryption");
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });

        jLabel7.setText("operation type");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox4, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox3, 0, 190, Short.MAX_VALUE))
                        .addGap(20, 20, 20))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel1, "encryption");

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "decryption", "encryption", "preview" }));
        jComboBox5.setFocusable(false);
        jComboBox5.setPreferredSize(new java.awt.Dimension(190, 28));
        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });

        jLabel2.setText("operation type");

        jLabel9.setText("import plugin");

        jComboBox8.setFocusable(false);
        jComboBox8.setPreferredSize(new java.awt.Dimension(190, 28));

        jLabel10.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel10.setText("start decryption");
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jLabel10)
                .addContainerGap(246, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel2, "decryption");

        jComboBox6.setFocusable(false);
        jComboBox6.setPreferredSize(new java.awt.Dimension(190, 28));

        jLabel3.setText("import plugin");

        jLabel11.setText("operation type");

        jComboBox9.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "preview", "encryption", "decryption" }));
        jComboBox9.setFocusable(false);
        jComboBox9.setPreferredSize(new java.awt.Dimension(190, 28));
        jComboBox9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox9ActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel12.setText("start preview");
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel11)
                    .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jLabel12)
                .addContainerGap(246, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel5, "preview");

        jPanel4.setBackground(new java.awt.Color(35, 35, 35));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 226, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        if(jLabel8.isEnabled()) {
            startEncryption();
        }
    }//GEN-LAST:event_jLabel8MouseClicked

    private void jComboBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox7ActionPerformed
        CardLayout cl = (CardLayout)(jPanel3.getLayout());
        cl.show(jPanel3, jComboBox7.getSelectedItem().toString());
        jComboBox7.setSelectedIndex(0);
    }//GEN-LAST:event_jComboBox7ActionPerformed

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        CardLayout cl = (CardLayout)(jPanel3.getLayout());
        cl.show(jPanel3, jComboBox5.getSelectedItem().toString());
        jComboBox5.setSelectedIndex(0);
    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        if(jLabel10.isEnabled()) {
            startDecryption();
        }
    }//GEN-LAST:event_jLabel10MouseClicked

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        if(jLabel12.isEnabled()) {
            startPreview();
        }
    }//GEN-LAST:event_jLabel12MouseClicked

    private void jComboBox9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox9ActionPerformed
        CardLayout cl = (CardLayout)(jPanel3.getLayout());
        cl.show(jPanel3, jComboBox9.getSelectedItem().toString());
        jComboBox9.setSelectedIndex(0);
    }//GEN-LAST:event_jComboBox9ActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JComboBox jComboBox7;
    private javax.swing.JComboBox jComboBox8;
    private javax.swing.JComboBox jComboBox9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    // End of variables declaration//GEN-END:variables
}
