/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ghtopo.javaghtopo;
import static GHTopoOperationnel.GeneralFunctions.*;
import static GHTopoOperationnel.TCallDialogs.ChooseColor;
import static UtilsComposants.TOperationsSurComposants.*;
import GHTopoOperationnel.Types.TReseau;
import java.awt.Color;
import java.util.ResourceBundle;

public class TCadreReseau extends javax.swing.JPanel {
    private ResourceBundle fMyResourceBundle = null;
    private String getResourceByKey(String k)
    {
        return fMyResourceBundle.getString(k);
    }        
   
    public void initialiserCadreReseau(TReseau RS)
    {
        editIdxReseau.setText(intToStr(RS.IdxReseau));
        editNomReseau.setText(RS.NomReseau);
        //btnColor.
        cmbTypeReseau.setSelectedIndex(RS.TypeReseau);
        editObsReseau.setText(RS.ObsReseau);
        Color c = new Color(RS.CouleurReseauR, RS.CouleurReseauG, RS.CouleurReseauB);
        btnColorReseau.setOpaque(true);
        btnColorReseau.setBackground(c);
        btnColorReseau.setText(sprintf("%02X%02X%02X", RS.CouleurReseauR, RS.CouleurReseauG, RS.CouleurReseauB));
    }       
    public TReseau getReseauFromForm()
    {
        TReseau result = new TReseau();
        result.IdxReseau   = strToIntDef(editIdxReseau.getText().trim(), 0);
        result.NomReseau   = editNomReseau.getText().trim();
        result.ObsReseau   = editObsReseau.getText().trim();
        result.TypeReseau  = cmbTypeReseau.getSelectedIndex();
        Color c = btnColorReseau.getBackground();
        result.CouleurReseauR = c.getRed();
        result.CouleurReseauG = c.getGreen();
        result.CouleurReseauB = c.getBlue();
        
        return result;
    }        
    
    public TCadreReseau() {
        this.fMyResourceBundle = java.util.ResourceBundle.getBundle("com/ghtopo/javaghtopo/Bundle");
        initComponents();
        String [] zob = {
            getResourceByKey("rsCDR_RESEAU_CMB_CAVITE_NATURELLE"),
            getResourceByKey("rsCDR_RESEAU_CMB_CAVITE_ARTIFICIELLE"),
            getResourceByKey("rsCDR_RESEAU_CMB_TOPO_SURFACE"),
            getResourceByKey("rsCDR_RESEAU_CMB_THALWEG"),
            getResourceByKey("rsCDR_RESEAU_CMB_PATH"),
            getResourceByKey("rsCDR_RESEAU_CMB_SENTIER"),
            getResourceByKey("rsCDR_RESEAU_CMB_AUTRE")
        };
        preparerComboBox(cmbTypeReseau, zob, 0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cmbTypeReseau = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        editIdxReseau = new javax.swing.JTextField();
        editNomReseau = new javax.swing.JTextField();
        editObsReseau = new javax.swing.JTextField();
        btnColorReseau = new javax.swing.JButton();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/ghtopo/javaghtopo/Bundle"); // NOI18N
        jLabel1.setText(bundle.getString("rsCDR_RESEAU_LB_NUMERO")); // NOI18N

        jLabel7.setText(bundle.getString("rsCDR_RESEAU_LB_OBSERV")); // NOI18N

        jLabel2.setText(bundle.getString("rsCDR_RESEAU_LB_NOM_RESEAU")); // NOI18N

        jLabel3.setText(bundle.getString("rsCDR_RESEAU_LB_COULEUR")); // NOI18N

        cmbTypeReseau.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbTypeReseau.setNextFocusableComponent(editObsReseau);

        jLabel4.setText(bundle.getString("rsCDR_RESEAU_LB_TYPE_RESEAU")); // NOI18N

        editIdxReseau.setText("0");

        editNomReseau.setNextFocusableComponent(cmbTypeReseau);

        editObsReseau.setNextFocusableComponent(editIdxReseau);

        btnColorReseau.setBackground(new java.awt.Color(255, 51, 51));
        btnColorReseau.setForeground(new java.awt.Color(255, 102, 102));
        btnColorReseau.setText("toto");
        btnColorReseau.setAutoscrolls(true);
        btnColorReseau.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnColorReseau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnColorReseauActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel7)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbTypeReseau, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editNomReseau)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(editIdxReseau, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnColorReseau, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 112, Short.MAX_VALUE))
                    .addComponent(editObsReseau))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(editIdxReseau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnColorReseau, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(editNomReseau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(cmbTypeReseau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editObsReseau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addContainerGap(14, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnColorReseauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnColorReseauActionPerformed
        // TODO add your handling code here:
        Color c = btnColorReseau.getBackground();
        afficherMessage(c.toString());
        c = ChooseColor(this.btnColorReseau, "Couleur de réseau", c);
        //afficherMessage(c.toString());
        btnColorReseau.setBackground(c);

        btnColorReseau.setText(sprintf("%X", c.getRGB()));
    }//GEN-LAST:event_btnColorReseauActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnColorReseau;
    private javax.swing.JComboBox cmbTypeReseau;
    private javax.swing.JTextField editIdxReseau;
    private javax.swing.JTextField editNomReseau;
    private javax.swing.JTextField editObsReseau;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    // End of variables declaration//GEN-END:variables
}
