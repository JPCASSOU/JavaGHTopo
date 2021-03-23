package GHTopoOperationnel;


import static GHTopoOperationnel.CalculMatriciel3x3.*;
import static GHTopoOperationnel.Constantes.*;  // import des fonctions générales
import static GHTopoOperationnel.GeneralFunctions.*;  // import des fonctions générales
import GHTopoOperationnel.Types.TCode; // calcul matriciel et vectoriel
import GHTopoOperationnel.Types.TEntiteEtendue;
import GHTopoOperationnel.Types.TEntrance;
import GHTopoOperationnel.Types.TExpe;
import GHTopoOperationnel.Types.TJonction;
import GHTopoOperationnel.Types.TLongAzPente;
import GHTopoOperationnel.Types.TReseau;
import GHTopoOperationnel.Types.TSecteur;
import GHTopoOperationnel.Types.TStation;
import GHTopoOperationnel.Types.TViseeEnAntenne;
import java.awt.Color;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
//**********************************************************************************************************************
// Code de calcul validé
// TODO: Antennes à finaliser
//**********************************************************************************************************************
public class TCodeDeCalcul
{
    private TToporobotStructure  mFMyDocTopo;
    private ArrayList<TBranche>  mFListeBranches; // les branches
    private ArrayList<TJonction> mFListeDesJonctions;
    private TTableDesEntites     mFMyTableEntites;
    // Bundle I18N
    private ResourceBundle fMyResourceBundle = null;
    // Les matrices
    private int mFNumeroNoeudMaxi;
    private int mRMatrix[][];
    private double mBMatrix[][];
    private double mSecondMembre[];
    private double mWMatrix[]; // la matrice W est diagonale, on la stocke sous forme d'un vecteur
    //--------------------------------------------------------------------------
    private String getResourceByKey(String k)
    {
        return fMyResourceBundle.getString(k);
    }        
    
