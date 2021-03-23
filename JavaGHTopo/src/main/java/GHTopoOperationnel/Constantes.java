// Petit pense-bête GIT:
// Configuration de git:
// - Créer le dossier du projet à GITer
// - Ouvrir un terminal Git
//    -- > git init # initialise le contexte
//    ---> git clone https://jpcassou@wwwsgfr/git/JavaGHTopo.git


// Avant de changer de session / PC / site:

// Dans le sens Poste 1 -> serveur
//----------------------------------
// |Projects\____ -> [clkic droit sur [JavaGHTopo]
//                   --> |Git -> Commit ... boite de dialogue
//                   --> |Git -> push
//---

// Dans le sens serveur -> Poste 2:
//----------------------------------
// Si (contexte GIT non armé) { armer le contexte GIT une fois pour toutes }
//----------------------------------
// 1. |Projects\____ -> [clic droit sur [JavaGHTopo]
//                   --> |Git -> pull
//                   << introduire 5 bugs toutes les 9 lignes de code >>
//
// 2. Poste2 devient Poste1 puis faire procédure Poste1->serveur avant

//------------------------------------------------------------------------------
// A la ligne de commande:
// Dans le sens Poste 1 -> serveur
//----------------------------------
// 1. Se mettre à la racide du projet (dossier contenant le .git)
// git status
// 2. git add -A
// 3. git commit -m "<texte du commentaire"
// 4. git push
//
//------------------------
// Dans le sens serveur -> Poste 2
//----------------------------------
// 1. Se mettre à la racide du projet (dossier contenant le .git)
// git status
// 2. git pull origin master
// 2.1 saisir ce #+?@ de mot de passe
// 3. git log
// Login: jpcassou
// ------------------
// NOTA: Les chiffres topo ont été copiés dans la racine de GHTopo 
//       (HOWTO: Comment récupérer 
package GHTopoOperationnel;
//**********************************************************************************************************************
public class Constantes
{
    public static final String JAVA_GHTOPO_NAME = "JavaGHTopo";
    public static final int UNITE_CLINO_LASERMETRE_STANLEY_TLM330_DIR = 800; // Stanley TLM330
    public static final int UNITE_CLINO_LASERMETRE_STANLEY_TLM330_INV = UNITE_CLINO_LASERMETRE_STANLEY_TLM330_DIR - 10;
    // XML et KML
    // URL des specs XML

    public static final String KML_OPENGIS_WEBSITE     = "http://www.opengis.net/kml/2.2";
    public static final String KML_GOOGLE_KML_WEBSITE  = "http://www.google.com/kml/ext/2.2";
    public static final String W3C_W3_WEBSITE          = "http://www.w3.org/2005/Atom";
    public static final String W3C_XML_SCHEMA_WEBSITE  = "http://www.w3.org/2001/XMLSchema-instance";
    public static final String GPX_TOPOGRAPHIX_WEBSITE = "http://www.topografix.com/GPX/1/0";

    public static final String STYLE_GALERIE_PAR_DEFAUT = "StyleGalerieParDefaut";

    public static final double INV_DEGREES = 1/360.00;
    // remplacement des enums (en Java, ce sont des objets et non des int)
    // les enums de Java sont difficiles à utiliser
    // onglets
    public static final int ongletENTREE    = 0;
    public static final int ongletRESEAU    = 1;
    public static final int ongletSECTEUR   = 2;
    public static final int ongletCODE      = 3;
    public static final int ongletEXPE      = 4;
    public static final int ongletSERIE     = 5;
    public static final int ongletANTENNES  = 6;


    // valeurs de retour des boites de dialogue
    public static final int mrNONE        = 0;
    public static final int mrOK          = 1;
    public static final int mrCANCEL      = 2;
    public static final int mrYES         = 3;
    public static final int mrNO          = 4;
    public static final int mrCLOSE       = 5;

    // TTypeDeVisee
    public static final int tgDEFAULT     = 0;         // X défaut = galerie fossile
    public static final int tgENTRANCE    = 1;
    public static final int tgFOSSILE     = 2;
    public static final int tgVADOSE      = 3;
    public static final int tgENNOYABLE   = 4;
    public static final int tgSIPHON      = 5;
    public static final int tgFIXPOINT    = 6;
    public static final int tgSURFACE     = 7;
    public static final int tgTUNNEL      = 8;
    public static final int tgMINE        = 9;
    public static final int tgTYPE_ENTITES_VISEES_ANTENNE = 10;

