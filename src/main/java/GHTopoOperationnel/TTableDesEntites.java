package GHTopoOperationnel;


import static GHTopoOperationnel.Constantes.*;

import static GHTopoOperationnel.GeneralFunctions.*;    // import des fonctions générales
import static GHTopoOperationnel.MetaFiltreFunctions.*; // fonctions de MétaFiltre
import GHTopoOperationnel.TPalette256;
import GHTopoOperationnel.Types.TCode;
import GHTopoOperationnel.Types.TEntiteEtendue;
import GHTopoOperationnel.Types.TEntrance;
import GHTopoOperationnel.Types.TExpe;
import GHTopoOperationnel.Types.TFiltre;
import GHTopoOperationnel.Types.TPoint2Df;
import GHTopoOperationnel.Types.TPoint3Df;
import GHTopoOperationnel.Types.TPtsSectionTransversale;
import GHTopoOperationnel.Types.TReseau;
import GHTopoOperationnel.Types.TSecteur;
import GHTopoOperationnel.Types.TVentilationSpeleometrie;
import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
/**
 * Created by jean-pierre.cassou on 22/05/14.
 *///extends  ArrayList<TEntiteEtendue>
public class TTableDesEntites
{
    private ResourceBundle fMyResourceBundle = null;
    // le tri ne fonctionne pas sur les ArrayList. 
    // le plus simple est de créer un List<TEntiteEtendue>
    // à initialiser en myListeDesEntites = new ArrayList<TEntiteEtendue>
    private List<TEntiteEtendue> myListeDesEntites;
    private TPoint3Df myCoinBasGauche;
    private TPoint3Df myCoinHautDroit;
    private TPalette256 myPalette256;
    
    
    //--------------------------------------------------------------------------
    // listes internes:
    private ArrayList<TReseau>  fListeReseaux; // réseaux
    // liste des entrées 
    private ArrayList<TEntrance> fListeEntrances;
    private ArrayList<TSecteur> fListeSecteurs; // secteurs
    private ArrayList<TCode>    fListeCodes; // codes
    private ArrayList<TExpe>    fListeExpes; // expés
    // liste des dates
    private ArrayList<Integer> fListeDesDates;
   
    //--------------------------------------------------------------------------
    // statistiques du réseau
    private ArrayList<TVentilationSpeleometrie> fSpeleometrieParReseaux;
    private ArrayList<TVentilationSpeleometrie> fSpeleometrieParSecteurs;
    private ArrayList<TVentilationSpeleometrie> fSpeleometrieParExpes;
    private ArrayList<TVentilationSpeleometrie> fSpeleometrieParCodes;
    private ArrayList<TVentilationSpeleometrie> fSpeleometrieParDates;
    private TPoint3Df myPositionPointZero;
    //--------------------------------------------------------------------------
    // pour les diagrammes
    private double[] fClassRepartRoseDiagram;
    // couleur maxi et mini
    private Color fCouleurMiniZ = Color.GREEN;
    private Color fCouleurMaxiZ = Color.BLUE;
    public void calcCouleursByDepth()
    {        
        int Nb = this.getNbEntites();
        afficherMessageFmt("calcCouleursByDepth(%d)", Nb);
        for (int i = 0; i < Nb; i++)
        {
            TEntiteEtendue E = this.getEntite(i);
            E.ColourByDepth = getColorDegrade(E.UneStation2Z, myCoinBasGauche.Z, myCoinHautDroit.Z, fCouleurMiniZ, fCouleurMaxiZ);
            putEntite(i, E);
        }    
    }
    // réinitialiser
    public void initialiserTableEntites()
    {
        myListeDesEntites.clear();
        fListeEntrances.clear();
        fListeReseaux.clear();
        fListeSecteurs.clear();
        fListeCodes.clear();
        fListeExpes.clear();
        
        fListeDesDates.clear();
        
        fSpeleometrieParReseaux.clear();
        fSpeleometrieParSecteurs.clear();
        fSpeleometrieParExpes.clear();
        fSpeleometrieParCodes.clear();
        fSpeleometrieParDates.clear();
        // calcul couleur mini et maxi        
    } 
    public void listerTablesReseauxSecteursCodesExpes()
    {
        afficherMessage("===========================");
        int Nb = 0;
        Nb = getNbReseaux();
        afficherMessageFmt("Liste des %d réseaux", Nb);
        Nb = getNbSecteurs();
        afficherMessageFmt("Liste des %d secteurs", Nb);
        Nb = getNbCodes();
        afficherMessageFmt("Liste des %d codes", Nb);
        Nb = getNbExpes();
        afficherMessageFmt("Liste des %d expés", Nb);
    }     
    // Les entrées
    public int getNbEntrances()
    {
        return fListeEntrances.size();
    }       
    public TEntrance getEntrance(int Idx)
    {
        return this.fListeEntrances.get(Idx);
    }        
    public void addEntrance(TEntrance E)
    {
        this.fListeEntrances.add(E);
    }        
    // Les réseaux
    public int getNbReseaux()
    {
        return this.fListeReseaux.size();
    }
    public void addReseau(TReseau MyReseau)
    {
        this.fListeReseaux.add(MyReseau);
    }
    public TReseau getReseau(int Idx)
    {
        return this.fListeReseaux.get(Idx);
    }
    // Les secteurs
    public int getNbSecteurs()
    {
        return this.fListeSecteurs.size();
    }
    public void addSecteur(TSecteur MySecteur)
    {
        this.fListeSecteurs.add(MySecteur);
    }
   
