
package GHTopoOperationnel;

import static GHTopoOperationnel.GeneralFunctions.*;
import GHTopoOperationnel.Types.TPoint2Df;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

public class TConvertisseurCoordonnees {
    private ArrayList<CoordinateReferenceSystem> fListeCoordsSystems; 
    /**
     * Retourne le numéro de fuseau UTM depuis Lon/Lat
     * 
     * @param qLong
     * @param qLat
     * @return int Numéro de fuseau UTM
     */
    public int getNumeroFuseauUTM(double qLong, double qLat)
    {
        // fuseau 30 = de -6Â° Ã  0Â°
        final double EF = 6.0; // Ã©tendue du fuseau UTM
        
        for (int i = 0; i < 60; i++)
        {
            double Lo = -180 + i* EF; 
            double Ld = -180 + (i + 1) * EF;
            if (IsInRangeDbl(qLong, Lo, Ld)) return i + 1;
        }            
        return -1;        
    }  
    /**
     * Retourne le nombre de systèmes de coordonnées
     * 
     * @return int
     */
    public int getNbSystCoords()
    {
        return this.fListeCoordsSystems.size();
    }
    /**
     * Retourne le code EPSG du système de coordonnées
     * @param c CoordinateReferenceSystem
     * @return int
     */
    public int getCodeEPSG(CoordinateReferenceSystem c)
    {
        try
        {
            return CRS.lookupEpsgCode(c, true);
        }
        catch (Exception e)
        {
            return -1;
        }
    }  
    /**
     * Crée et ajoute un CRS depuis un code EPSG
     * 
     * @param codeEPSG int
     * @throws FactoryException 
     */
    public void addCoordSystemByEPSG(int codeEPSG) throws FactoryException
    {
        try
        {            
            CoordinateReferenceSystem EWE = CRS.decode(sprintf("EPSG:%d", codeEPSG), true);
            fListeCoordsSystems.add(EWE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }   
    /**
     * Retourne le système de coordonnées CRS depuis index de liste
     * @param idx int
     * @return CoordinateReferenceSystem
     */
    public CoordinateReferenceSystem getCoordSystem(int idx)
    {
        return fListeCoordsSystems.get(idx);
    }     
    /**
     * Retourne l'index de liste pour un code EPSG donné
     * @param codeEPSG int
     * @return 
     */
    public int getIndexOfEPSG(final int codeEPSG)
    {        
        int Nb = this.getNbSystCoords();
        if (Nb > 0)
        {    
            for (int i = 0; i < Nb; i++)
            {
                try
                {    
                    CoordinateReferenceSystem c = getCoordSystem(i);
                    Integer qEPSG = CRS.lookupEpsgCode(c, true);
                    if (qEPSG == codeEPSG) return i;
                }
                catch (Exception e)
                {
                    return -1;
                }    
            }    
        } 
        return -1;        
    }        
    /**
     * Retourne le système de coordonnées CRS depuis code EPSG
     * @param codeEPSG int
     * @return CoordinateReferenceSystem
     */
    public CoordinateReferenceSystem getCoordSystemByCodeEPSG(final int codeEPSG)
    {
        int Nb = this.getNbSystCoords();
        if (Nb > 0)
        {    
            for (int i = 0; i < Nb; i++)
            {
                try
                {    
                    CoordinateReferenceSystem c = getCoordSystem(i);
                    Integer qEPSG = CRS.lookupEpsgCode(c, true);
                    if (qEPSG == codeEPSG) return c;
                }
                catch (Exception e)
                {
                    return null;
                }    
            }    
        }   
        return null;
    }
    /**
     * Calcul de conversion 
     * @param src   CoordinateReferenceSystem
     * @param dest  CoordinateReferenceSystem
     * @param X double (en mètres ou en degrés décimaux)
     * @param Y double
     * @return 
     */
    private TPoint2Df conversion(CoordinateReferenceSystem src, CoordinateReferenceSystem dest, double X, double Y) 
    {
        TPoint2Df result = makeTPoint2Df(0.0, 0.0);
        try
        {
            DirectPosition2D srcDirectPosition2D   = new DirectPosition2D(src, X, Y);
            DirectPosition2D destDirectPosition2D  = new DirectPosition2D();
            boolean b = true;
            MathTransform mathTransform = CRS.findMathTransform(src, dest, b);
            mathTransform.transform(srcDirectPosition2D, destDirectPosition2D);
            result = makeTPoint2Df(destDirectPosition2D.x, destDirectPosition2D.y);  
            
            return result;
        }   
        catch (Exception e)
        {
            e.printStackTrace();
        }    
        return result;
    }
    /**
     * Wrapper pour la fonction conversion()
     * @param idxsrc  int Index source
     * @param idxdest int Index cible
     * @param X double
     * @param Y double
     * @return TPoint2Df
     */
    public TPoint2Df conversionByIdx(int idxsrc, int idxdest, double X, double Y) 
    {
        if (idxsrc == idxdest)
        {
            return makeTPoint2Df(X, Y);
        }    
        else
        {
            CoordinateReferenceSystem src  = getCoordSystem(idxsrc);        
            CoordinateReferenceSystem dest = getCoordSystem(idxdest);
            return conversion(src, dest, X, Y);
        }
    } 
    /**
     * Wrapper pour la fonction conversion()
     * 
     * @param epsgSrc   int Code EPSG source
     * @param epsgDest  int Code EPSG cible
     * @param X double
     * @param Y double
     * @return TPoint2Df
     */
    public TPoint2Df conversionByCodeEPSG(int epsgSrc, int epsgDest, double X, double Y) 
    {
        if (epsgSrc == epsgDest)
        {
            return makeTPoint2Df(X, Y);
        }    
        else
        {
            CoordinateReferenceSystem src  = getCoordSystemByCodeEPSG(epsgSrc);
            CoordinateReferenceSystem dest = getCoordSystemByCodeEPSG(epsgDest);
            return conversion(src, dest, X, Y);
        }    
    }   
    /**
     *  Initialisation + bac à sable
     */
    public boolean initialiser()
    {        
        try
        {
            // on initialise d'emblÃ©e avec WGS84 et LT93
            this.addCoordSystemByEPSG(4326);
            this.addCoordSystemByEPSG(2154);
            // on passe aux Lambert France (obsolÃ¨te, format de la forme 403460, 89600)
            for (int i = 1; i <= 4; i++) this.addCoordSystemByEPSG(27560 + i);
            // on passe aux Lambert France (zones I Ã  IV, format de la forme 403460, 3089600)
            for (int i = 1; i <= 4; i++) this.addCoordSystemByEPSG(27570 + i);
            // France 9 zones
            for (int i = 2; i <= 10; i++) this.addCoordSystemByEPSG(3940 + i);
            // DOM TOM
            addCoordSystemByEPSG(4559); // Antilles;
            addCoordSystemByEPSG(2975); // RÃ©union;
            addCoordSystemByEPSG(2972); // Guyanne;
            addCoordSystemByEPSG(4471); // Mayotte;
            addCoordSystemByEPSG(4467); // Saint-Pierre on Nique Long;
            // UTM Hemisphere nord
            for (int i = 1; i <= 60; i++) this.addCoordSystemByEPSG(32601 + i);
            // UTM Hemisphere sud
            for (int i = 1; i <= 60; i++) this.addCoordSystemByEPSG(32701 + i);
            // International 
            // Belgique            
            addCoordSystemByEPSG(31300);
            addCoordSystemByEPSG(31370);
            // Autriche
            addCoordSystemByEPSG(31297);
            // Suisse (Toporobot)
            addCoordSystemByEPSG(4151);
            addCoordSystemByEPSG(4343);
            addCoordSystemByEPSG(4344);
            addCoordSystemByEPSG(4932);
            addCoordSystemByEPSG(4933);
            addCoordSystemByEPSG(61516405);
            addCoordSystemByEPSG(61516413);
            // Google
            //addCoordSystemByEPSG(900913);

            afficherMessage("nouvelle méthode:");
            CoordinateReferenceSystem c1 = getCoordSystem(0);
            CoordinateReferenceSystem c2 = getCoordSystem(1);
            TPoint2Df p0 = makeTPoint2Df(0.066, 48.235);
            TPoint2Df p1 = conversion(c1, c2, p0.X, p0.Y);
            /*
            afficherMessageFmt("De %s %.8f, %.8f vers %s %.2f, %.2f",
                               c1.getName(),
                               p0.X, p0.Y, 
                               c2.getName(),
                               p1.X, p1.Y);
            //*/
            // test de la fonction getfuseauUTM
            double Shuanghe_Lon = 107.00;
            double Shuanghe_Lat = 28.25;
            afficherMessageFmt("Fuseau UTM pour lon = %.8f: %d", Shuanghe_Lon, getNumeroFuseauUTM(Shuanghe_Lon, Shuanghe_Lat));
            return true;            
        } 
        catch (Exception e)
        {
            e.printStackTrace();            
            return false;
        }
    }        
    public TConvertisseurCoordonnees()
    {
        fListeCoordsSystems = new ArrayList<CoordinateReferenceSystem>();
        fListeCoordsSystems.clear();
    }            
}

//------------------------------------------------------------------------------
/*
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <proxies>
    <proxy>
      <id>myproxy</id>
      <active>true</active>
      <protocol>http</protocol>
      <host>direct1.proxy.i2</host>
      <port>8080</port>
      
    </proxy>
  </proxies>
  <servers>
    <server>
        <id>ghtopo-repository</id>
        <username>jpcassou</username>
        <password>JPCass0u</password>
    </server>
  </servers>
  <mirrors>
    <mirror>
        <id>ghtopo-repository</id>
        <url>https://www.si4g.fr/nexus/content/groups/ghtopo</url>
        <mirrorOf>*</mirrorOf>
    </mirror>
  </mirrors>
</settings>

//----------------------------------------------------------------------------*/