

package com.ghtopo.javaghtopo;
import static GHTopoOperationnel.Constantes.*;
import static GHTopoOperationnel.GeneralFunctions.*;
import GHTopoOperationnel.TPalette256;
import GHTopoOperationnel.TSerie;
import GHTopoOperationnel.TToporobotStructure;
import GHTopoOperationnel.Types.TCode;
import GHTopoOperationnel.Types.TEntrance;
import GHTopoOperationnel.Types.TExpe;
import GHTopoOperationnel.Types.TReseau;
import GHTopoOperationnel.Types.TSecteur;
import java.util.ResourceBundle;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;



public class TdlgSelectDansListes extends JDialog 
{
    private TPalette256 Palette256;
    private ComponentRenderer myComponentRenderer;
    private boolean fFullyListed = false;
    private TToporobotStructure myDocTopo;
    private int modeSelectDsListes = 0;
    private int fElementSelected = 0;
    private int fModalResult = mrCANCEL;
    private ResourceBundle fMyResourceBundle = null;
    
    //- I18N
    private String getResourceByKey(String k)
    {
        return fMyResourceBundle.getString(k);
    }      
    //***************************************
    public TdlgSelectDansListes() 
    {
        this.fMyResourceBundle = java.util.ResourceBundle.getBundle("com/ghtopo/javaghtopo/Bundle");
        initComponents();  
        Palette256 = new TPalette256();
        myComponentRenderer = new ComponentRenderer();
        lsbListeElements.setCellRenderer(myComponentRenderer);
        
        this.setLocationRelativeTo(null); // centrée
    }
 
    public void initialiserDlg(TToporobotStructure t, int ModeSelection, int elementSelected)
    {
        fFullyListed = false;
        myDocTopo          = t;
        fElementSelected   = elementSelected;
        modeSelectDsListes = ModeSelection;
        myComponentRenderer.SetDocTopoAndSelection(myDocTopo, Palette256, ModeSelection);
        Palette256.generateTOPOROBOTPalette();
        String WU = "";
        switch(ModeSelection)
        {    
           case mslENTRANCES:
                WU = "Entrées";
                break;
            case mslRESEAUX:                
                WU = "Réseaux";
                break;    
            case mslSECTEURS:                
                WU = "Secteurs";
                break;    
            case mslCODES:                
                WU = "Codes";
                break;    
            case mslEXPES:                
                WU = "Expés";
                break;    
            case mslSERIES:                
                WU = "Séries";
                break;    
            default:
                break;
        } 
        
        this.setTitle(WU + " - " + myDocTopo.getNomEtude());
        
        refreshListe();
    }        
    public int getIdxElementSelected()
    {
        return fElementSelected;
    }      
    public int getModalResult()
    {
        return fModalResult;
    }      
    private void refreshListe()
    {
        fFullyListed = false;
        DefaultListModel ldm = new DefaultListModel();
        lsbListeElements.setModel(ldm);
        lsbListeElements.removeAll();
        int nb = 0;
        afficherMessageFmt("Mode sélection: %d", modeSelectDsListes);
        switch (modeSelectDsListes)
        {
            case mslENTRANCES:
                nb = myDocTopo.getNbEntrances();
                for (int i = 0; i < nb; i++)
                {
                    TEntrance e = myDocTopo.getEntrance(i);
                    ldm.addElement(sprintf("%d - %s", e.eNumEntree, e.eNomEntree));
                }    
                break;
            case mslRESEAUX:                
                nb = myDocTopo.getNbReseaux(); 
                afficherMessageFmt("--- %d reseaux", nb);
                
                for (int i = 0; i < nb; i++)
                {
                    TReseau r = myDocTopo.getReseau(i);
                    ldm.addElement(sprintf("%d - %s", r.IdxReseau, r.NomReseau));
                }    
                break;    
            case mslSECTEURS:                
                nb = myDocTopo.getNbSecteurs();
                for (int i = 0; i < nb; i++)
                {
                    TSecteur s = myDocTopo.getSecteur(i);
                    ldm.addElement(sprintf("%d - %s", s.IdxSecteur, s.NomSecteur));
                }    
                break;    
            case mslCODES:                
                nb = myDocTopo.getNbCodes();
                for (int i = 0; i < nb; i++)
                {
                    TCode c = myDocTopo.getCode(i);
                    ldm.addElement(sprintf("%d - %s", c.IdxCode, c.Commentaire));
                }    
                break;    
            case mslEXPES:                
                nb = myDocTopo.getNbExpes();
                for (int i = 0; i < nb; i++)
                {
                    TExpe ex = myDocTopo.getExpe(i);
                    ldm.addElement(sprintf("%d - %s", ex.IdxExpe, ex.Commentaire));    
                }    
                break;    
            case mslSERIES:                
                nb = myDocTopo.getNbSeries();
                for (int i = 0; i < nb; i++)
                {
                    TSerie sr = myDocTopo.getSerie(i);
                    ldm.addElement(sprintf("%d - %s", sr.IdxSerie, sr.NomSerie));    
                }    
                break;    
            default:
                break;
        } 
        lsbListeElements.setSelectedIndex(fElementSelected);
        lbNbElements.setText(sprintf("%d items", nb));
        fFullyListed = true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbNbElements = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(2, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(2, 32767));
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lsbListeElements = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lbNbElements.setText("jLabel1");

        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        btnCancel.setText("Annuler");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        lsbListeElements.setAutoscrolls(false);
        lsbListeElements.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lsbListeElementsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(lsbListeElements);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbNbElements)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 894, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbNbElements)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOK)
                    .addComponent(btnCancel))
                .addGap(4, 4, 4))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        // TODO add your handling code here:
        fElementSelected = lsbListeElements.getSelectedIndex();
        fModalResult = mrOK;     // pose la valeur de ModalResult
        this.setVisible(false);  // ferme le dialogue
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        fModalResult = mrCANCEL;
        this.setVisible(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void lsbListeElementsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lsbListeElementsValueChanged
        // TODO add your handling code here:
        if (fFullyListed) // liste complète ?
        {
            ///...
        }    
    }//GEN-LAST:event_lsbListeElementsValueChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(TdlgSelectDansListes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TdlgSelectDansListes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TdlgSelectDansListes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TdlgSelectDansListes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TdlgSelectDansListes().setVisible(true);
            }
        });
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOK;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbNbElements;
    private javax.swing.JList lsbListeElements;
    // End of variables declaration//GEN-END:variables
}


