
package GHTopoOperationnel;
import java.awt.*;
import java.util.ArrayList;

import GHTopoOperationnel.Types.TStation;

public class TBranche extends ArrayList<TStation>{
    public int    NoSerie;
    public int    NoReseau;
    public int    NoeudDepart;
    public int    NoeudArrivee;
    public double Rigidite;
    public double LongDev;
    public double DeltaX;
    public double DeltaY;
    public double DeltaZ;
    public double XDepart;
    public double YDepart;
    public double ZDepart;
    public double XArrivee;
    public double YArrivee;
    public double ZArrivee;
    //*********************************************
    public void addStation(TStation S)
    {
        this.add(S);
    }
    String IDLitteral;
    int Secteur;
    int TypeGalerie;
    int Code;
    int Expe;
    double Longueur;
    double Azimut;
    double Pente;
    double LG;
    double LD;
    double HZ;
    double HN;
    String Commentaires;
    public void addStationByValeurs(int qIDStation, String qIDTerrain,
                                    int qSecteur, int qTypeVisee,
                                    int qCode, int qExpe,
                                    double qLongueur, double qAzimut, double qPente,
                                    double qLG, double qLD, double qHZ, double qHN,
                                    String qCommentaires)
    {
        TStation WU = new TStation();
        WU.IDStation = qIDStation;
        WU.IDTerrain = qIDTerrain;
        WU.Secteur   = qSecteur;
        WU.Code      = qCode;
        WU.Expe      = qExpe;
        WU.TypeVisee = qTypeVisee;
        WU.Longueur  = qLongueur;
        WU.Azimut    = qAzimut;
        WU.Pente     = qPente;
        WU.LD = qLD;
        WU.LG = qLG;
        WU.HZ = qHZ;
        WU.HN = qHN;
        WU.Commentaires = qCommentaires;
        this.add(WU);
    }
    public TStation getStation(int Idx)
    {
        return this.get(Idx);
    }
    public int getNbStations()
    {
        return this.size();
    }
    public void putStation(int Idx, TStation S)
    {
        this.set(Idx, S);
    }
    //*********************************************
    public void TBranche()
    {
        this.clear();
    }

}
