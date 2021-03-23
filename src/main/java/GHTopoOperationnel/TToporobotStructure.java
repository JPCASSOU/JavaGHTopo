package GHTopoOperationnel;
import static GHTopoOperationnel.GeneralFunctions.*;
import static GHTopoOperationnel.TCallDialogs.getGHTopoDirectory;
import static GHTopoOperationnel.TXMLKeysConstants.*;
import GHTopoOperationnel.Types.TCode;
import GHTopoOperationnel.Types.TEntrance;
import GHTopoOperationnel.Types.TExpe;
import GHTopoOperationnel.Types.TPoint2Df;
import GHTopoOperationnel.Types.TReseau;
import GHTopoOperationnel.Types.TSecteur;
import GHTopoOperationnel.Types.TStation;
import GHTopoOperationnel.Types.TViseeEnAntenne;
import java.awt.Color;


import java.io.*;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import org.jdom2.*;
import org.jdom2.output.*;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
/**
Localisation des fichiers:
Dans ./nom_du_projet/src/
 + Main.java
 + TToporobotStructure.java
 + TTableEntites.java
 + etc ...

Structure du projet:
[Nom_du_projet]
-->[.idea]
-->[src]
     +[com.company]
     + Main.java
     + TToporobotStructure
     + TTableEntites ...

 */
public class TToporobotStructure
{
    // Bundle I18N 
    private ResourceBundle fMyResourceBundle = null;
    // nom de l'étude
    private String mFNomEtude;
    private String mFCommentairesEtude;
    // systemes de coordonnees
    private int mFCodeEPSGParDefaut         = 2154; 
    private String mFCodeGHTopoParDefaut    = "LT93"; 
    private String mFSystCoordsCommentaires = "Lambert 93 France";
    
    // coordonnées par défaut
    private double mFDefaultCoordX;
    private double mFDefaultCoordY;
    private double mFDefaultCoordZ;
    // référence
    private int mFRefSer;
    private int mFRefPt;
    
    // pour XML
    private Document myDocument = null;
    // listes simples
    
    private List<TEntrance> mFListeEntrees;  // les entrées
    private List<TReseau>   mFListeReseaux;  // les réseaux
    private List<TSecteur>  mFListeSecteurs; // les secteurs
    private List<TCode>     mFListeCodes;  // les codes
    private List<TExpe>     mFListeExpes;  // les expés
    private List<TViseeEnAntenne> mFListeViseesAntennes;
    private List<TSerie>    mFListeSeries; // les séries
    // le bundle
    private String getResourceByKey(String k)
    {
        return fMyResourceBundle.getString(k);
    }        
    // codes EPSG
	private int getDefaultCodeEPSG()
	{
		return mFCodeEPSGParDefaut;
    }
	private void setDefaultCodeEPSG(int v)
	{
	    mFCodeEPSGParDefaut = v;
	}
	//*******************************************************************************************
    // settings
    public void setNomEtude(String S)
    {
        this.mFNomEtude = S;
    }
    public String getNomEtude()
    {
        return this.mFNomEtude;
    }
    public void setCommentairesEtude(String S)
    {
        this.mFCommentairesEtude = S;
    }
    public String getCommentairesEtude()
    {
        return this.mFCommentairesEtude;
    }
    public void setSystemeCoordonnees(int CodeEPSG, String GHTopoCode, String Commentaires)
    {
        this.mFCodeEPSGParDefaut      = (CodeEPSG == 0) ? 2154 : Math.abs(CodeEPSG);
        this.mFCodeGHTopoParDefaut    = (GHTopoCode.equals("")) ? "LT93" : GHTopoCode;
        this.mFSystCoordsCommentaires = Commentaires;
    }
    public int getCoordSystCodeEPSG()
    {
        return this.mFCodeEPSGParDefaut;
    }
    public String getCoordSystCodeGHTopo()
    {
        return this.mFCodeGHTopoParDefaut;
    }
    public String getCoordSystCommentaire()
    {
        return this.mFSystCoordsCommentaires;
    }
    
    public void setDefaultCoords(double QX, double QY, double QZ)
    {
        this.mFDefaultCoordX = QX;
        this.mFDefaultCoordY = QY;
        this.mFDefaultCoordZ = QZ;
    }
    public void setDefaultRefSerSt(int QSer, int QSt)
    {
        this.mFRefSer = QSer;
        this.mFRefPt = QSt;
    }
    public double getPoint0X()
    {
        return this.mFDefaultCoordX;
    }
    public double getPoint0Y()
    {
        return this.mFDefaultCoordY;
    }
    public double getPoint0Z()
    {
        return this.mFDefaultCoordZ;
    }
	//*******************************************************************************************
    // Les entrées
    public int getNbEntrances()
    {
        return this.mFListeEntrees.size();
    }
    public void addEntrance(TEntrance MyEntrance)
    {
        this.mFListeEntrees.add(MyEntrance);
    }
    public void putEntrance(int Idx, TEntrance MyEntree)
    {
        this.mFListeEntrees.set(Idx, MyEntree);
    }
    public TEntrance getEntrance(int Idx)
    {
        return this.mFListeEntrees.get(Idx);
    }
    public void addEntranceByValues(int qIdxEntrance, String qNomEntrance,
                                    int qRefSer, int qRefPt,
                                    double qX, double qY, double qZ,
                                    String qObsEntrance)
    {
        TEntrance S = new TEntrance();
        S.eNumEntree     = qIdxEntrance;
        S.eNomEntree     = qNomEntrance;
        S.eRefSer        = qRefSer;
        S.eRefSt         = qRefPt;
        S.eXEntree       = qX;
        S.eYEntree       = qY;
        S.eZEntree       = qZ;
        S.eObserv        = qObsEntrance;
        this.addEntrance(S);
    }
    public int getNextValidIdxEntrance()
    {
        int result = -1;
        int nb = this.getNbEntrances();
        for (int i = 1; i < nb; i++)
        {
            TEntrance EWE = this.getEntrance(i);
            if (EWE.eNumEntree > result) result = EWE.eNumEntree;
        }    
        result++;
        return result;
    }        
    public void addEntranceWithLastValues()
    {
        int Nb = this.getNbEntrances();
        // récupérer la dernière expé
        TEntrance EWE = getEntrance(Nb - 1);
       
        // et on ajoute
        this.addEntranceByValues(getNextValidIdxEntrance(),
                                 "Nouvelle entrée",
                                 EWE.eRefSer, EWE.eRefSt,
                                 EWE.eXEntree, EWE.eYEntree, EWE.eZEntree,
                                 "");
    }        
    //*******************************************************************************************
    // Les réseaux
    public int getNbReseaux()
    {
        return this.mFListeReseaux.size();
    }
    public void addReseau(TReseau MyReseau)
    {
        this.mFListeReseaux.add(MyReseau);
    }
    public void putReseau(int Idx, TReseau MyReseau)
    {
        this.mFListeReseaux.set(Idx, MyReseau);
    }
    public TReseau getReseau(int Idx)
    {
        return this.mFListeReseaux.get(Idx);
    }
    public void addReseauByValues(int qIdxReseau, int R, int G, int B, int qTypeReseau, String qNomReseau)
    {
        TReseau S = new TReseau();
        S.IdxReseau       = qIdxReseau;
        S.CouleurReseauR  = R;
        S.CouleurReseauG  = G;
        S.CouleurReseauB  = B;
        S.NomReseau     = qNomReseau;
        S.TypeReseau    = qTypeReseau;
        this.addReseau(S);
    }
    public int getNextValidIdxReseau()
    {
        int result = -1;
        int nb = this.getNbReseaux();
        for (int i = 1; i < nb; i++)
        {
            TReseau EWE = this.getReseau(i);
            if (EWE.IdxReseau > result) result = EWE.IdxReseau;
        }    
        result++;
        return result;
    }        
    public void addReseauWithLastValues()
    {
        int Nb = this.getNbReseaux();
        // récupérer la dernière expé
        TReseau EWE = getReseau(Nb - 1);
       
        // et on ajoute
        this.addReseauByValues(getNextValidIdxReseau(),
                               EWE.CouleurReseauR,
                               EWE.CouleurReseauG,
                               EWE.CouleurReseauB,
                               EWE.TypeReseau, 
                               "Nouveau réseau");
    }        
    //*******************************************************************************************

