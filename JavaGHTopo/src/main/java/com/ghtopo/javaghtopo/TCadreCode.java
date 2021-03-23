/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ghtopo.javaghtopo;

import static GHTopoOperationnel.Constantes.*;
import static GHTopoOperationnel.GeneralFunctions.*;
import GHTopoOperationnel.Types.TCode;
import static UtilsComposants.TOperationsSurComposants.*;
import java.util.ResourceBundle;
import javax.swing.JComboBox;
import javax.swing.JLabel;
/**
 *
 * @author jean-pierre.cassou
 */
public class TCadreCode extends javax.swing.JPanel 
{
    private ResourceBundle fMyResourceBundle = null;
    private void S666(JComboBox QCmb, int QIdx, JLabel QLbl, String QCaption)
    {
        QCmb.setSelectedIndex(QIdx);
        QLbl.setText(QCaption);
    }        
    private void S777(JComboBox QCmb1, int QIdx1, JLabel QLbl, String QCaption, JComboBox QCmb2, int QIdx2)
    {
        QCmb1.setSelectedIndex(QIdx1);
        QLbl.setText(QCaption);
        QCmb2.setSelectedIndex(QIdx2);
    }
    public void initialiseCadreCode(TCode myCode)            
    {
        // valeurs internes
        lbCodeComp.setText(sprintf("%d", trunc(myCode.UniteBoussole)));
        lbCodeIncl.setText(sprintf("%d", trunc(myCode.UniteClino)));
        
        editIdxCode.setText(intToStr(myCode.IdxCode));
        editObsCode.setText(myCode.Commentaire);
        editPsiL.setText(ensureConvertNumberToStr(myCode.PsiL));
        editPsiAz.setText(ensureConvertNumberToStr(myCode.PsiAz));
        editPsiP.setText(ensureConvertNumberToStr(myCode.PsiP));
        // visées directes par défaut
        cmbAzDirectInv.setSelectedIndex(0);
        cmbIncDirectInv.setSelectedIndex(0);
        // Graduation du compas
        int ubb = trunc(myCode.UniteBoussole);
        switch(ubb)
        {    
            case 389:
            case 399:
            case 400: 
                S666(cmbUB, 0, lbErrAz, "Az (gr)");
                break;
            case 349:
            case 359:
            case 360: 
                S666(cmbUB, 1, lbErrAz, "Az (deg)");
                break;
            // visées inverses 350, 390:
            case 350: 
                S777(cmbUB, 1, lbErrAz, "Az (deg)" , cmbAzDirectInv, 1);
                break;
            case 390: 
                S777(cmbUB, 0, lbErrAz, "Az (gr)", cmbAzDirectInv, 1);
                break;
            default:
                cmbUB.setSelectedIndex(0);
                break;
        }   
        // Graduation du clinomètre
        int ucc = trunc(myCode.UniteClino);
        switch (ucc)
        {
            case 399:
            case 400: 
            case 401:    
                S666(cmbUC, 0, lbErrInc, "Inc (gr)");
                break;
            case 359:
            case 360: 
            case 361:    
                S666(cmbUC, 1, lbErrInc, "Inc (deg)");
                break;
            case 370: 
                S666(cmbUC, 2, lbErrInc, "Inc (%)");
                break;
            case 380: 
                S666(cmbUC, 3, lbErrInc, "Inc (m)");
                break;
            case UNITE_CLINO_LASERMETRE_STANLEY_TLM330_DIR: 
                S666(cmbUC, 4, lbErrInc, "dz (m)");
                break;
            case UNITE_CLINO_LASERMETRE_STANLEY_TLM330_INV: 
                S777(cmbUC, 4, lbErrInc, "dz (m)", cmbIncDirectInv, 1);
                break;
            // visées inverses
            case 350: 
                S777(cmbUC, 1, lbErrInc, "Inc (deg)", cmbIncDirectInv, 1);
                break;
            case 390: 
                S777(cmbUC, 0, lbErrInc, "Inc (gr)", cmbIncDirectInv, 1);    
                break;
            default:
                S666(cmbUC, 0, lbErrInc, "Inc (gr)");
                break;
        }    
        // Position du zéro
        switch(ucc)
        {
            case 359:
            case 399: 
                cmbPosZero.setSelectedIndex(0);  // nadiral
                break;
            case 360:
            case 400: 
                cmbPosZero.setSelectedIndex(1);  // horizontal       
                break;
            case 361:
            case 401: 
                cmbPosZero.setSelectedIndex(2);  // zénithal
                break;
            default:
                cmbPosZero.setSelectedIndex(1);
                break;
        }     
    }
    public TCode getCodeFromForm()
    {
        TCode C = new TCode();
        C.IdxCode = strToIntDef(editIdxCode.getText(), 0);
        switch (cmbUB.getSelectedIndex())
        {
            case 0: C.UniteBoussole = 400.00; break;
            case 1: C.UniteClino    = 360.00; break;     
        }    
        switch (cmbUC.getSelectedIndex())
        { 
            case 0: C.UniteClino = 400.00; break;
            case 1: C.UniteClino = 360.00; break;
            case 2: C.UniteClino = 380.00; break;
            case 3: C.UniteClino = 380.00; break;
            case 4: C.UniteClino = UNITE_CLINO_LASERMETRE_STANLEY_TLM330_DIR; break;    
        }
        // visée directe ou inverse: azimuts: retrancher 10 du code azimut
        if (cmbAzDirectInv.getSelectedIndex()  == 1) C.UniteBoussole  -= 10.0; 
        if (cmbIncDirectInv.getSelectedIndex() == 1) C.UniteClino     -= 10.0; 
        // position du zéro
        switch(cmbPosZero.getSelectedIndex())
        {
            case 0: C.UniteClino -= 1.00; break;
            case 1: ; break;
            case 2: C.UniteClino += 1.00; break;
        }    
        C.PsiL   = strToFloatDef(editPsiL.getText() , 0.01); // cm
        C.PsiAz  = strToFloatDef(editPsiAz.getText(), 1.00); // unites angle
        C.PsiP   = strToFloatDef(editPsiP.getText() , 1.00); // unites angle
        C.FactLong = strToFloatDef(editFactLongueurs.getText() , 1.00); // facteur de correction
        C.AngLimite = 0.00; // angle limite non supporté
        // type de galeries
        C.Commentaire = editObsCode.getText().trim();
        return C;    
    }        
    private String getResourceByKey(String k)
    {
        return fMyResourceBundle.getString(k);
    }  
    
