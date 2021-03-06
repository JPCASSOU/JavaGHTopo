/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ghtopo.javaghtopo;

import static GHTopoOperationnel.GeneralFunctions.*;
import GHTopoOperationnel.Types.TEntrance;
import GHTopoOperationnel.TToporobotStructure;
import java.util.ResourceBundle;
import javax.swing.JTextField;


/**
 *
 * @author jean-pierre.cassou
 */
public class TCadreEntree extends javax.swing.JPanel {
    private ResourceBundle fMyResourceBundle = null;
    private TToporobotStructure myDocTopo;
    private String getResourceByKey(String k)
    {
        return fMyResourceBundle.getString(k);
    }        
   
    public void initialiserCadreEntree(TToporobotStructure t,TEntrance e)
    {
        myDocTopo = t;
        editIdxEntree.setText(intToStr(e.eNumEntree));
        editNomEntree.setText(e.eNomEntree);
        editObsEntree.setText(e.eObserv);
        editRefSerie.setText(intToStr(e.eRefSer));
        editRefPt.setText(intToStr(e.eRefSt));
        editXEntree.setText(ensureConvertNumberToStr(e.eXEntree));
        editYEntree.setText(ensureConvertNumberToStr(e.eYEntree));
        editZEntree.setText(ensureConvertNumberToStr(e.eZEntree));
    }        
    public TEntrance getEntranceFromForm()
    {
        TEntrance result = new TEntrance();
        result.eNumEntree = strToIntDef(editIdxEntree.getText().trim(), 0);
        result.eNomEntree = editNomEntree.getText().trim();
        result.eObserv    = editObsEntree.getText().trim();
        result.eRefSer    = strToIntDef(editRefSerie.getText().trim(), 0);
        result.eRefSt     = strToIntDef(editRefPt.getText().trim(), 0);
        result.eXEntree   = ensureConvertStrToNumber(editXEntree.getText().trim(), 0.00);
        result.eYEntree   = ensureConvertStrToNumber(editYEntree.getText().trim(), 0.00);
        result.eZEntree   = ensureConvertStrToNumber(editZEntree.getText().trim(), 0.00);
        return result;
    }        
    public TCadreEntree() {
        this.fMyResourceBundle = java.util.ResourceBundle.getBundle("com/ghtopo/javaghtopo/Bundle");
        initComponents();
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
        jLabel3 = new javax.swing.JLabel();
        btnFindSerieSt = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        editIdxEntree = new javax.swing.JTextField();
        editNomEntree = new javax.swing.JTextField();
        editXEntree = new javax.swing.JTextField();
        editYEntree = new javax.swing.JTextField();
        editRefSerie = new javax.swing.JTextField();
        editZEntree = new javax.swing.JTextField();
        editRefPt = new javax.swing.JTextField();
        editObsEntree = new javax.swing.JTextField();

        jLabel1.setText("Numero");

        jLabel3.setText("Nom");

        btnFindSerieSt.setText("...");
        btnFindSerieSt.setNextFocusableComponent(editXEntree);
        btnFindSerieSt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindSerieStActionPerformed(evt);
            }
        });

        jLabel4.setText("Station");

        jLabel2.setText("X =");

        jLabel5.setText("Y =");

        jLabel6.setText("Z =");

        jLabel7.setText("Observ.");

        editIdxEntree.setText("0");
        editIdxEntree.setMinimumSize(new java.awt.Dimension(60, 25));
        editIdxEntree.setName(""); // NOI18N
        editIdxEntree.setNextFocusableComponent(editNomEntree);
        editIdxEntree.setPreferredSize(new java.awt.Dimension(60, 25));

        editNomEntree.setMinimumSize(new java.awt.Dimension(6, 25));
        editNomEntree.setNextFocusableComponent(editRefSerie);
        editNomEntree.setPreferredSize(new java.awt.Dimension(6, 25));

        editXEntree.setText("0,00");
        editXEntree.setMinimumSize(new java.awt.Dimension(100, 25));
        editXEntree.setNextFocusableComponent(editYEntree);
        editXEntree.setPreferredSize(new java.awt.Dimension(100, 25));

        editYEntree.setText("0,00");
        editYEntree.setMinimumSize(new java.awt.Dimension(100, 25));
        editYEntree.setNextFocusableComponent(editZEntree);
        editYEntree.setPreferredSize(new java.awt.Dimension(100, 25));

        editRefSerie.setText("0");
        editRefSerie.setMinimumSize(new java.awt.Dimension(60, 25));
        editRefSerie.setName(""); // NOI18N
        editRefSerie.setNextFocusableComponent(editRefPt);
        editRefSerie.setPreferredSize(new java.awt.Dimension(60, 25));

        editZEntree.setText("0,00");
        editZEntree.setMinimumSize(new java.awt.Dimension(100, 25));
        editZEntree.setNextFocusableComponent(editObsEntree);
        editZEntree.setPreferredSize(new java.awt.Dimension(100, 25));

        editRefPt.setText("0");
        editRefPt.setMinimumSize(new java.awt.Dimension(60, 25));
        editRefPt.setName(""); // NOI18N
        editRefPt.setNextFocusableComponent(btnFindSerieSt);
        editRefPt.setPreferredSize(new java.awt.Dimension(60, 25));

        editObsEntree.setMinimumSize(new java.awt.Dimension(6, 25));
        editObsEntree.setNextFocusableComponent(editIdxEntree);
        editObsEntree.setPreferredSize(new java.awt.Dimension(6, 25));
        editObsEntree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editObsEntreeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(editRefSerie, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(editRefPt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnFindSerieSt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(144, 180, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(editNomEntree, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(editIdxEntree, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(editZEntree, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(editYEntree, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(editXEntree, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(194, 194, 194)))
                                .addContainerGap())))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(20, 20, 20)
                        .addComponent(editObsEntree, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(editIdxEntree, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(editNomEntree, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnFindSerieSt)
                            .addComponent(jLabel4)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(editRefSerie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(editRefPt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(editXEntree, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(editYEntree, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(editZEntree, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(editObsEntree, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnFindSerieStActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindSerieStActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFindSerieStActionPerformed

    private void editObsEntreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editObsEntreeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editObsEntreeActionPerformed
        // TODO add your handling code here:                                                   
                                         


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFindSerieSt;
    private javax.swing.JTextField editIdxEntree;
    private javax.swing.JTextField editNomEntree;
    private javax.swing.JTextField editObsEntree;
    private javax.swing.JTextField editRefPt;
    private javax.swing.JTextField editRefSerie;
    private javax.swing.JTextField editXEntree;
    private javax.swing.JTextField editYEntree;
    private javax.swing.JTextField editZEntree;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    // End of variables declaration//GEN-END:variables
}