    // Les secteurs
    public int getNbSecteurs()
    {
        return this.mFListeSecteurs.size();
    }
    public void addSecteur(TSecteur MySecteur)
    {
        this.mFListeSecteurs.add(MySecteur);
    }
    public void putSecteur(int Idx, TSecteur MySecteur)
    {
        this.mFListeSecteurs.set(Idx, MySecteur);
    }
    public TSecteur getSecteur(int Idx)
    {
        return this.mFListeSecteurs.get(Idx);
    }
    public void addSecteurByValues(int qIdxecteur, int R, int G, int B, String qNomSecteur)
    {
        TSecteur S = new TSecteur();
        S.IdxSecteur     = qIdxecteur;
        S.CouleurSecteurR = R;
        S.CouleurSecteurG = G;
        S.CouleurSecteurB = B;
        S.NomSecteur     = qNomSecteur;
        this.addSecteur(S);
    }
    public int getNextValidIdxSecteur()
    {
        int result = -1;
        int nb = this.getNbSecteurs();
        for (int i = 1; i < nb; i++)
        {
            TSecteur EWE = this.getSecteur(i);
            if (EWE.IdxSecteur > result) result = EWE.IdxSecteur;
        }    
        result++;
        return result;
    }        
    public void addSecteurWithLastValues()
    {
        int Nb = this.getNbSecteurs();
        // récupérer la dernière expé
        TSecteur EWE = getSecteur(Nb - 1);
       
        // et on ajoute
        this.addSecteurByValues(getNextValidIdxSecteur(),
                                EWE.CouleurSecteurR,
                                EWE.CouleurSecteurG,
                                EWE.CouleurSecteurB,
                                "Nouveau secteur");
    }        
    //*******************************************************************************************
    // Les Codes
    public int getNbCodes()
    {
        return this.mFListeCodes.size();
    }
    public void addCode(TCode MyCode)
    {
        this.mFListeCodes.add(MyCode);
    }
    // ajout code avec la dernière valeur
    public void addCodeWithLastValues()
    {
        int Nb = this.getNbCodes();
        // récupérer la dernière expé
        TCode EWE = getCode(Nb - 1);
        // et on ajoute
        this.addCodeByValeurs(this.getNextValidIdxCode(),
                              EWE.UniteBoussole, EWE.UniteClino,
                              EWE.PsiL, EWE.PsiAz, EWE.PsiP,
                              EWE.FactLong, EWE.AngLimite,
                              EWE.Commentaire);        
    }        
    public void addCodeByValeurs(int qIdxCode, double qUniteBoussole, double qUniteClino,
                                  double qPsiL, double qPsiAz, double qPsiP,
                                  double qFactLong, double qAngLimite,
                                  String qCommentaire)
    {
        TCode EWE = new TCode();
        EWE.IdxCode        = qIdxCode;
        EWE.UniteBoussole  = qUniteBoussole;
        EWE.UniteClino     = qUniteClino;
        EWE.PsiL           = qPsiL;
        EWE.PsiAz          = qPsiAz;
        EWE.PsiP           = qPsiP;
        EWE.FactLong       = qFactLong;
        EWE.AngLimite      = qAngLimite;
        EWE.Commentaire    = qCommentaire;
        this.addCode(EWE);
    }
    public void putCode(int Idx, TCode MyCode)
    {
        this.mFListeCodes.set(Idx, MyCode);
    }
    public TCode getCode(int Idx)
    {
        return this.mFListeCodes.get(Idx);
    }
    public TCode getCodeByIndex(int Idx)
    {
        TCode result = getCode(0);
        for (int i = 0; i < this.getNbCodes(); i++)
        {
            result = this.getCode(i);
            if (result.IdxCode == Idx) break;
        }
        return result;
    }
    // vérifier si un index est valide
    public boolean isValidIdxCode(int Idx)
    {
        int nb = getNbCodes();
        for (int i = 0; i < nb; i++)
        {
            TCode EWE = getCode(Idx);
            if (Idx == EWE.IdxCode) return false;
        }
        return true;
    }      
    // attrapper le prochain numéro de code valide
    public int getNextValidIdxCode()
    {
        int result = -1;
        int nb = this.getNbCodes();
        for (int i = 1; i < nb; i++)
        {
            TCode EWE = this.getCode(i);
            if (EWE.IdxCode > result) result = EWE.IdxCode;
        }    
        result++;
        return result;
    }
    //*******************************************************************************************
    // Les Expes
    public int getNbExpes()
    {
        return this.mFListeExpes.size();
    }
    public void addExpe(TExpe MyExpe)
    {
        TExpe EWE = new TExpe();
        EWE = MyExpe;
        this.mFListeExpes.add(EWE);
    }
    // ajout expé avec la dernière valeur
    public void addExpeWithLastValues()
    {
        int Nb = this.getNbExpes();
        // récupérer la dernière expé
        TExpe EWE = this.getExpe(Nb - 1);
        // paramétrage
        int qd = ensureMakeGHTopoDateFromInt(EWE.DateLeve);
        this.addExpeByValeurs(this.getNextValidIdxExpe(),
                              getDay(qd), getMonth(qd), getYear(qd),
                              EWE.Speleometre, EWE.Speleographe,
                              EWE.ModeDecl, EWE.Declinaison, EWE.Inclinaison,
                              EWE.IdxColor, 
                              EWE.Commentaire);        
    }        
    public void addExpeByValeurs(int qIdxExpe, int qJourExpe,  int qMoisExpe, int qAnneeExpe,
                                 String qSpeleometre, String qSpeleographe,
                                 int qModeDecl, double qDeclinaison, double qInclinaison,
                                 int qIdxColor, String qCommentaire)
    {
        TExpe EWE = new TExpe();
        EWE.IdxExpe       = qIdxExpe;
        EWE.DateLeve      = ensureMakeGHTopoDateFromYMD(qAnneeExpe, qMoisExpe, qJourExpe);
        EWE.Speleometre   = qSpeleometre;
        EWE.Speleographe  = qSpeleographe;
        EWE.ModeDecl      = qModeDecl;
        EWE.Declinaison   = qDeclinaison;
        EWE.Inclinaison   = qInclinaison;
        EWE.IdxColor      = qIdxColor;
        EWE.Commentaire   = qCommentaire;
        this.addExpe(EWE);
    }
    public void putExpe(int Idx, TExpe MyExpe)
    {
        this.mFListeExpes.set(Idx, MyExpe);
    }
    public TExpe getExpe(int Idx)
    {
        return this.mFListeExpes.get(Idx);
    }
    
