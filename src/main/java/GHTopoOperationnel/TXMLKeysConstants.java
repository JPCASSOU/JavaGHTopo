// Fichier de déclaration des clés pour le format GHTopo XML GTX
package GHTopoOperationnel;
import java.lang.String;

/**
 *
 * @author jean-pierre.cassou
 */
public class TXMLKeysConstants {
    public static final String GTX_KEY_DOCUMENT_ROOT        = "GHTopo";
    public static final String GTX_KEY_SECTION_GENERAL      = "General";
    public static final String    GTX_KEY_CAVITE               = "Cavite";
    public static final String    GTX_ATTR_NOM_ETUDE           = "FolderName";
    public static final String    GTX_ATTR_COORDS_SYSTEM_EPSG    = "CoordsSystemEPSG";
    public static final String    GTX_ATTR_COORDS_SYSTEM_GHTOPO  = "CoordsSystemGHTopo";
    public static final String    GTX_ATTR_COORDS_SYSTEM_DESC    = "CoordsSystemDescription";
    public static final String    GTX_ATTR_COMMENTAIRES_ETUDE  = "FolderObservations";
    public static final String    GTX_ATTR_DATE_LAST_SAVE      = "DateLastSave";
	
    public static final String GTX_KEY_SECTION_ENTRANCES    = "Entrances";
    public static final String    GTX_KEY_ENTRANCE             = "Entrance";
    public static final String    GTX_ATTR_ENTRANCE_IDX      = "Numero";
    public static final String    GTX_ATTR_ENTRANCE_NAME     = "Name";
    public static final String    GTX_ATTR_ENTRANCE_REFSERIE = "RefSerie";
    public static final String    GTX_ATTR_ENTRANCE_REFPT    = "RefPoint";
    public static final String    GTX_ATTR_ENTRANCE_X        = "X";
    public static final String    GTX_ATTR_ENTRANCE_Y        = "Y";
    public static final String    GTX_ATTR_ENTRANCE_Z        = "Z";
    public static final String    GTX_ATTR_ENTRANCE_OBS      = "Comments";
    public static final String GTX_KEY_SECTION_RESEAUX     = "Networks";
    public static final String    GTX_KEY_RESEAU            = "Network";
    public static final String       GTX_ATTR_RESEAU_IDX     = "Numero";
    public static final String       GTX_ATTR_RESEAU_COLOR_R   = "ColorR";
    public static final String       GTX_ATTR_RESEAU_COLOR_G   = "ColorG";
    public static final String       GTX_ATTR_RESEAU_COLOR_B   = "ColorB";
    public static final String       GTX_ATTR_RESEAU_TYPE    = "Type";
    public static final String       GTX_ATTR_RESEAU_NAME    = "Name";
    public static final String       GTX_ATTR_RESEAU_OBS     = "Comments";

    public static final String GTX_KEY_SECTION_SECTEURS    = "Secteurs";  //IdxSecteur,  NomSecteur, CouleurSecteur
    public static final String    GTX_KEY_SECTEUR            = "Secteur";
    public static final String       GTX_ATTR_SECTEUR_IDX     = "Numero";
    public static final String       GTX_ATTR_SECTEUR_NAME    = "Name";
    public static final String       GTX_ATTR_SECTEUR_COLOR_R   = "ColorR";
    public static final String       GTX_ATTR_SECTEUR_COLOR_G   = "ColorG";
    public static final String       GTX_ATTR_SECTEUR_COLOR_B   = "ColorB";

    public static final String   GTX_KEY_SECTION_CODES       = "Codes";
    public static final String     GTX_KEY_CODE              = "Code";
      // <Code PsiL="0.000" PsiP="0.000" Type="0" PsiAz="0.000" Numero="0" Comments="Item pour entrees et points fixes" FactLong="1.000" ClinoUnit="400.00" AngleLimite="0.00" CompassUnit="400.00"/>
    public static final String       GTX_ATTR_CODE_NUMERO    = "Numero";
    public static final String       GTX_ATTR_CODE_UCLINO    = "ClinoUnit";
    public static final String       GTX_ATTR_CODE_UCOMPASS  = "CompassUnit";
    public static final String       GTX_ATTR_CODE_FACT_LONG = "FactLong";
    public static final String       GTX_ATTR_CODE_TYPE      = "Type";
    public static final String       GTX_ATTR_CODE_ANGLIMITE = "AngleLimite";
    public static final String       GTX_ATTR_CODE_PSI_L     = "PsiL";
    public static final String       GTX_ATTR_CODE_PSI_A     = "PsiAz";
    public static final String       GTX_ATTR_CODE_PSI_P     = "PsiP";
    public static final String       GTX_ATTR_CODE_OBS       = "Comments";

    public static final String   GTX_KEY_SECTION_EXPES       = "Seances";
    public static final String      GTX_KEY_EXPE              = "Trip";
      //<Trip Date="0000-01-01" Color="0" Numero="0" Comments="Item pour entrees et points fixes" Surveyor1="" Surveyor2="" Declination="0.0000" Inclination="0.0000" ModeDeclination="0"/>