    // TModeRepresentationGaleries
    public static final int rgDEFAULT     = 0;
    public static final int rgRESEAUX     = 1;
    public static final int rgSECTEURS    = 2;
    public static final int rgSEANCES     = 3;
    public static final int rgGRAY        = 4;
    public static final int rgDEPTH       = 5;
    // TOutputFormatGIS
    public static final int gisGHCAVEDRAW = 0;
    public static final int gisKML = 1;
    public static final int gisGPX = 2;
    public static final int gisOSM = 3;
    public static final int gisCARTO_EXPLOREUR = 4;
    public static final int gisMEMORY_MAP = 5;
    public static final int gisAUTOCAD_2D = 6;

    // TPolyMode = (, , , );
    public static final int tpmENTETE_POLY = 0;
    public static final int tpmSTART_POLY = 1;
    public static final int tpmPOINT_POLY = 2;
    public static final int tpmEND_POLY = 3;

    // TModeSelectionListes
    public static final int mslENTRANCES   = 0;
    public static final int mslRESEAUX     = 1;
    public static final int mslSECTEURS    = 2;
    public static final int mslCODES       = 3;
    public static final int mslEXPES       = 4;
    public static final int mslSERIES      = 5;
    public static final int mslANTENNES    = 6;

    // TTypeFiltre
    public static final int ftINVALID      = 0;
    public static final int ftSCALAR       = 1;
    public static final int ftINTERVAL     = 2;
    public static final int ftFLAGS        = 3;
    // TConnectorFiltres
    public static final int ftERROR          = 0;
    public static final int ftAND            = 1;
    public static final int ftOR             = 2;
    // TOperatorFiltre
    public static final int ftLT             = 1; // <
    public static final int ftEQ             = 2; // =
    public static final int ftGT             = 3; // >

    // pour le MétaFiltre
    public static final int kFLT_NIL         = 0;      //  NIL               NIL
    public static final int kFLT_ALL         = 1;      //  ALL               ALL
    public static final int kFLT_ID          = 2;      //  ID                ID
    public static final int kFLT_LONGUEUR    = 3;      //  LONGUEUR          LONGUEUR
    public static final int kFLT_AZIMUT      = 4;      //  AZIMUT            AZIMUT
    public static final int kFLT_PENTE       = 5;      //  PENTE             PENTE
    public static final int kFLT_DATE        = 6;      //  DATE              DATE
    public static final int kFLT_COULEUR     = 7;      //  COULEUR           COULEUR
    public static final int kFLT_X           = 8;      //  X                 X
    public static final int kFLT_Y           = 9;      //  Y                 Y
    public static final int kFLT_Z           = 10;     //  Z                 Z
    public static final int kFLT_LARGEUR     = 11;     //  LARGEUR           LARGEUR
    public static final int kFLT_HAUTEUR     = 12;     //  HAUTEUR           HAUTEUR
    public static final int kFLT_DATES       = 13;     //  DATES             DATES
    public static final int kFLT_COULEURS    = 14;     //  COULEURS          COULEURS
    public static final int kFLT_SERIE       = 15;     //  SERIE;
    public static final int kFLT_RESEAU      = 16;     //  Réseau
    public static final int kFLT_CODE        = 17;     //  code
    public static final int kFLT_EXPE        = 18;     //  Séance (expé)
    public static final int kFLT_TYPEVISEE   = 19;     //  type de visée
    public static final int kFLT_SECTEUR     = 20;     // secteur
// ensembles (sets)
// TElementsDrawn 
    public static final int edVoid             = 0;
    public static final int edPolygonals       = 1;
    public static final int edStations         = 2;
    public static final int edIDStations       = 4;
    public static final int edAltitudes        = 8;
    public static final int edCotes            = 16;
    public static final int edWalls            = 32;
    public static final int edCrossSections    = 64;
    public static final int edFillGalerie      = 128;
    public static final int edQuadrilles       = 256;
    public static final int edENTRANCES        = 512;
    public static final int edANNOTATIONS      = 1024;
    public static final int edJonctions        = 2048;
    
    // multiplier l'item précédent par 2
}