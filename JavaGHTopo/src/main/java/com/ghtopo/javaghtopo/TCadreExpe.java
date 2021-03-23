package com.ghtopo.javaghtopo;

import GHTopoOperationnel.Types.TExpe;
import static GHTopoOperationnel.TCallDialogs.*;
import static GHTopoOperationnel.GeneralFunctions.*;
import static UtilsComposants.TOperationsSurComposants.*;
import GHTopoOperationnel.TPalette256;
import java.io.File;
import java.util.ResourceBundle;
public class TCadreExpe extends javax.swing.JPanel {
    private TPalette256 fPalette256;
    private ResourceBundle fMyResourceBundle = null;
    private String getResourceByKey(String k)
    {
        return fMyResourceBundle.getString(k);
    }   
    public void initialiseCadreExpe(TExpe myExpe)
    {      
        editIdxExpe.setText(intToStr(myExpe.IdxExpe));
        editJourExpe.setText(intToStr(getDay(myExpe.DateLeve)));
        editMoisExpe.setText(intToStr(getMonth(myExpe.DateLeve)));
        editAnneeExpe.setText(intToStr(getYear(myExpe.DateLeve)));
        editDeclinaison.setText(ensureConvertNumberToStr(myExpe.Declinaison));
        cmbModeDeclinaison.setSelectedIndex(myExpe.ModeDecl);
        editInclinaison.setText(ensureConvertLongOrLatToXML(myExpe.Inclinaison));
        editObsExpe.setText(myExpe.Commentaire);
        editSpeleometre.setText(myExpe.Speleometre);
        editSpeleographe.setText(myExpe.Speleographe);
        setLbCouleurByIdx(myExpe.IdxColor);
    }
        
    public TExpe getExpeFromForm()
    {
        TExpe myExpe = new TExpe();
        myExpe.IdxExpe      = strToIntDef(editIdxExpe.getText(), 0);
        int JJ   = strToIntDef(editJourExpe.getText(), 1);
        int MM   = strToIntDef(editMoisExpe.getText(), 1);
        int AAAA = strToIntDef(editAnneeExpe.getText(), 2000);
        myExpe.DateLeve     = ensureMakeGHTopoDateFromYMD(AAAA, MM, JJ);
        myExpe.Declinaison  = strToFloatDef(editDeclinaison.getText(), 0.00);
        myExpe.Inclinaison  = strToFloatDef(editInclinaison.getText(), 0.00);
        myExpe.ModeDecl     = cmbModeDeclinaison.getSelectedIndex();
        myExpe.Speleometre  = editSpeleometre.getText().trim();
        myExpe.Speleographe = editSpeleographe.getText().trim();
        myExpe.Commentaire  = editObsExpe.getText().trim();
        myExpe.IdxColor     = strToIntDef(editIdxCouleur.getText(), 15); //15: gris
        return myExpe;
    } 
    