    //--------------------------------------------------------------------------
    // Les jonctions
    public void addJonctionByValeurs(int QNoNoeud, String QIDJonction, double QX, double  QY, double QZ)
    {
        TJonction EWE = new TJonction();
        EWE.NoNoeud     = QNoNoeud;
        EWE.IDJonction  = QIDJonction;
        EWE.X           = QX;
        EWE.Y           = QY;
        EWE.Z           = QZ;
        this.mFListeDesJonctions.add(EWE);
    }
    public TJonction getJonction(int Idx)
    {
        return this.mFListeDesJonctions.get(Idx);
    }
    public void putJonction(int Idx, TJonction J)
    {
        this.mFListeDesJonctions.set(Idx, J);
    }
    public int getNbJonctions()
    {
        return this.mFListeDesJonctions.size();
    }
    public int findJunction(String qIDJunction)
    {        
        int Nb = this.getNbJonctions();
        for (int i = 0; i < Nb; i++)
        {    
            TJonction J = getJonction(i);
            //afficherMessageFmt("Jonction trouvée: %s (%d)", qIDJunction, mFListeDesJonctions.size());     
            if (qIDJunction.equals(J.IDJonction)) return i;            
        }
        return -1;
    }        
    // la station est une jonction ?
    public boolean stationIsJunction(int qSer, int qSt)
    {
        String EWE = sprintf("%d.%d", qSer, qSt);
        
        return (findJunction(EWE) != -1);
    }        
    // recensement des jonctions
    public void recenserJonctions()
    {
        afficherMessageFmt(getResourceByKey("rsCODE_CALCUL_RECENSER_JONCTIONS"), 
                            this.mFMyDocTopo.getNbEntrances(), this.mFMyDocTopo.getNbSeries());
        // créer une liste provisoire
        ArrayList<String> LS = new ArrayList<String>();
        LS.clear();
        int NbreElements = this.mFMyDocTopo.getNbSeries();
        // ajout du noeud "0.0"
        String zdar = sprintf("%d.%d", 0, 0); //NOI18N
        LS.add(zdar);
        // les extrémités de séries sont aussi des jonctions
        for (int i = 0; i < NbreElements; i++)
        {
            TSerie Ser = this.mFMyDocTopo.getSerie(i);
            zdar = sprintf("%d.%d", Ser.SerDep, Ser.PtDep); //NOI18N
            if (! LS.contains(zdar)) LS.add(zdar);
            zdar = sprintf("%d.%d", Ser.SerArr, Ser.PtArr); //NOI18N
            if (! LS.contains(zdar)) LS.add(zdar);
        }
        // attribution des jonctions
        // les entrées sont des jonctions
        NbreElements = this.mFMyDocTopo.getNbEntrances();
        for (int i=0; i < NbreElements; i++)
        {
            TEntrance Entr = this.mFMyDocTopo.getEntrance(i);
            zdar = sprintf("%d.%d", Entr.eRefSer, Entr.eRefSt); //NOI18N
			// pas de doublons
            if (! LS.contains(zdar)) LS.add(zdar);
        }
        for (int i = 0; i < LS.size(); i++)
        {
            zdar = LS.get(i);
            this.addJonctionByValeurs(i, zdar, 0.0, 0.00, 0.00);
        }
        
        // nettoyage de la liste provisoire
        LS.clear();

    }
    // L'optimisation effectuée dans la version FPC n'est pas nécessaire ici.
    public int getNoeud(int Ser, int St)
    {
        int result = -1;
        String QAT = sprintf("%d.%d", Ser, St); //NOI18N
        int nbreElements = this.getNbJonctions();
        for (int q = 0; q < nbreElements; q++)
        {
            if (this.getJonction(q).IDJonction.equals(QAT))
            {
                result = q;
                break;
            }
        }
        return result;
    }
    //------------------------------------------------------------------------------------------------------------------
    // Les branches
    public void addBranche(TBranche Br)
    {
        this.mFListeBranches.add(Br);
    }
    public int  getNbBranches()
    {
        return this.mFListeBranches.size();
    }
    public TBranche getBranche(int Idx)
    {
        return this.mFListeBranches.get(Idx);
    }
    public void putBranche(int Idx, TBranche Br)
    {
        this.mFListeBranches.set(Idx, Br);
    }
    public TBranche initBranche(int QNoSerie, int QNoReseau, int QNoeudDepart, int QNoeudArrivee, double QRigidite) {
        TBranche QBranche = new TBranche();
        QBranche.clear();
        QBranche.NoSerie = QNoSerie;
        QBranche.NoReseau = QNoReseau;
        QBranche.NoeudDepart = QNoeudDepart;
        QBranche.NoeudArrivee = QNoeudArrivee;
        QBranche.Rigidite = QRigidite;
        QBranche.DeltaX = 0.001;
        QBranche.DeltaY = 0.001;
        QBranche.DeltaZ = 0.001;
        QBranche.XDepart = 0.00;
        QBranche.YDepart = 0.00;
        QBranche.ZDepart = 0.00;
        QBranche.XArrivee = 0.00;
        QBranche.YArrivee = 0.00;
        QBranche.ZArrivee = 0.00;
        QBranche.addStationByValeurs(0, "", //NOI18N
                                     0, 0,
                                     0, 0,
                                     0.01, 0.00, 0.00,
                                     0.00, 0.00, 0.00, 0.00,
                                     ""); //NOI18N
        return QBranche;
    }
    public void recenserBranches()
    {
        int NbrDeSeries    = this.mFMyDocTopo.getNbSeries();
        int NbrDeJonctions = this.getNbJonctions();
        
        afficherMessageFmt(getResourceByKey("rsCODE_CALCUL_RECENSER_BRANCHES"), NbrDeSeries, NbrDeJonctions);
        // première branche     S  R  D  A  K      X     Y     Z
        TBranche Branche0;
        Branche0 = this.initBranche(0, 0, 0, 0, 1.00);
        this.addBranche(Branche0);
        Branche0 = this.initBranche(1, 0, 0, 1, 1.00);
        this.addBranche(Branche0);
        int Br = 0;
        for (int i = 1; i < NbrDeSeries; i++)
        {
            TSerie     mySerie = this.mFMyDocTopo.getSerie(i);            
            int        Nd      = this.getNoeud(mySerie.SerDep, mySerie.PtDep);
            Branche0 = this.initBranche(mySerie.IdxSerie, mySerie.Reseau, Nd, -1, 1.00);
            int NbreStations = mySerie.getNbStations();
            for (int v = 1; v < NbreStations; v++)
            {
                TStation myVisee = mySerie.getStation(v);
                myVisee.IDStation = v;
                Nd = this.getNoeud(mySerie.IdxSerie, myVisee.IDStation);
                
                Branche0.addStation(myVisee);
                if ((Nd > -1) && (v < (NbreStations - 1))) 
				{
                    // clôturer la branche courante
                    Branche0.NoeudArrivee = Nd;
                    this.addBranche(Branche0);
                    // et creer la suivante
                    Br++;
                    Branche0 = initBranche(mySerie.IdxSerie, mySerie.Reseau, Nd, -1, 1.00);
                    Branche0.NoeudDepart = Nd;
                }
                // fin de serie = fin de branche: cloturer la branche
                if (v == NbreStations - 1) 
				{
                    Nd = this.getNoeud(mySerie.SerArr, mySerie.PtArr);
                    Branche0.NoeudArrivee = Nd;
                    this.addBranche(Branche0);
                    Br++;
                }
            }
        }
        // entrées
        int NbEntrees = this.mFMyDocTopo.getNbEntrances();
        int NoeudDeDepart = this.getNoeud(1, 0);
        double QQX = this.mFMyDocTopo.getPoint0X();
        double QQY = this.mFMyDocTopo.getPoint0Y();
        double QQZ = this.mFMyDocTopo.getPoint0Z();
        for (int i = 0; i < NbEntrees; i++)
        {
            TEntrance Entree = this.mFMyDocTopo.getEntrance(i);
            int Nd = this.getNoeud(Entree.eRefSer, Entree.eRefSt);
            Branche0 = initBranche(Entree.eRefSer, 0, 0, Nd, 1.00);
            TLongAzPente QQ  = getBearingInc(Entree.eXEntree - QQX, Entree.eYEntree - QQY, Entree.eZEntree - QQZ, 360.00, 360.00);
            Branche0.addStationByValeurs(0, "", 0, tgENTRANCE, 0, 0, QQ.aLongueur, QQ.aAzimut, QQ.aPente, 0.00, 0.00, 0.00, 0.00, ""); //NOI18N
            this.addBranche(Branche0);
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    // calcul des accroissements pour une visée
    private TStation calculerVisee(TStation ST)
    {
        TExpe EE = this.mFMyDocTopo.getExpeByIndex(ST.Expe);
        TCode CC = this.mFMyDocTopo.getCodeByIndex(ST.Code);
        return calculerUneVisee(ST, CC.UniteBoussole, CC.UniteClino, EE.Declinaison, EE.Inclinaison);
    }
    // factorisation du code
    private TBranche calculerBranche(TBranche Branche) {
        TBranche Result = Branche;
        int Nb = Branche.getNbStations();
        Result.DeltaX = 0.00;
        Result.DeltaX = 0.00;
        Result.DeltaY = 0.00;
        Result.DeltaZ = 0.00;
        Result.LongDev = 0.00;
        for (int i = 0; i < Nb; i++) {
            TStation QVisee = this.calculerVisee(Branche.getStation(i));
            Branche.DeltaX += QVisee.dX;
            Branche.DeltaY += QVisee.dY;
            Branche.DeltaZ += QVisee.dZ;
            Branche.LongDev += QVisee.LDev;
            Branche.putStation(i, QVisee);
        }
        return Branche;
    }
    public void calculerLesBranches()
    {
        int NbElements = this.getNbBranches();
        afficherMessage(sprintf(getResourceByKey("rsCODE_CALCUL_TRAITEMENT_BRANCHES"), NbElements));
        for (int i = 0; i < NbElements; i++)
        {
            TBranche QBr = this.calculerBranche(this.getBranche(i));
            this.putBranche(i, QBr);
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    private int getMaxNode()
    {
        int M = -666;
        for (int i = 1; i < this.getNbBranches() - 1; i++)
        {
            TBranche B = this.getBranche(i);
            if (B.NoeudDepart > M)  M = B.NoeudDepart;
            if (B.NoeudArrivee > M) M = B.NoeudArrivee;
        }
        return M;
    }
    // Matrice d'incidence R
    // cette fonction est normalement imbriquée dans MakeRMatrix
    private int beuh(TBranche QB, int QJ)
    {
        if      (QB.NoeudDepart  == (QJ)) return -1;
        else if (QB.NoeudArrivee == (QJ)) return +1;
        else    return 0;
    }
    public void makeRMatrix()
    {
        int QNB = this.getNbBranches();
        int QNJ = this.getNbJonctions(); //FNumeroNoeudMaxi;
        afficherMessage(sprintf("MakeRMatrix: %d x %d", QNB, QNJ)); //NOI18N
        this.mRMatrix = new int[QNB+1][QNJ + 1];
        for (int i = 1; i < QNB; i++)
        {
            TBranche B = this.getBranche(i);
            for (int j = 1; j < QNJ; j++) 
	    {
                this.mRMatrix[i][j] = this.beuh(B, j);
            }
        }
    }
	//------------------------------------------------------------------------------------------------------------------    
	// matrice diagonale de pondération W
        // TODO: Implémenter la raideur 
    
	public void makeWMatrix(int Axe)
        {
            int nb = this.getNbBranches();
            this.mWMatrix = new double[nb + 1];
            for (int i = 1; i < nb; i++)
            {
                TBranche Br = mFListeBranches.get(i);
                switch (Axe)
                {
                    case 1: 
                            this.mWMatrix[i] = 1.00; // 
                            break;
                    case 2:
                            this.mWMatrix[i] = 1.00; // 
                            break;
                    case 3:
                            this.mWMatrix[i] = 1.00; // 
                            break;
                }	
	    }		
	}
    //------------------------------------------------------------------------------------------------------------------
    // Second membre A = Rt.W.D
    public void makeSecondMembre(int Axe)
    {
        int QNB = this.getNbBranches();
        int QNJ = this.getNbJonctions();
        double WU = 0.00;
        this.mSecondMembre = new double[QNB + 1];

        afficherMessageFmt("MakeSecondMembre axe %d: %d ", Axe, QNB); //NOI18N
        for (int i = 1; i < QNJ; i++)
        {
            double ww = 0.00;
            for (int k = 1; k < QNB; k++)
            {
                TBranche Br = this.getBranche(k);
                switch (Axe)
                {
                    case 1:
                        WU = Br.DeltaX;
                        break;
                    case 2:
                        WU = Br.DeltaY;
                        break;
                    case 3:
                        WU = Br.DeltaZ;
                        break;
                }
                ww += this.mRMatrix[k][i] * WU;
            }
            this.mSecondMembre[i] = ww;
        }
    }
	
	//------------------------------------------------------------------------------------------------------------------
    // Matrice de compensation B = Rt.W.R avec W = matrice identité pour le moment
    public void makeBMatrix()
    {
        int m = this.getNbBranches() + 1;
        int n = this.getNbBranches() + 1;  //this.FNumeroNoeudMaxi + 1;
        int vecLowIndex[];
        int vecHighIndex[];

        afficherMessageFmt("MakeBMatrix: %d x %d", m, n); //NOI18N
        this.mBMatrix = new double[n+1][n+1];
        vecLowIndex = new int[n + 1];
        vecHighIndex= new int[n + 1];
        int QNJ = this.getNbJonctions();
        int QNB = this.getNbBranches();
        afficherMessageFmt("Tables d'index: %d noeuds, %d branches", QNJ, QNB); //NOI18N
        for (int i = 0; i < (n + 1); i++)
        {
            vecLowIndex[i]  = 0;
            vecHighIndex[i] = 0;
        }
        // recherche des indices mini et maxi
        for (int i = 1; i < QNJ; i++)
        {
            for (int j = 1; j < QNB; j++)
            {
                if (Math.abs(this.mRMatrix[j][i]) > 0)
                {
                    vecLowIndex[i] = j;
                    break;
                }
            }
        }

        for (int i = 1; i < QNJ; i++)
        {
            for (int j = QNB-1; j > 1; j--)
            {
                if (Math.abs(this.mRMatrix[j][i]) > 0)
                {
                    vecHighIndex[i] = j;
                    break;
                }
            }
        }
        afficherMessage("Matrice B, triangle inf"); //NOI18N
        for (int i = 1; i < QNJ; i++)
        {
            for (int j = 1; j <= i; j++)
            {
                double ww = 0.00;
                for (int k = vecLowIndex[i]; k <= vecHighIndex[i]; k++)
                {
                    ww += this.mRMatrix[k][i] * this.mRMatrix[k][j];
                }
                this.mBMatrix [i][j] = ww;
            }
        }
        // la matrice B est symétrique
        afficherMessage("Matrice B, triangle sup"); //NOI18N
        for (int i = 1; i < QNJ; i++)
        {
            for (int j = i+1; j < n; j++)
            {
                this.mBMatrix[i][j] = this.mBMatrix[j][i];
            }
        }
    }
    public void SolveMatrix(int axe)
    {
        afficherMessageFmt(getResourceByKey("rsCODE_CALCUL_FACTORISE_MTX_COMP"), axe);
        double WU = 0.00;
        switch (axe)
        {
            case 1:
                WU = this.mFMyDocTopo.getPoint0X();
                break;
            case 2:
                WU = this.mFMyDocTopo.getPoint0Y();
                break;
            case 3:
                WU = this.mFMyDocTopo.getPoint0Z();
                break;
        }
        int m = this.getNbBranches() + 1;
        int n = this.getNbJonctions() + 1;
        double vecXX[]  = new double[n+1];
        double matV[][] = new double [n+1][n+1];
        double vecS[]   = new double [n+1];
        afficherMessage("-- Descente: V.V* = A"); //NOI18N
        int QNJ = this.getNbJonctions();
        for (int i = 1; i < QNJ; i++)
        {
            double vv = 0.00;
            for (int k = 1; k < i; k++)
                vv += Math.pow(matV[i][k], 2);
            matV[i][i] = Math.sqrt(Math.abs(this.mBMatrix[i][i] - vv));
            for (int j = i+1; j < QNJ; j++)
            {
                double ww = 0.00;
                for (int k = 1; k < i; k++)
                    ww += matV[i][k] * matV[j][k];
                matV[j][i] = (this.mBMatrix[i][j] - ww) / (matV[i][i] + 1e-24);
            }
        }
        afficherMessage("-- Triangularisation"); //NOI18N
        for (int i = 1; i < QNJ; i++) {
            double ww = 0.00;
            for (int k = 1; k < i; k++)
                ww = ww + matV[i][k] * vecS[k];
            vecS[i] = (this.mSecondMembre[i] - ww) / (matV[i][i] + 1e-24);
        }
        afficherMessage("-- Remontee du système; inconnues recherchees"); //NOI18N
        for (int i = QNJ - 1; i > 0; i--)
        {
            double ww = 0.00;
            for (int k = i + 1; k < QNJ; k++)
                ww = ww + matV[k][i] * vecXX[k];
            vecXX[i] = (vecS[i] - ww) / (matV[i][i] + 1e-24);
        }
        //#XX[0] := XX[1];
        //# print(XX)
        afficherMessage(getResourceByKey("rsCODE_CALCUL_UPDATE_JONCTIONS"));
        for (int i = 0; i < QNJ; i++)
        {
            TJonction JC = this.getJonction(i);
            switch (axe)
            {
                case 1:
                    JC.X = vecXX[i] + this.mFMyDocTopo.getPoint0X();
                    break;
                case 2:
                    JC.Y = vecXX[i] + this.mFMyDocTopo.getPoint0Y();
                    break;
                case 3:
                    JC.Z = vecXX[i] + this.mFMyDocTopo.getPoint0Z();
                    break;
            }
            this.putJonction(i, JC);
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    //******************************************************************************************************************
    public void repartirEcarts()
    {
        int nbBranches = this.getNbBranches();
        int nbNoeuds   = this.getNbJonctions();
        afficherMessageFmt(getResourceByKey("rsCODE_CALCUL_REPARTITION_ECARTS"), nbNoeuds, nbBranches);
        TJonction myNoeud1;
        TJonction myNoeud2;
        for (int i = 1; i < nbBranches; i++)
        {
            TBranche myBranche = this.getBranche(i);
            myNoeud1 = this.getJonction(myBranche.NoeudDepart);
            myNoeud2 = this.getJonction(myBranche.NoeudArrivee);
            double offsetX = (myNoeud2.X - myNoeud1.X) - myBranche.DeltaX;
            double offsetY = (myNoeud2.Y - myNoeud1.Y) - myBranche.DeltaY;
            double offsetZ = (myNoeud2.Z - myNoeud1.Z) - myBranche.DeltaZ;
            double longCumulee = 0.00;
            double qPX = 0.00;
            double qPY = 0.00;
            double qPZ = 0.00;
            
            int nbStations = myBranche.getNbStations();
            for (int j = 0; j < nbStations; j++)
            {
                TStation myVisee = myBranche.getStation(j);
                longCumulee += myVisee.LDev;
                double r = longCumulee / myBranche.LongDev;
                qPX += myVisee.dX;
                qPY += myVisee.dY;
                qPZ += myVisee.dZ;
                myVisee.X = myNoeud1.X + qPX + offsetX * r;
                myVisee.Y = myNoeud1.Y + qPY + offsetY * r;
                myVisee.Z = myNoeud1.Z + qPZ + offsetZ * r;
                myBranche.putStation(j, myVisee);
            }
            this.putBranche(i, myBranche);
        }
    }
    // Traitement des antennes------------------------------------------------------------------------------------------
    public TEntiteEtendue calculerViseeAntenne(TViseeEnAntenne myVA, int qNo)
    {

        TEntiteEtendue myEntiteAccrochage = this.mFMyTableEntites.getEntiteFromSerSt(myVA.SerieDepart, myVA.PtDepart);
        TEntiteEtendue result = new TEntiteEtendue();
        TStation qVS = new TStation();
        
        qVS.Secteur     = 0;
        qVS.TypeVisee   = tgTYPE_ENTITES_VISEES_ANTENNE; // visée spéciale tgTYPE_ENTITES_VISEES_ANTENNE
        qVS.Code        = myVA.Code;
        qVS.Expe        = myVA.Expe;
        qVS.Longueur    = myVA.Longueur;
        qVS.Azimut      = myVA.Azimut;
        qVS.LG          = 0.00;
        qVS.LG          = 0.00;
        qVS.HZ          = 0.00;
        qVS.HN          = 0.00;
        qVS.IDTerrain = myVA.IDTerrainStation;
        qVS.Commentaires     = myVA.Commentaires;

        qVS = calculerVisee(qVS);
        result.EntiteSerie   = -qNo; // TODO: Autre méthode de numérotation à implémenter -- myVA.SerieDepart;
        result.EntiteStation = 0;
        result.oIDLitteral   = myVA.IDTerrainStation;
        result.TypeEntite    = tgTYPE_ENTITES_VISEES_ANTENNE;
        result.eCode         = myVA.Code;
        result.eExpe         = myVA.Expe;
        TExpe EE = this.mFMyDocTopo.getExpeByIndex(myVA.Expe);
        result.eReseau       = myVA.Reseau;
        result.eSecteur      = myVA.Secteur;
        result.DateLeve      = ensureMakeGHTopoDateFromInt(EE.DateLeve); // conversion sécurisée
        result.oLongueur     = myVA.Longueur;
        result.oAzimut       = myVA.Azimut;
        result.oPente        = myVA.Pente;
        result.ColourByDepth = new Color(0x80, 0x80, 0x80);
        result.oCommentaires = myVA.Commentaires;
        result.oLG           = 0.00;
        result.oLD           = 0.00;
        result.oHZ           = 0.00;
        result.oHN           = 0.00;
        result.Drawn         = true;
        //  centerline
        result.UneStation1X = myEntiteAccrochage.UneStation2X;
        result.UneStation1Y = myEntiteAccrochage.UneStation2Y;
        result.UneStation1Z = myEntiteAccrochage.UneStation2Z;

        result.UneStation2X = result.UneStation1X + qVS.dX;
        result.UneStation2Y = result.UneStation1Y + qVS.dY;
        result.UneStation2Z = result.UneStation1Z + qVS.dZ;
        //  habillage
        result.X1PD = myEntiteAccrochage.UneStation2X;
        result.Y1PD = myEntiteAccrochage.UneStation2Y;
        result.X1PG = myEntiteAccrochage.UneStation2X;
        result.Y1PG = myEntiteAccrochage.UneStation2Y;
        result.Z1PH = myEntiteAccrochage.UneStation2Z;
        result.Z1PB = myEntiteAccrochage.UneStation2Z;
        result.X2PD = result.UneStation2X;
        result.Y2PD = result.UneStation2Y;
        result.X2PG = result.UneStation2X;
        result.Y2PG = result.UneStation2Y;
        result.Z2PH = result.UneStation2Z;
        result.Z2PB = result.UneStation2Z;
        return result;
    }
    //------------------------------------------------------------------------------------------------------------------
    public void traiterLesAntennes()
    {
        int nbAntennes = this.mFMyDocTopo.getNbViseesAntenne();
        afficherMessageFmt(getResourceByKey("rsCODE_CALCUL_TRAITEMENT_ANTENNES"), nbAntennes);
        if (nbAntennes > 0)
        {
            for (int i = 0; i < nbAntennes; i++)
            {
                TViseeEnAntenne myViseeAntenne = this.mFMyDocTopo.getViseeEnAntenne(i);
                this.mFMyTableEntites.addEntite(calculerViseeAntenne(myViseeAntenne, i));
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    private void traiterBranche(TBranche qBranche)
    {
        int nbPts = qBranche.getNbStations(); //EWE := QBranche.PointsTopo.Count;
        TReseau aReseau   = mFMyDocTopo.getReseau(qBranche.NoReseau);
        
        List <TStation> provTabVisee = new ArrayList<TStation>();
        provTabVisee.clear();
        double alphaV = 0.00;
        double alphaD = 0.00;
        double cosAlphaD = 0.00;
        double sinAlphaD = 0.00;
        TStation v1;
        TStation qv = new TStation();
        // station nulle
        //qv = qBranche.getStation(0);
        //provTabVisee.add(qv);
        for (int st = 0; st < nbPts; st++)
        {
            v1 = qBranche.getStation(st);
            qv = v1;
            provTabVisee.add(qv);
        }
        // ajout d'une station en fin de tableau
        //qv = provTabVisee.get(nbPts - 1);
        provTabVisee.add(qv);
        provTabVisee.add(qv);
        //---------------------------------------
        TStation qv0 = provTabVisee.get(0);
        TStation qv1 = provTabVisee.get(1);
        TStation qv2;
        qv0.LD = qv1.LD;
        qv0.LG = qv1.LG;
        qv0.HZ = qv1.HZ;
        qv0.HN = qv1.HN;
        qv0.TypeVisee = qv1.TypeVisee;
        provTabVisee.set(0, qv0);
        // calcul contours
        for (int st = 1; st < nbPts; st++)
        {
            try
            {    
                double alphad = 0.00;
                double qCosAlphaD = 0.00;
                double qSinAlphaD = 0.00;
                qv0 = provTabVisee.get(st - 1);
                qv1 = provTabVisee.get(st);
                qv2 = provTabVisee.get(st + 1);
                if (st == 1)
                {
                    alphaD = calculerAngleBissecteur(qv1.X - qv0.X,
                                                     qv1.Y - qv0.Y,
                                                     qv1.X - qv0.X,
                                                     qv1.Y - qv0.Y);
                }
                qCosAlphaD = Math.cos(alphaD);
                qSinAlphaD = Math.sin(alphaD);
                TExpe EX = mFMyDocTopo.getExpeByIndex(qv1.Expe);
                TEntiteEtendue EE = new TEntiteEtendue();
                EE.eCode       = qv1.Code;
                EE.eExpe       = qv1.Expe;
                EE.eSecteur    = qv1.Secteur;
                EE.eReseau     = aReseau.IdxReseau;
                EE.TypeEntite  = qv1.TypeVisee; //2
                EE.DateLeve    = ensureMakeGHTopoDateFromInt(EX.DateLeve); // conversion sécurisée

                EE.EntiteSerie    = qBranche.NoSerie;
                EE.EntiteStation  = qv1.IDStation;
                // données originales
                EE.oLongueur       = qv1.Longueur;
                EE.oAzimut         = qv1.Azimut;
                EE.oPente          = qv1.Pente;
                EE.oLG             = qv1.LG;
                EE.oLD             = qv1.LD;
                EE.oHZ             = qv1.HZ;
                EE.oHN             = qv1.HN;
                // centerline
                EE.UneStation1X    = qv0.X ;
                EE.UneStation1Y    = qv0.Y ;
                EE.UneStation1Z    = qv0.Z ;
                EE.UneStation2X    = qv1.X ;
                EE.UneStation2Y    = qv1.Y ;
                EE.UneStation2Z    = qv1.Z ;
                // habillage
                EE.X1PD = EE.UneStation1X + qv0.LD * qCosAlphaD; //Cos(AlphaD);
                EE.Y1PD = EE.UneStation1Y + qv0.LD * qSinAlphaD; //sin(AlphaD);
                EE.X1PG = EE.UneStation1X - qv0.LG * qCosAlphaD;  //Cos(AlphaG);  cos(x+pi) = -cos(x)
                EE.Y1PG = EE.UneStation1Y - qv0.LG * qSinAlphaD; //Sin(AlphaG);   sin(x+pi) = -sin(x)
                EE.Z1PH = EE.UneStation1Z + qv0.HZ;
                EE.Z1PB = EE.UneStation1Z - qv0.HN;
                alphaD  = calculerAngleBissecteur(qv1.X - qv0.X,
                                                  qv1.Y - qv0.Y,
                                                  qv2.X - qv1.X,
                                                  qv2.Y - qv1.Y);
                // TODO: Ces deux instructions manquent dans GHTopoFPC
                qCosAlphaD = Math.cos(alphaD);
                qSinAlphaD = Math.sin(alphaD);

                EE.X2PD = EE.UneStation2X + qv1.LD * qCosAlphaD; //Cos(AlphaD);
                EE.Y2PD = EE.UneStation2Y + qv1.LD * qSinAlphaD; //sin(AlphaD);
                EE.X2PG = EE.UneStation2X - qv1.LG * qCosAlphaD; // Cos(AlphaG);
                EE.Y2PG = EE.UneStation2Y - qv1.LG * qSinAlphaD; //Sin(AlphaG);
                EE.Z2PH = EE.UneStation2Z + qv1.HZ;
                EE.Z2PB = EE.UneStation2Z - qv1.HN;
                EE.ColourByDepth = new Color(0, 0, 255);
                EE.oIDLitteral   = qv1.IDTerrain;
                EE.oCommentaires = qv1.Commentaires;
                EE.IsJonction = stationIsJunction(qBranche.NoSerie, qv1.IDStation);
                if (EE.TypeEntite != tgENTRANCE) mFMyTableEntites.addEntite(EE);
            }
            catch (Exception E)
            {
                ;                
            }    
        }    
    }
    //------------------------------------------------------------------------------------------------------------------
    public void calculerContoursGaleries()
    {
        int nbBranches = this.getNbBranches();
        afficherMessageFmt(getResourceByKey("rsCODE_CALCUL_CONTOURS_GALERIES"), nbBranches);
        for (int i = 1; i < nbBranches; i++)
        {
            TBranche myBr = this.getBranche(i);
            traiterBranche(myBr);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
   
    //******************************************************************************************************************
    // copier les tables Reseaux, Expes, Codes Secteurs
    private void recopierTableEntrees()
    {
        int Nb = mFMyDocTopo.getNbEntrances();
        for (int i = 0; i < Nb; i++)
        {
            TEntrance EE = mFMyDocTopo.getEntrance(i);
            mFMyTableEntites.addEntrance(EE);
        }            
    }        
    
    private void recopierTableReseaux()
    {
        int Nb = mFMyDocTopo.getNbReseaux();
        for (int i = 0; i < Nb; i++)
        {
            TReseau RR = mFMyDocTopo.getReseau(i);
            mFMyTableEntites.addReseau(RR);
        }            
    }        
    private void recopierTableSecteurs()
    {
        int Nb = mFMyDocTopo.getNbSecteurs();
        for (int i = 0; i < Nb; i++)
        {
            TSecteur SS = mFMyDocTopo.getSecteur(i);
            mFMyTableEntites.addSecteur(SS);
        }    
    }        
    private void recopierTableCodes()
    {
        int Nb = mFMyDocTopo.getNbCodes();
        for (int i = 0; i < Nb; i++)
        {
            TCode CC = mFMyDocTopo.getCode(i);
            mFMyTableEntites.addCode(CC);
        }    
    }        
    private void recopierTableExpes()
    {
        int Nb = mFMyDocTopo.getNbExpes();
        for (int i = 0; i < Nb; i++)
        {
            TExpe EX = mFMyDocTopo.getExpe(i);
            mFMyTableEntites.addExpe(EX);
        }        
    }        
    public void recopierTablesEntreesReseauxSecteursCodesExpes()
    {
        afficherMessage("recopierTablesReseauxSecteursCodesExpes"); //NOI18N
        recopierTableEntrees();
        recopierTableReseaux();
        recopierTableSecteurs();
        recopierTableExpes();
        recopierTableCodes();
    }
     //Constructeur par défaut
    public TCodeDeCalcul(TToporobotStructure MyDocTopo, TTableDesEntites MyTableEntites)
    {
        fMyResourceBundle = java.util.ResourceBundle.getBundle("com/ghtopo/javaghtopo/Bundle");
       
        afficherMessage(getResourceByKey("rsCODE_CALCUL_INIT_CODE_CALCUL"));
        this.mFMyDocTopo         = MyDocTopo;
        this.mFMyTableEntites    = MyTableEntites;
        
        this.mFListeBranches     = new ArrayList<TBranche>();
        this.mFListeBranches.clear();
        this.mFListeDesJonctions = new ArrayList<TJonction>();
        this.mFListeDesJonctions.clear();
        
        this.mFMyTableEntites.initialiserTableEntites();
        this.recopierTablesEntreesReseauxSecteursCodesExpes();
        
    }
}
