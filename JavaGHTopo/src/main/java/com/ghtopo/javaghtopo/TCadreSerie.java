// 22/08/2014
package com.ghtopo.javaghtopo;

import static GHTopoOperationnel.Constantes.*;
import static GHTopoOperationnel.GeneralFunctions.*;
import static UtilsComposants.TOperationsSurComposants.*;
import static GHTopoOperationnel.TCallDialogs.*;
import GHTopoOperationnel.Types.TStation;
import GHTopoOperationnel.TSerie;
import GHTopoOperationnel.TToporobotStructure;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFileChooser;
import java.io.File;
import java.util.ResourceBundle;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import org.hsqldb.resources.BundleHandler;



public class TCadreSerie extends javax.swing.JPanel {
    private TToporobotStructure myFDocTopo; // pour recherches dans le doc
    private TSerie myFSerie;
    private DefaultTableModel myModelTable;
    private JComboBox cmbTypeVisee;
    private DefaultCellEditor dce;
    private ResourceBundle fMyResourceBundle = null;
    // le bundle
    private String getResourceByKey(String k)
    {
        return fMyResourceBundle.getString(k);
    }        
   
    public void initialiseCadreSerie(TToporobotStructure dt, TSerie serie)
    {
        myFDocTopo = dt; 
        myFSerie = serie; 
        editIdxSerie.setText(intToStr(myFSerie.IdxSerie));
        editNomSerie.setText(myFSerie.NomSerie);
        editObservSerie.setText(myFSerie.ObsSerie);
        editSerieDep.setText(intToStr(myFSerie.SerDep));
        editPtDep.setText(intToStr(myFSerie.PtDep));
        editSerieArr.setText(intToStr(myFSerie.SerArr));
        editPtArr.setText(intToStr(myFSerie.PtArr));
        editIdxReseau.setText(intToStr(myFSerie.Reseau));
        cmbChance.setSelectedIndex(myFSerie.Chance);
        cmbObstacle.setSelectedIndex(myFSerie.Obstacle);
        // tableau
        int nbVisees = this.myFSerie.getNbStations();
        //((DefaultTableModel) grdVisees.getModel()).setRowCount(nbVisees);
        myModelTable = (DefaultTableModel) grdVisees.getModel();
        myModelTable.setRowCount(nbVisees);
        for (int i = 0; i < nbVisees; i++)
        {
            putTStationInGrid(i,  myFSerie.getStation(i));
        }            
    } 
    public TSerie getSerieFromForm()
    {
        TSerie result = new TSerie();
        // entete
        result.IdxSerie    = strToIntDef(editIdxSerie.getText().trim(), 0);
        result.NomSerie    = editNomSerie.getText().trim();
        result.ObsSerie    = editObservSerie.getText().trim();
        result.SerDep      = strToIntDef(editSerieDep.getText().trim(), 0);
        result.PtDep       = strToIntDef(editPtDep.getText().trim(), 0);
        result.SerArr      = strToIntDef(editSerieArr.getText().trim(), 0);
        result.PtArr       = strToIntDef(editPtArr.getText().trim(), 0);
        result.Reseau      = strToIntDef(editIdxReseau.getText().trim(), 0);
        result.Chance      = cmbChance.getSelectedIndex();
        result.Obstacle    = cmbObstacle.getSelectedIndex();  
        // tableau
        result.clearVisees();
        int Nb = grdVisees.getRowCount();
        for (int i = 0; i < Nb; i++)
        {
           result.addStation(makeTStationFromGrid(i));
        }    
        return result;
    }        
    private TStation makeTStationFromGrid(int NoLigne)
    {
        TStation result = new TStation();
        result.IDStation = (int) grdVisees.getValueAt(NoLigne, 0);
        result.IDTerrain = grdVisees.getValueAt(NoLigne, 1).toString().trim().toUpperCase();
        result.Secteur   = (int) grdVisees.getValueAt(NoLigne, 2);
        result.TypeVisee = (int) grdVisees.getValueAt(NoLigne, 3);
        result.Code      = (int) grdVisees.getValueAt(NoLigne, 4);
        result.Expe      = (int) grdVisees.getValueAt(NoLigne, 5);
        result.Longueur  = (double) grdVisees.getValueAt(NoLigne, 6);
        result.Azimut    = (double) grdVisees.getValueAt(NoLigne, 7);
        result.Pente     = (double) grdVisees.getValueAt(NoLigne, 8);
        result.LG        = (double) grdVisees.getValueAt(NoLigne, 9);
        result.LD        = (double) grdVisees.getValueAt(NoLigne, 10);
        result.HZ        = (double) grdVisees.getValueAt(NoLigne, 11);
        result.HN        = (double) grdVisees.getValueAt(NoLigne, 12);
        result.Commentaires = grdVisees.getValueAt(NoLigne, 13).toString().trim();
        return result;
    }  
    private void putTStationInGrid(int NoLigne, TStation st)
    {
        grdVisees.setValueAt(st.IDStation, NoLigne, 0);
        grdVisees.setValueAt(st.IDTerrain, NoLigne, 1);
        grdVisees.setValueAt(st.Secteur, NoLigne, 2);
        grdVisees.setValueAt(st.TypeVisee, NoLigne, 3);
        grdVisees.setValueAt(st.Code, NoLigne, 4);
        grdVisees.setValueAt(st.Expe, NoLigne, 5);
        grdVisees.setValueAt(st.Longueur, NoLigne, 6);
        grdVisees.setValueAt(st.Azimut, NoLigne, 7);
        grdVisees.setValueAt(st.Pente, NoLigne, 8);
        grdVisees.setValueAt(st.LG, NoLigne, 9);
        grdVisees.setValueAt(st.LD, NoLigne, 10);
        grdVisees.setValueAt(st.HZ, NoLigne, 11);
        grdVisees.setValueAt(st.HN, NoLigne, 12);
        grdVisees.setValueAt(st.Commentaires, NoLigne, 13);
    }        
    //--------------------------------------------------------------------------
    public TCadreSerie() 
    {
        this.fMyResourceBundle = java.util.ResourceBundle.getBundle("com/ghtopo/javaghtopo/Bundle");
        cmbTypeVisee = new JComboBox();
        String[] bb = {
                "Défaut", // tgDEFAULT     = 0,         // X défaut = galerie fossile
                "Entrée", // tgENTRANCE    = 1,
                "Fossile", //tgFOSSILE     = 2,
                "Semi-active", //tgVADOSE      = 3,
                "Ennoyable",  //tgENNOYABLE   = 4,
                "Siphon", // tgSIPHON      = 5,
                "Point fixe", //tgFIXPOINT    = 6,
                "Spéciale", //tgSURFACE     = 7,
                "Tunnel artificiel", //      = 8,
                "Filon minier", //public static final int tgMINE        = 9,                            
        };
        preparerComboBox(cmbTypeVisee, bb, 0);
        dce = new DefaultCellEditor(cmbTypeVisee);
        initComponents();
	
		
        String[] cc = {
            getResourceByKey("cdrSERIE_CHANCE_NONE"),
            getResourceByKey("cdrSERIE_CHANCE_LOW"),
            getResourceByKey("cdrSERIE_CHANCE_GOOD"),
            getResourceByKey("cdrSERIE_CHANCE_EXCELLENT")
        };
        preparerComboBox(cmbChance, cc, 0);
        String [] dd =
        {
            getResourceByKey("rsCDR_SERIE_OBSTACLE_NONE"),
            getResourceByKey("rsCDR_SERIE_OBSTACLE_PUITS"),
            getResourceByKey("rsCDR_SERIE_OBSTACLE_CHIMNEY"),
            getResourceByKey("rsCDR_SERIE_OBSTACLE_ETROITURE"),
            getResourceByKey("rsCDR_SERIE_OBSTACLE_LAKE"),
            getResourceByKey("rsCDR_SERIE_OBSTACLE_SIPHON"),
            getResourceByKey("rsCDR_SERIE_OBSTACLE_COLLAPSE"),
            getResourceByKey("rsCDR_SERIE_OBSTACLE_SPELEOTHEMS"),
            getResourceByKey("rsCDR_SERIE_OBSTACLE_SEDIMENTS"),
            getResourceByKey("rsCDR_SERIE_OBSTACLE_OTHER"),
            getResourceByKey("rsCDR_SERIE_OBSTACLE_TOXIC_GAS"),
            getResourceByKey("rsCDR_SERIE_OBSTACLE_OIES_AGRESSIVES"),
            getResourceByKey("rsCDR_SERIE_OBSTACLE_ANIMAUX_DANGEREUX"),
            getResourceByKey("rsCDR_SERIE_OBSTACLE_FUCKING_BLUE_BOY")
        };  
        preparerComboBox(cmbObstacle, dd, 0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popmnuMenuGrille = new javax.swing.JPopupMenu();
        mnuGrdNewStation = new javax.swing.JMenuItem();
        mnuGrdInsertLine = new javax.swing.JMenuItem();
        mnuGrdDeleteLine = new javax.swing.JMenuItem();
        jScrollPane2 = new javax.swing.JScrollPane();
        grdVisees = new javax.swing.JTable();
        cmbChance = new javax.swing.JComboBox();
        cmbObstacle = new javax.swing.JComboBox();
        btnChooseSerPtDep = new javax.swing.JButton();
        btnChooseSerPtArr = new javax.swing.JButton();
        btnChooseReseau = new javax.swing.JButton();
        editObservSerie = new javax.swing.JFormattedTextField();
        editIdxSerie = new javax.swing.JTextField();
        editNomSerie = new javax.swing.JTextField();
        editPtDep = new javax.swing.JTextField();
        editSerieDep = new javax.swing.JTextField();
        editSerieArr = new javax.swing.JTextField();
        editPtArr = new javax.swing.JTextField();
        editIdxReseau = new javax.swing.JTextField();
        chkLocked = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        mnuGrdNewStation.setText("Ajouter station");
        mnuGrdNewStation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuGrdNewStationActionPerformed(evt);
            }
        });
        popmnuMenuGrille.add(mnuGrdNewStation);

        mnuGrdInsertLine.setText("Insérer ligne");
        mnuGrdInsertLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuGrdInsertLineActionPerformed(evt);
            }
        });
        popmnuMenuGrille.add(mnuGrdInsertLine);

        mnuGrdDeleteLine.setText("Supprimer ligne");
        mnuGrdDeleteLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuGrdDeleteLineActionPerformed(evt);
            }
        });
        popmnuMenuGrille.add(mnuGrdDeleteLine);

        grdVisees.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "St", "ID", "Secteur", "Type visée", "Code", "Expé", "Long", "Azimut", "Pente", "Gauche", "Droite", "Haut", "Bas", "Observ"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        grdVisees.setComponentPopupMenu(popmnuMenuGrille);
        grdVisees.setName("null");
        grdVisees.setRequestFocusEnabled(false);
        grdVisees.setRowHeight(20);
        grdVisees.setShowHorizontalLines(false);
        grdVisees.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(grdVisees);
        if (grdVisees.getColumnModel().getColumnCount() > 0) {
            grdVisees.getColumnModel().getColumn(3).setCellEditor(dce);
        }
        grdVisees.getAccessibleContext().setAccessibleName("null");
        grdVisees.getAccessibleContext().setAccessibleDescription("");

        cmbChance.setMinimumSize(new java.awt.Dimension(58, 25));
        cmbChance.setNextFocusableComponent(cmbObstacle);
        cmbChance.setPreferredSize(new java.awt.Dimension(50, 25));

        cmbObstacle.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbObstacle.setMinimumSize(new java.awt.Dimension(58, 25));
        cmbObstacle.setNextFocusableComponent(editObservSerie);
        cmbObstacle.setPreferredSize(new java.awt.Dimension(50, 25));

        btnChooseSerPtDep.setText("null");
        btnChooseSerPtDep.setMaximumSize(new java.awt.Dimension(35, 25));
        btnChooseSerPtDep.setMinimumSize(new java.awt.Dimension(35, 25));
        btnChooseSerPtDep.setNextFocusableComponent(editSerieArr);
        btnChooseSerPtDep.setPreferredSize(new java.awt.Dimension(35, 25));
        btnChooseSerPtDep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChooseSerPtDepActionPerformed(evt);
            }
        });

        btnChooseSerPtArr.setText("null");
        btnChooseSerPtArr.setMaximumSize(new java.awt.Dimension(35, 25));
        btnChooseSerPtArr.setMinimumSize(new java.awt.Dimension(35, 25));
        btnChooseSerPtArr.setNextFocusableComponent(chkLocked);
        btnChooseSerPtArr.setPreferredSize(new java.awt.Dimension(35, 25));
        btnChooseSerPtArr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChooseSerPtArrActionPerformed(evt);
            }
        });

        btnChooseReseau.setText("null");
        btnChooseReseau.setMaximumSize(new java.awt.Dimension(35, 25));
        btnChooseReseau.setMinimumSize(new java.awt.Dimension(35, 25));
        btnChooseReseau.setNextFocusableComponent(cmbChance);
        btnChooseReseau.setPreferredSize(new java.awt.Dimension(35, 25));
        btnChooseReseau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChooseReseauActionPerformed(evt);
            }
        });

        editObservSerie.setMinimumSize(new java.awt.Dimension(6, 25));
        editObservSerie.setNextFocusableComponent(grdVisees);
        editObservSerie.setPreferredSize(new java.awt.Dimension(6, 25));

        editIdxSerie.setText("0");
        editIdxSerie.setMinimumSize(new java.awt.Dimension(60, 25));
        editIdxSerie.setNextFocusableComponent(editNomSerie);
        editIdxSerie.setPreferredSize(new java.awt.Dimension(60, 25));

        editNomSerie.setText("0");
        editNomSerie.setMinimumSize(new java.awt.Dimension(60, 25));
        editNomSerie.setNextFocusableComponent(editSerieDep);
        editNomSerie.setPreferredSize(new java.awt.Dimension(60, 25));

        editPtDep.setText("0");
        editPtDep.setMinimumSize(new java.awt.Dimension(60, 25));
        editPtDep.setNextFocusableComponent(btnChooseSerPtDep);
        editPtDep.setPreferredSize(new java.awt.Dimension(60, 25));

        editSerieDep.setText("0");
        editSerieDep.setMinimumSize(new java.awt.Dimension(60, 25));
        editSerieDep.setNextFocusableComponent(editPtDep);
        editSerieDep.setPreferredSize(new java.awt.Dimension(60, 25));

        editSerieArr.setText("0");
        editSerieArr.setMinimumSize(new java.awt.Dimension(60, 25));
        editSerieArr.setNextFocusableComponent(editPtArr);
        editSerieArr.setPreferredSize(new java.awt.Dimension(60, 25));

        editPtArr.setText("0");
        editPtArr.setMinimumSize(new java.awt.Dimension(60, 25));
        editPtArr.setNextFocusableComponent(btnChooseSerPtArr);
        editPtArr.setPreferredSize(new java.awt.Dimension(60, 25));

        editIdxReseau.setText("0");
        editIdxReseau.setMinimumSize(new java.awt.Dimension(60, 25));
        editIdxReseau.setNextFocusableComponent(btnChooseReseau);
        editIdxReseau.setPreferredSize(new java.awt.Dimension(60, 25));

        chkLocked.setText("Verrouillé");
        chkLocked.setNextFocusableComponent(editIdxReseau);

        jLabel1.setText("Numéro");

        jLabel2.setText("Nom");

        jLabel3.setText("Départ");

        jLabel4.setText("Arrivée");

        jLabel5.setText("Réseau");

        jLabel6.setText("Observations");

        jLabel7.setText("Chances");

        jLabel8.setText("Obstacle");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel7))
                        .addGap(41, 41, 41))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(editSerieDep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(editPtDep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnChooseSerPtDep, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21)
                                .addComponent(jLabel4))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cmbChance, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel8)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(editSerieArr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(editPtArr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnChooseSerPtArr, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cmbObstacle, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chkLocked)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(editIdxReseau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnChooseReseau, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(89, 168, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(editIdxSerie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(editNomSerie, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(editObservSerie, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editIdxSerie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editNomSerie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnChooseSerPtDep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(editSerieDep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(editPtDep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4)
                                .addComponent(editSerieArr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnChooseSerPtArr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(chkLocked)
                                .addComponent(editPtArr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cmbObstacle, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmbChance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8)
                            .addComponent(jLabel7)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(editIdxReseau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnChooseReseau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editObservSerie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnChooseSerPtDepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChooseSerPtDepActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_btnChooseSerPtDepActionPerformed

    private void btnChooseSerPtArrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChooseSerPtArrActionPerformed
        // TODO add your handling code here:        
    }//GEN-LAST:event_btnChooseSerPtArrActionPerformed

    private void btnChooseReseauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChooseReseauActionPerformed
        // TODO add your handling code here:
        int EWE = strToIntDef(this.editIdxReseau.getText(), 0);
        EWE = selectionnerDansListe(myFDocTopo, mslRESEAUX, EWE);
        editIdxReseau.setText(sprintf("%d", EWE));
    }//GEN-LAST:event_btnChooseReseauActionPerformed

    private void mnuGrdInsertLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuGrdInsertLineActionPerformed
        // TODO add your handling code here:
        Object EWE[] = {null, null, null, null, null, null, null, null, null, null, null, null, null, null};
        int NoLigneInseree = grdVisees.getSelectedRow();
        myModelTable.insertRow(NoLigneInseree, EWE);
        TStation s;
        if (NoLigneInseree > 0) 
        {
            s = makeTStationFromGrid(NoLigneInseree - 1);
            s.Longueur = 0.00;
            s.Azimut   = 0.00;
            s.Pente    = 0.00;
            s.LG       = 0.00;
            s.LD       = 0.00;
            s.HZ       = 0.00;
            s.HN       = 0.00;
            s.Commentaires = "** Inseree **";
            putTStationInGrid(NoLigneInseree, s);
        }
    }//GEN-LAST:event_mnuGrdInsertLineActionPerformed

    private void mnuGrdNewStationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuGrdNewStationActionPerformed
        // TODO add your handling code here
        // on attrappe la dernière station
        int lastLine = grdVisees.getRowCount() - 1;
        TStation s = makeTStationFromGrid(lastLine);
        // on vire les mesures
        s.IDTerrain = autoIncrementerLbTerrain(s.IDTerrain); // TODO: IDterrain autoincrémenté
        s.Longueur = 0.00;
        s.Azimut   = 0.00;
        s.Pente    = 0.00;
        s.LG       = 0.00;
        s.LD       = 0.00;
        s.HZ       = 0.00;
        s.HN       = 0.00;
        s.Commentaires = "";
        // on crée la ligne
        Object wu[] = {null, null, null, null, null, null, null, null, null, null, null, null, null, null};
        myModelTable.addRow(wu);
        // on attrappe le numéro de dernière ligne et on recopie la station
        lastLine = grdVisees.getRowCount() - 1;
        putTStationInGrid(lastLine, s);        
    }//GEN-LAST:event_mnuGrdNewStationActionPerformed

    private void mnuGrdDeleteLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuGrdDeleteLineActionPerformed
        // TODO add your handling code here:
        int wu = grdVisees.getSelectedRow();
        afficherMessageFmt("Ligne: %d", wu);
        if (questionOuiNon(null, "Editeur de série", sprintf("Supprimer la ligne %d", wu))) 
            myModelTable.removeRow(wu);        
    }//GEN-LAST:event_mnuGrdDeleteLineActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChooseReseau;
    private javax.swing.JButton btnChooseSerPtArr;
    private javax.swing.JButton btnChooseSerPtDep;
    private javax.swing.JCheckBox chkLocked;
    private javax.swing.JComboBox cmbChance;
    private javax.swing.JComboBox cmbObstacle;
    private javax.swing.JTextField editIdxReseau;
    private javax.swing.JTextField editIdxSerie;
    private javax.swing.JTextField editNomSerie;
    private javax.swing.JFormattedTextField editObservSerie;
    private javax.swing.JTextField editPtArr;
    private javax.swing.JTextField editPtDep;
    private javax.swing.JTextField editSerieArr;
    private javax.swing.JTextField editSerieDep;
    private javax.swing.JTable grdVisees;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JMenuItem mnuGrdDeleteLine;
    private javax.swing.JMenuItem mnuGrdInsertLine;
    private javax.swing.JMenuItem mnuGrdNewStation;
    private javax.swing.JPopupMenu popmnuMenuGrille;
    // End of variables declaration//GEN-END:variables
}
