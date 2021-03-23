package GHTopoOperationnel;
import static GHTopoOperationnel.GeneralFunctions.*;
import GHTopoOperationnel.Types.TPoint2Df;
import GHTopoOperationnel.Types.TPtsSectionTransversale;
import java.awt.Color;
import java.util.ArrayList;
// Validé.
public class TSilhouetteGalerie
{
    private ArrayList<TPtsSectionTransversale> FArrayPtsSectTransversale; 
    private ArrayList<TPoint2Df> FArrayPolygone;
    private Color fFillColor;
    private Color fLineColor;
    public void addPtsParois(TPtsSectionTransversale E)
    {
        FArrayPtsSectTransversale.add(E);        
    }   
    public TPtsSectionTransversale getPtsParois(int idx)
    {
        return this.FArrayPtsSectTransversale.get(idx);
    }        
    public int getNbPtsParois()
    {
        return FArrayPtsSectTransversale.size();
    }        
    public TPoint2Df getPointPolygoneParois(int idx)
    {
        return FArrayPolygone.get(idx);
    }        
    public int getNbPointsPolygoneParois()
    {
        return FArrayPolygone.size();
    } 
    public void addPointPolygoneParois(TPoint2Df pt)
    {
        FArrayPolygone.add(pt);
    }        
    
    
    public int buildPolygoneParois()
    {
        
        int result = 0;
        FArrayPolygone.clear();
        try
        {
            int Nb = getNbPtsParois();
            //afficherMessageFmt("-- buildPolygoneParois: %d noeuds", Nb);
            // paroi de droite
            for (int i = 0; i < Nb; i++)
            {    
                TPtsSectionTransversale WU = this.getPtsParois(i);
                addPointPolygoneParois(makeTPoint2Df(WU.ParoiDroiteX, WU.ParoiDroiteY));
            }    
            // paroi de gauche
            for (int i = Nb - 1; i >= 0; i--)
            {    
                TPtsSectionTransversale WU = this.getPtsParois(i);
                addPointPolygoneParois(makeTPoint2Df(WU.ParoiGaucheX, WU.ParoiGaucheY));                
            }  
            result = getNbPointsPolygoneParois(); 
        }   
        catch (Exception e)
        {
            e.printStackTrace();             
        }     
        return result;
    }   
    // obtenir couleur remplissage
    public Color getFillColor()
    {
        return fFillColor;
    }
    public Color getLineColor()
    {
        return fLineColor;
    }
    // vider la silhouette
    public void clearSilhouette(Color qLineColor, Color qFillColor)
    {
       FArrayPtsSectTransversale.clear();
       FArrayPolygone.clear();
       fLineColor = qLineColor;
       fFillColor = qFillColor;
    }        
    
    // constructeur
    public TSilhouetteGalerie()
    {
        FArrayPtsSectTransversale = new ArrayList<TPtsSectionTransversale>();
        FArrayPolygone = new ArrayList<TPoint2Df>();
        FArrayPolygone.clear();
        FArrayPtsSectTransversale.clear();
        fLineColor = Color.BLUE;
        fFillColor = Color.BLUE;
    }            
}