    public static final String      GTX_ATTR_EXPE_NUMERO    = "Numero";
    public static final String      GTX_ATTR_EXPE_DATE      = "Date";
    public static final String      GTX_ATTR_EXPE_IDXCOLOR  = "Color";
    public static final String      GTX_ATTR_EXPE_SURVEY1   = "Surveyor1";
    public static final String      GTX_ATTR_EXPE_SURVEY2   = "Surveyor2";
    public static final String      GTX_ATTR_EXPE_DECLINAT  = "Declination";
    public static final String      GTX_ATTR_EXPE_INCLINAT  = "Inclination";
    public static final String      GTX_ATTR_EXPE_MODEDECL  = "ModeDeclination";
    public static final String      GTX_ATTR_EXPE_OBS       = "Comments";

    public static final String   GTX_KEY_SECTION_SERIES      = "Series";
    public static final String      GTX_KEY_SERIE             = "Serie";
      // <Serie ="Galerie  vers 1/9" Color="#00000000" ="9" ="7" ="3" Numero="4" ="1" ="1" ="0" ="0" ="">
    public static final String      GTX_ATTR_SERIE_Numero   = "Numero";
    public static final String      GTX_ATTR_SERIE_NAME     = "Name";
    public static final String      GTX_ATTR_SERIE_SERDEP   = "SerDep";
    public static final String      GTX_ATTR_SERIE_PTDEP    = "PtDep";
    public static final String      GTX_ATTR_SERIE_SERARR   = "SerArr";
    public static final String      GTX_ATTR_SERIE_PTARR    = "PtArr";
    public static final String      GTX_ATTR_SERIE_RESEAU   = "Network";
    public static final String      GTX_ATTR_SERIE_OBSTACLE = "Obstacle";
    public static final String      GTX_ATTR_SERIE_CHANCE   = "Chance";
    public static final String      GTX_ATTR_SERIE_RAIDEUR  = "Raideur";
    public static final String      GTX_ATTR_SERIE_OBS      = "Commments";
    public static final String      GTX_ATTR_SERIE_COLOR    = "Color";
    public static final String      GTX_KEY_STATIONS        = "Stations";
        // <Shot Az="398.00" ID="3.5" Up="2.00" Code="2" Down="1.00" Expe="1" Incl="1.00" Left="1.00" Label="" Right="1.00" Length="10.000" Comments="" TypeShot="0"/>
    public static final String      GTX_KEY_VISEE         = "Shot";
    public static final String         GTX_ATTR_VISEE_ID   = "ID";
    public static final String         GTX_ATTR_VISEE_LBL  = "Label";
    public static final String         GTX_ATTR_VISEE_SECTEUR = GTX_KEY_SECTEUR;
    public static final String         GTX_ATTR_VISEE_TYPE = "TypeShot";
    public static final String         GTX_ATTR_VISEE_CODE = GTX_KEY_CODE;
    public static final String         GTX_ATTR_VISEE_EXPE = GTX_KEY_EXPE;
    public static final String         GTX_ATTR_VISEE_LONG = "Length";
    public static final String         GTX_ATTR_VISEE_AZ   = "Az";
    public static final String         GTX_ATTR_VISEE_P    = "Incl";
    public static final String         GTX_ATTR_VISEE_LG   = "Left";
    public static final String         GTX_ATTR_VISEE_LD   = "Right";
    public static final String         GTX_ATTR_VISEE_HZ   = "Up";
    public static final String         GTX_ATTR_VISEE_HN   = "Down";
    public static final String         GTX_ATTR_VISEE_OBS  = "Comments";

    public static final String   GTX_KEY_SECTION_ANTENNAS    = "AntennaShots";
    public static final String     GTX_KEY_ANTENNA_SHOT      = "AntennaShot";
    public static final String       GTX_KEY_ANTENNA_NUMERO    = "Numero";
    public static final String       GTX_KEY_ANTENNA_NETWORK   = "Network";
    public static final String       GTX_KEY_ANTENNA_LABEL     = "Label";
    public static final String       GTX_KEY_ANTENNA_SECTEUR   = GTX_KEY_SECTEUR;
    public static final String       GTX_KEY_ANTENNA_SERIE     = "SerDep";
    public static final String       GTX_KEY_ANTENNA_POINT     = "PtDep";
    public static final String       GTX_KEY_ANTENNA_CODE      = GTX_KEY_CODE;
    public static final String       GTX_KEY_ANTENNA_TRIP      = GTX_KEY_EXPE;
    public static final String       GTX_KEY_ANTENNA_LONG      = "Length";
    public static final String       GTX_KEY_ANTENNA_AZIMUT    = "Az";
    public static final String       GTX_KEY_ANTENNA_PENTE     = "Incl";
    public static final String       GTX_KEY_ANTENNA_OBS       = "Comments";
    //<AntennaShot ="0.00" Code="1" ="1" ="0.00" ="" ="0" ="0.001" ="0" ="1" ="0" =""/>      
}