    private void setLbCouleurByIdx(int idx)
    {        
        editIdxCouleur.setText(sprintf("%d", idx));
        editIdxCouleur.setBackground(fPalette256.getColorByIdx(idx));
    }        
    public TCadreExpe() {
        this.fMyResourceBundle = java.util.ResourceBundle.getBundle("com/ghtopo/javaghtopo/Bundle");
        initComponents();
        String[] md = {getResourceByKey("rsCDR_EXPE_DECL_MANUELLE"), 
                       getResourceByKey("rsCDR_EXPE_DECL_AUTOMATIQUE")
        };
        preparerComboBox(cmbModeDeclinaison, md, 0);
        fPalette256 = new TPalette256();
        fPalette256.generateTOPOROBOTPalette();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textField1 = new java.awt.TextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnChooseColor = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cmbModeDeclinaison = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        btnChooseDeclinaison = new javax.swing.JButton();
        editIdxExpe = new javax.swing.JTextField();
        editJourExpe = new javax.swing.JTextField();
        editMoisExpe = new javax.swing.JTextField();
        editAnneeExpe = new javax.swing.JTextField();
        editIdxCouleur = new javax.swing.JTextField();
        editSpeleometre = new javax.swing.JTextField();
        editSpeleographe = new javax.swing.JTextField();
        editInclinaison = new javax.swing.JTextField();
        editDeclinaison = new javax.swing.JTextField();
        editObsExpe = new javax.swing.JTextField();

        textField1.setText("null");

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/ghtopo/javaghtopo/Bundle"); // NOI18N
        jLabel1.setText(bundle.getString("rsCDR_EXPE_NUMERO")); // NOI18N

        jLabel2.setText(bundle.getString("rsCDR_EXPE_DATE")); // NOI18N

        jLabel3.setText(bundle.getString("rsCDR_EXPE_OBSERVATIONS")); // NOI18N

        jLabel4.setText(bundle.getString("rsCDR_EXPE_IDXCOULEUR")); // NOI18N

        btnChooseColor.setText("null");
        btnChooseColor.setNextFocusableComponent(editSpeleometre);
        btnChooseColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChooseColorActionPerformed(evt);
            }
        });

        jLabel5.setText(bundle.getString("rsCDR_EXPE_SPELÉOMETRE")); // NOI18N

        jLabel6.setText(bundle.getString("rsCDR_EXPE_SPELEOGRAPHE")); // NOI18N

        jLabel7.setText(bundle.getString("rsCDR_EXPE_DECLINAISON")); // NOI18N

        cmbModeDeclinaison.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbModeDeclinaison.setMinimumSize(new java.awt.Dimension(60, 25));
        cmbModeDeclinaison.setNextFocusableComponent(editDeclinaison);
        cmbModeDeclinaison.setPreferredSize(new java.awt.Dimension(56, 25));

        jLabel8.setText(bundle.getString("rsCDR_EXPE_INCLINAISON")); // NOI18N

        btnChooseDeclinaison.setText("null");
        btnChooseDeclinaison.setMaximumSize(new java.awt.Dimension(45, 25));
        btnChooseDeclinaison.setMinimumSize(new java.awt.Dimension(45, 25));
        btnChooseDeclinaison.setNextFocusableComponent(editInclinaison);
        btnChooseDeclinaison.setPreferredSize(new java.awt.Dimension(45, 25));
        btnChooseDeclinaison.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChooseDeclinaisonActionPerformed(evt);
            }
        });

        editIdxExpe.setText("0");
        editIdxExpe.setMinimumSize(new java.awt.Dimension(60, 25));
        editIdxExpe.setNextFocusableComponent(editJourExpe);
        editIdxExpe.setPreferredSize(new java.awt.Dimension(60, 25));

        editJourExpe.setText("0");
        editJourExpe.setMinimumSize(new java.awt.Dimension(60, 25));
        editJourExpe.setNextFocusableComponent(editMoisExpe);
        editJourExpe.setPreferredSize(new java.awt.Dimension(60, 25));

        editMoisExpe.setText("0");
        editMoisExpe.setMinimumSize(new java.awt.Dimension(60, 25));
        editMoisExpe.setNextFocusableComponent(editAnneeExpe);
        editMoisExpe.setPreferredSize(new java.awt.Dimension(60, 25));

        editAnneeExpe.setText("0");
        editAnneeExpe.setMinimumSize(new java.awt.Dimension(80, 25));
        editAnneeExpe.setNextFocusableComponent(editIdxCouleur);
        editAnneeExpe.setPreferredSize(new java.awt.Dimension(80, 25));

        editIdxCouleur.setBackground(new java.awt.Color(204, 255, 102));
        editIdxCouleur.setText("0");
        editIdxCouleur.setMinimumSize(new java.awt.Dimension(60, 25));
        editIdxCouleur.setNextFocusableComponent(btnChooseColor);
        editIdxCouleur.setPreferredSize(new java.awt.Dimension(60, 25));

        editSpeleometre.setMinimumSize(new java.awt.Dimension(6, 25));
        editSpeleometre.setNextFocusableComponent(editSpeleographe);
        editSpeleometre.setPreferredSize(new java.awt.Dimension(6, 25));

        editSpeleographe.setMinimumSize(new java.awt.Dimension(6, 25));
        editSpeleographe.setNextFocusableComponent(cmbModeDeclinaison);
        editSpeleographe.setPreferredSize(new java.awt.Dimension(6, 25));

        editInclinaison.setText("0.00");
        editInclinaison.setMinimumSize(new java.awt.Dimension(80, 25));
        editInclinaison.setNextFocusableComponent(editObsExpe);
        editInclinaison.setPreferredSize(new java.awt.Dimension(80, 25));

        editDeclinaison.setText("0.00");
        editDeclinaison.setMinimumSize(new java.awt.Dimension(80, 25));
        editDeclinaison.setNextFocusableComponent(btnChooseDeclinaison);
        editDeclinaison.setPreferredSize(new java.awt.Dimension(80, 25));

        editObsExpe.setMinimumSize(new java.awt.Dimension(6, 25));
        editObsExpe.setNextFocusableComponent(editIdxExpe);
        editObsExpe.setPreferredSize(new java.awt.Dimension(6, 25));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addGap(16, 16, 16))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(22, 22, 22)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(editSpeleographe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(editSpeleometre, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel4))
                                .addGap(31, 31, 31)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(editIdxCouleur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnChooseColor, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(editIdxExpe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(editInclinaison, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(editJourExpe, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(editMoisExpe, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(editAnneeExpe, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 501, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(editObsExpe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(82, 82, 82)
                                .addComponent(cmbModeDeclinaison, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(editDeclinaison, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnChooseDeclinaison, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(227, 227, 227))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(editIdxExpe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(editJourExpe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editMoisExpe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editAnneeExpe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(editIdxCouleur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnChooseColor))
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(editSpeleometre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(editSpeleographe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cmbModeDeclinaison, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnChooseDeclinaison, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editDeclinaison, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(editInclinaison, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(editObsExpe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(42, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnChooseColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChooseColorActionPerformed
        // TODO add your handling code here:
        int EWE = strToIntDef(editIdxCouleur.getText(), 0);
        EWE = selectCouleurTopo(EWE);
        setLbCouleurByIdx(EWE);
    }//GEN-LAST:event_btnChooseColorActionPerformed

    private void btnChooseDeclinaisonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChooseDeclinaisonActionPerformed
        // TODO add your handling code here:
//        TCalculateurDeclimag C = new TCalculateurDeclimag();
//        if (C.initialiser(getGHTopoDirectory() + File.separator + "IGRF"))
//        {
//            ;
//            
//        }            
    }//GEN-LAST:event_btnChooseDeclinaisonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChooseColor;
    private javax.swing.JButton btnChooseDeclinaison;
    private javax.swing.JComboBox cmbModeDeclinaison;
    private javax.swing.JTextField editAnneeExpe;
    private javax.swing.JTextField editDeclinaison;
    private javax.swing.JTextField editIdxCouleur;
    private javax.swing.JTextField editIdxExpe;
    private javax.swing.JTextField editInclinaison;
    private javax.swing.JTextField editJourExpe;
    private javax.swing.JTextField editMoisExpe;
    private javax.swing.JTextField editObsExpe;
    private javax.swing.JTextField editSpeleographe;
    private javax.swing.JTextField editSpeleometre;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private java.awt.TextField textField1;
    // End of variables declaration//GEN-END:variables
}