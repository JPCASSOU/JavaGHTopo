// Troubleshooting:
// Designer: Erreur: Error in loading component
//           Remède: Reconstruire le projet
//------------------------------------------------------------------------------
// 17/07/2014: Gestion des resourcestrings
//             Fixation de l'obtention du nom de fichier issu des OpenDialog   
package com.ghtopo.javaghtopo;

import static GHTopoOperationnel.Constantes.*;
import static GHTopoOperationnel.GeneralFunctions.*;
import static GHTopoOperationnel.GeneralFunctions.*;
import static GHTopoOperationnel.GeneralFunctions.afficherMessage;
import static GHTopoOperationnel.GeneralFunctions.getDay;
import static GHTopoOperationnel.GeneralFunctions.getDescTypeReseau;
import static GHTopoOperationnel.GeneralFunctions.getMonth;
import static GHTopoOperationnel.GeneralFunctions.getYear;
import static GHTopoOperationnel.GeneralFunctions.indexOfString;

import static GHTopoOperationnel.TCallDialogs.*;
import GHTopoOperationnel.TCodeDeCalcul;
import GHTopoOperationnel.TDateTime;
import GHTopoOperationnel.TPalette256;
import GHTopoOperationnel.TSerie;
import GHTopoOperationnel.TTableDesEntites;
import GHTopoOperationnel.TToporobotStructure;
import GHTopoOperationnel.Types.TCode;
import GHTopoOperationnel.Types.TEntiteEtendue;
import GHTopoOperationnel.Types.TEntrance;
import GHTopoOperationnel.Types.TExpe;
import GHTopoOperationnel.Types.TPoint3Df;
import GHTopoOperationnel.Types.TReseau;
import GHTopoOperationnel.Types.TSecteur;
import GHTopoOperationnel.Types.TStation;
import java.awt.Color;
import java.io.File;
import java.util.ResourceBundle;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.ListModel;
import org.jdom2.filter.Filters;
public class TMainWindow extends javax.swing.JFrame {
    private ResourceBundle fMyResourceBundle = null;
   
    // Champs privés
    private TToporobotStructure myFDocuTopo;
    private TTableDesEntites    myFTableEntites;
    private TPalette256         myFPalette256;
    // Convertisseur de coordonnéess ?
    
    
    private boolean mFDocuTopoLoaded = false;
    private int     myModeSelectionDsListes = mslENTRANCES;
    
    private int     currIdxEntrance  = 0;
    private int     currIdxReseau    = 0;
    private int     currIdxSecteur   = 0;
    private int     currIdxCode      = 0;
    private int     currIdxExpe      = 0;
    private int     currIdxSerie     = 0;
    // renderer de ligne
    private ComponentRenderer myComponentRenderer;
    // les listes sont complètes ?
    // l'ajout d'un élément dans une JList déclenche un événement ValueChanged (OnChange - like)
    private boolean fFullyListed = false;
    //------------------------------------------------
    /** Constructeur **/
    public TMainWindow() 
    {            
        this.fMyResourceBundle = java.util.ResourceBundle.getBundle("com/ghtopo/javaghtopo/Bundle");
        initComponents();
        myComponentRenderer = new ComponentRenderer();
        lsbListe.setCellRenderer(myComponentRenderer);
        
        this.setLocationRelativeTo(null); // centrée
        this.myFDocuTopo = new TToporobotStructure("MaTopo"); //NOI18N
        this.myFDocuTopo.viderListes(true);
        this.myFTableEntites = new TTableDesEntites();  
        this.myFPalette256 = new TPalette256();
        this.myFPalette256.generateTOPOROBOTPalette();
        this.tabpanBDD.setSelectedIndex(ongletENTREE);
        
        
        // bac à sable
        String WU[] = {"toto", "tata", "tutu", "titi", "6666"}; //NOI18N
        int EWE = indexOfString("titi", true,  WU); //NOI18N
        afficherMessageFmt("Test de indexOfstring: %d", EWE); //NOI18N
          
            //------------------------------------------------------------------
            // Sandbox:
            afficherMessage("*** Sandbox *****");
            
            afficherMessage("Formattage de lon/lat");
            afficherMessage(ensureConvertNumberToStr(3.1415926535));
            afficherMessage(ensureConvertNumberToStr(0.1415926535));
            afficherMessage(ensureConvertNumberToStr(-0.1415926535));
            afficherMessage(ensureConvertNumberToStr(8.94530));
            afficherMessage(ensureConvertNumberToStr(14.0));
            afficherMessage(ensureConvertNumberToStr(-65.33));
            afficherMessage(ensureConvertNumberToStr(-128.666));
            afficherMessage(ensureConvertNumberToStr(-12878914.654321));
            afficherMessage(ensureConvertNumberToStr(12878914.654321));
            afficherMessage("Increment string");
            afficherMessage(autoIncrementerLbTerrain("1.1"));
            afficherMessage(autoIncrementerLbTerrain("10/33"));
            afficherMessage(autoIncrementerLbTerrain("69-73"));
            afficherMessage(autoIncrementerLbTerrain("ACDC,666"));
            afficherMessage(autoIncrementerLbTerrain("ACDC321:1247"));
            afficherMessage(autoIncrementerLbTerrain("ACDC321:AB368"));
            afficherMessage(autoIncrementerLbTerrain(""));
            afficherMessage(autoIncrementerLbTerrain("A64"));
            afficherMessage("Dégradé couleurs");
            double qz = 666.00;
            double qZ = 2131.00;
            Color cz = new Color(255,110, 0);
            //Color cZ = new Color(25, 45, 244);
            Color cZ = new Color(0, 0, 255);
            double z = qz + 1.00;
            while(z < qZ)
            {
                Color C = getColorDegrade(z, qz, qZ, cz, cZ);
                afficherMessageFmt("Cote: %.2f (%.2f -> %.2f) = %d, %d, %d", z, qz, qZ, C.getRed(), C.getGreen(), C.getBlue());
                z +=10.0;
            }    
            
            afficherMessage(autoIncrementerLbTerrain("1.1"));
            //------------------------------------------------------------------
            afficherMessage("Objet TDate");
            TDateTime myObjDate = new TDateTime(45 * 365 * 86400);
            afficherMessageFmt("Date courante: %02d/%02d/%04d  %02d:%02d:%02d", 
                               myObjDate.getDay(), myObjDate.getMonth(), myObjDate.getYear(),
                               myObjDate.getHour(), myObjDate.getMinute(), myObjDate.getSeconds());
            afficherMessageFmt("%d", myObjDate.getDateSerialInSeconds());
            
            myObjDate.setDateTime(1991, 06, 24, 12, 34, 56, 78);
            afficherMessageFmt("Date transmise: %02d/%02d/%04d %02d:%02d:%02d", 
                               myObjDate.getDay(), myObjDate.getMonth(), myObjDate.getYear(),
                               myObjDate.getHour(), myObjDate.getMinute(), myObjDate.getSeconds()
            );
            afficherMessageFmt("Date OOo.org: %.8f", myObjDate.getDateAsOOoFormat());
            //------------------------------------------------------------------
            afficherMessage("Objet Couleur");
           
            
            afficherMessage("*** End Sandbox *****");
    }

