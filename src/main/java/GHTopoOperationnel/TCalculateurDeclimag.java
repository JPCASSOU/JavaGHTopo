// Wrapper pour Orekit (déclinaison magnétique)
package GHTopoOperationnel;

import static GHTopoOperationnel.GeneralFunctions.afficherMessageFmt;
import java.io.File;
import org.orekit.data.DataProvider;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.models.earth.GeoMagneticElements;
import org.orekit.models.earth.GeoMagneticField;
import org.orekit.models.earth.GeoMagneticFieldFactory;
public class TCalculateurDeclimag 
{
    /**
     * Retourne la déclinaison magnétique en degrés
     * @param lon double longitude (deg)
     * @param lat double latitude (deg)
     * @param alt double altitude (m)
     * @param YYYY int année 4 chiffres > 1900 
     * @param MM   int mois (1-12)
     * @param DD   int jour (1-31)
     * @return double déclinaison en degrés
     */
    public double CalcDeclimag(double lon, double lat, double alt, int YYYY, int MM, int DD)
    {
        try
        {    
            // indétermination pour les dates 01/01/XXX5
            // en attente de la correction du bug signalé à Orekit
            // Solution provisoire: ajouter un jour.
            int reste = (YYYY % 5);
            if ((DD == 1) && (MM == 1) && (reste == 0)) DD += 1;            
            double qyear = GeoMagneticField.getDecimalYear(DD, MM, YYYY);            
            //afficherMessageFmt("decimalYear = %.6f au %02d/%02d/%04d -- Reste %d", qyear, DD, MM, YYYY, reste);
            GeoMagneticField model = GeoMagneticFieldFactory.getIGRF(qyear);
            GeoMagneticElements result = model.calculateField(lat, lon, alt / 1000);
            return result.getDeclination();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -361.00;
        }   
    }        
    /**
     * Initialisation
     * @param DataPath String Chemin du dossier contenant les fichiers *.COF
     * @return boolean 
     */
    public boolean initialiser(String DataPath)
    {
        try
        {
            File orekitData = new File(DataPath);
            DataProvider provider = new DirectoryCrawler(orekitData);
            DataProvidersManager.getInstance().addProvider(provider); 
            return true;
        }   
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }       
    }        
            
    public TCalculateurDeclimag()
    {
        
    }        
}