    public TSecteur getSecteur(int Idx)
    {
        return this.fListeSecteurs.get(Idx);
    }
    // Les Codes
    public int getNbCodes()
    {
        return this.fListeCodes.size();
    }
    public void addCode(TCode MyCode)
    {
        this.fListeCodes.add(MyCode);
    }
    public TCode getCode(int Idx)
    {
        return this.fListeCodes.get(Idx);
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
    // Les Expes
    public int getNbExpes()
    {
        return this.fListeExpes.size();
    }
    public void addExpe(TExpe MyExpe)
    {
        this.fListeExpes.add(MyExpe);
    }
    
    public TExpe getExpe(int Idx)
    {
        return this.fListeExpes.get(Idx);
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
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    // MétaFiltre
    private int setEntiteVisibleByIdx(int idx, boolean b)
    {
        TEntiteEtendue E = getEntite(idx);
        E.Drawn = b;
        putEntite(idx, E);
        return (b) ? 1 : 0;
    }
    private int setAllVisible(boolean b)
    {
        int result = 0;
        int nb = getNbEntites();
        for (int i = 1; i < nb; i++) result += setEntiteVisibleByIdx(i, b);
        return result;
    }        
    public int applyMetaFiltre(String myFiltre)
    {
        int result = 0;
        // filtre vide = afficher tout
        String qFiltre = myFiltre.trim().toUpperCase();
        if (qFiltre.equals("")) return setAllVisible(true);
        // évaluation paresseuse: si on trouve "TOUT" ou "RIEN", on SetAllVisible et on sort
        
        if (qFiltre.contains(getResourceByKey("rsMETAFILTRE_KWD_0"))) return setAllVisible(false);
        if (qFiltre.contains(getResourceByKey("rsMETAFILTRE_KWD_1"))) return setAllVisible(true);
        afficherMessageFmt(": applyMetaFiltre: %s", qFiltre);
        //**********************************************************************
        try
        {    
            ArrayList<TFiltre> lf = new ArrayList<>();
            lf = recenserFiltres(myFiltre);
            int nb = lf.size();
            afficherMessageFmt("%d filtres", nb);
            for (int i = 0; i < nb; i++)
            {
                TFiltre F = lf.get(i);
                afficherMessageFmt("-- Filtre: %d = %s - Idx: %d, Type: %d, Conn: %d - Op: %d - V = %s B1 = %s - B2 = %s", i, F.Caption, F.Filter, F.TypeFiltre, F.ConnectedWithPrevious, F.Operateur, F.Valeur ,F.BorneInf, F.BorneSup);
            } 
            result = setAllVisible(false);
            // application des filtres
            int NbViseesRetenues = 0;
            double DevelVisees   = 0.00;
            int NbEntites = getNbEntites();
            for (int i = 1; i < NbEntites; i++)
            {
                TEntiteEtendue EE = getEntite(i);
                EE.Drawn = true;
                int nbFiltres = lf.size();
                for (int f = 0; f < nbFiltres; f++)
                {
                    TFiltre FF = lf.get(f);
                    boolean dd = DoDrawVisee(FF, EE);
                    //afficherMessageFmt("%d: Filtre: %s - Dessiné: %s", i, FF.Caption, (dd) ? "Oui": "Non");
                    switch (FF.ConnectedWithPrevious)
                    {
                        case ftERROR:
                            EE.Drawn = EE.Drawn || DoDrawVisee(FF, EE);
                            //afficherMessageFmt("%d: Filtre: %s - Dessiné: %s", i, FF.Caption, (EE.Drawn) ? "Oui": "Non");
                            break;
                        case ftOR:
                            EE.Drawn = EE.Drawn || DoDrawVisee(FF, EE);
                            break;
                        case ftAND:
                            EE.Drawn = EE.Drawn && DoDrawVisee(FF, EE);
                            break;
                        default:
                            EE.Drawn = EE.Drawn || DoDrawVisee(FF, EE);
                            break;
                    } 
                    if (FF.Basculed) EE.Drawn = (! EE.Drawn);                    
                }   
                if (EE.Drawn) 
                {
                    NbViseesRetenues++;
                    double LV = hypot3D(EE.UneStation2X - EE.UneStation1X,
                                        EE.UneStation2Y - EE.UneStation1Y,
                                        EE.UneStation2Z - EE.UneStation1Z);
                    DevelVisees += LV;
                }
                putEntite(i, EE);
            } // for (int i = 1; i < NbEntites; i++)
            return NbViseesRetenues;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return -1;
        }   
    }  
    
    //--------------------------------------------------------------------------
    
    //--------------------------------------------------------------------------
    public TPoint3Df getCoinBasGauche()
    {
        return myCoinBasGauche;        
    }      
    public TPoint3Df getCoinHautDroit()
    {
        return myCoinHautDroit;
    }      
    //--------------------------------------------------------------------------
    public void addEntite(TEntiteEtendue EE)
    {
        this.myListeDesEntites.add(EE);
    }
    public TEntiteEtendue getEntite(int idx)
    {
        return this.myListeDesEntites.get(idx);
    }
    public TEntiteEtendue getEntiteFromSerSt(int ser, int st)
    {
        int nbEnt = this.getNbEntites();
        TEntiteEtendue result = this.getEntite(0);
        for (int i = 1; i < nbEnt; i++)
        {
            result = this.getEntite(i);
            if ((result.EntiteSerie == ser) && (result.EntiteStation == st))
            {
                break;
            }
        }
        return result;
    }
    public void putEntite(int idx, TEntiteEtendue E)
    {
        this.myListeDesEntites.set(idx, E);
    }        
    public int getNbEntites()
    {
        return this.myListeDesEntites.size();
    }
    public int setColorVisee(int c)
    {
        return 0;
    }
    // lister les dates
    public int getDate(int idx)
    {
        return fListeDesDates.get(idx);
    }
    public int getNbDates()
    {
        return fListeDesDates.size();
    }        
    // tri
    private void triLesDates()
    {        
        Collections.sort(this.fListeDesDates, new Comparator<Integer>() 
        {
            @Override
            public int compare(Integer p1,  Integer p2) 
            {
                if      (p1 > p2) return  1;
                else if (p1 < p2) return -1;
                else              return  0;
            }    
        });    
    }
    public void recenserLesDates()
    {
        int nb = this.getNbEntites();
        afficherMessage("");
        afficherMessageFmt("Recensement des dates pour %d entités", nb);
        fListeDesDates.clear();
        for (int i = 1; i < nb; i++)
        {
            TEntiteEtendue EWE = getEntite(i);
            if (! fListeDesDates.contains(EWE.DateLeve)) fListeDesDates.add(EWE.DateLeve);
        }   
        // tri
        triLesDates();
        // contrôle
        nb = fListeDesDates.size();
        afficherMessageFmt("Liste des %d dates", nb);
        for (int i = 0; i < nb; i++)
        {
            Integer WU = getDate(i);
            afficherMessageFmt("-- %d = %d", i, WU);
        }
    }        
    // lister les entitées
    public void listerLesEntites()
    {
        int nb = this.getNbEntites();
        afficherMessage("");
        afficherMessageFmt("Liste des %d entités", nb);
        for (int i = 1; i < nb; i++) {
            TEntiteEtendue qEntite = this.getEntite(i);
            afficherMessage(sprintf("%d; %d.%d;%s; %.2f; %.2f; %.2f;; %.2f; %.2f;; %.2f; %.2f; %d",
                    i, qEntite.EntiteSerie, qEntite.EntiteStation, qEntite.oIDLitteral,
                    qEntite.UneStation2X, qEntite.UneStation2Y, qEntite.UneStation2Z,
                    qEntite.X2PG, qEntite.Y2PG, qEntite.X2PD, qEntite.Y2PD, qEntite.TypeEntite
            ));
        }
    }
    // mini et maxi
    public void setMiniMaxi()
    {
        this.myCoinBasGauche = makeTPoint3Df( 1E24,  1E24,  1E24);
        this.myCoinHautDroit = makeTPoint3Df(-1E24, -1E24, -1E24);
        for (int i = 1; i < this.getNbEntites(); i++)
        {
            TEntiteEtendue E = this.getEntite(i);
            if (E.UneStation2X < this.myCoinBasGauche.X) this.myCoinBasGauche.X = E.UneStation2X;
            if (E.UneStation2Y < this.myCoinBasGauche.Y) this.myCoinBasGauche.Y = E.UneStation2Y;
            if (E.UneStation2Z < this.myCoinBasGauche.Z) this.myCoinBasGauche.Z = E.UneStation2Z;
            
            if (E.UneStation2X > this.myCoinHautDroit.X) this.myCoinHautDroit.X = E.UneStation2X;
            if (E.UneStation2Y > this.myCoinHautDroit.Y) this.myCoinHautDroit.Y = E.UneStation2Y;
            if (E.UneStation2Z > this.myCoinHautDroit.Z) this.myCoinHautDroit.Z = E.UneStation2Z;
        }
        afficherMessage(sprintf("Mini: %.2f, %.2f, %.2f - Maxi = %.2f, %.2f, %.2f",
                                      this.myCoinBasGauche.X, this.myCoinBasGauche.Y, this.myCoinBasGauche.Z,
                                      this.myCoinHautDroit.X, this.myCoinHautDroit.Y, this.myCoinHautDroit.Z));
    }  
    // Entité la plus proche de la souris
    public int getIdxEntiteNearToPoint(TPoint2Df P)
    {
        //afficherMessageFmt("getIdxEntiteNearToPoint: %.2f, %.2f", P.X, P.Y);
        double r = 1E+36;
        int result = -1;
        int nb = this.getNbEntites();
        for (int i = 1; i < nb; i++)
        {
            TEntiteEtendue EWE = this.getEntite(i);
            double dp = Math.pow((P.X - EWE.UneStation2X), 2) + Math.pow((P.Y - EWE.UneStation2Y), 2);
            if (dp < r)
            {
                r = dp;
                result = i;
            }            
        }    
        return result;
    }
    // trier selon profondeurs -- VALIDE !!
    public void trierParProfondeurs()
    {        
        Collections.sort(this.myListeDesEntites, new Comparator<TEntiteEtendue>() 
        {

            @Override
            public int compare(TEntiteEtendue p1,  TEntiteEtendue p2) 
            {
                if      (p1.UneStation2Z > p2.UneStation2Z) return  1;
                else if (p1.UneStation2Z < p2.UneStation2Z) return -1;
                else                                        return  0;
            }    
        });
        //*/        
    }
    // trier selon séries et stations
    public void trierParSeriesStations()
    {        
        Collections.sort(this.myListeDesEntites, new Comparator<TEntiteEtendue>() 
        {
            @Override
            public int compare(TEntiteEtendue p1,  TEntiteEtendue p2) 
            {
                
                int SP1 = 10000 * p1.EntiteSerie + p1.EntiteStation;
                int SP2 = 10000 * p2.EntiteSerie + p2.EntiteStation;
                if      (SP1 < SP2) return -1;
                else if (SP1 > SP2) return  1;
                else                return  0;                        
            }    
        });
        //*/        
    }   
    // couleur par expé
    public Color getCouleurEntiteRGBByExpe(TEntiteEtendue E)
    {
        TExpe EWE = this.getExpeByIndex(E.eExpe);
        try
        {
            return this.myPalette256.getColorByIdx(EWE.IdxColor);
        }    
        catch (Exception ex)
        {
            return Color.WHITE;
        }    
    }     
    // mode de colorisation
    public Color getColorViseeFromModeRepresentation(int QModeRepresentationGaleries, TEntiteEtendue E)
    {
        Color result = new Color(80,80,80);
        switch (QModeRepresentationGaleries)
        {
            case rgDEFAULT:
                break;
            case rgRESEAUX:
                TReseau R = getReseau(E.eReseau);
                result = new Color(R.CouleurReseauR, R.CouleurReseauG, R.CouleurReseauB);
                break;
            case rgSECTEURS:
                TSecteur S = getSecteur(E.eSecteur);
                result = new Color(S.CouleurSecteurR, S.CouleurSecteurG, S.CouleurSecteurB);
                break;
            case rgSEANCES:
                TExpe EWE = getExpeByIndex(E.eExpe);
                result = myPalette256.getColorByIdx(EWE.IdxColor);
                //afficherMessageFmt("getColorViseeFromModeRepresentation: Expe %d - Couleur %d = %d", E.eExpe, EWE.IdxColor, result.getRGB());                
                break;
            case rgGRAY:
                result = new Color(128, 128, 128); // niveaux de gris
                break;
            case rgDEPTH:
                result = E.ColourByDepth;
                break;
            default:
                result = new Color(128, 128, 128); // niveaux de gris                
        }    
        return result;
    }   
    //--------------------------------------------------------------------------
    // calcul de la spéléométrie
    private TVentilationSpeleometrie getEmptyVentilation(String c)
    {
        TVentilationSpeleometrie EWE = new TVentilationSpeleometrie();
        EWE.Caption    = c;
        EWE.Ennoyables = 0;
        EWE.Filons     = 0;
        EWE.Fossiles   = 0;
        EWE.Siphons    = 0;
        EWE.Speciaux   = 0;
        EWE.Vadoses    = 0;
        EWE.Tunnels    = 0;
        return EWE;
    }   
    private TVentilationSpeleometrie makeLigneRecap(TVentilationSpeleometrie QV, TEntiteEtendue EE)
    {
        TVentilationSpeleometrie result = QV;
        double dv = hypot3D(EE.UneStation2X - EE.UneStation1X,
                            EE.UneStation2Y - EE.UneStation1Y,
                            EE.UneStation2Z - EE.UneStation1Z);
        switch (EE.TypeEntite)
        {
            case tgDEFAULT:
                result.Fossiles   += dv;
                break;
            case tgENTRANCE:
                break;
            case tgFOSSILE:
                result.Fossiles   += dv;
                break;
            case tgVADOSE:
                result.Vadoses    += dv;
                break;
            case tgENNOYABLE:
                result.Ennoyables += dv;
                break;
            case tgSIPHON:
                result.Siphons    += dv;
                break;
            case tgFIXPOINT:    
                break;
            case tgSURFACE:
                result.Speciaux   += dv;
                break;     
            case tgTUNNEL:
                result.Tunnels    += dv;
                break;
            case tgMINE:
                result.Filons     += dv;
                break;    
            default:
                result.Fossiles   += dv;
                break;
        }   
        return result;
    }  
    private void ajouterLongueurVisee(ArrayList<TVentilationSpeleometrie> t, TEntiteEtendue QE, int idx)
    {
        TVentilationSpeleometrie v = t.get(idx); // on prend
        v = makeLigneRecap(v, QE);               // on traite   
        t.set(idx, v);                           // et on remet à sa place
    }        
    private void afficherTableauSpeleometrique(ArrayList<TVentilationSpeleometrie> t)
    {
        afficherMessage("afficherTableauSpeleometrique");
        afficherMessage("=============================");
        int nb = t.size();
        afficherMessage("idx; caption; Fossiles; Vadoses; Ennoyables; Siphons;;Tunnels; Mines;; Spéciaux");
        for (int i = 0; i < nb; i++)
        {
            TVentilationSpeleometrie v = t.get(i);
            afficherMessageFmt("%d;%s; %.2f; %.2f; %.2f; %.2f;; %.2f; %.2f; %.2f", 
                              i, v.Caption,
                              v.Fossiles, v.Vadoses, v.Ennoyables, v.Siphons,
                              v.Tunnels, v.Filons, v.Speciaux
                              );
        }    
        
    }        
    public void afficherLaSpeleometrie()
    {
        afficherMessage("afficherLaSpeleometrie");
        afficherTableauSpeleometrique(fSpeleometrieParReseaux);
        afficherTableauSpeleometrique(fSpeleometrieParSecteurs);
        afficherTableauSpeleometrique(fSpeleometrieParCodes);
        afficherTableauSpeleometrique(fSpeleometrieParExpes);
        afficherTableauSpeleometrique(fSpeleometrieParDates);
    }        
    public void calculerSpeleometrie()
    {
        afficherMessage("calculerSpeleometrie");
        // purge des tableaux
        this.fSpeleometrieParReseaux.clear();
        this.fSpeleometrieParReseaux.clear();
        this.fSpeleometrieParSecteurs.clear();
        this.fSpeleometrieParExpes.clear();
        this.fSpeleometrieParCodes.clear();
        this.fSpeleometrieParDates.clear();
        // préparer les tableaux
        int nb = 0;
        nb = getNbReseaux();
        for (int i = 0; i < nb; i++)
        {
            TReseau RR = getReseau(i);
            fSpeleometrieParReseaux.add(getEmptyVentilation(RR.NomReseau));
        }
        nb = getNbSecteurs();
        for (int i = 0; i < nb; i++) 
        {
            TSecteur SS = getSecteur(i);
            fSpeleometrieParSecteurs.add(getEmptyVentilation(SS.NomSecteur));
        }
        nb = getNbExpes();
        for (int i = 0; i < nb; i++) 
        {    
            TExpe EX = getExpe(i);
            fSpeleometrieParExpes.add(getEmptyVentilation(EX.Commentaire));
        }
        nb = getNbCodes();
        for (int i = 0; i < nb; i++) 
        {
            TCode C = getCode(i);
            fSpeleometrieParCodes.add(getEmptyVentilation(C.Commentaire));
        }
        nb = getNbDates();
        for (int i = 0; i < nb; i++) 
        {
            String EWE = makeFormattedDateFromEnsuredInt(getDate(i));
            fSpeleometrieParDates.add(getEmptyVentilation(EWE));
        }
        
        int NbEntites = getNbEntites();
        TEntiteEtendue EWE = new TEntiteEtendue();
        int nbExpes = this.getNbExpes();
        afficherMessageFmt("  Speleometrie par expes (%d expes)", nbExpes);
        
        try
        {    
            for (int i = 1; i < NbEntites; i++)
            {
                EWE = getEntite(i);
                for (int j = 0; j < nbExpes; j++)
                {
                    TExpe EE = getExpe(j);
                    if (EWE.eExpe == EE.IdxExpe) ajouterLongueurVisee(fSpeleometrieParExpes, EWE, j);
                } 
            }
            afficherMessage("Expes OK");
        }
        catch (Exception e)
        {
            afficherMessage("expes KO");
            e.printStackTrace();
        }
        // par dates
        int nbDates = this.getNbDates();
        afficherMessageFmt("  Speleometrie par dates (%d dates)", nbDates);
        try
        {    
            for (int i = 1; i < NbEntites; i++)
            {
                EWE = getEntite(i);
                for (int j = 0; j < nbDates; j++)
                {
                    Integer DT = getDate(j);
                    if (Objects.equals(DT, EWE.DateLeve)) ajouterLongueurVisee(fSpeleometrieParDates, EWE, j);                   
                } 
            }
            afficherMessage("Dates OK");
        }
        catch (Exception e)
        {
            afficherMessage("Dates KO");
            e.printStackTrace();
        }
        // par codes
        int nbCodes = this.getNbCodes();
        afficherMessageFmt("  Speleometrie par codes (%d codes)", nbCodes);
        try
        {    
            for (int i = 1; i < NbEntites; i++)
            {
                EWE = getEntite(i);
                for (int j = 0; j < nbCodes; j++)
                {
                    TCode C = getCode(j);
                    if (EWE.eCode == C.IdxCode) ajouterLongueurVisee(fSpeleometrieParCodes, EWE, j);
                } 
            }
            afficherMessage("Codes OK");
        }
        catch (Exception e)
        {
            afficherMessage("Codes KO");
            e.printStackTrace();
        }
        // par réseaux
        int nbReseaux = this.getNbReseaux();
        afficherMessageFmt("  Speleometrie par réseaux (%d réseaux)", nbReseaux);
        try
        {    
            for (int i = 1; i < NbEntites; i++)
            {
                EWE = getEntite(i);
                for (int j = 0; j < nbReseaux; j++)
                {
                    TReseau RS = getReseau(j);
                    if (EWE.eReseau == RS.IdxReseau) ajouterLongueurVisee(fSpeleometrieParReseaux, EWE, j);
                } 
            }
            afficherMessage("Réseaux OK");
        }
        catch (Exception e)
        {
            afficherMessage("Réseaux KO");
            e.printStackTrace();
        }
        // par secteurs
        int nbSecteurs = this.getNbSecteurs();
        afficherMessageFmt("  Speleometrie par secteurs (%d secteurs)", nbSecteurs);
        try
        {    
            for (int i = 1; i < NbEntites; i++)
            {
                EWE = getEntite(i);
                for (int j = 0; j < nbSecteurs; j++)
                {
                    TSecteur SC = getSecteur(j);
                    if (EWE.eSecteur == SC.IdxSecteur) ajouterLongueurVisee(fSpeleometrieParSecteurs, EWE, j);
                } 
            }
            afficherMessage("Secteurs OK");
        }
        catch (Exception e)
        {
            afficherMessage("Secteurs KO");
            e.printStackTrace();
        }
        // calcul de la spéléométrie totale
        afficherMessage("Speleometrie totale");
    }    
    //--------------------------------------------------------------------------
    // Point zéro
    public void setPositionPointZero()
    {
        try
        {    
            TEntiteEtendue EWE = getEntiteFromSerSt(1, 1);
            myPositionPointZero = makeTPoint3Df(EWE.UneStation2X, EWE.UneStation2Y, EWE.UneStation2Z);
        }
        catch (Exception e)
        {
            myPositionPointZero = makeTPoint3Df(0.00, 0.00, 0.00);
        }
    }        
    public TPoint3Df getPositionPointZero()
    {
        return myPositionPointZero;
    }        
    public TPoint3Df getDeltaXYZFromPositionSt0(TEntiteEtendue ST)
    {
        return makeTPoint3Df(ST.UneStation2X - myPositionPointZero.X,
                             ST.UneStation2Y - myPositionPointZero.Y,
                             ST.UneStation2Z - myPositionPointZero.Z );
    }

    //--------------------------------------------------------------------------
    //******************************************************************************
    // Exportations 
    // Nota: Adaptation simple de la version Pascal --> on n'utilise pas les outils XML
    // encodage du style
    private static void KML_WritePolygonStyle(FileWriter wrt, 
                                                String IDStyle,
                                                Color LineColor, 
                                                Color FillColor,
                                                double LineWidth)
            throws IOException
    {
        wrt.write(sprintf("   <Style id=\"%s\">\n", IDStyle)); 
        wrt.write(              "     <LineStyle>\n");
        wrt.write(sprintf("       %s\n", javaColorToKMLColor(LineColor)));
        wrt.write(sprintf("       <width>%.1f</width>\n", LineWidth));
        wrt.write(              "     </LineStyle>\n");
        wrt.write(              "     <PolyStyle>\n");
        wrt.write(sprintf("       %s", javaColorToKMLColor(FillColor)));
        wrt.write(              "     </PolyStyle>\n");
        wrt.write(              "   </Style>\n");
    }
    // données OSM
    private static void WriteOSMNodeRef(FileWriter wrt, long Idx) throws IOException// long = entier sur 128 couilles.
    {
        wrt.write( sprintf("      <nd ref=\"%d\" />\n", -Idx));
    }
    private static void WriteOSMNode(FileWriter wrt, long Idx, String Action, boolean Visible, 
                                     double QOSMLon, double QOSMLat) throws IOException
    {        
        wrt.write(sprintf("  <node id=\"%d\" action=\"%s\" visible=\"%s\" lat=\"%s\" lon=\"%s\" />\n",
                       -Idx,       // ajout ds OSM ==> IDX négatifs
                        Action,
                        Visible ? "true" : "false",
                        ensureConvertNumberToStr(QOSMLat), 
                        ensureConvertNumberToStr(QOSMLon)
                       ));
    }
    
    private static void WriteOSMTag(FileWriter wrt, String K, String V) throws IOException
    {        
        wrt.write(String.format("   <tag k=\"%s\" v=\"%s\" />\n", K, V));
    }
    // en-tête de fichiers et déf des styles
    private static void WriteHeader(FileWriter wrt, int Mode) throws IOException
    {
        int ii;
        Color QColor        = Color.RED; 
        Color QColorShadow  = Color.DARK_GRAY;
        TExpe MyExpe;
        Color WU;
        //QColor         := SetColorRGBA(CouleurDefaut, $FF);
        //QColorShadow := SetColorRGBA(clWhite, $FF);
        switch (Mode)
        {    
            case gisGHCAVEDRAW:  // GHCaveDraw; sans objet
                break;
            case gisKML: 
                wrt.write( FormatterVersionEncodageXML("1.0", "UTF-8"));
                wrt.write(String.format("<kml xmlns=\"%s\" xmlns:gx=\"%s\" xmlns:kml=\"%s\" xmlns:atom=\"%s\">\n",
                                        KML_OPENGIS_WEBSITE,
                                        KML_GOOGLE_KML_WEBSITE,
                                        KML_OPENGIS_WEBSITE,
                                        W3C_W3_WEBSITE
                          ));
                wrt.write( "  <Document>\n");
                // styles de polygones
                KML_WritePolygonStyle(wrt, STYLE_GALERIE_PAR_DEFAUT, QColor, QColor, 1.0);
                // dossier principal = la cavité
                wrt.write( "    <Folder>\n");
                wrt.write(sprintf("    <name>%s</name>\n", "MyCavite001"));
                wrt.write(sprintf("    <open>%d</open>\n", 1));
                break;
            case gisGPX: // GPX
                wrt.write( FormatterVersionEncodageXML("1.0", "ISO-8859-1")); //<?xml version="1.0" encoding="ISO-8859-1"?>"));
                wrt.write( String.format("<gpx version=\"%.1\" creator=\"%s\" xmlns:xsi=\"%s\" xmlns=\"%s\">\n",
                                1.1, // version xml
                                "JavaGHTopo", // logiciel auteur
                                W3C_XML_SCHEMA_WEBSITE,
                                GPX_TOPOGRAPHIX_WEBSITE
                                ));
                break;
            case gisOSM: // OSM
                wrt.write( FormatterVersionEncodageXML("1.0", "UTF-8"));
                wrt.write(sprintf("<osm version=\"%s\" upload=\"%s\" generator=\"%s\">\n",
                          "0.6", "true", "JavaGHTopo"));
                break;
            case gisCARTO_EXPLOREUR: // Carto Exploreur
                wrt.write( "F;T;Lib\n");
                break;
            case gisMEMORY_MAP:  // Memory Map: pas d"entete
                break;
            case gisAUTOCAD_2D:  // AutoCAD
                break;
            default:
                break;
        }  
    }
    private static void WriteFooter(FileWriter wrt, int Mode) throws IOException
    {
        switch (Mode)
        {
            case gisGHCAVEDRAW:  // GHCaveDraw; sans objet
                break;
            case gisKML:  //KML
                // dossier principal = la cavité
                wrt.write("    </Folder>\n");
                wrt.write("  </Document>\n");
                wrt.write("</kml>\n");
                break;
            case gisGPX:
                wrt.write(" </gpx>\n");
                break;
            case gisOSM:
                wrt.write(" </osm>\n");
                break;
            case gisCARTO_EXPLOREUR: // Carto Exploreur , pas de footer
                break;
            case gisMEMORY_MAP: // Memory Map, pas de footer
                break;
            case gisAUTOCAD_2D: // AutoCAD 2D
                break;
            default:
                break;
        }           
    }
   
    private static void WriteConvertedPoint(FileWriter wrt,
                                              TConvertisseurCoordonnees CnvCoords, 
                                              int EPSGSource,
                                              String qPrefixStation,
                                              int Mode, int PolyMode, TEntiteEtendue ET, Color DefaultColor)
                                              throws IOException
    {
        String FMT_CE = "P;%.12f;%.12f;%.0f;-9999;;0.00;00:00:00;0;0.0;-1\n";
        Color CPColor = DefaultColor;
        TPoint2Df P2 = makeTPoint2Df(0.0, 0.0);
        double RR = 0.00;
        // éliminer visées trop courtes et trop longues
        RR = hypot3D(ET.UneStation2X - ET.UneStation1X,
                     ET.UneStation2Y - ET.UneStation1Y,
                     ET.UneStation2Z - ET.UneStation1Z);
        if (RR > 320.00) return;
        if (tgENTRANCE == ET.TypeEntite)  return; // éliminer entrées
        switch (Mode)
        {
            case gisGHCAVEDRAW:  // GHCaveDraw; sans objet
                break;
            case gisKML:
                break;
            case gisGPX:
                switch(PolyMode)
                {
                    case tpmENTETE_POLY: // en-tete de track/série
                        wrt.write("    <trk> <trkseg>\n");    
                        break;
                    case tpmSTART_POLY: // début de polyligne (série)
                        P2 = CnvCoords.conversionByCodeEPSG(EPSGSource, 4326, ET.UneStation1X, ET.UneStation1Y);
                        wrt.write(sprintf("        <trkpt lat=\"%s\" lon=\"%s\"></trkpt> <!-- %d.%d -->\n", 
                                                ensureConvertNumberToStr(P2.X), 
                                                ensureConvertNumberToStr(P2.Y), 
                                                ET.EntiteSerie, ET.EntiteStation));
                        break;
                    case tpmPOINT_POLY: // point de polyligne (série)
                        P2 = CnvCoords.conversionByCodeEPSG(EPSGSource, 4326, ET.UneStation2X, ET.UneStation2Y);
                        wrt.write(sprintf("        <trkpt lat=\"%s\" lon=\"%s\"></trkpt> <!-- %d.%d -->\n", 
                                               ensureConvertNumberToStr(P2.X), 
                                               ensureConvertNumberToStr(P2.Y), 
                                               ET.EntiteSerie, ET.EntiteStation));
                        break;
                    case tpmEND_POLY: //  GPX: balise de cloture  
                        wrt.write("    </trkseg> </trk>\n");                        
                }    
                break;
            case gisOSM:  // OSM = rien à faire
                break;
            case gisCARTO_EXPLOREUR: // Carto Exploreur (deprecated)    
                switch(PolyMode)
                {
                    case tpmENTETE_POLY: // en-tete de track/série
                        wrt.write(sprintf("T; Serie%d;1;0;%d;139;2;1\n", ET.EntiteSerie, CPColor));
                        break;
                    case tpmSTART_POLY: // début de polyligne (série)
                        P2 = CnvCoords.conversionByCodeEPSG(EPSGSource, 4326, ET.UneStation1X, ET.UneStation1Y);
                        wrt.write(sprintf(FMT_CE, P2.X, P2.Y, ET.UneStation1Z, ET.EntiteSerie, ET.EntiteStation)); // P2.V, P2.U dans l'ordre Carto
                        break;
                    case tpmPOINT_POLY: // point de polyligne (série)
                        P2 = CnvCoords.conversionByCodeEPSG(EPSGSource, 4326, ET.UneStation2X, ET.UneStation2Y);
                        wrt.write(sprintf("        <trkpt lat=\"%s\" lon=\"%s\"></trkpt> <!-- %d.%d -->\n", 
                                               ensureConvertNumberToStr(P2.X), 
                                               ensureConvertNumberToStr(P2.Y), 
                                               ET.EntiteSerie, ET.EntiteStation));
                        break;
                    case tpmEND_POLY: //  GPX: balise de cloture  
                        break;                       
                }    
                break;
            case gisMEMORY_MAP:
                switch(PolyMode)
                {
                    case tpmENTETE_POLY: // en-tete de track/série
                        wrt.write(sprintf("TK01, \"Serie%d\", \"Track:%s\", 1, 0, %d, 0, %d, 0, 0, 0, 0",
                                                ET.EntiteSerie, qPrefixStation, 0, CPColor));
                        break;
                    case tpmSTART_POLY: // début de polyligne (série)
                        P2 = CnvCoords.conversionByCodeEPSG(EPSGSource, 4326, ET.UneStation1X, ET.UneStation1Y);
                        wrt.write(sprintf("TP01,%.6f,%.6f, 0, 0, 0", P2.X, P2.Y)); // TODO: P2.V, P2.U dans l'ordre MemoryMap
                        break;
                    case tpmPOINT_POLY: // point de polyligne (série)
                        P2 = CnvCoords.conversionByCodeEPSG(EPSGSource, 4326, ET.UneStation2X, ET.UneStation2Y);
                        wrt.write(sprintf("TP01,%.6f,%.6f, 0, 0, 0", P2.X, P2.Y)); // TODO: P2.V, P2.U dans l'ordre MemoryMap
                        break;
                    case tpmEND_POLY: //  GPX: balise de cloture  
                        break;                       
                }
                break;
            case gisAUTOCAD_2D:                
                break;
            default:
                break;
        }    
    }        
    // dessiner le polygone de silhouette
    
    private static void DessinerPolygoneSilhouette(FileWriter wrt,
                                                   TConvertisseurCoordonnees CnvCoords, 
                                                   int EPSGSource,
                                                   int Mode, 
                                                   boolean UseColorGroupes,
                                                   TSilhouetteGalerie QSilhouette, 
                                                   String TagString, long TagNum)
                                                   throws IOException
    {
        final double DELTA_Z_SURFACE = 1.00;
        int Nb = 0;
        
        String fred_mignon = ""; // Fred_Mignon est en string lol ;)
        Color QC = new Color(0, 0, 0);
        TPoint2Df PP = makeTPoint2Df(0.00, 0.00);
        TPoint2Df P2 = makeTPoint2Df(0.00, 0.00);
         // construction du polygone
        if (0 == QSilhouette.buildPolygoneParois()) return;
        switch (Mode)
        {    
            case gisGHCAVEDRAW: ; // GHCaveDraw; sans objet
                break;
            case gisKML: 
                Nb = QSilhouette.getNbPointsPolygoneParois();
                wrt.write("      <Placemark>\n");
                wrt.write(sprintf("       <!-- Polygone: %s (%d sommets) -->\n", "MyPolygone", Nb));
                wrt.write(sprintf("         <name>%s</name>\n", TagString));
                if (UseColorGroupes) // utilise les couleurs de groupe de la topographie
                {    
                      QC = QSilhouette.getFillColor();
                      fred_mignon = STYLE_GALERIE_PAR_DEFAUT +
                                    sprintf("%.2X%.2X%.2X%.2X", QC.getRed(), QC.getGreen(), QC.getBlue(), QC.getAlpha());
                      wrt.write(sprintf("         <styleUrl>#%s</styleUrl>\n", fred_mignon));
                }
                else                        // sinon, utilise la couleur spécifiée
                {
                      wrt.write(sprintf("         <styleUrl>#%s</styleUrl>\n", STYLE_GALERIE_PAR_DEFAUT));
                }
                wrt.write(        "         <Polygon>\n");
                wrt.write(sprintf("           <tesselate>%d</tesselate>\n", 1));
                wrt.write( "           <outerBoundaryIs>\n");
                wrt.write( "             <LinearRing>\n");
                wrt.write( "               <coordinates>\n");
                for (int ii = 0; ii < Nb; ii++)
                {
                    PP = QSilhouette.getPointPolygoneParois(ii);
                    P2 = CnvCoords.conversionByCodeEPSG(EPSGSource, 4326, PP.X, PP.Y);
                    //wrt.write(sprintf("             %.16f,%.16f,%.0f", [P2.V, P2.U, DELTA_Z_SURFACE]));   //Lon, Lat
                    wrt.write(sprintf("             %s,%s,%.0f\n", 
                                            ensureConvertLongOrLatToXML(P2.X), 
                                            ensureConvertLongOrLatToXML(P2.Y),
                                            DELTA_Z_SURFACE));   //Lon, Lat
                }
                wrt.write( "               </coordinates>\n");
                wrt.write( "             </LinearRing>\n");
                wrt.write( "           </outerBoundaryIs>\n");
                wrt.write( "         </Polygon>\n");
                wrt.write( "      </Placemark>\n");
                break;    
            case gisGPX: // sans objet: les GPX contiennent des traces et non des zones
                break;
            case gisOSM: // OpenStreetMap
                // première passe: Liste des sommets
                long  QStartSommets = 10000 * (1 + TagNum);
                Nb = QSilhouette.getNbPointsPolygoneParois();
                afficherMessageFmt("Generation polygone %d - %s: %d noeuds", TagNum, TagString, Nb);
                for (int ii = 0; ii < Nb; ii++)
                {
                    PP = QSilhouette.getPointPolygoneParois(ii);
                    
                    P2 = CnvCoords.conversionByCodeEPSG(EPSGSource, 4326, PP.X, PP.Y);
                    WriteOSMNode(wrt, QStartSommets + (ii + 1), "modify", true, P2.X, P2.Y);        //QLon, QLat
                    //afficherMessageFmt("-- Point %d: %.0f, %.0f -> %f %f", ii, PP.X, PP.Y, P2.X, P2.Y);
                }
                // seconde passe: Vertex
                long QStartWay = 100000 * (1 + TagNum);
                wrt.write(sprintf("   <way id=\"%d\" action=\"%s\" visible=\"%s\">\n", -QStartWay, "modify", "true"));
                for (int ii = 0; ii < Nb; ii++)
                {    
                    PP = QSilhouette.getPointPolygoneParois(ii);
                    P2 = CnvCoords.conversionByCodeEPSG(EPSGSource, 4326, PP.X, PP.Y);
                    WriteOSMNodeRef(wrt, QStartSommets + (ii + 1));
                }
                // cloture du polygone
                WriteOSMNodeRef(wrt, QStartSommets + 1);
                WriteOSMTag(wrt, "leisure", "common");
                wrt.write( "   </way>\n");
                break;
            case gisCARTO_EXPLOREUR:    
                break;
            case gisMEMORY_MAP: 
                break;
            case gisAUTOCAD_2D:
                break;
            default:
                break;
        }        
    }        
    private void ExporterEntreeKML(FileWriter wrt, long Idx, TEntrance E, double lon, double lat) throws IOException
    {
        ;
    }      
    private void ExporterEntreeOSM(FileWriter wrt, long Idx, TEntrance EE, double lon, double lat) throws IOException
    {
        final String KVB = "    <tag k=\"%s\" v=\"%s\"/>\n";
        wrt.write(sprintf("  <node id=\"%d\" action=\"%s\" visible=\"%s\" lat=\"%s\" lon=\"%s\">\n", 
                                            -(Idx+1), "modify", "true", 
                                            ensureConvertLongOrLatToXML(lat), 
                                            ensureConvertLongOrLatToXML(lon)));   
                    wrt.write(sprintf(KVB, "name", EE.eNomEntree));
                    wrt.write(sprintf(KVB, "natural", "cave_entrance"));
                    wrt.write("  </node>\n");
    }      
    
    // export des entrées
    private void ExporterLesEntrees(FileWriter wrt,
                                    TConvertisseurCoordonnees CnvCoords,
                                    int EPSGSource,
                                    int Mode)
                                    throws IOException
    {
        
        int Nb = this.getNbEntrances();
        afficherMessageFmt("ExporterLesEntrees(%d entrees)", Nb);
        for (int i = 0; i < Nb; i++)
        {
            TEntrance EE = getEntrance(i);
            TPoint2Df P2 = CnvCoords.conversionByCodeEPSG(EPSGSource, 4326, EE.eXEntree, EE.eYEntree);
            switch (Mode)
            {
                case gisKML:
                    break;    
                case gisGPX:
                    break;                    
                case gisOSM:
                    ExporterEntreeOSM(wrt, i, EE, P2.X, P2.Y);
                    break;
                default:
                    break;
            }        
        }    
    }        
    //--------------------------------------------------------------------------
    // l'exportation elle-même
    public void exportForCarto(String qFileName,
                               TConvertisseurCoordonnees Convertisseur,
                               int QSystemeGeog,
                               int Mode, //// TOutputFormatGIS = (gisGHCAVEDRAW, gisKML, gisGPX, gisOSM, gisCARTO_EXPLOREUR, gisMEMORY_MAP, gisAUTOCAD_2D);
                               boolean DoMakeSilhouette,
                               String Filtres,
                               String PrefixStations,
                               Color  CouleurDefaut,
                               boolean DoUseColorGroupes)
    {
        this.trierParSeriesStations();
        String QAT[] = {"gisGHCAVEDRAW", "gisKML", "gisGPX", "gisOSM", "gisCARTO_EXPLOREUR", "gisMEMORY_MAP", "gisAUTOCAD_2D"};
        afficherMessageFmt("exportForCarto: %s - %s", qFileName, ChooseString(Mode, QAT));
        // si Mode = 0, export vers GHCaveDraw
        if (Mode == gisGHCAVEDRAW)
        {            
            this.ExportGCP(qFileName, "");
            return;
        }  
        final String  STYLE_GALERIE_PAR_DEFAUT = "StyleGalerieParDefaut";
        TConvertisseurCoordonnees myCoordConvertisseur = Convertisseur;
        afficherMessage("-- Convertisseur OK");

        int OldIdxSerie = 0;
        TEntiteEtendue E1;
        TEntiteEtendue E2;
        Color qColorEntite = new Color(0,0,0,0);
        TPtsSectionTransversale PtsParois;
       
        TSilhouetteGalerie mySilhouette = new TSilhouetteGalerie();
        int NbreSilhouettes = 0;
        // Un coup de MétaFiltre pour commencer
        afficherMessage("-- MétaFiltre");
        this.applyMetaFiltre(Filtres);
        this.setMiniMaxi();
        // Tri par séries et stations (peut-être inutile)
        afficherMessage("-- Tri par séries/stations");
        this.trierParSeriesStations();
        // l'exportation elle-même
        final File fichier = new File(qFileName); 
        try 
        {
            // Creation du fichier
            afficherMessage("-- Ouverture fichier");
            fichier.createNewFile();
            // creation d'un writer (un écrivain)
            final FileWriter writer = new FileWriter(fichier);
            try 
            {
                WriteHeader(writer, Mode);
                // les entrées
                ExporterLesEntrees(writer, Convertisseur, QSystemeGeog, Mode);
                
                // les silhouettes
                if (DoMakeSilhouette)
                {
                    afficherMessage("Mode silhouettes");
                    mySilhouette.clearSilhouette(CouleurDefaut, CouleurDefaut);
                    // premier polygone
                    E1 = getEntite(1);
                   
                    qColorEntite = CouleurDefaut; //GetCouleurEntiteRGBByExpe(E1);
                    PtsParois = new TPtsSectionTransversale();
                    PtsParois.ParoiDroiteX = E1.X1PD;
                    PtsParois.ParoiDroiteY = E1.Y1PD;
                    PtsParois.ParoiGaucheX = E1.X1PG;
                    PtsParois.ParoiGaucheY = E1.Y1PG;
                    mySilhouette.addPtsParois(PtsParois);
                    int nb = this.getNbEntites();
                    afficherMessageFmt("%d entités", nb);
                    TPtsSectionTransversale PtsParois1;
                    for (int i = 2; i < nb; i++)
                    {
                        //(Assigned(FProcDispProgression)) then FProcDispProgression(i, 2, self.GetNbEntites, Format("Entite %d / %d", [i, self.GetNbEntites]));
                    
                        E1 = getEntite(i);
                        E2 = getEntite(i-1);
                        

                        PtsParois1 = new TPtsSectionTransversale();
                        PtsParois1.ParoiDroiteX = E1.X2PD;
                        PtsParois1.ParoiDroiteY = E1.Y2PD;
                        PtsParois1.ParoiGaucheX = E1.X2PG;
                        PtsParois1.ParoiGaucheY = E1.Y2PG;
                        if (IsSameSerie(E1, E2)) 
                        {
                            mySilhouette.addPtsParois(PtsParois1);                            
                        }
                        else
                        { // on construit et trace le polygone                            
                            DessinerPolygoneSilhouette(writer,
                                                       Convertisseur,
                                                       QSystemeGeog, 
                                                       Mode, false,
                                                       mySilhouette, 
                                                       sprintf("Galerie%d", NbreSilhouettes), 
                                                       NbreSilhouettes);
                            NbreSilhouettes++;
                            // on purge la silhouette courante pour en accueillir la suivante
                            qColorEntite = CouleurDefaut; //GetCouleurEntiteRGBByExpe(E1);
                            mySilhouette.clearSilhouette(qColorEntite, qColorEntite);
                            TPtsSectionTransversale PtsParois2 = new TPtsSectionTransversale();
                            PtsParois2.ParoiDroiteX = E1.X1PD;
                            PtsParois2.ParoiDroiteY = E1.Y1PD;
                            PtsParois2.ParoiGaucheX = E1.X1PG;
                            PtsParois2.ParoiGaucheY = E1.Y1PG;
                            mySilhouette.addPtsParois(PtsParois2);

                        }                      
                    } 
                    afficherMessage("Mode silhouettes terminé");
                    // dernier polygone
                    DessinerPolygoneSilhouette(writer,
                                               Convertisseur,
                                               QSystemeGeog, 
                                               Mode, false,
                                               mySilhouette, 
                                               sprintf("Galerie%d", NbreSilhouettes), 
                                               NbreSilhouettes);
                    NbreSilhouettes++;
                    mySilhouette.clearSilhouette(Color.WHITE, Color.white);
                }   
                else
                {
                    afficherMessage("-- centerlines only");
                    E1 = getEntite(1);
                   
                    WriteConvertedPoint(writer, Convertisseur, QSystemeGeog, PrefixStations, Mode, tpmSTART_POLY,  E1, CouleurDefaut);
                    int nb = getNbEntites();
                    for (int i = 2; i < nb; i++) 
                    {
                        //if (Assigned(FProcDispProgression)) then FProcDispProgression(i, 2, self.GetNbEntites, Format("Entite %d / %d", [i, self.GetNbEntites]));
                        E1 = getEntite(i);
                        E2 = getEntite(i-1);
                        if (IsSameSerie(E1, E2)) 
                        {
                            WriteConvertedPoint(writer, Convertisseur, QSystemeGeog, PrefixStations, Mode, tpmPOINT_POLY, E1, CouleurDefaut);
                        } 
                        else
                        {
                          WriteConvertedPoint(writer, Convertisseur, QSystemeGeog, PrefixStations, Mode, tpmEND_POLY, E1, CouleurDefaut); // pour GPX uniquement: balise de cloture
                          WriteConvertedPoint(writer, Convertisseur, QSystemeGeog, PrefixStations, Mode, tpmENTETE_POLY, E1, CouleurDefaut);
                          WriteConvertedPoint(writer, Convertisseur, QSystemeGeog, PrefixStations, Mode, tpmSTART_POLY, E1, CouleurDefaut);
                        }
                    }
                    // dernière entité
                    WriteConvertedPoint(writer, Convertisseur, QSystemeGeog, PrefixStations, Mode, tpmEND_POLY, E1, CouleurDefaut); // pour GPX uniquement: balise de cloture
                }  
                
                afficherMessage("-- Footer");
                WriteFooter(writer, Mode); 
                writer.flush();
            } 
            finally 
            {
                // quoiqu'il arrive, on ferme le fichier
                writer.close();
            }
        } catch (Exception e) 
        {
            e.printStackTrace();
            afficherMessage("-- Impossible de creer le fichier");
        }
    } 
    private String getResourceByKey(String k)
    {
        return fMyResourceBundle.getString(k);
    }
    //--------------------------------------------------------------------------
    // Diagrammes
    public int    getRoseDiagramNbPetales()
    {
        return fClassRepartRoseDiagram.length;
    }        
    public double getRoseDiagramValueByIdx(int idx)
    {
        return fClassRepartRoseDiagram[idx];
    }        
    // histogramme des directions : calcul
    public boolean ParseRoseDiagram(int NbPetales, double ViseeMini)
    {
        double LP;
        double dx;
        double dy;
        double dz;
        double Ang;
        double LPT;
        String s;
        double ClasseMax;
        double Interval;
        boolean b;
        TEntiteEtendue MyEntite;
        afficherMessageFmt("%s.ParseDiagram: Nb = %d; Mini=%.2f", this.getClass().getName(), NbPetales, ViseeMini);
        boolean result = false;
        try
        {    
            Interval = Math.PI / NbPetales;
            // dimensionner table des classes
            fClassRepartRoseDiagram = new double[NbPetales];
            // initialiser cette table
            for (int i = 0; i < NbPetales; i++) fClassRepartRoseDiagram[i] = 0.00;
            // boucle de calcul
            LPT = 0.00;
            int Nb = this.getNbEntites();
            for (int i = 0; i < Nb; i++)
            {
                MyEntite = this.getEntite(i);
                // calcul longueur projetee
                //afficherMessage(MyEntite.Drawn ? "Drawn" : "Undrawn");
                //if (!MyEntite.Drawn) continue;
                if (MyEntite.TypeEntite == tgENTRANCE) continue;
                // /!\ Utiliser IsViseetInNaturalCave et non IsViseetInCaveOrTunnel
                if (!IsViseetInNaturalCave(MyEntite)) continue;                   
                dx = MyEntite.UneStation2X - MyEntite.UneStation1X;
                dy = MyEntite.UneStation2Y - MyEntite.UneStation1Y;
                dz = MyEntite.UneStation2Z - MyEntite.UneStation1Z;
                LP = Math.hypot(dx, dy);
                // test: passer à entitée suivante si LP<ViseeMini
                if (LP < ViseeMini) continue;
                // cumul des longueurs projetées
                LPT += LP;
                // calcul de l'angle
                // pour le calcul d'une rosette symétrique
                // l'angle retourné est dans la plage [0; PI]
                //Ang:=(0.50*PI)-ArcSin(dy/LP);
                Ang = Math.atan2(dx, dy + 1e-05);
                if (Ang < 0) Ang += 2 * Math.PI;
                // répartition
                for (int j = 0; j < NbPetales; j++)
                {
                    if (IsInRangeDbl(Ang, j * Interval, (j + 1) * Interval)) fClassRepartRoseDiagram[j] += LP;
                    if (IsInRangeDbl(Ang, Math.PI + j * Interval, Math.PI + (j + 1) * Interval)) fClassRepartRoseDiagram[j] += LP;                   
                }                 
            } 
            // Longueur totale nulle -> sortie de la fonction
            if (LPT <= 0.01) return false;
            // mise en pourcentage
            ClasseMax = 0.00;
            for (int j = 0; j < NbPetales; j++) 
                if (fClassRepartRoseDiagram[j] > ClasseMax) ClasseMax = fClassRepartRoseDiagram[j];
            for (int j = 0; j < NbPetales; j++)
                fClassRepartRoseDiagram[j] = fClassRepartRoseDiagram[j] / ClasseMax;
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;    
        }
    }    
    //--------------------------------------------------------------------------
    // Export vers GHCaveDraw
    // Validé. Couleurs à vérifier
    public void ExportGCP(String FichierGCD, String qFiltre)
    {
        final String TAB = "\t"; 
        final String FMT = "    %d"+TAB+ "%s"+ // ID de la visée
                                    TAB+"%d"+TAB+"%d" + // attributs
                                    TAB+"%s"+TAB+"%s"+TAB+"%s" +  // point de départ
                                    TAB+"%s"+TAB+"%s"+TAB+"%s" +  // point d"arrivée
                                    TAB+"%s"+TAB+"%s"+TAB+"%s" +  // point droit  (avec Z du sol)
                                    TAB+"%s"+TAB+"%s"+TAB+"%s" + "\n";   // point gauche (avec Z du plafond)               
        afficherMessageFmt("Export GCP: %s - %s", FichierGCD, qFiltre);
        // sauvegarde du filtre
        String OldFilter = qFiltre;
        this.applyMetaFiltre("");
        // l'exportation elle-même
        final File fichier = new File(FichierGCD); 
        try 
        {
            // Creation du fichier
            afficherMessage("-- Ouverture fichier");
            fichier.createNewFile();
            // creation d'un writer (un écrivain)
            final FileWriter writer = new FileWriter(fichier);
            try 
            {
                // points de base des éléments du dessin
                writer.write("# Base points\n");
                writer.write("");
                writer.write("basepoints\n");
                int Nb = this.getNbEntites();
                for (int i = 1; i < Nb; i++)
                {
                    TEntiteEtendue E = this.getEntite(i);
                    if (E.Drawn)
                    {    
                        long ID = 100000 * E.EntiteSerie +
                                  10     * E.EntiteStation;
                        writer.write(sprintf(FMT,
                                    ID,
                                     E.oIDLitteral,
                                     E.TypeEntite, // ensureConvertStrToNumber
                                     javaColorToPasColor(getCouleurEntiteRGBByExpe(E)), //E.ColorEntite,
                                     ensureConvertCoordToGCD(E.UneStation1X),
                                     ensureConvertCoordToGCD(E.UneStation1Y),
                                     ensureConvertCoordToGCD(E.UneStation1Z),
                                     ensureConvertCoordToGCD(E.UneStation2X),
                                     ensureConvertCoordToGCD(E.UneStation2Y),
                                     ensureConvertCoordToGCD(E.UneStation2Z),
                                     
                                     ensureConvertCoordToGCD(E.X2PG),
                                     ensureConvertCoordToGCD(E.Y2PG),
                                     ensureConvertCoordToGCD(E.Z1PB),
                                     ensureConvertCoordToGCD(E.X2PD),
                                     ensureConvertCoordToGCD(E.Y2PD),
                                     ensureConvertCoordToGCD(E.Z1PH)
                                     ));
                    }
                }
                writer.write("endbasepoints\n");

                // cloture
                writer.flush();
            }
            finally
            {
                writer.close();
                // rétablissement du filtre
                this.applyMetaFiltre(OldFilter);
            }   
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }    
    } 

    //--------------------------------------------------------------------------
    // Constructeur
    public TTableDesEntites()
    {
        afficherMessage("Création de TTableDesEntites");
        this.fMyResourceBundle = java.util.ResourceBundle.getBundle("com/ghtopo/javaghtopo/Bundle");
        this.myListeDesEntites = new ArrayList<TEntiteEtendue>();
        this.myListeDesEntites.clear();
        this.myCoinBasGauche = makeTPoint3Df(0.00, 0.00, 0.00);
        this.myCoinHautDroit = makeTPoint3Df(0.00, 0.00, 0.00);
        // table des entrées
        fListeEntrances = new ArrayList<>();
        // tables Réseaux, Secteus, Expés, Codes
        fListeReseaux  = new ArrayList<>(); // réseaux
        fListeSecteurs = new ArrayList<>(); // secteurs
        fListeCodes    = new ArrayList<>(); // codes
        fListeExpes    = new ArrayList<>(); // expés
        // démarrage de la palette
        this.myPalette256 = new TPalette256();
        this.myPalette256.generateTOPOROBOTPalette();
        // table des dates
        this.fListeDesDates = new ArrayList<Integer>();
        // tableau de stats
        this.fSpeleometrieParReseaux  = new ArrayList<TVentilationSpeleometrie>();
        this.fSpeleometrieParSecteurs = new ArrayList<TVentilationSpeleometrie>();
        this.fSpeleometrieParExpes    = new ArrayList<TVentilationSpeleometrie>();
        this.fSpeleometrieParCodes    = new ArrayList<TVentilationSpeleometrie>();
        this.fSpeleometrieParDates    = new ArrayList<TVentilationSpeleometrie>();        
    }
}