    private String getResourceByKey(String k)
    {
        return fMyResourceBundle.getString(k);
    }       
    // Classes utilitaires de calcul
    private void chargerFichierXTB(String qFileName)
    {
        afficherMessage(getResourceByKey("rsLoad_XTB_FILE"));       
        myFDocuTopo.loadFileXTB(qFileName);
        //myFDocuTopo.ListerLeDocument();
        this.mFDocuTopoLoaded = true;
    } 
    private void calculerLeReseau()
    {
        afficherMessage(getResourceByKey("rsCALCULATE_NETWORK"));
        //if (this.mFDocuTopoLoaded)
        {
            TCodeDeCalcul myCodeCalcul = new TCodeDeCalcul(myFDocuTopo, myFTableEntites);
            // calcul topo
            myCodeCalcul.recenserJonctions();
            myCodeCalcul.recenserBranches();
            // Branches
            myCodeCalcul.calculerLesBranches();
            // Matrices
            myCodeCalcul.makeRMatrix();
            myCodeCalcul.makeBMatrix();
            for (int i = 1; i <=3; i++) {
                myCodeCalcul.makeSecondMembre(i);
                myCodeCalcul.SolveMatrix(i);
            }
            // Calcul de répartition
            myCodeCalcul.repartirEcarts();
            // Contour des galeries
            myCodeCalcul.calculerContoursGaleries();
            // traitement des antennes -- TODO: Fixer le pb des antennes
            //myCodeCalcul.traiterLesAntennes();
            // trier par séries
            myFTableEntites.trierParSeriesStations();
            // lister les entitées
            //myFTableEntites.listerLesEntites();
            // mini et maxi
            myFTableEntites.setMiniMaxi();
            // trier les entitées
            myFTableEntites.trierParProfondeurs();
            // les dates
            myFTableEntites.recenserLesDates();
            // relister
            myFTableEntites.listerTablesReseauxSecteursCodesExpes();
            //myFTableEntites.listerLesEntites();
            // calcul de la spéléométrie
            myFTableEntites.calculerSpeleometrie();
            // afficher spéléométrie
            myFTableEntites.afficherLaSpeleometrie();
            
        }    
    }        
   
    public void initialiserVue3D()
    {
        //tCadreVue3D1.initialiseVue3D(myFTableEntites);
    }    
    //**************************************************************************
    // gestion de la liste
     private void listerLesEntrees(int qidx)
    {
        fFullyListed = false;
        lsbListe.removeAll();
        DefaultListModel ldm = new DefaultListModel();
        lsbListe.setModel(ldm);
        int nb = this.myFDocuTopo.getNbEntrances();
        lbNbreElements.setText(sprintf(getResourceByKey("rsLISTE_INTEGREE_NBENTREES"), nb));
        for (int i = 0; i < nb; i++)
        {
            TEntrance myEntrance = this.myFDocuTopo.getEntrance(i);
            String EWE = sprintf("%d : %s - %d.%d", //NOI18N
                                        myEntrance.eNumEntree,
                                        myEntrance.eNomEntree,
                                        myEntrance.eRefSer, myEntrance.eRefSt
                                        );  
            ldm.addElement(EWE);
        }
        currIdxCode = qidx;
        lsbListe.setSelectedIndex(qidx);        
        fFullyListed = true;
    } 
   
    private void listerLesReseaux(int qidx)
    {
        fFullyListed = false;
        lsbListe.removeAll();
        DefaultListModel ldm = new DefaultListModel();
        lsbListe.setModel(ldm);
        int nb = this.myFDocuTopo.getNbReseaux();
        lbNbreElements.setText(sprintf(getResourceByKey("rsLISTE_INTEGREE_NBRESEAUX"), nb));
        for (int i = 0; i < nb; i++)
        {
            TReseau myReseau = this.myFDocuTopo.getReseau(i);
            String EWE = sprintf("%d : %s (%s)", //NOI18N
                                        myReseau.IdxReseau,
                                        myReseau.NomReseau,
                                        getDescTypeReseau(myReseau.TypeReseau));
            ldm.addElement(EWE);
        }
        currIdxReseau = qidx;
        lsbListe.setSelectedIndex(qidx);        
        fFullyListed = true;
    } 
    private void listerLesSecteurs(int qidx)
    {
        fFullyListed = false;
        lsbListe.removeAll();
        DefaultListModel ldm = new DefaultListModel();
        lsbListe.setModel(ldm);
        int nb = this.myFDocuTopo.getNbSecteurs();
        lbNbreElements.setText(sprintf(getResourceByKey("rsLISTE_INTEGREE_NBSECTEURS"), nb));
        for (int i = 0; i < nb; i++)
        {
            TSecteur mySecteur = this.myFDocuTopo.getSecteur(i);
            String EWE = sprintf("%d : %s", //NOI18N
                                        mySecteur.IdxSecteur,
                                        mySecteur.NomSecteur);
            ldm.addElement(EWE);
        }
        currIdxSecteur = qidx;
        lsbListe.setSelectedIndex(qidx);        
        fFullyListed = true;
    } 
    
