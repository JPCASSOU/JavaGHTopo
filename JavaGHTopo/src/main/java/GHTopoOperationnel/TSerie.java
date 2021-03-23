package GHTopoOperationnel;

import GHTopoOperationnel.Types.TStation;
import java.util.ArrayList;

/**
 * Created by jean-pierre.cassou on 16/05/14.
 */
public class TSerie extends ArrayList<TStation>{
    public int     IdxSerie = 0;
    public String  NomSerie = "";
    public String  ObsSerie = "";
    public int     Reseau   = 0;
    public int     SerDep   = 0;
    public int     PtDep    = 0;
    public int     SerArr   = 0;
    public int     PtArr    = 0;
    public int     Chance   = 0;
    public int     Obstacle = 0;
    public double  Raideur  = 1.00;
    public TSerie()
    {
        this.clearVisees();
        //this.AddStationByValues("", 0, 0, 0, 0, 0.001, 0.00, 0.00, 0.0, 0.0, 0.0, 0.0, "Added");
    }
    public void clearVisees()
    {
        this.clear();
    }         
    public void setIndexSerie(int Idx)
    {
        this.IdxSerie = Idx;
    }
    public void setNomSerie(String S)
    {
        this.NomSerie = S;
    }
    public void setObsSerie(String S)
    {
        this.ObsSerie = S;
    }
    public void setNoReseau(int R)
    {
        this.Reseau = R;
    }
    public void setRaideur(double R)
    {
        this.Raideur = R;
    }
    public void setChanceObstacle(int qChance, int qObstacle)
    {
        this.Chance   = qChance;
        this.Obstacle = qObstacle;
    }
    public void setStationsExtremites(int SD, int PD, int SA, int PA)
    {
        this.SerDep = SD;
        this.PtDep  = PD;
        this.SerArr = SA;
        this.PtArr  = PA;
    }
    public void addStation(TStation S)
    {
        this.add(S);
    }
    public void addStationByValues(int qIDStation, String qIDTerrain,
                                   int qSecteur, int qTypeGalerie,
                                   int qCode, int qExpe,
                                   double qLong, double qAzimut, double qPente,
                                   double qLG, double qLD, double qHZ, double qHN,
                                   String qCommentaires)
    {
        TStation EWE = new TStation();
        EWE.IDStation     = qIDStation;
        EWE.IDTerrain     = qIDTerrain;
        EWE.Secteur       = qSecteur;
        EWE.TypeVisee     = qTypeGalerie;
        EWE.Code          = qCode;
        EWE.Expe          = qExpe;
        EWE.Longueur      = qLong;
        EWE.Azimut        = qAzimut;
        EWE.Pente         = qPente;
        EWE.LG            = qLG;
        EWE.LD            = qLD;
        EWE.HZ            = qHZ;
        EWE.HN            = qHN;
        EWE.Commentaires  = qCommentaires;
        this.add(EWE);

    }
    public int getNbStations()
    {
        return this.size();
    }
    public TStation getStation(int Idx)
    {
        return this.get(Idx);
    }

}