    public TExpe getExpeByIndex(int Idx)
    {
        TExpe result = getExpe(0);
        for (int i = 0; i < this.getNbExpes(); i++)
        {
            result = this.getExpe(i);
            if (result.IdxExpe == Idx) break;
        }
        return result;
    }
    // vérifier si un index est valide
    public boolean isValidIdxExpe(int Idx)
    {
        int nb = getNbExpes();
        for (int i = 0; i < nb; i++)
        {
            TExpe EWE = getExpe(Idx);
            if (Idx == EWE.IdxExpe) return false;
        }
        return true;
    }
    // attrapper le prochain numéro de expé valide
    public int getNextValidIdxExpe()
    {
        int result = -1;
        int nb = this.getNbExpes();
        for (int i = 1; i < nb; i++)
        {
            TExpe EWE = this.getExpe(i);
            if (EWE.IdxExpe > result) result = EWE.IdxExpe;
        }    
        result++;
        return result;
    }
    //*******************************************************************************************
    // Les séries
    public int getNbSeries()
    {
        return this.mFListeSeries.size();
    }
    private void clearSeries()
    {
        afficherMessage("-- Vider liste des séries\n");
        this.mFListeSeries.clear();
    }
    public void addSerie(TSerie S){
        this.mFListeSeries.add(S);
    }
    // attrapper le prochain numéro de série valide
    public int getNextValidIdxSerie()
    {
        int result = -1;
        int nb = this.getNbSeries();
        for (int i = 1; i < nb; i++)
        {
            TSerie s = this.getSerie(i);
            if (s.IdxSerie > result) result = s.IdxSerie;
        }    
        result++;
        return result;
    }
    // vérifier si un index est valide
    public boolean isValidIdxSerie(int Idx)
    {
        int nb = getNbSeries();
        for (int i = 0; i < nb; i++)
        {
            TSerie EWE = getSerie(Idx);
            if (Idx == EWE.IdxSerie) return false;
        }
        return true;
    }        
    public void addEmptySerie()
    {
        TSerie s = new TSerie();
        // on vide la table des stations
        s.clearVisees();
        s.IdxSerie = getNextValidIdxSerie();
        s.NomSerie = "Nouvelle série";
        s.Chance   = 0;
        s.ObsSerie = "";
        s.Obstacle = 0;
        s.SerDep   = 1;
        s.PtDep    = 0;
        s.Raideur  = 1.00;
        // attrapper les dernieres expés, réseaux, secteurs, etc ...
        TReseau RR   = getReseau(getNbReseaux() - 1);
        s.Reseau     = RR.IdxReseau;
        TSecteur SS  = getSecteur(getNbSecteurs() - 1);
        TCode CC     = getCode(getNbCodes() - 1);
        TExpe EE     = getExpe(getNbExpes() - 1);
        // créer la station 0
        s.addStationByValues(0, "", SS.IdxSecteur, 0, 
                             CC.IdxCode, EE.IdxExpe,
                             0.001, 0.00, 0.00,
                             0.00, 0.00, 0.00, 0.00,
                             "");
        // et ajouter
        this.addSerie(s);
    }        
    public TSerie getSerie(int Idx)
    {
        return this.mFListeSeries.get(Idx);
    }
    public TSerie getSerieByIndex(int Idx)
    {
        TSerie result = getSerie(0);
        for (int i = 0; i < this.getNbSeries(); i++)
        {
            result = this.getSerie(i);
            if (result.IdxSerie == Idx) break;
        }
        return result;
    }
    public void putSerie(int Idx, TSerie S)
    {
        this.mFListeSeries.set(Idx, S);
    }        
    public void removeSerie(int idx)
    {
        this.mFListeSeries.remove(idx);
    }
    //*******************************************************************************************
    // les Antennes
    public int getNbViseesAntenne()
    {
        return this.mFListeViseesAntennes.size();
    }
    public void AddViseeEnAntenne(TViseeEnAntenne MyAntenne)
    {
        this.mFListeViseesAntennes.add(MyAntenne);
    }
    public void putViseeEnAntenne(int Idx, TViseeEnAntenne MyAntenne)
    {
        this.mFListeViseesAntennes.set(Idx, MyAntenne);
    }
    public TViseeEnAntenne getViseeEnAntenne(int Idx)
    {
        return this.mFListeViseesAntennes.get(Idx);
    }
        