    private void listerLesCodes(int qidx)
    {
        fFullyListed = false;
        afficherMessageFmt("Passe dans listerLesCodes: %d", qidx); //NOI18N
        lsbListe.removeAll();
        DefaultListModel ldm = new DefaultListModel();
        lsbListe.setModel(ldm);
        int nb = this.myFDocuTopo.getNbCodes(); 
        lbNbreElements.setText(sprintf(getResourceByKey("rsLISTE_INTEGREE_NBCODES"), nb));
        for (int i = 0; i < nb; i++)
        {
            TCode myCode = this.myFDocuTopo.getCode(i);
            String EWE = sprintf("%d : %s - %.0f, %.0f", //NOI18N
                                        myCode.IdxCode,
                                        myCode.Commentaire,
                                        myCode.UniteBoussole,
                                        myCode.UniteClino);
            afficherMessage("-- " + EWE); //NOI18N //NOI18N
            ldm.addElement(EWE);
        }
        currIdxCode = qidx;
        lsbListe.setSelectedIndex(qidx);        
        fFullyListed = true;
    } 
    private void listerLesExpes(int qidx)
    {
        fFullyListed = false;
        afficherMessageFmt("Passe dans listerLesExpes: %d", qidx); //NOI18N
        lsbListe.removeAll();
        DefaultListModel ldm = new DefaultListModel();
        lsbListe.setModel(ldm);
        ldm.clear();
        int nb = this.myFDocuTopo.getNbExpes(); 
        lbNbreElements.setText(sprintf(getResourceByKey("rsLISTE_INTEGREE_NBEXPES"), nb));
        for (int i = 0; i < nb; i++)
        {
            TExpe myExpe = this.myFDocuTopo.getExpe(i);
            String EWE = sprintf("%d : %02d/%02d/%04d - %s; %s - %s", //NOI18N
                                        myExpe.IdxExpe,
                                        getDay(myExpe.DateLeve), getMonth(myExpe.DateLeve), getYear(myExpe.DateLeve),
                                        myExpe.Speleometre,
                                        myExpe.Speleographe,
                                        myExpe.Commentaire);
            ldm.addElement(EWE);
        }
        currIdxExpe = qidx;
        lsbListe.setSelectedIndex(qidx);        
        fFullyListed = true;
    }
    private void listerLesSeries(int qidx)
    {
        fFullyListed = false;
        lsbListe.removeAll();
        DefaultListModel ldm = new DefaultListModel();
        lsbListe.setModel(ldm);
        int nb = this.myFDocuTopo.getNbSeries();
        lbNbreElements.setText(sprintf(getResourceByKey("rsLISTE_INTEGREE_NBSERIES"), nb));
        for (int i = 0; i < nb; i++)
        {
            TSerie mySerie = this.myFDocuTopo.getSerie(i);
            String EWE = sprintf("%d : %s - %d.%d -> %d.%d", //NOI18N
                                        mySerie.IdxSerie,
                                        mySerie.NomSerie,
                                        mySerie.SerDep, mySerie.PtDep,
                                        mySerie.SerArr, mySerie.PtArr
                                      );
            ldm.addElement(EWE);
        }
        currIdxSerie = qidx;
        lsbListe.setSelectedIndex(qidx);
        fFullyListed = true;
    } 
    public void lister(int modeSelection, int Idx)
    {
        myModeSelectionDsListes = modeSelection;
        myComponentRenderer.SetModeDeSelection(myModeSelectionDsListes);
        afficherMessageFmt("lister %d %d", modeSelection, Idx);
        int idxSelect = 0;
        switch(modeSelection)
        {
            case mslENTRANCES:
                idxSelect = (Idx == -1) ? currIdxEntrance : Idx; 
                listerLesEntrees(idxSelect);
                mettreEntreeDansSonCadre(idxSelect);
                break;
            case mslRESEAUX:
                idxSelect = (Idx == -1) ? currIdxReseau   : Idx;
                listerLesReseaux(idxSelect);
                mettreReseauDansSonCadre(idxSelect);
                break;
            case mslSECTEURS:
                idxSelect = (Idx == -1) ? currIdxSecteur  : Idx;
                listerLesSecteurs(idxSelect);
                mettreSecteurDansSonCadre(idxSelect);
                break;
            case mslCODES:
                idxSelect = (Idx == -1) ? currIdxCode     : Idx;
                listerLesCodes(idxSelect);
                mettreCodeDansSonCadre(idxSelect);
                break;
            case mslEXPES:
                idxSelect = (Idx == -1) ? currIdxExpe     : Idx;
                listerLesExpes(idxSelect);
                mettreExpeDansSonCadre(idxSelect);
                break;
            case mslSERIES:
                idxSelect = (Idx == -1) ? currIdxSerie    : Idx;
                listerLesSeries(idxSelect);
                mettreSerieDansSonCadre(idxSelect);
                
                break;
            default:
                break;
        }
       
    }        
    //--------------------------------------------------------------------------
    // préparer le frontal
    private void mettreEntreeDansSonCadre(int idx)
    {
        TEntrance myEntrance = this.myFDocuTopo.getEntrance(idx);
        tCadreEntree1.initialiserCadreEntree(this.myFDocuTopo, myEntrance);
    }   
    private void mettreReseauDansSonCadre(int idx)
    {
        TReseau myReseau = this.myFDocuTopo.getReseau(idx);
        tCadreReseau1.initialiserCadreReseau(myReseau);
    }
    private void mettreSecteurDansSonCadre(int idx)
    {
        TSecteur mySecteur = this.myFDocuTopo.getSecteur(idx);
        tCadreSecteur1.initialiserCadreSecteur(mySecteur);
    }
    private void mettreCodeDansSonCadre(int idx)
    {
        TCode myCode = this.myFDocuTopo.getCode(idx);
        tCadreCode1.initialiseCadreCode(myCode);
    }
    private void mettreExpeDansSonCadre(int idx)
    {
        TExpe myExpe = this.myFDocuTopo.getExpe(idx);
        tCadreExpe1.initialiseCadreExpe(myExpe);
    }
    private void mettreSerieDansSonCadre(int idx)
    {
        TSerie mySerie = this.myFDocuTopo.getSerie(idx);
        tCadreSerie1.initialiseCadreSerie(this.myFDocuTopo, mySerie);
    } 
    private void mettreGeneralDansSonCadre()
    {
        tCadreGeneral1.initialiseCadreGeneral(this.myFDocuTopo);
    }        
    public void preparerFrontal()
    {
        this.setTitle(sprintf(" %s - %s", this.myFDocuTopo.getNomEtude(), JAVA_GHTOPO_NAME)); //NOI18N
        mettreGeneralDansSonCadre();
        lister(mslRESEAUX, 0);
        lister(mslSECTEURS, 0);
        lister(mslCODES, 0);
        lister(mslEXPES, 0);
        lister(mslSERIES, 1);
        myComponentRenderer.SetDocTopoAndSelection(this.myFDocuTopo, this.myFPalette256, this.myModeSelectionDsListes);

        // antennes
        tCadreViseeAntenne1.initialiserCadreViseesAntennes(myFDocuTopo);
        // index de l'onglet des entrées
        myModeSelectionDsListes = mslENTRANCES;
        tabpanBDD.setSelectedIndex(myModeSelectionDsListes + 1);
        lister(mslENTRANCES, 0);
        
    }        
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        lbNbreElements = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        btnTrier = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lsbListe = new javax.swing.JList();
        jButton6 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        tabpanBDD = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        btnCalcTtesDecliMags = new javax.swing.JButton();
        tCadreGeneral1 = new com.ghtopo.javaghtopo.TCadreGeneral();
        tCadreEntree1 = new com.ghtopo.javaghtopo.TCadreEntree();
        tCadreReseau1 = new com.ghtopo.javaghtopo.TCadreReseau();
        tCadreSecteur1 = new com.ghtopo.javaghtopo.TCadreSecteur();
        tCadreCode1 = new com.ghtopo.javaghtopo.TCadreCode();
        tCadreExpe1 = new com.ghtopo.javaghtopo.TCadreExpe();
        tCadreSerie1 = new com.ghtopo.javaghtopo.TCadreSerie();
        tCadreViseeAntenne1 = new com.ghtopo.javaghtopo.TCadreViseeAntenne();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jButton1 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        editFiltre = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        mnuFILE = new javax.swing.JMenu();
        mnuOuvrirXTB = new javax.swing.JMenuItem();
        mnuOuvrirXML = new javax.swing.JMenuItem();
        mnuSaveXML = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        mnuExportGIS = new javax.swing.JMenuItem();
        mnuExportGHCaveDraw = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        mnuQuitter = new javax.swing.JMenuItem();
        mnuVisualisation = new javax.swing.JMenu();
        mnuVue2D = new javax.swing.JMenuItem();
        mnuVue3D = new javax.swing.JMenuItem();
        mnuVueOSM = new javax.swing.JMenuItem();
        mnuVueOpenGL = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        mnuStats = new javax.swing.JMenuItem();
        mnuTools = new javax.swing.JMenu();
        mnuCalculette = new javax.swing.JMenuItem();
        mnuPalette = new javax.swing.JMenuItem();
        mnuCalibrerDistoX = new javax.swing.JMenuItem();
        mnuHelp = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setName("Form"); // NOI18N

        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setName("jPanel2"); // NOI18N