    public TCadreCode() {
        this.fMyResourceBundle = java.util.ResourceBundle.getBundle("com/ghtopo/javaghtopo/Bundle");
        initComponents();
        String[] ubc = {getResourceByKey("rsCDR_CODES_UB_GRADES"), 
                        getResourceByKey("rsCDR_CODES_UB_DEGRES")};
        preparerComboBox(cmbUB, ubc, 0);
        String[] ucd = {getResourceByKey("rsCDR_CODES_UC_GRADES"), 
                        getResourceByKey("rsCDR_CODES_UC_DEGRES"), 
                        getResourceByKey("rsCDR_CODES_UC_PERCENT"),
                        getResourceByKey("rsCDR_CODES_UC_DENIV"), 
                        getResourceByKey("rsCDR_CODES_UC_LASERMETRE_TLM330")};
        preparerComboBox(cmbUC, ucd, 0);
        String[] invdir = {getResourceByKey("rsCDR_CODES_VISDIRINV_DIRECTE"), 
                           getResourceByKey("rsCDR_CODES_VISDIRINV_INVERSE")};
        preparerComboBox(cmbAzDirectInv, invdir, 0);
        preparerComboBox(cmbIncDirectInv, invdir, 0);
        String[] poszero = {getResourceByKey("rsCDR_CODES_ZERO_NADIRAL"), 
                            getResourceByKey("rsCDR_CODES_ZERO_HORIZONTAL"), 
                            getResourceByKey("rsCDR_CODES_ZERO_ZENITHAL")};
        preparerComboBox(cmbPosZero, poszero, 0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textField3 = new java.awt.TextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmbUB = new javax.swing.JComboBox();
        cmbAzDirectInv = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        cmbUC = new javax.swing.JComboBox();
        cmbIncDirectInv = new javax.swing.JComboBox();
        cmbPosZero = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        editIdxCode = new javax.swing.JTextField();
        editPsiL = new javax.swing.JTextField();
        editPsiAz = new javax.swing.JTextField();
        editPsiP = new javax.swing.JTextField();
        editObsCode = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lbCodeIncl = new javax.swing.JLabel();
        lbErrInc = new javax.swing.JLabel();
        lbCodeComp = new javax.swing.JLabel();
        lbErrAz = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        editFactLongueurs = new javax.swing.JTextField();

        textField3.setText("null");

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/ghtopo/javaghtopo/Bundle"); // NOI18N
        jLabel1.setText(bundle.getString("rsCDR_CODES_NUMERO")); // NOI18N

        jLabel7.setText(bundle.getString("rsCDR_CODES_OBSERV")); // NOI18N

        jLabel2.setText(bundle.getString("rsCDR_CODES_AZIMUTS")); // NOI18N

        cmbUB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbUB.setMinimumSize(new java.awt.Dimension(60, 25));
        cmbUB.setNextFocusableComponent(cmbAzDirectInv);
        cmbUB.setPreferredSize(new java.awt.Dimension(60, 25));

        cmbAzDirectInv.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbAzDirectInv.setMinimumSize(new java.awt.Dimension(56, 25));
        cmbAzDirectInv.setNextFocusableComponent(cmbUC);
        cmbAzDirectInv.setPreferredSize(new java.awt.Dimension(56, 25));

        jLabel3.setText(bundle.getString("rsCDR_CODES_LB_VERTICAL")); // NOI18N

        cmbUC.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbUC.setMinimumSize(new java.awt.Dimension(60, 25));
        cmbUC.setNextFocusableComponent(cmbIncDirectInv);
        cmbUC.setPreferredSize(new java.awt.Dimension(60, 25));

        cmbIncDirectInv.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbIncDirectInv.setMinimumSize(new java.awt.Dimension(56, 25));
        cmbIncDirectInv.setNextFocusableComponent(cmbPosZero);
        cmbIncDirectInv.setPreferredSize(new java.awt.Dimension(56, 25));

        cmbPosZero.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbPosZero.setMinimumSize(new java.awt.Dimension(56, 25));
        cmbPosZero.setNextFocusableComponent(editPsiL);
        cmbPosZero.setPreferredSize(new java.awt.Dimension(56, 25));

        jLabel4.setText(bundle.getString("rsCDR_CODES_LB_POSZERO")); // NOI18N

        editIdxCode.setText("0");
        editIdxCode.setMinimumSize(new java.awt.Dimension(80, 20));
        editIdxCode.setNextFocusableComponent(cmbUB);
        editIdxCode.setPreferredSize(new java.awt.Dimension(80, 25));

        editPsiL.setText("0,00");
        editPsiL.setMinimumSize(new java.awt.Dimension(80, 20));
        editPsiL.setNextFocusableComponent(editPsiAz);
        editPsiL.setPreferredSize(new java.awt.Dimension(80, 25));

        editPsiAz.setText("0,00");
        editPsiAz.setMinimumSize(new java.awt.Dimension(80, 20));
        editPsiAz.setNextFocusableComponent(editPsiP);
        editPsiAz.setPreferredSize(new java.awt.Dimension(80, 25));

        editPsiP.setText("0,00");
        editPsiP.setMinimumSize(new java.awt.Dimension(80, 20));
        editPsiP.setNextFocusableComponent(editObsCode);
        editPsiP.setPreferredSize(new java.awt.Dimension(80, 25));

        editObsCode.setNextFocusableComponent(editIdxCode);
        editObsCode.setPreferredSize(new java.awt.Dimension(6, 25));

        jLabel5.setText("Psi Longueurs");

        jLabel6.setText("Psi Azimuts");

        jLabel8.setText("Psi Pentes");

        lbCodeIncl.setText("0");
        lbCodeIncl.setToolTipText("");
        lbCodeIncl.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbErrInc.setText("0");
        lbErrInc.setToolTipText("");
        lbErrInc.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbCodeComp.setText("0");
        lbCodeComp.setToolTipText("");
        lbCodeComp.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbErrAz.setText("0");
        lbErrAz.setToolTipText("");
        lbErrAz.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel9.setText("Facteur Longueurs");

        editFactLongueurs.setText("0,00");
        editFactLongueurs.setMinimumSize(new java.awt.Dimension(80, 20));
        editFactLongueurs.setNextFocusableComponent(editPsiAz);
        editFactLongueurs.setPreferredSize(new java.awt.Dimension(80, 25));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel1)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(editObsCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(editPsiL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(editFactLongueurs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(editPsiAz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(editPsiP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(editIdxCode, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(cmbUB, 0, 177, Short.MAX_VALUE)
                                            .addComponent(cmbUC, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(cmbAzDirectInv, 0, 151, Short.MAX_VALUE)
                                            .addComponent(cmbIncDirectInv, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(cmbPosZero, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(lbErrInc, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lbCodeIncl, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(lbErrAz, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lbCodeComp, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(0, 49, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(16, 16, 16))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(editIdxCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel2)
                                            .addComponent(cmbUB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cmbAzDirectInv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel3)
                                            .addComponent(cmbUC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cmbIncDirectInv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cmbPosZero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lbErrInc)
                                            .addComponent(lbCodeIncl)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel4)
                                            .addComponent(lbCodeComp)
                                            .addComponent(lbErrAz))
                                        .addGap(33, 33, 33)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(editPsiL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel5))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel6))
                                    .addComponent(editFactLongueurs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9))
                                .addGap(9, 9, 9))
                            .addComponent(editPsiAz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addGap(10, 10, 10))
                    .addComponent(editPsiP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editObsCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbAzDirectInv;
    private javax.swing.JComboBox cmbIncDirectInv;
    private javax.swing.JComboBox cmbPosZero;
    private javax.swing.JComboBox cmbUB;
    private javax.swing.JComboBox cmbUC;
    private javax.swing.JTextField editFactLongueurs;
    private javax.swing.JTextField editIdxCode;
    private javax.swing.JTextField editObsCode;
    private javax.swing.JTextField editPsiAz;
    private javax.swing.JTextField editPsiL;
    private javax.swing.JTextField editPsiP;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lbCodeComp;
    private javax.swing.JLabel lbCodeIncl;
    private javax.swing.JLabel lbErrAz;
    private javax.swing.JLabel lbErrInc;
    private java.awt.TextField textField3;
    // End of variables declaration//GEN-END:variables
}