    //------------------------------------------------------------------------------------------------------------------
    //Constructeur par défaut
    public TToporobotStructure(String qFileName)
    {
        this.fMyResourceBundle = java.util.ResourceBundle.getBundle("com/ghtopo/javaghtopo/Bundle");
        afficherMessage(sprintf("Instanciation de TToporobotStructure %s ", qFileName));
        // listes internes
        this.mFListeEntrees             = new ArrayList<TEntrance>();
        this.mFListeReseaux             = new ArrayList<TReseau>();
        this.mFListeSecteurs   		= new ArrayList<TSecteur>();
        this.mFListeCodes      		= new ArrayList<TCode>();
        this.mFListeExpes      		= new ArrayList<TExpe>();
        this.mFListeViseesAntennes 	= new ArrayList<TViseeEnAntenne>();
        this.mFListeSeries     		= new ArrayList<TSerie>();	
	this.mFCodeEPSGParDefaut        = 2154; // EPSG par défaut = Lambert93 France métropolitaine
    }
    public void viderListes(boolean doCreateFirstElements)
    {
        this.clearSeries();
        this.mFListeEntrees.clear();
        this.mFListeReseaux.clear();
        this.mFListeSecteurs.clear();
        this.mFListeCodes.clear();
        this.mFListeExpes.clear();
        this.mFListeViseesAntennes.clear();
		
        if (doCreateFirstElements)
        {    
            // init Code
            TCode code0 = new TCode();
            code0.IdxCode       = 0;
            code0.UniteBoussole = 360.00;
            code0.UniteClino    = 360.00;
            code0.FactLong      = 1.000;
            code0.PsiP          = 0.01;
            code0.PsiAz         = 0.01;
            code0.PsiP          = 0.01;
            code0.AngLimite     = 0.00;
            code0.Commentaire   = "";
            this.addCode(code0);
            // init Expé
            TExpe expe0 = new TExpe();
            expe0.IdxExpe       = 0;
            expe0.IdxColor      = 0;
            expe0.DateLeve      = ensureMakeGHTopoDateFromYMD(2014, 1, 1);
            expe0.Declinaison   = 0.00;
            expe0.Inclinaison   = 0.00;
            expe0.ModeDecl      = 0;
            expe0.Speleographe  = "";
            expe0.Speleometre   = "";
            expe0.Commentaire   = "";
            this.addExpe(expe0);
            // init Réseau
            TReseau Reseau0 = new TReseau();
            Reseau0.IdxReseau     = 0;
            Reseau0.NomReseau     = "Reseau0";
            Reseau0.CouleurReseauB = 255; // Bleu
            Reseau0.CouleurReseauG = 0; // Bleu
            Reseau0.CouleurReseauB = 0; // Bleu
            Reseau0.TypeReseau    = 0;
            Reseau0.ObsReseau     = "";
            this.addReseau(Reseau0);
            // init Secteur
            TSecteur Secteur0 = new TSecteur();
            Secteur0.IdxSecteur   = 0;
            Secteur0.NomSecteur   = "";
            Secteur0.CouleurSecteurR = 255; // Bleu
            Secteur0.CouleurSecteurG = 0; // Bleu
            Secteur0.CouleurSecteurB = 0; // Bleu
            this.addSecteur(Secteur0);
        }    

        // initialiser séries, codes, expés
        TSerie Serie0 = new TSerie();
        Serie0.setIndexSerie(0);
        Serie0.setNoReseau(0);
        Serie0.setNoReseau(0);
        Serie0.setRaideur(1.00);
        Serie0.setStationsExtremites(0, 0, 1, 0);
        Serie0.setChanceObstacle(0, 0);
        Serie0.setNoReseau(0);
        Serie0.addStationByValues(0, "", 0, 0, 0, 0, 0.001, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, "");
        this.addSerie(Serie0);
        
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Chargement d'un fichier XTB
    public int loadFileXTB(String FichierXTB)
    {
        afficherMessage("LoadFileXTB: " + FichierXTB);
        // liste provisoire pour les entrées
        ArrayList<String> ProvListeEntrees = new ArrayList<String>();
        ProvListeEntrees.clear();
        String protectionLigne;
        int result = -1;
        this.viderListes(true);
        String maLigne = "";
        String[] prmsLn;
        int NbLignes = 0;
        int QR;
        int QG;
        int QB;
        // général
        this.mFNomEtude = "Untitled";
        this.mFCommentairesEtude = "";
        this.mFDefaultCoordX = 0.00;
        this.mFDefaultCoordY = 0.00;
        this.mFDefaultCoordZ = 0.00;
        this.mFCodeEPSGParDefaut = 2154; // 
        
        // réseaux, secteurs, codes, expés
        TEntrance myEntrance;
        TReseau  myReseau; //    = new TReseau();
        TSecteur mySecteur; //   = new TSecteur();
        TCode    myCode;
        TExpe    myExpe;
        TSerie   mySerie;
        TStation myStation;
        TViseeEnAntenne MyViseeAntenne;
        // protection de la ligne par des colonnes bidon ajoutées:
        // la fonction Split retourne un tableau dynamique.
        protectionLigne = "";
        
        for (int i = 0; i < 60; i++) protectionLigne += "\t ";
		int prefix1 = 0;
        int prefix2 = 0;
        InputStream myFlux;
        InputStreamReader myLecture;
        BufferedReader myBuffer = null; 
        try
        {   
            myFlux    = new FileInputStream(FichierXTB);
            myLecture = new InputStreamReader(myFlux, Charset.forName("windows-1252"));
            myBuffer  = new BufferedReader(myLecture);
            mySerie   = new TSerie(); // c'est la première série, on crée
            try
            {    
                while ((maLigne = myBuffer.readLine().trim())!= null)
                {
                    try
                    {// ici, on protège le reste en ajoutant des colonnes bidon
                        maLigne += protectionLigne;
                        //AfficherMessage(maLigne);

                        if (maLigne.equalsIgnoreCase(""))
                        {
                            prefix1 = -100; // lignes vides
                            continue;
                        }
                        else if (maLigne.charAt(0) == '#')
                        {
                            afficherMessage(sprintf("Ligne %d est un commentaire: %s", NbLignes, maLigne));
                            prefix1 = -1000; // commentaires sur une ligne
                            NbLignes++;
                            continue;
                        }
                        else if (maLigne.charAt(0) == '{')
                        {
                            afficherMessage(sprintf("Ligne %d est un commentaire: %s", NbLignes, maLigne));
                            do {
                                maLigne = myBuffer.readLine();
                                afficherMessage(sprintf("Ligne %d est un commentaire: %s ", NbLignes, maLigne));
                                NbLignes++;
                            } while (maLigne.charAt(0) != '}');

                            continue;
                        }
                        prmsLn = maLigne.split("\t");
                        prefix1 = strToIntDef(prmsLn[0].trim(), -110);
                        //*************************************************
                        switch (prefix1)
                        {
                            case -9999:  // Arrêt forcé du traitement (utiliser avec précaution)
                                break;
                            case -1000:  // Commentaire
                                break;
                            case -900:   // balise de pause
                                break;
                            case -110:   // Ligne invalide; ignorée
                                break;
                            case -100:   // ignorer les lignes vides
                                break;
                            case  -20:   // horodatage
                                break;
                            case  -19:   // Nom de l'étude
                                this.setNomEtude(prmsLn[1].trim()); // Nouvelle section: Nom de l'étude
                                this.setCommentairesEtude(prmsLn[2].trim());
                                break;

                            case  -15:   // Nouvelle section: Système de coordonnées :
                                //-15	-32648	UTM48	UTM fuseau 48
                                
                                // #1	Code EPSG	Code GHTopo	Libellé du système de coordonnées
                                // -15	31563	LT3	Lambert 3 France
                                this.setSystemeCoordonnees(strToIntDef(prmsLn[1], 2154), 
                                                           prmsLn[2].trim(),
                                                           prmsLn[3].trim());
                               
                                break;
                            case  -10: // Nouvelle section: secteurs
                                mySecteur = new TSecteur();
                                mySecteur.IdxSecteur =  strToIntDef(prmsLn[1], 0);
                                mySecteur.CouleurSecteurR = strToIntDef(prmsLn[2], 0);
                                mySecteur.CouleurSecteurG = strToIntDef(prmsLn[3], 0);
                                mySecteur.CouleurSecteurB = strToIntDef(prmsLn[4], 0);
                                mySecteur.NomSecteur     = prmsLn[5].trim();
                                //afficherMessage(mySecteur.NomSecteur);

                                this.addSecteur(mySecteur);
                                break;
                            case  -9: // antennes
                                MyViseeAntenne = new TViseeEnAntenne();
                                MyViseeAntenne.IDViseeAntenne   = strToIntDef(prmsLn[1], 0);

                                MyViseeAntenne.Reseau           = strToIntDef(prmsLn[2], 0);
                                MyViseeAntenne.Secteur          = strToIntDef(prmsLn[3], 0);

                                MyViseeAntenne.SerieDepart      = strToIntDef(prmsLn[4], 0);
                                MyViseeAntenne.PtDepart         = strToIntDef(prmsLn[5], 0);
                                MyViseeAntenne.Code             = strToIntDef(prmsLn[6], 0);
                                MyViseeAntenne.Expe             = strToIntDef(prmsLn[7], 0);
                                MyViseeAntenne.IDTerrainStation = prmsLn[8].trim();
                                MyViseeAntenne.Longueur         = strToFloatDef(prmsLn[9], 0.00);
                                MyViseeAntenne.Azimut           = strToFloatDef(prmsLn[10], 0.00);
                                MyViseeAntenne.Pente            = strToFloatDef(prmsLn[11], 0.00);
                                MyViseeAntenne.Commentaires     = prmsLn[12].trim();
                                this.AddViseeEnAntenne(MyViseeAntenne);
                                break;
                            case  -8: // réseaux
                                myReseau = new TReseau();
                                myReseau.IdxReseau   = strToIntDef(prmsLn[1], 0);
                                myReseau.CouleurReseauR = strToIntDef(prmsLn[2], 0);
                                myReseau.CouleurReseauG = strToIntDef(prmsLn[3], 0);
                                myReseau.CouleurReseauB = strToIntDef(prmsLn[4], 0);


                                myReseau.TypeReseau    = strToIntDef(prmsLn[5], 0);
                                myReseau.NomReseau     = prmsLn[6].trim();
                                myReseau.ObsReseau     = prmsLn[7].trim();
                                this.addReseau(myReseau);
                                break;
                            case  -6: // Section -6: Entrée
                                ProvListeEntrees.add(prmsLn[2].trim());
                                break;
                            case  -5: // Section -5: Entrées
                                if (this.getNbEntrances() == 0) //nombre d'entrées nul = on définit l'entrée par défaut
                                {
                                    this.setDefaultCoords(strToFloatDef(prmsLn[2].trim(), 0.00),
                                                          strToFloatDef(prmsLn[3].trim(), 0.00),
                                                          strToFloatDef(prmsLn[4].trim(), 0.00));
                                    this.setDefaultRefSerSt(strToIntDef(prmsLn[5].trim(), 1), strToIntDef(prmsLn[6].trim(), 0));
                                }

                                myEntrance = new TEntrance();
                                myEntrance.eNumEntree    =  this.getNbEntrances() + 1;
                                try {
                                    myEntrance.eNomEntree = ProvListeEntrees.get(myEntrance.eNumEntree - 1);
                                }
                                catch (Exception E)
                                {
                                    myEntrance.eNomEntree = sprintf("Entree%d", myEntrance.eNumEntree);
                                }
                                myEntrance.eXEntree  = strToFloatDef(prmsLn[2].trim(), 0.00);
                                myEntrance.eYEntree  = strToFloatDef(prmsLn[3].trim(), 0.00);
                                myEntrance.eZEntree  = strToFloatDef(prmsLn[4].trim(), 0.00);
                                if ((myEntrance.eXEntree < 100.00) || (myEntrance.eYEntree < 100.00)) writeWarning("Entrée non géoréférencée");
                                myEntrance.eRefSer = strToIntDef(prmsLn[5].trim(), 1);
                                myEntrance.eRefSt  = strToIntDef(prmsLn[6].trim(), 0);
                                myEntrance.eObserv = prmsLn[7].trim();
                                if ((myEntrance.eRefSer < 1) || (myEntrance.eRefSt < 0)) writeWarning(sprintf("%d: Mauvais raccordement: %d - %s - %d.%d",  NbLignes, myEntrance.eNumEntree, myEntrance.eNomEntree, myEntrance.eRefSer, myEntrance.eRefSt));
                                this.addEntrance(myEntrance);
                               
                            case  -4:
                                break;
                            case  -3:
                                break;
                            case  -2: // Expés
                                myExpe = new TExpe();
                                myExpe.IdxExpe       = strToIntDef(prmsLn[1].trim(), 0);
                                int qJourExpe  = strToIntDef(prmsLn[2].trim(),1);
                                int qMoisExpe  = strToIntDef(prmsLn[3].trim(),1);
                                int qAnneeExpe = strToIntDef(prmsLn[4].trim(),2014);
                                myExpe.DateLeve      = ensureMakeGHTopoDateFromYMD(qAnneeExpe, qMoisExpe, qJourExpe);
                                myExpe.Speleometre   = prmsLn[5].trim();             // spéléomètre
                                myExpe.Speleographe  = prmsLn[6].trim();             // spéléographe
                                myExpe.ModeDecl      = strToIntDef(prmsLn[7].trim(), 0);   // déclinaison auto ?
                                myExpe.Declinaison   = strToFloatDef(prmsLn[8].trim(), 0.00); // déclinaison
                                myExpe.Inclinaison   = strToFloatDef(prmsLn[9].trim(), 0.00); // correction clino x10
                                myExpe.IdxColor      = strToIntDef(prmsLn[10].trim(), 0);  // couleur
                                myExpe.Commentaire   = prmsLn[11].trim();            // commentaire
                                this.addExpe(myExpe);
                                break;
                            case  -1: // Codes
                                myCode = new TCode();
                                myCode.IdxCode       = strToIntDef(prmsLn[1].trim(), 0);
                                myCode.UniteBoussole = strToFloatDef(prmsLn[2].trim(), 360.00);  // unité boussole
                                myCode.UniteClino    = strToFloatDef(prmsLn[3].trim(), 360.00);  // unite  CLINO
                                myCode.PsiL          = strToFloatDef(prmsLn[4].trim(), 0.01);  // precision longueur
                                myCode.PsiAz         = strToFloatDef(prmsLn[5].trim(), 0.1);  // precision azimut
                                myCode.PsiP          = strToFloatDef(prmsLn[6].trim(), 0.1);  // precision pente
                                myCode.FactLong      = strToFloatDef(prmsLn[7].trim(), 1.00);  // Facteur des longueurs
                                myCode.AngLimite     = strToFloatDef(prmsLn[8].trim(), 0.00);  // angle limite
                                myCode.Commentaire   = prmsLn[9].trim();              // commentaire
                                this.addCode(myCode);
                                break;
                            default: // on est dans les séries

                                prefix1 = strToIntDef(prmsLn[0].trim(), 0);
                                if (prefix1 > 0) { //  begin
                                    prefix2 = strToIntDef(prmsLn[1].trim(), -2);
                                    if (prefix2 == -1)
                                    { //  begin
                                        // si c'est la première série, on crée
                                        if (prefix1 == 1)
                                        {
                                            mySerie = new TSerie();
                                            //AfficherMessage("Crée nouvelle série");
                                        }
                                        else  // sinon on ferme la série courante et on crée la suivante
                                        {
                                            this.addSerie(mySerie);
                                            mySerie = new TSerie();
                                        }
                                        mySerie.setIndexSerie(prefix1);
                                        mySerie.setStationsExtremites(strToIntDef(prmsLn[2].trim(), -1),
                                                                      strToIntDef(prmsLn[3].trim(), -1),
                                                                      strToIntDef(prmsLn[4].trim(), -1),
                                                                      strToIntDef(prmsLn[5].trim(), -1)
                                        );
                                        mySerie.setChanceObstacle(strToIntDef(prmsLn[7].trim(), 0), strToIntDef(prmsLn[8].trim(), 0));
                                        mySerie.setNomSerie(prmsLn[9].trim());
                                        mySerie.setObsSerie(prmsLn[10].trim());
                                        mySerie.setNoReseau(strToIntDef(prmsLn[11].trim(), 0));
                                        mySerie.setRaideur(strToFloatDef(prmsLn[12].trim(), 1.02));
                                    } //end; // if (Prefix2 = -1) then
                                    else
                                    {
                                        myStation = new TStation();

                                        myStation.IDStation   = strToIntDef(prmsLn[1].trim(), 0);
                                        myStation.Code        = strToIntDef(prmsLn[2].trim(), 0);
                                        myStation.Expe        = strToIntDef(prmsLn[3].trim(), 0);
                                        myStation.Longueur    = strToFloatDef(prmsLn[4].trim(), 0.00);
                                        if (myStation.Longueur < 0.00) writeWarning(sprintf("%d: Longueur incorrecte (%.2f)", NbLignes, myStation.Longueur));
                                        myStation.Longueur = Math.abs(myStation.Longueur);
                                        myStation.Azimut       = strToFloatDef(prmsLn[5].trim(), 0.00);
                                        myStation.Pente        = strToFloatDef(prmsLn[6].trim(), 0.00);
                                        myStation.LG           = strToFloatDef(prmsLn[7].trim(), 0.00);  //LG
                                        myStation.LD           = strToFloatDef(prmsLn[8].trim(), 0.00);  //LD
                                        myStation.HZ           = strToFloatDef(prmsLn[9].trim(), 0.00);
                                        myStation.HN           = strToFloatDef(prmsLn[10].trim(), 0.00);
                                        myStation.Commentaires = prmsLn[11].trim();
                                        myStation.IDTerrain    = prmsLn[12].trim();
                                        myStation.TypeVisee    = strToIntDef(prmsLn[13], 0);
                                        myStation.Secteur      = strToIntDef(prmsLn[14].trim(), 0);
                                        mySerie.addStation(myStation);
                                    }
                                }
                                break;
                        }        
                    }
                    catch (Exception e)
                    {
                        afficherMessageFmt("*** Erreur en ligne: %d: %s", NbLignes, maLigne);
                    }    

                    //*************************************************
                    NbLignes++;
                } //  while ((MaLigne = myBuffer.readLine())!=null)
                result = 0;

                // et on ajoute la dernière série
                afficherMessage(sprintf("%d lignes lues", NbLignes));
                this.addSerie(mySerie);
                //**************************************************************
                // vérifications et auto-corrections
            }
            finally
            {    
                myBuffer.close(); 
            }    
            
        }
        catch (FileNotFoundException fne)
        {
            afficherMessage(sprintf("Fichier %s introuvable", FichierXTB));
            result = -1;
        }    
        catch (Exception E)
        {
            E.printStackTrace();
            result = -1;
        }
        // On définit le point de référence
        afficherMessage("Section GENERAL");
        if (getNbEntrances() > 0)
        {
            myEntrance = getEntrance(0);
            this.setDefaultCoords(myEntrance.eXEntree, myEntrance.eYEntree, myEntrance.eZEntree);
            this.setDefaultRefSerSt(myEntrance.eRefSer, myEntrance.eRefSt);

        }    
        return result;
    }
	//*******************************************************************************************
    // Tris 
    //	TODO: Vérifier et tester
    public void trierLesCodes()
    {
        afficherMessage("trierLesCodes()");
        Collections.sort(this.mFListeCodes, new Comparator<TCode>() 
        {
            @Override
            public int compare(TCode p1,  TCode p2) 
            {
                if      (p1.IdxCode > p2.IdxCode) return  1;
                else if (p1.IdxCode < p2.IdxCode) return -1;
                else              return  0;
            }    
        }); 
        //*/
    }
    public void trierLesExpes()
    {	
        afficherMessage("trierLesExpes()"); 
        Collections.sort(this.mFListeExpes, new Comparator<TExpe>() 
        {
            @Override
            public int compare(TExpe p1,  TExpe p2) 
            {
                if      (p1.IdxExpe > p2.IdxExpe) return  1;
                else if (p1.IdxExpe < p2.IdxExpe) return -1;
                else              return  0;
            }    
        });
        //*/
    }

    public void trierLesSeries()
    {
        afficherMessage("trierLesSeries()");
        Collections.sort(this.mFListeSeries, new Comparator<TSerie>() 
        {
            @Override
            public int compare(TSerie p1,  TSerie p2) 
            {
                if      (p1.IdxSerie > p2.IdxSerie) return  1;
                else if (p1.IdxSerie < p2.IdxSerie) return -1;
                else              return  0;
            }    
        });
        //*/
    }
    //--------------------------------------------------------------------------
    // calcul des déclinaisons magnétiques
    public void calculerLesDeclinaisonsMagnetiques()
    {
        int Nb = this.getNbExpes();
        afficherMessageFmt("calculerLesDeclinaisonsMagnetiques: %d expes", Nb);
        TCalculateurDeclimag TCD = new TCalculateurDeclimag();
        TConvertisseurCoordonnees TCC = new TConvertisseurCoordonnees();
        if (! TCC.initialiser()) 
        {
            afficherMessage("Convertisseur de coordonnées inopérant");
            return;
        }    
        if (! TCD.initialiser(getGHTopoDirectory() + File.separator + "IGRF"))
        {
            afficherMessage("Calculateur de déclinaisons magnétiques inopérant");
            return;
        }    
        int EPSGCourant = this.getCoordSystCodeEPSG();
        //CoordinateReferenceSystem CRS = TCC.getCoordSystemByCodeEPSG(EPSGCourant);
        CoordinateReferenceSystem CRS = TCC.getCoordSystemByCodeEPSG(EPSGCourant);
        afficherMessageFmt("-- Système de coordonnées: %d - %s", EPSGCourant, CRS.getName());
        TPoint2Df LonLat = TCC.conversionByCodeEPSG(EPSGCourant, 4326, this.mFDefaultCoordX, this.mFDefaultCoordY);
        afficherMessageFmt("-- Coordonnées origine: %.2f, %.2f (lon = %.8f, lat = %.8f", this.mFDefaultCoordX, this.mFDefaultCoordY, LonLat.X, LonLat.Y);
        for (int i = 0; i < Nb; i++)
        {
            TExpe EE = this.getExpe(i);
            int DT = EE.DateLeve;           
            int AAAA = getYear(DT);            
            int MM   = getMonth(DT);
            int JJ   = getDay(DT);
            
            double decl = TCD.CalcDeclimag(LonLat.X, LonLat.Y, this.mFDefaultCoordZ, AAAA, MM, JJ);
            afficherMessageFmt("-- %d: %02d/%02d/%04d - %.8f", i, JJ, MM, AAAA, decl);
            EE.ModeDecl    = 0; // on bascule en manuel pour protéger le résultat du calcul de declinaison
            EE.Declinaison = decl;
            this.putExpe(i, EE);
        }           
    }        
    //--------------------------------------------------------------------------
    public void ListerLeDocument()
    {
        int NbreElements = 0;
        afficherMessage("-----------------------------------------");
        afficherMessage("Lister le document");
        afficherMessageFmt("Nom de l'étude: %s" ,this.getNomEtude());
        afficherMessageFmt("Commentaires  : %s" , this.getCommentairesEtude());
        afficherMessageFmt("Code EPSG     : %d" , this.mFCodeEPSGParDefaut);		
        afficherMessageFmt("X référence: %.2f", this.getPoint0X());
        afficherMessageFmt("Y référence: %.2f", this.getPoint0Y());
        afficherMessageFmt("Z référence: %.2f", this.getPoint0Z());

        afficherMessage("");
        afficherMessageFmt("Liste des %d entrées", this.getNbEntrances());
        NbreElements = this.getNbEntrances();
        for (int i = 0; i < NbreElements; i++)
        {
            TEntrance myEntr = this.getEntrance(i);
            
            afficherMessageFmt("%d: %d %s - %d.%d - %.2f, %.2f, %.2f - %s",
                                          i, myEntr.eNumEntree, myEntr.eNomEntree,
                                             myEntr.eRefSer, myEntr.eRefSt,
                                             myEntr.eXEntree, myEntr.eYEntree,  myEntr.eZEntree,
                                             myEntr.eObserv);
        }
        afficherMessage("");
        afficherMessageFmt("Liste des %d réseaux", this.getNbReseaux());
        NbreElements = this.getNbReseaux();
        for (int i = 0; i < NbreElements; i++)
        {
            TReseau myR = this.getReseau(i);
            afficherMessageFmt("%d: %d - %d, %d, %d - %s, %s",
                    i, myR.IdxReseau,
                    myR.CouleurReseauR, myR.CouleurReseauG, myR.CouleurReseauB,
                    myR.NomReseau, myR.ObsReseau);
        }
        afficherMessage("");
        afficherMessageFmt("Liste des %d secteurs", this.getNbSecteurs());
        NbreElements = this.getNbSecteurs();
        for (int i = 0; i < NbreElements; i++)
        {
            TSecteur mySec = this.getSecteur(i);
            afficherMessageFmt("%d: %d - %d, %d, %d - %s",
                    i, mySec.IdxSecteur,
                    mySec.CouleurSecteurR, mySec.CouleurSecteurG, mySec.CouleurSecteurB,
                    mySec.NomSecteur);
        }
        afficherMessage("");
        afficherMessageFmt("Liste des %d antennes", this.getNbViseesAntenne());
        NbreElements = this.getNbViseesAntenne();
        for (int i = 0; i < NbreElements; i++)
        {
            TViseeEnAntenne myA = this.getViseeEnAntenne(i);
            afficherMessageFmt("%d: %d %s - R = %d S = %d - %d.%d - C = %d, E = %d - %.2f, %.2f, %.2f - %s",
                                          i, myA.IDViseeAntenne, myA.IDTerrainStation,
                                          myA.Reseau, myA.Secteur,
                                          myA.SerieDepart, myA.PtDepart,
                                          myA.Code, myA.Expe,
                                          myA.Longueur, myA.Azimut, myA.Pente,
                                          myA.Commentaires);
        }
        afficherMessage("");
        afficherMessageFmt("Liste des %d codes", this.getNbCodes());
        NbreElements = this.getNbCodes();
        for (int i = 0; i < NbreElements; i++)
        {
            TCode myC = this.getCode(i);
            afficherMessageFmt("%d: %d - %.2f, %.2f - %.3f, %.3f, %.3f - %.4f, %.2f -  %s",
                                           i, myC.IdxCode, myC.UniteBoussole, myC.UniteClino,
                                              myC.PsiL, myC.PsiAz, myC.PsiP,
                                              myC.FactLong, myC.AngLimite,
                                              myC.Commentaire);
        }
        afficherMessage("");
        afficherMessageFmt("Liste des %d expés", this.getNbExpes());

        NbreElements = this.getNbExpes();
        for (int i = 0; i < NbreElements; i++) {
            TExpe myE = this.getExpe(i);
            int qAnnee = getYear(myE.DateLeve);
            int qMois  = getMonth(myE.DateLeve);
            int qJour  = getDay(myE.DateLeve);
            afficherMessageFmt("%d: %d - %02d/%02d/%04d - %s, %s - %d - %.2f, %.2f - %d - %s",
                    i, myE.IdxExpe,
                    qJour, qMois, qAnnee,
                    myE.Speleometre, myE.Speleographe,
                    myE.ModeDecl, myE.Declinaison, myE.Inclinaison, myE.IdxColor, myE.Commentaire);
        }
        afficherMessage("");
        afficherMessageFmt("Liste des %d séries", this.getNbSeries());
        NbreElements = this.getNbSeries();
        for (int i = 0; i < NbreElements; i++)
        {

            TSerie mySr = this.getSerie(i);
            afficherMessageFmt("%d: %d - %d.%d -> %d.%d - %s - %d points", i, mySr.IdxSerie, mySr.SerDep, mySr.PtDep, mySr.SerArr, mySr.PtArr, mySr.NomSerie, mySr.getNbStations());
            int NbreStations = mySr.getNbStations();
            for (int j = 0; j < NbreStations; j++)
            {
                TStation St = mySr.getStation(j);
                afficherMessageFmt(" ---; %d.%d; %s; %d; %d; %d; %.2f; %.2f; %.2f; %.2f; %.2f; %.2f; %.2f; %s; Type: %d",
                                                 mySr.IdxSerie, j,
                                                 St.IDTerrain, St.Secteur,
                                                 St.Code, St.Expe,
                                                 St.Longueur, St.Azimut, St.Pente,
                                                 St.LG, St.LD, St.HZ, St.HN,
                                                 St.Commentaires,
                                                 St.TypeVisee);
            }
        }
    }
    // chargement d'un fichier GHTopo XML
    public int loadFileXML(String fichierXML) throws SAXException, IOException
    {
        afficherMessage("LoadFileXML: " + fichierXML);
        int result = -1;        
	this.mFCodeEPSGParDefaut = 2154;
        this.viderListes(true); // TODO: Dans GHTopoFPC, ne pas écrire les éléments 0 dans le fichier XML
        //this.viderListes(false); // les fichiers XML contiennent déjà les éléments 0
        XMLReader saxReader = XMLReaderFactory.createXMLReader(); //"org.apache.xerces.parsers.SAXParser");
        try
        {   
            saxReader.setContentHandler(new TXMLContentHandlerForGHTopo(this));
            saxReader.parse(fichierXML);
            //------------------------------------------------------------------
            // Si entrées présentes, entrée par défaut = point de référence
            //if (this.getNbEntrances() > 0)
            //{    
                TEntrance e = this.getEntrance(0);
                this.setDefaultRefSerSt(1, 0);
                this.setDefaultCoords(e.eXEntree, e.eYEntree, e.eZEntree);
            //}    
            //------------------------------------------------------------------
            result = 0;
        }
        /*
        catch (FileNotFoundException fne)
        {
            afficherMessage(sprintf("Fichier %s introuvable", fichierXML));
            result = -1;
        } 
        //*/
        catch (Exception e)
        {
            e.printStackTrace();
            result = -1;
        }    
        return result;
            
    }
    // sauvegarde au format xtb
    //	@Deprecated:
    public void saveToXTB(String FichierXTB)
    {
        int Nb = 0;
        afficherMessage("saveToXTB: " + FichierXTB);
        // header
        // general
        // entrées
        Nb = getNbEntrances();
        for (int i = 0; i < Nb; i++)
        {
            TEntrance EE = getEntrance(i);
        }    
        // réseaux
        Nb = getNbReseaux();
        for (int i = 0; i < Nb; i++)
        {
            TReseau RR = getReseau(i);
        }    
        Nb = getNbSecteurs();
        // secteurs
        for (int i = 0; i < Nb; i++)
        {
            TSecteur SC = getSecteur(i);
        }    
        // codes
        Nb = getNbCodes();
        for (int i = 0; i < Nb; i++)
        {
            TCode CC = getCode(i);
        }    
        // expés
        Nb = getNbExpes();
        for (int i = 0; i < Nb; i++)
        {
            TExpe EE = getExpe(i);
        }    
        // séries
        Nb = getNbSeries();
        for (int i = 0; i < Nb; i++)
        {
            TSerie mySerie = getSerie(i);
            //....
            int NbSts = mySerie.getNbStations();
            for (int j = 0; j < NbSts; j++)
            {
                TStation myStation = mySerie.getStation(j);
                //....
            }    
        }    
        // antennes
        Nb = getNbViseesAntenne();
        for (int i = 0; i < Nb; i++)
        {
           
        }    
    }
    //--------------------------------------------------------------------------
    // sauvegarde au format xml
    private void XMLFlush(String qFichierXML)
    {        
	try
        {
           //On utilise ici un affichage classique avec getPrettyFormat()
           XMLOutputter sortie = new XMLOutputter(org.jdom2.output.Format.getPrettyFormat());
           //Remarquez qu'il suffit simplement de créer une instance de FileOutputStream
           //avec en argument le nom du fichier pour effectuer la sérialisation.
           sortie.output(myDocument, new FileOutputStream(qFichierXML));
        }
        catch (java.io.IOException e)
        {
            e.printStackTrace();
        }    
    }
    
    private void AddAttributInt(Element Noeud, String key, int value)
    {
       Attribute result = new Attribute(key, sprintf("%d", value));
       Noeud.setAttribute(result);
    }        
    private void AddAttributHex(Element Noeud, String key, int value)
    {
       Attribute result = new Attribute(key, sprintf("#%X", value));
       Noeud.setAttribute(result);
    }       
    private void AddAttributStr(Element Noeud, String key, String value)
    {
       Attribute result = new Attribute(key, value.trim());
       Noeud.setAttribute(result);
    }    
    private void AddAttributDbl(Element Noeud, String key, double value)
    {
       Attribute result = new Attribute(key, ensureConvertNumberToStr(value));
       Noeud.setAttribute(result);
    }
    public void saveToXML(String FichierXML)
    {
        afficherMessage("saveToXML: " + FichierXML);
        Element GHTopoRoot = new Element(GTX_KEY_DOCUMENT_ROOT);
        //On crée un nouveau Document JDOM basé sur la racine que l'on vient de créer
        myDocument = new Document(GHTopoRoot);
        //myDocument.
        //DocType myDocType = new DocType(GHTopoRoot.getName());
        // myDocument.setDocType(myDocType);
        int Nb = 0;
        Attribute aAttrib = null;
        try
        {
            // header
            // general
            afficherMessage("-- Section General");
            Element eleHeader = new Element(GTX_KEY_SECTION_GENERAL);
            GHTopoRoot.addContent(eleHeader);
            Element xmlCavite = new Element(GTX_KEY_CAVITE);
            eleHeader.addContent(xmlCavite);
            afficherMessage(mFNomEtude);
            AddAttributStr(xmlCavite, GTX_ATTR_NOM_ETUDE, this.mFNomEtude);
            AddAttributStr(xmlCavite, GTX_ATTR_COMMENTAIRES_ETUDE, this.mFCommentairesEtude);
            // systèmes de coordonnées
            AddAttributStr(xmlCavite, GTX_ATTR_COORDS_SYSTEM_EPSG, sprintf("%d", this.getCoordSystCodeEPSG()));
            AddAttributStr(xmlCavite, GTX_ATTR_COORDS_SYSTEM_GHTOPO, this.getCoordSystCodeGHTopo());
            AddAttributStr(xmlCavite, GTX_ATTR_COORDS_SYSTEM_DESC  , this.getCoordSystCommentaire());
            // date de dernière sauvegarde
            AddAttributStr(xmlCavite, GTX_ATTR_DATE_LAST_SAVE, dateMaintenant()); 
			
            // entrées
            Element eleEntrances = new Element(GTX_KEY_SECTION_ENTRANCES);
            GHTopoRoot.addContent(eleEntrances);
            Nb = this.getNbEntrances();
            afficherMessageFmt("---- Section ENTRANCES: %d", Nb);
            for (int i = 0; i < Nb; i++)
            {
                TEntrance ee = this.getEntrance(i);
                Element xmlEntrance = new Element(GTX_KEY_ENTRANCE);
                eleEntrances.addContent(xmlEntrance);
                AddAttributInt(xmlEntrance, GTX_ATTR_ENTRANCE_IDX, ee.eNumEntree);
                AddAttributStr(xmlEntrance, GTX_ATTR_ENTRANCE_NAME, ee.eNomEntree);
                AddAttributInt(xmlEntrance, GTX_ATTR_ENTRANCE_REFSERIE, ee.eRefSer);
                AddAttributInt(xmlEntrance, GTX_ATTR_ENTRANCE_REFPT, ee.eRefSt);
                AddAttributStr(xmlEntrance, GTX_ATTR_ENTRANCE_OBS, ee.eObserv);
                AddAttributDbl(xmlEntrance, GTX_ATTR_ENTRANCE_X, ee.eXEntree);
                AddAttributDbl(xmlEntrance, GTX_ATTR_ENTRANCE_Y, ee.eYEntree);
                AddAttributDbl(xmlEntrance, GTX_ATTR_ENTRANCE_Z, ee.eZEntree);
            }    
            // réseaux
            afficherMessage("--- Section RESEAUX");
            Element eleReseaux = new Element(GTX_KEY_SECTION_RESEAUX);
            GHTopoRoot.addContent(eleReseaux);
            Nb = this.getNbReseaux();
            for (int i = 1; i < Nb; i++)
            {    
                TReseau rr = this.getReseau(i);
                Element xmlReseau = new Element(GTX_KEY_RESEAU);
                eleReseaux.addContent(xmlReseau);
                AddAttributInt(xmlReseau, GTX_ATTR_RESEAU_IDX, rr.IdxReseau);
                AddAttributStr(xmlReseau, GTX_ATTR_RESEAU_NAME, rr.NomReseau);
                AddAttributStr(xmlReseau, GTX_ATTR_RESEAU_OBS, rr.ObsReseau);
                AddAttributInt(xmlReseau, GTX_ATTR_RESEAU_COLOR_R, rr.CouleurReseauR);
                AddAttributInt(xmlReseau, GTX_ATTR_RESEAU_COLOR_G, rr.CouleurReseauG);
                AddAttributInt(xmlReseau, GTX_ATTR_RESEAU_COLOR_B, rr.CouleurReseauB);
                AddAttributInt(xmlReseau, GTX_ATTR_RESEAU_TYPE, rr.TypeReseau);
            }
            // secteurs
            afficherMessage("--- Section SECTEURS");
            Element eleSecteurs = new Element(GTX_KEY_SECTION_SECTEURS);
            GHTopoRoot.addContent(eleSecteurs);
            Nb = this.getNbSecteurs();
            for (int i = 1; i < Nb; i++)
            {    
                TSecteur ss = this.getSecteur(i);
                Element xmlSecteur = new Element(GTX_KEY_SECTEUR);
                eleSecteurs.addContent(xmlSecteur);
                AddAttributInt(xmlSecteur, GTX_ATTR_SECTEUR_IDX, ss.IdxSecteur);
                AddAttributStr(xmlSecteur, GTX_ATTR_SECTEUR_NAME, ss.NomSecteur);
                AddAttributInt(xmlSecteur, GTX_ATTR_SECTEUR_COLOR_R, ss.CouleurSecteurR);
                AddAttributInt(xmlSecteur, GTX_ATTR_SECTEUR_COLOR_G, ss.CouleurSecteurG);
                AddAttributInt(xmlSecteur, GTX_ATTR_SECTEUR_COLOR_B, ss.CouleurSecteurB);
            }
            // codes
            afficherMessage("--- Section CODES");
            Element eleCodes = new Element(GTX_KEY_SECTION_CODES);
            GHTopoRoot.addContent(eleCodes);
            Nb = this.getNbCodes();
            for (int i = 1; i < Nb; i++)
            {    
                TCode cc = this.getCode(i);
                Element xmlCode = new Element(GTX_KEY_CODE);
                eleCodes.addContent(xmlCode);
                AddAttributInt(xmlCode, GTX_ATTR_CODE_NUMERO, cc.IdxCode);
                AddAttributDbl(xmlCode, GTX_ATTR_CODE_UCOMPASS, cc.UniteBoussole);
                AddAttributDbl(xmlCode, GTX_ATTR_CODE_UCLINO, cc.UniteClino);
                AddAttributDbl(xmlCode, GTX_ATTR_CODE_PSI_L, cc.PsiL);
                AddAttributDbl(xmlCode, GTX_ATTR_CODE_PSI_A, cc.PsiAz);
                AddAttributDbl(xmlCode, GTX_ATTR_CODE_PSI_P, cc.PsiP);
                AddAttributDbl(xmlCode, GTX_ATTR_CODE_ANGLIMITE, cc.AngLimite);
                AddAttributDbl(xmlCode, GTX_ATTR_CODE_FACT_LONG, cc.FactLong);
                AddAttributInt(xmlCode, GTX_ATTR_CODE_TYPE     , 0);                
                AddAttributStr(xmlCode, GTX_ATTR_CODE_OBS, cc.Commentaire);
            }
            // expés
            Nb = this.getNbExpes();
            afficherMessage("--- Section EXPES");
            Element eleExpes = new Element(GTX_KEY_SECTION_EXPES);
            GHTopoRoot.addContent(eleExpes);
            for (int i = 1; i < Nb; i++)
            {    
                TExpe ex = this.getExpe(i);
                Element xmlExpe = new Element(GTX_KEY_CODE);
                eleExpes.addContent(xmlExpe);
                AddAttributInt(xmlExpe, GTX_ATTR_EXPE_NUMERO, ex.IdxExpe);
                AddAttributInt(xmlExpe, GTX_ATTR_EXPE_IDXCOLOR, ex.IdxColor);
                AddAttributInt(xmlExpe, GTX_ATTR_EXPE_DATE, ex.DateLeve);
                AddAttributInt(xmlExpe, GTX_ATTR_EXPE_MODEDECL, ex.ModeDecl);
                AddAttributStr(xmlExpe, GTX_ATTR_EXPE_SURVEY1, ex.Speleometre);
                AddAttributStr(xmlExpe, GTX_ATTR_EXPE_SURVEY2, ex.Speleographe);
                AddAttributStr(xmlExpe, GTX_ATTR_EXPE_OBS, ex.Commentaire);
                AddAttributDbl(xmlExpe, GTX_ATTR_EXPE_DECLINAT, ex.Declinaison);
                AddAttributDbl(xmlExpe, GTX_ATTR_EXPE_INCLINAT, ex.Inclinaison);
            }
            // séries
            Nb = this.getNbSeries();
            afficherMessage("--- Section SERIE");
            Element eleSeries = new Element(GTX_KEY_SECTION_SERIES);
            GHTopoRoot.addContent(eleSeries);
            for (int i = 1; i < Nb; i++)
            {    
                TSerie sr = this.getSerie(i);
                Element xmlSerie = new Element(GTX_KEY_SERIE);
                eleSeries.addContent(xmlSerie);
                AddAttributInt(xmlSerie, GTX_ATTR_SERIE_Numero, sr.IdxSerie);
                AddAttributStr(xmlSerie, GTX_ATTR_SERIE_NAME, sr.NomSerie);
                AddAttributStr(xmlSerie, GTX_ATTR_SERIE_OBS, sr.ObsSerie);
                AddAttributInt(xmlSerie, GTX_ATTR_SERIE_CHANCE, sr.Chance);
                AddAttributInt(xmlSerie, GTX_ATTR_SERIE_OBSTACLE, sr.Obstacle);
                AddAttributInt(xmlSerie, GTX_ATTR_SERIE_SERDEP, sr.SerDep);
                AddAttributInt(xmlSerie, GTX_ATTR_SERIE_SERARR, sr.SerArr);
                AddAttributInt(xmlSerie, GTX_ATTR_SERIE_PTDEP, sr.PtDep);
                AddAttributInt(xmlSerie, GTX_ATTR_SERIE_PTARR, sr.PtArr);
                AddAttributDbl(xmlSerie, GTX_ATTR_SERIE_RAIDEUR, sr.Raideur);
                AddAttributInt(xmlSerie, GTX_ATTR_SERIE_RESEAU, sr.Reseau);
                // stations
                Element eleStationsLst = new Element(GTX_KEY_STATIONS);
                xmlSerie.addContent(eleStationsLst);
                for (int j = 0; j < sr.getNbStations(); j++)
                {
                    TStation st = sr.getStation(j);
                    Element xmlStation = new Element(GTX_KEY_VISEE);
                    eleStationsLst.addContent(xmlStation);
                    AddAttributInt(xmlStation, GTX_ATTR_VISEE_ID, st.IDStation);
                    AddAttributInt(xmlStation, GTX_ATTR_VISEE_SECTEUR, st.Secteur);
                    AddAttributInt(xmlStation, GTX_ATTR_VISEE_TYPE, st.TypeVisee);
                    AddAttributInt(xmlStation, GTX_ATTR_VISEE_CODE, st.Code);
                    AddAttributInt(xmlStation, GTX_ATTR_VISEE_EXPE, st.Expe);
                    AddAttributDbl(xmlStation, GTX_ATTR_VISEE_LONG, st.Longueur);
                    AddAttributDbl(xmlStation, GTX_ATTR_VISEE_AZ, st.Azimut);
                    AddAttributDbl(xmlStation, GTX_ATTR_VISEE_P, st.Pente);
                    AddAttributDbl(xmlStation, GTX_ATTR_VISEE_LG, st.LG);
                    AddAttributDbl(xmlStation, GTX_ATTR_VISEE_LD, st.LD);
                    AddAttributDbl(xmlStation, GTX_ATTR_VISEE_HZ, st.HZ);
                    AddAttributDbl(xmlStation, GTX_ATTR_VISEE_HN, st.HN);
                    AddAttributStr(xmlStation, GTX_ATTR_VISEE_OBS, st.Commentaires);
                    AddAttributStr(xmlStation, GTX_ATTR_VISEE_LBL, st.IDTerrain);                                       
                } 
            }
            // antennes
            Nb =  this.getNbViseesAntenne();
            afficherMessage("--- Section ANTENNES");
            Element eleAntennes = new Element(GTX_KEY_SECTION_ANTENNAS);
            GHTopoRoot.addContent(eleAntennes);
            for (int i = 0; i < Nb; i++)
            {    
                TViseeEnAntenne va = this.getViseeEnAntenne(i);
                Element xmlViseeEnAntenne = new Element(GTX_KEY_ANTENNA_SHOT);
                eleAntennes.addContent(xmlViseeEnAntenne);
                AddAttributInt(xmlViseeEnAntenne, GTX_KEY_ANTENNA_NUMERO, va.IDViseeAntenne);
                AddAttributInt(xmlViseeEnAntenne, GTX_KEY_ANTENNA_SERIE, va.SerieDepart);
                AddAttributInt(xmlViseeEnAntenne, GTX_KEY_ANTENNA_POINT, va.PtDepart);
                AddAttributInt(xmlViseeEnAntenne, GTX_KEY_ANTENNA_CODE, va.Code);
                AddAttributInt(xmlViseeEnAntenne, GTX_KEY_ANTENNA_TRIP, va.Expe);
                AddAttributInt(xmlViseeEnAntenne, GTX_KEY_ANTENNA_NETWORK, va.Reseau);
                AddAttributInt(xmlViseeEnAntenne, GTX_KEY_ANTENNA_SECTEUR, va.Secteur);
                AddAttributDbl(xmlViseeEnAntenne, GTX_KEY_ANTENNA_LONG, va.Longueur);
                AddAttributDbl(xmlViseeEnAntenne, GTX_KEY_ANTENNA_AZIMUT, va.Azimut);
                AddAttributDbl(xmlViseeEnAntenne, GTX_KEY_ANTENNA_PENTE, va.Pente);
                AddAttributStr(xmlViseeEnAntenne, GTX_KEY_ANTENNA_LABEL, va.IDTerrainStation);
                AddAttributStr(xmlViseeEnAntenne, GTX_KEY_ANTENNA_OBS, va.Commentaires);
            }
            // flush
            XMLFlush(FichierXML);
        }
        catch (Throwable E)            
        {    
            E.printStackTrace();
        }
    }        
}