        lbNbreElements.setText("null");
        lbNbreElements.setName("lbNbreElements"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/ghtopo/javaghtopo/Bundle"); // NOI18N
        jButton2.setText(bundle.getString("rsMAIN_WND_BTN_AJOUTER")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText(bundle.getString("rsMAIN_WND_BTN_SUPPRIMER")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N

        jButton4.setText(bundle.getString("rsMAIN_WND_BTN_MODIFIER")); // NOI18N
        jButton4.setName("jButton4"); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        btnTrier.setText("null");
        btnTrier.setName("btnTrier"); // NOI18N
        btnTrier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTrierActionPerformed(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        lsbListe.setAutoscrolls(false);
        lsbListe.setName("lsbListe"); // NOI18N
        lsbListe.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lsbListeValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(lsbListe);

        jButton6.setText("jButton6");
        jButton6.setName("jButton6"); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lbNbreElements)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnTrier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton6)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbNbreElements)
                            .addComponent(jButton2)
                            .addComponent(jButton4)
                            .addComponent(jButton6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTrier))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane1.setLeftComponent(jPanel2);

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setName("jPanel3"); // NOI18N

        tabpanBDD.setName("tabpanBDD"); // NOI18N
        tabpanBDD.setOpaque(true);
        tabpanBDD.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabpanBDDStateChanged(evt);
            }
        });

        jPanel1.setName("jPanel1"); // NOI18N

        btnCalcTtesDecliMags.setText("Calculer les déclinaisons magnétiques");
        btnCalcTtesDecliMags.setName("btnCalcTtesDecliMags"); // NOI18N
        btnCalcTtesDecliMags.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcTtesDecliMagsActionPerformed(evt);
            }
        });

        tCadreGeneral1.setName("tCadreGeneral1"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tCadreGeneral1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCalcTtesDecliMags, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(349, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tCadreGeneral1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCalcTtesDecliMags)
                .addContainerGap(321, Short.MAX_VALUE))
        );

        tabpanBDD.addTab("Général", jPanel1);

        tCadreEntree1.setName("tCadreEntree1"); // NOI18N
        tabpanBDD.addTab(bundle.getString("rsMAIN_WND_TAB_ENTREES"), tCadreEntree1); // NOI18N

        tCadreReseau1.setName("tCadreReseau1"); // NOI18N
        tabpanBDD.addTab(bundle.getString("rsMAIN_WND_TAB_RESEAUX"), tCadreReseau1); // NOI18N

        tCadreSecteur1.setName("tCadreSecteur1"); // NOI18N
        tabpanBDD.addTab(bundle.getString("rsMAIN_WND_TAB_SECTEURS"), tCadreSecteur1); // NOI18N

        tCadreCode1.setName("tCadreCode1"); // NOI18N
        tabpanBDD.addTab(bundle.getString("rsMAIN_WND_TAB_CODES"), tCadreCode1); // NOI18N

        tCadreExpe1.setName("tCadreExpe1"); // NOI18N
        tabpanBDD.addTab(bundle.getString("rsMAIN_WND_TAB_EXPES"), tCadreExpe1); // NOI18N

        tCadreSerie1.setName("tCadreSerie1"); // NOI18N
        tabpanBDD.addTab(bundle.getString("rsMAIN_WND_TAB_SERIES"), tCadreSerie1); // NOI18N

        tCadreViseeAntenne1.setName("tCadreViseeAntenne1"); // NOI18N
        tabpanBDD.addTab(bundle.getString("rsMAIN_WND_TAB_ANTENNES"), tCadreViseeAntenne1); // NOI18N

        jLayeredPane1.setMinimumSize(new java.awt.Dimension(800, 600));
        jLayeredPane1.setName("jLayeredPane1"); // NOI18N

        jButton1.setText("null");
        jButton1.setName("null");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton5.setText("null");
        jButton5.setName("null");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        editFiltre.setText("null");
        editFiltre.setName("editFiltre"); // NOI18N

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jButton5)
                    .addComponent(editFiltre, javax.swing.GroupLayout.PREFERRED_SIZE, 584, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(386, Short.MAX_VALUE))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addGap(38, 38, 38)
                .addComponent(editFiltre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton5)
                .addContainerGap(517, Short.MAX_VALUE))
        );
        jLayeredPane1.setLayer(jButton1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jButton5, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(editFiltre, javax.swing.JLayeredPane.DEFAULT_LAYER);

        tabpanBDD.addTab(bundle.getString("rsMAIN_WND_TAB_APERCU_2D"), jLayeredPane1); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabpanBDD)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabpanBDD)
        );

        jSplitPane1.setRightComponent(jPanel3);

        jMenuBar1.setName("jMenuBar1"); // NOI18N

        mnuFILE.setText(bundle.getString("rsMAIN_WND_MNU_FILE")); // NOI18N
        mnuFILE.setName("mnuFILE"); // NOI18N

        mnuOuvrirXTB.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        mnuOuvrirXTB.setText(bundle.getString("rsMAIN_WND_MNU_FILE_OPENXTB")); // NOI18N
        mnuOuvrirXTB.setName("mnuOuvrirXTB"); // NOI18N
        mnuOuvrirXTB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuOuvrirXTBActionPerformed(evt);
            }
        });
        mnuFILE.add(mnuOuvrirXTB);

        mnuOuvrirXML.setText(bundle.getString("rsMAIN_WND_MNU_OPEN_XML")); // NOI18N
        mnuOuvrirXML.setName("mnuOuvrirXML"); // NOI18N
        mnuOuvrirXML.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuOuvrirXMLActionPerformed(evt);
            }
        });
        mnuFILE.add(mnuOuvrirXML);

        mnuSaveXML.setText(bundle.getString("rsMAIN_WND_MNU_FILE_SAVE_XML")); // NOI18N
        mnuSaveXML.setName("mnuSaveXML"); // NOI18N
        mnuSaveXML.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSaveXMLActionPerformed(evt);
            }
        });
        mnuFILE.add(mnuSaveXML);
        mnuFILE.add(jSeparator4);

        mnuExportGIS.setText(bundle.getString("rsMAIN_WND_MNU_FILE_EXPORT_SIG")); // NOI18N
        mnuExportGIS.setName("mnuExportGIS"); // NOI18N
        mnuExportGIS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuExportGISActionPerformed(evt);
            }
        });
        mnuFILE.add(mnuExportGIS);

        mnuExportGHCaveDraw.setText("Générer polygonale GHCaveDraw");
        mnuExportGHCaveDraw.setName("mnuExportGHCaveDraw"); // NOI18N
        mnuExportGHCaveDraw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuExportGHCaveDrawActionPerformed(evt);
            }
        });
        mnuFILE.add(mnuExportGHCaveDraw);

        jSeparator1.setName("jSeparator1"); // NOI18N
        mnuFILE.add(jSeparator1);

        mnuQuitter.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        mnuQuitter.setText(bundle.getString("rsMAIN_WND_MNU_FILE_QUIT")); // NOI18N
        mnuQuitter.setName("mnuQuitter"); // NOI18N
        mnuQuitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuQuitterActionPerformed(evt);
            }
        });
        mnuFILE.add(mnuQuitter);

        jMenuBar1.add(mnuFILE);

        mnuVisualisation.setText(bundle.getString("rsMAIN_WND_MNU_VISUALISATION")); // NOI18N
        mnuVisualisation.setName("mnuVisualisation"); // NOI18N

        mnuVue2D.setText(bundle.getString("rsMAIN_WND_MNU_VISUALISATION_PLAN")); // NOI18N
        mnuVue2D.setName("mnuVue2D"); // NOI18N
        mnuVue2D.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuVue2DActionPerformed(evt);
            }
        });
        mnuVisualisation.add(mnuVue2D);

        mnuVue3D.setText(bundle.getString("rsMAIN_WND_MNU_VISUALISATION_VUE_3D")); // NOI18N
        mnuVue3D.setName("mnuVue3D"); // NOI18N
        mnuVue3D.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuVue3DActionPerformed(evt);
            }
        });
        mnuVisualisation.add(mnuVue3D);

        mnuVueOSM.setText("null");
        mnuVueOSM.setName("mnuVueOSM"); // NOI18N
        mnuVueOSM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuVueOSMActionPerformed(evt);
            }
        });
        mnuVisualisation.add(mnuVueOSM);

        mnuVueOpenGL.setText("Vue 3D OpenGL");
        mnuVueOpenGL.setName("mnuVueOpenGL"); // NOI18N
        mnuVueOpenGL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuVueOpenGLActionPerformed(evt);
            }
        });
        mnuVisualisation.add(mnuVueOpenGL);
        mnuVisualisation.add(jSeparator3);

        mnuStats.setText("Statistiques");
        mnuStats.setName("mnuStats"); // NOI18N
        mnuStats.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuStatsActionPerformed(evt);
            }
        });
        mnuVisualisation.add(mnuStats);

        jMenuBar1.add(mnuVisualisation);

        mnuTools.setText(bundle.getString("rsMAIN_WND_MNU_TOOLS")); // NOI18N
        mnuTools.setName("mnuTools"); // NOI18N

        mnuCalculette.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_K, java.awt.event.InputEvent.CTRL_MASK));
        mnuCalculette.setText(bundle.getString("rsMAIN_WND_MNU_TOOLS_CALCULETTE")); // NOI18N
        mnuCalculette.setName("mnuCalculette"); // NOI18N
        mnuCalculette.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCalculetteActionPerformed(evt);
            }
        });
        mnuTools.add(mnuCalculette);

        mnuPalette.setText("null");
        mnuPalette.setName("mnuPalette"); // NOI18N
        mnuPalette.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuPaletteActionPerformed(evt);
            }
        });
        mnuTools.add(mnuPalette);

        mnuCalibrerDistoX.setText("Calibrer un DistoX");
        mnuCalibrerDistoX.setName("mnuCalibrerDistoX"); // NOI18N
        mnuCalibrerDistoX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCalibrerDistoXActionPerformed(evt);
            }
        });
        mnuTools.add(mnuCalibrerDistoX);

        jMenuBar1.add(mnuTools);

        mnuHelp.setText(bundle.getString("rsMAIN_WND_MNU_HELP")); // NOI18N
        mnuHelp.setName("mnuHelp"); // NOI18N

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        jMenuItem1.setText(bundle.getString("rsMAIN_WND_MNU_HELP_APROPOS")); // NOI18N
        jMenuItem1.setName("null");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        mnuHelp.add(jMenuItem1);

        jMenuBar1.add(mnuHelp);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSplitPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuOuvrirXTBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuOuvrirXTBActionPerformed
    try
        {
            final JFileChooser fc = new JFileChooser(getGHTopoDirectory());
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) 
            {
                File file = fc.getSelectedFile();
                //myFDocuTopo.loadFileXML("Toporabot.gtx");
                // file.getAbsolutePath est l'équivalent de TOpenDialog.filename
                String WU = file.getAbsolutePath(); 
                //showMessage("", WU);
                myFDocuTopo.loadFileXTB(WU);
                //myFDocuTopo.ListerLeDocument();
                // calculer le réseau
                calculerLeReseau();
                
                // peupler l'interface
                preparerFrontal();
               
                // document ouvert avec succès
                mFDocuTopoLoaded = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }            
    }//GEN-LAST:event_mnuOuvrirXTBActionPerformed

    private void mnuOuvrirXMLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuOuvrirXMLActionPerformed
      try
        {
            final JFileChooser fc = new JFileChooser(getGHTopoDirectory());
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) 
            {
                File file = fc.getSelectedFile();
                //showMessage("Fichier choisi", file.getName()); //NOI18N
                // file.getAbsolutePath est l'équivalent de TOpenDialog.filename
                String WU = file.getAbsolutePath(); 
                //myFDocuTopo.loadFileXML("Toporabot.gtx");
                myFDocuTopo.loadFileXML(WU);
                //myFDocuTopo.ListerLeDocument();
                // calculer le réseau
                calculerLeReseau();
                // peupler l'interface
                preparerFrontal();
                
                // document ouvert avec succès
                mFDocuTopoLoaded = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }  
    }//GEN-LAST:event_mnuOuvrirXMLActionPerformed

    private void mnuSaveXMLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSaveXMLActionPerformed
        try
        {
            final JFileChooser fc = new JFileChooser(getGHTopoDirectory());
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) 
            {
                File file = fc.getSelectedFile();
                String WU = file.getAbsolutePath();
                if (file.exists())
                {    
                    if (questionOuiNon(this, getResourceByKey("rsMSG_WARN_FILE_EXISTS"), WU + getResourceByKey("rsMSG_DO_ERASE_FILE_EXISTS")))
                        myFDocuTopo.saveToXML(WU);
                }
                else
                    myFDocuTopo.saveToXML(WU);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }    
    }//GEN-LAST:event_mnuSaveXMLActionPerformed

    private void mnuQuitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuQuitterActionPerformed
 // TODO add your handling code here:
        if (questionOuiNon(this, getResourceByKey("rsAPPLICATION_NAME"), getResourceByKey("rsMSg_QUESTION_QUIT"))) System.exit(0);
    }//GEN-LAST:event_mnuQuitterActionPerformed

    private void mnuCalculetteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuCalculetteActionPerformed
        // TODO add your handling code here:
        afficherCalculette();
    }//GEN-LAST:event_mnuCalculetteActionPerformed

    private void mnuExportGISActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuExportGISActionPerformed
        // TODO add your handling code here:
        afficherExportGIS(myFTableEntites);
    }//GEN-LAST:event_mnuExportGISActionPerformed

    private void mnuVue3DActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuVue3DActionPerformed
        // TODO add your handling code here:
        afficherVue3D(myFTableEntites);
    }//GEN-LAST:event_mnuVue3DActionPerformed

    private void mnuPaletteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuPaletteActionPerformed
        // TODO add your handling code here:
        int toto = selectCouleurTopo(0);
    }//GEN-LAST:event_mnuPaletteActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        afficherVue2D(myFTableEntites);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tabpanBDDStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabpanBDDStateChanged
        // TODO add your handling code here:
        if (mFDocuTopoLoaded)
        {
            myModeSelectionDsListes = tabpanBDD.getSelectedIndex() - 1;
            afficherMessageFmt("Onglet %d sélectionné", myModeSelectionDsListes); //NOI18N
            lister(myModeSelectionDsListes, -1);
        }    
    }//GEN-LAST:event_tabpanBDDStateChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if (questionOuiNon(this, getResourceByKey("rsMSG_ADD_ELEMENT"), 
                                 getResourceByKey("rsMSG_AJOUTER")))
        {
            int Q = 0;
            switch (myModeSelectionDsListes)
            {
                case mslENTRANCES: 
                     myFDocuTopo.addEntranceWithLastValues();
                    Q = myFDocuTopo.getNbEntrances() - 1;
                    listerLesEntrees(Q);
                    mettreEntreeDansSonCadre(currIdxEntrance);
                    break;
                case mslRESEAUX: 
                     myFDocuTopo.addReseauWithLastValues();
                    Q = myFDocuTopo.getNbReseaux()- 1;
                    listerLesReseaux(Q);
                    mettreReseauDansSonCadre(currIdxReseau);
                    break;
                case mslSECTEURS: 
                    myFDocuTopo.addSecteurWithLastValues();
                    Q = myFDocuTopo.getNbSecteurs() - 1;
                    listerLesSecteurs(Q);
                    mettreSecteurDansSonCadre(currIdxSecteur);
                    break;
                case mslCODES: 
                    myFDocuTopo.addCodeWithLastValues();
                    Q = myFDocuTopo.getNbCodes()- 1;
                    listerLesCodes(Q);
                    mettreCodeDansSonCadre(Q);
                    break;    
                case mslEXPES: 
                    myFDocuTopo.addExpeWithLastValues();
                    Q = myFDocuTopo.getNbExpes() - 1;
                    listerLesExpes(Q);
                    mettreExpeDansSonCadre(Q);
                    break;
                case mslSERIES:
                    myFDocuTopo.addEmptySerie();
                    Q = myFDocuTopo.getNbSeries() - 1;
                    listerLesSeries(Q);
                    mettreSerieDansSonCadre(Q);
                    break;
                default:
                    break;
            }
        }            
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        int qIdx = lsbListe.getSelectedIndex();
        if (qIdx >=0)
        {
            switch (myModeSelectionDsListes)
            {    
                case mslENTRANCES:
                    TEntrance Ent = tCadreEntree1.getEntranceFromForm();
                    afficherMessageFmt("%d - %s - %d.%d - %.2f, %.2f, %.2f", Ent.eNumEntree, Ent.eNomEntree, Ent.eRefSer, Ent.eRefSt, Ent.eXEntree, Ent.eYEntree, Ent.eZEntree); //NOI18N
                    myFDocuTopo.putEntrance(qIdx, Ent);
                    listerLesEntrees(qIdx);
                    break;
                case mslRESEAUX:
                    TReseau R = tCadreReseau1.getReseauFromForm();
                    myFDocuTopo.putReseau(qIdx, R);
                    listerLesReseaux(qIdx);
                    break;
                case mslSECTEURS:
                    TSecteur Sec = tCadreSecteur1.getSecteurFromForm();
                    myFDocuTopo.putSecteur(qIdx, Sec);
                    listerLesSecteurs(qIdx);
                    break;
                case mslCODES:
                    TCode C = tCadreCode1.getCodeFromForm();
                    myFDocuTopo.putCode(qIdx, C);
                    listerLesCodes(qIdx);
                    break;
                case mslEXPES:
                    TExpe E = tCadreExpe1.getExpeFromForm();
                    myFDocuTopo.putExpe(qIdx, E);
                    listerLesExpes(qIdx);
                    break;                    
                case mslSERIES:                    
                    TSerie Ser = tCadreSerie1.getSerieFromForm();
                    myFDocuTopo.putSerie(qIdx, Ser);
                    listerLesSeries(qIdx);
                default:
                    break;
            }
        }    
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        afficherAboutBox();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void mnuVue2DActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuVue2DActionPerformed
        // TODO add your handling code here:
        afficherVue2D(myFTableEntites);
    }//GEN-LAST:event_mnuVue2DActionPerformed

    private void mnuVueOSMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuVueOSMActionPerformed
        // TODO add your handling code here:
        afficherOSMContext(myFTableEntites);
    }//GEN-LAST:event_mnuVueOSMActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        // tests de Found separators
        String s = editFiltre.getText();
        afficherMessageFmt("Filtres: '%s' - Elements visibles: %d / %s", s, myFTableEntites.applyMetaFiltre(s), myFTableEntites.getNbEntites());        //NOI18N

    }//GEN-LAST:event_jButton5ActionPerformed

    private void btnTrierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTrierActionPerformed
        // TODO add your handling code here:
        switch (myModeSelectionDsListes)
        {
            case mslCODES:
                myFDocuTopo.trierLesCodes();
                listerLesCodes(1);
                mettreCodeDansSonCadre(currIdxCode);
                break;    
            case mslEXPES: 
                myFDocuTopo.trierLesExpes();
                listerLesExpes(1);
                mettreExpeDansSonCadre(currIdxExpe);
                break;
            case mslSERIES:
                myFDocuTopo.trierLesSeries();
                listerLesSeries(1);
                mettreSerieDansSonCadre(currIdxExpe);
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btnTrierActionPerformed

    private void mnuCalibrerDistoXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuCalibrerDistoXActionPerformed
        // TODO add your handling code here:
        calibrerDistoX();
    }//GEN-LAST:event_mnuCalibrerDistoXActionPerformed

    private void lsbListeValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lsbListeValueChanged
        // TODO add your handling code here:
        if (fFullyListed) // liste complète ?
        {
            lister(myModeSelectionDsListes, lsbListe.getSelectedIndex());
        }
    }//GEN-LAST:event_lsbListeValueChanged

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        int EWE = selectionnerDansListe(myFDocuTopo, mslSERIES, 0);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void mnuStatsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuStatsActionPerformed
        // TODO add your handling code here:
        if (this.mFDocuTopoLoaded)
        {
            afficherStatistiques(this.myFTableEntites);
        }    
    }//GEN-LAST:event_mnuStatsActionPerformed

    private void mnuExportGHCaveDrawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuExportGHCaveDrawActionPerformed
        // TODO add your handling code here:
        if (! mFDocuTopoLoaded) return;            
        try
        {
            final JFileChooser fc = new JFileChooser(getGHTopoDirectory());
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) 
            {
                File file = fc.getSelectedFile();
                String WU = file.getAbsolutePath();
                if (file.exists())
                {    
                    if (questionOuiNon(this, getResourceByKey("rsMSG_WARN_FILE_EXISTS"), WU + getResourceByKey("rsMSG_DO_ERASE_FILE_EXISTS")))
                        myFTableEntites.ExportGCP(WU, "");
                }
                else
                    myFTableEntites.ExportGCP(WU, "");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }    
    }//GEN-LAST:event_mnuExportGHCaveDrawActionPerformed

    private void mnuVueOpenGLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuVueOpenGLActionPerformed
        // TODO add your handling code here:
        afficherVue3DOpenGL(myFTableEntites);
    }//GEN-LAST:event_mnuVueOpenGLActionPerformed

    private void btnCalcTtesDecliMagsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcTtesDecliMagsActionPerformed
        // TODO add your handling code here:
        myFDocuTopo.calculerLesDeclinaisonsMagnetiques();
    }//GEN-LAST:event_btnCalcTtesDecliMagsActionPerformed

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
                if ("Nimbus".equals(info.getName())) { //NOI18N
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TMainWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JButton btnCalcTtesDecliMags;
    javax.swing.JButton btnTrier;
    javax.swing.JTextField editFiltre;
    javax.swing.JButton jButton1;
    javax.swing.JButton jButton2;
    javax.swing.JButton jButton3;
    javax.swing.JButton jButton4;
    javax.swing.JButton jButton5;
    javax.swing.JButton jButton6;
    javax.swing.JLayeredPane jLayeredPane1;
    javax.swing.JMenuBar jMenuBar1;
    javax.swing.JMenuItem jMenuItem1;
    javax.swing.JPanel jPanel1;
    javax.swing.JPanel jPanel2;
    javax.swing.JPanel jPanel3;
    javax.swing.JScrollPane jScrollPane1;
    javax.swing.JPopupMenu.Separator jSeparator1;
    javax.swing.JPopupMenu.Separator jSeparator3;
    javax.swing.JPopupMenu.Separator jSeparator4;
    javax.swing.JSplitPane jSplitPane1;
    javax.swing.JLabel lbNbreElements;
    javax.swing.JList lsbListe;
    javax.swing.JMenuItem mnuCalculette;
    javax.swing.JMenuItem mnuCalibrerDistoX;
    javax.swing.JMenuItem mnuExportGHCaveDraw;
    javax.swing.JMenuItem mnuExportGIS;
    javax.swing.JMenu mnuFILE;
    javax.swing.JMenu mnuHelp;
    javax.swing.JMenuItem mnuOuvrirXML;
    javax.swing.JMenuItem mnuOuvrirXTB;
    javax.swing.JMenuItem mnuPalette;
    javax.swing.JMenuItem mnuQuitter;
    javax.swing.JMenuItem mnuSaveXML;
    javax.swing.JMenuItem mnuStats;
    javax.swing.JMenu mnuTools;
    javax.swing.JMenu mnuVisualisation;
    javax.swing.JMenuItem mnuVue2D;
    javax.swing.JMenuItem mnuVue3D;
    javax.swing.JMenuItem mnuVueOSM;
    javax.swing.JMenuItem mnuVueOpenGL;
    com.ghtopo.javaghtopo.TCadreCode tCadreCode1;
    com.ghtopo.javaghtopo.TCadreEntree tCadreEntree1;
    com.ghtopo.javaghtopo.TCadreExpe tCadreExpe1;
    com.ghtopo.javaghtopo.TCadreGeneral tCadreGeneral1;
    com.ghtopo.javaghtopo.TCadreReseau tCadreReseau1;
    com.ghtopo.javaghtopo.TCadreSecteur tCadreSecteur1;
    com.ghtopo.javaghtopo.TCadreSerie tCadreSerie1;
    com.ghtopo.javaghtopo.TCadreViseeAntenne tCadreViseeAntenne1;
    javax.swing.JTabbedPane tabpanBDD;
    // End of variables declaration//GEN-END:variables
}
