/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// 13/06/2014: 3D OK sauf couleurs.

package com.ghtopo.javaghtopo;

import java.util.ArrayList;
import static GHTopoOperationnel.GeneralFunctions.*;
import static GHTopoOperationnel.CalculMatriciel3x3.*;
import static GHTopoOperationnel.Constantes.*;

import GHTopoOperationnel.TTableDesEntites;
import GHTopoOperationnel.Types.TEntiteEtendue;
import GHTopoOperationnel.Types.TPoint2DDepth;
import GHTopoOperationnel.Types.TViseesTopo2D;
import GHTopoOperationnel.Types.TQuad;
import GHTopoOperationnel.Types.TPoint3Df;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author jean-pierre.cassou
 */
public class TCadreVue3D extends javax.swing.JPanel {
    private static final double PI180  = Math.PI / 180.0;
    private static final int    NBFACESBYVISEE = 6;
    private static final int    LOWINDEX       = 1;
    // position de la souris
    private Point curMousePosXY;
    private Point oldMousePosXY;
    // Table entités
    private TTableDesEntites myTableEntites;
    private int    fModeRepresentationGaleries = rgGRAY;
    private boolean fCanDraw;
    // Paramètres de vue
    private double fTheta = 0.00;
    private double fPhi   = 0.00;
    private double fZoom  = 1.00;
    private double fMagnification = 1.00;
    // tables de précalcul
    private ArrayList<TViseesTopo2D> FPolygonale;
    private ArrayList<TQuad> FTableQuads;
    // variables géométriques calculées 
    private Point  FCubeEnglobant[];
    private double FMatAux[];
    private Point  FTransfoInt;
    private double FRapportME;
    private double FLongueurDiagonale;
    private TPoint3Df FLookAt;
    private TPoint3Df FOffset;
    private boolean FDoDestTCanvas;
    //-----------------------------------
    // couleurs
    private Color fBackColor;
    private Color fLineColor;
    private Color fFillColor;
    // translation après calcul
    private Point fTranslater;
    // tout régénérer ?
    private boolean fDoToutRegenerer;
    public void setThetaPhiZoomMagn(double t, double p, double z, double m)
    {
        fTheta = t;
        fPhi   = p;
        fZoom  = z;
        fMagnification = m;
    }   
    public double getTheta()
    {
        return fTheta;
    }        
    public double getPhi()
    {
        return fPhi;
    }        
    public double getZoom()
    {
        return fZoom;
    }        
    public double getMagnification()
    {
        return fMagnification;
    }        
    /**
     * Creates new form TCadreVue3D
     */
    public TCadreVue3D() { 
        FCubeEnglobant = new Point[9]; 
        FMatAux        = new double[9];
        FTableQuads    = new ArrayList<TQuad>();
        FPolygonale    = new ArrayList<TViseesTopo2D>();
        FTransfoInt    = new Point(0, 0);
        fTranslater    = new Point(0, 0); 
        fDoToutRegenerer = true;
        initComponents();
    }
    public void initialiseVue3D(TTableDesEntites te)
    {
        fCanDraw       = false;
        FDoDestTCanvas = true;
        myTableEntites = te;
        // préparation tables temporaires
        myTableEntites.applyMetaFiltre("");
        myTableEntites.setMiniMaxi();
        fDoToutRegenerer = true;
        // mise en place des paramètres
        ResetParamTransformation();
        // construction des volumes et précalcul
        makeVolumesCavite();
        precalculVolumesCavite(true);
        // trier la topo
        qSortDatasByDepth();
        // redessin
        this.repaint();
    }   
    // coordonnées de transferts
    private TPoint2DDepth get2DDepthCoordinates(double X, double Y, double Z)
    {
        TPoint2DDepth Result = new TPoint2DDepth();
        Result.X = -(X+FOffset.X) * FMatAux[1] +
                    (Y+FOffset.Y) * FMatAux[3];
        Result.Y = -(X+FOffset.X) * FMatAux[5]
                   -(Y+FOffset.Y) * FMatAux[6]+
                    (fMagnification*(Z+FOffset.Z)) * FMatAux[4];
        Result.Depth = (-(X+FOffset.X) * FMatAux[7]
                        -(Y+FOffset.Y) * FMatAux[8]
                        -(Z+FOffset.Z) * FMatAux[2]);   
        return Result;
    }        
    private Point getScreenCoordinates (double X, double Y, boolean DoConvertTCanvasCoords)
    {        
        Point Result = new Point();
        if (DoConvertTCanvasCoords)
        {
            Result.x =                    (int)  Math.round(X * FRapportME) + FTransfoInt.x;
            Result.y = this.getHeight() - (int) (Math.round(Y * FRapportME) + FTransfoInt.y);
        }
        else
        {
            Result.x = (int) Math.round(X * FRapportME) + FTransfoInt.x;
            Result.y = (int) Math.round(Y * FRapportME) + FTransfoInt.y;
        } 
        //afficherMessage(sprintf("getScreenCoordinates(%.2f, %.2f -> %d, %d)", X, Y, Result.x, Result.y));
        return Result;
    }
    // calcul de la polygonale
    private void calcProjPolygonale(boolean doDestCanvas)
    { 
        int Nb = myTableEntites.getNbEntites();
        double d1 = 0.00;
        Point P = new Point();
        afficherMessageFmt("CalcProjPolygonale: %d entites", Nb);
        // vide la table des polygonales et ajoute la visée 0
        TViseesTopo2D  V = new TViseesTopo2D();
        FPolygonale.clear();
        FPolygonale.add(V);
        //--------------------
       
        
        for (int i = LOWINDEX; i < Nb; i++)
        {
            V = new TViseesTopo2D(); // créer toujours un nouvel objet
            TEntiteEtendue E = myTableEntites.getEntite(i);
            
            V.ColorEntite = myTableEntites.getColorViseeFromModeRepresentation(fModeRepresentationGaleries, E);
            V.TypeEntite  = E.TypeEntite;
            V.DateLeve    = E.DateLeve;
            TPoint2DDepth PtOut = get2DDepthCoordinates(E.UneStation1X,
                                                        E.UneStation1Y,
                                                        E.UneStation1Z); 
            V.UneStation1X = PtOut.X;
            V.UneStation1Y = PtOut.Y;
            P = getScreenCoordinates(PtOut.X, PtOut.Y, doDestCanvas);
            V.CoordEcrSt1X = P.x;
            V.CoordEcrSt1Y = P.y;
            d1 = PtOut.Depth;
            PtOut = get2DDepthCoordinates(E.UneStation2X,
                                          E.UneStation2Y,
                                          E.UneStation2Z);
            V.UneStation2X = PtOut.X;
            V.UneStation2Y = PtOut.Y;
            V.Depth           = 0.50 * (d1+PtOut.Depth);
            P = getScreenCoordinates(PtOut.X, PtOut.Y, doDestCanvas);
            V.CoordEcrSt2X = P.x;
            V.CoordEcrSt2Y = P.y;
            V.Drawn = E.Drawn;  
            FPolygonale.add(V);
            
        }
        /*
        for (int i = LOWINDEX; i < Nb; i++)
        {
            V = FPolygonale.get(i);
            afficherMessage(sprintf("--- %d - %f %f - %d, %d", i, V.UneStation2X, V.UneStation2Y, V.CoordEcrSt2X, V.CoordEcrSt2Y));
        }    
        //*/
    } 
    // paramètres de transformation angles en degrés
    private void setParamTransformation(double QTheta, double QPhi, double QZoom, double QMagnification, boolean DoDestCanvas, boolean DoRegenerer)
    {
        afficherMessage("setParamTransformation");
        
        TPoint2DDepth c;
        TPoint3Df cnMini;
        TPoint3Df cnMaxi;
        
        fZoom   = QZoom;
        fTheta  = QTheta;
        fPhi    = QPhi;
        fMagnification = QMagnification;
        
        FMatAux[0] = 0.00;
        FMatAux[1] = Math.sin(fTheta * PI180);
        FMatAux[2] = Math.sin(fPhi   * PI180);          // | -sin(A)           +cos(A)         0           0 | | x |   | X |
        FMatAux[3] = Math.cos(fTheta * PI180);          // |                                                 | |   |   |   |
        FMatAux[4] = Math.cos(fPhi   * PI180);          // | -cos(A).sin(P)    -sin(A).sin(P)  cos(A)      0 | | y |   | Y |
        FMatAux[5] = FMatAux[3] * FMatAux[2];           // |                                                 |*|   | = |   |
        FMatAux[6] = FMatAux[1] * FMatAux[2];           // | -cos(A).cos(P)    -sin(A).cos(P)  -sin(P)     R | | z |   | Z |
        FMatAux[7] = FMatAux[3] * FMatAux[4];           // |                                                 | |   |   |   |
        FMatAux[8] = FMatAux[1] * FMatAux[4];           // | 0                 0               0           1 | | 1 |   | 1 |
        FTransfoInt.x = trunc(this.getWidth() / 2);
        FTransfoInt.y = trunc(this.getHeight() / 2);
        FRapportME    = (this.getWidth() / FLongueurDiagonale) * fZoom;
        // direction d'observation
        FLookAt = makeTPoint3Df(Math.cos(fPhi * PI180)*Math.cos(fTheta * PI180),
                                Math.cos(fPhi * PI180)*Math.sin(fTheta * PI180),
                                Math.sin(fPhi * PI180));
        // cube
        cnMini = myTableEntites.getCoinBasGauche();
        cnMaxi = myTableEntites.getCoinHautDroit();
        //FCubeEnglobant[0].x = 0;
        //FCubeEnglobant[0].y = 0;
        c = get2DDepthCoordinates(cnMini.X, cnMini.Y, cnMini.Z);
        FCubeEnglobant[1] = getScreenCoordinates(c.X, c.Y, FDoDestTCanvas);
        c = get2DDepthCoordinates(cnMaxi.X, cnMini.Y, cnMini.Z);
        FCubeEnglobant[2] = getScreenCoordinates(c.X, c.Y, FDoDestTCanvas);
        c = get2DDepthCoordinates(cnMaxi.X, cnMaxi.Y, cnMini.Z);
        FCubeEnglobant[3] =getScreenCoordinates(c.X, c.Y, FDoDestTCanvas);
        c = get2DDepthCoordinates(cnMini.X, cnMaxi.Y, cnMini.Z);
        FCubeEnglobant[4] = getScreenCoordinates(c.X, c.Y, FDoDestTCanvas);
        c = get2DDepthCoordinates(cnMini.X, cnMini.Y, cnMaxi.Z);
        FCubeEnglobant[5] = getScreenCoordinates(c.X, c.Y, FDoDestTCanvas);
        c = get2DDepthCoordinates(cnMaxi.X, cnMini.Y, cnMaxi.Z);
        FCubeEnglobant[6] = getScreenCoordinates(c.X, c.Y, FDoDestTCanvas);
        c = get2DDepthCoordinates(cnMaxi.X, cnMaxi.Y, cnMaxi.Z);
        FCubeEnglobant[7] = getScreenCoordinates(c.X, c.Y, FDoDestTCanvas);
        c = get2DDepthCoordinates(cnMini.X, cnMaxi.Y, cnMaxi.Z);
        FCubeEnglobant[8] = getScreenCoordinates(c.X, c.Y, FDoDestTCanvas);
        calcProjPolygonale(FDoDestTCanvas);
        fDoToutRegenerer = DoRegenerer;
        if (fDoToutRegenerer)
        {                
            // précalculer les volumes
            makeVolumesCavite();
            precalculVolumesCavite(FDoDestTCanvas);
            qSortDatasByDepth();
        }  
        fCanDraw = true;
    }
    // remise à zéro des paramètres de transformation
    private void ResetParamTransformation()
    {
        fTranslater.x = 0;
        fTranslater.y = 0;
        TPoint3Df cnMini = myTableEntites.getCoinBasGauche();
        TPoint3Df cnMaxi = myTableEntites.getCoinHautDroit();
        FLongueurDiagonale = hypot3D(cnMaxi.X - cnMini.X,
                                     cnMaxi.Y - cnMini.Y,
                                     cnMaxi.Z - cnMini.Z);
        FOffset = makeTPoint3Df(-(cnMini.X+ 0.5*(cnMaxi.X - cnMini.X)),
                                -(cnMini.Y+ 0.5*(cnMaxi.Y - cnMini.Y)),
                                -(cnMini.Z+ 0.5*(cnMaxi.Z - cnMini.Z)));
        setParamTransformation(45.00, 32.00, 1.00, 1.00, FDoDestTCanvas, true);
        afficherMessage(sprintf("ResetParamTransformation:   Offset: %.2f - %.2f - %.2f", FOffset.X, FOffset.Y, FOffset.Z));
        
    } 
    // tri en profondeur
    private void qSortPolygonaleByDepth()
    {
        Collections.sort(this.FPolygonale, new Comparator<TViseesTopo2D>() 
        {
            @Override
            public int compare(TViseesTopo2D p1,  TViseesTopo2D p2) 
            {
                if      (p1.Depth > p2.Depth) return  1;
                else if (p1.Depth < p2.Depth) return -1;
                else                          return  0;
            }    
        });
        
    }
    private void qSortQuadsByDepth()
    {
        Collections.sort(this.FTableQuads, new Comparator<TQuad>() 
        {
            @Override
            public int compare(TQuad p1,  TQuad p2) 
            {
                if      (p1.Depth > p2.Depth) return -1;
                else if (p1.Depth < p2.Depth) return +1;
                else                          return  0;
            }    
        });
    }
    // trier la topo
    private void qSortDatasByDepth()
    {
        qSortPolygonaleByDepth();
        qSortQuadsByDepth();
    }    
   
    // fabrication des volumes de la cavité
    private void makeAndAddQuad(int NQ, Color C, int TE, boolean DR, int QIdxReseau, int QIdxSecteur, int QIdxExpe, 
                          TPoint3Df P1, TPoint3Df P2, TPoint3Df P3, TPoint3Df P4)
    {        
        TQuad Q = new TQuad();
        Q.Drawn   = DR;
        Q.VertexA = P1;
        Q.VertexB = P2;
        Q.VertexC = P3;
        Q.VertexD = P4;
        Q.Couleur = C;
        Q.TypeEntite = TE;
        Q.Visible = true;
        Q.IdxReseau    =  QIdxReseau;
        Q.IdxSecteur   =  QIdxSecteur;
        Q.IdxExpe      =  QIdxExpe;
        FTableQuads.add(Q);
    }    
     private void makeVolumesCavite()
    {
        // vide la table
        FTableQuads.clear();
        // ajoute le quad 0, inutilisé par la suite
        TQuad Q = new TQuad();
        FTableQuads.add(Q);
        int Nb = myTableEntites.getNbEntites();
        afficherMessage(sprintf("-- MakeVolumesCavite: %d entites", Nb));
        TPoint3Df P1 = new TPoint3Df();
        TPoint3Df P2 = new TPoint3Df();
        TPoint3Df P3 = new TPoint3Df();
        TPoint3Df P4 = new TPoint3Df();
        for (int i = LOWINDEX; i < Nb; i++)
        {
            TEntiteEtendue E = myTableEntites.getEntite(i);
            Color QC = myTableEntites.getColorViseeFromModeRepresentation(fModeRepresentationGaleries, E);
             // paroi gauche Quad 1
            P1 = makeTPoint3Df(E.X2PG, E.Y2PG, E.Z2PB);
            P2 = makeTPoint3Df(E.X1PG, E.Y1PG, E.Z1PB);
            P3 = makeTPoint3Df(E.X1PG, E.Y1PG, E.Z1PH);
            P4 = makeTPoint3Df(E.X2PG, E.Y2PG, E.Z2PH);
            makeAndAddQuad(NBFACESBYVISEE*(i-1) + 1, QC, E.TypeEntite, E.Drawn, E.eReseau, E.eSecteur, E.eExpe, P1, P2, P3, P4);
            // paroi droite Quad 2
            P1 = makeTPoint3Df(E.X1PD, E.Y1PD, E.Z1PB);
            P2 = makeTPoint3Df(E.X2PD, E.Y2PD, E.Z2PB);
            P3 = makeTPoint3Df(E.X2PD, E.Y2PD, E.Z2PH);
            P4 = makeTPoint3Df(E.X1PD, E.Y1PD, E.Z1PH);
            makeAndAddQuad(NBFACESBYVISEE*(i-1) + 2, QC, E.TypeEntite, E.Drawn, E.eReseau, E.eSecteur, E.eExpe, P1, P2, P3, P4);
            // paroi de dessus Quad 3
            P1 = makeTPoint3Df(E.X1PD, E.Y1PD, E.Z1PH);
            P2 = makeTPoint3Df(E.X2PD, E.Y2PD, E.Z2PH);
            P3 = makeTPoint3Df(E.X2PG, E.Y2PG, E.Z2PH);
            P4 = makeTPoint3Df(E.X1PG, E.Y1PG, E.Z1PH);
            makeAndAddQuad(NBFACESBYVISEE*(i-1) + 3, QC, E.TypeEntite, E.Drawn, E.eReseau, E.eSecteur, E.eExpe, P1, P2, P3, P4);
            // paroi de dessous Quad 4
            P1 = makeTPoint3Df(E.X1PG, E.Y1PG, E.Z1PB);
            P2 = makeTPoint3Df(E.X2PG, E.Y2PG, E.Z2PB);
            P3 = makeTPoint3Df(E.X2PD, E.Y2PD, E.Z2PB);
            P4 = makeTPoint3Df(E.X1PD, E.Y1PD, E.Z1PB);
            makeAndAddQuad(NBFACESBYVISEE *(i-1) + 4, QC, E.TypeEntite, E.Drawn, E.eReseau, E.eSecteur, E.eExpe, P1, P2, P3, P4);
            // paroi de face
            P1 = makeTPoint3Df(E.X1PG, E.Y1PG, E.Z1PB);
            P2 = makeTPoint3Df(E.X1PD, E.Y1PD, E.Z1PB);
            P3 = makeTPoint3Df(E.X1PD, E.Y1PD, E.Z1PH);
            P4 = makeTPoint3Df(E.X1PG, E.Y1PG, E.Z1PH);
            makeAndAddQuad(NBFACESBYVISEE *(i-1) + 5, QC, E.TypeEntite, E.Drawn, E.eReseau, E.eSecteur, E.eExpe, P1, P2, P3, P4);
            // paroi de derrière
            P1 = makeTPoint3Df(E.X2PD, E.Y2PD, E.Z2PB);
            P2 = makeTPoint3Df(E.X2PG, E.Y2PG, E.Z2PB);
            P3 = makeTPoint3Df(E.X2PG, E.Y2PG, E.Z2PH);
            P4 = makeTPoint3Df(E.X2PD, E.Y2PD, E.Z2PH);
            
            makeAndAddQuad(NBFACESBYVISEE *(i-1) + 6, QC, E.TypeEntite, E.Drawn, E.eReseau, E.eSecteur, E.eExpe, P1, P2, P3, P4); 
        }    
    }
    // precalculVolumesCavite(boolean b) 
    private void calc3DQuad(int Idx, TQuad QD)
    {
        TPoint2DDepth PP = new TPoint2DDepth();
        double PS;
        double NW;
        TPoint3Df M  = new TPoint3Df();
        TPoint3Df U  = new TPoint3Df();
        TPoint3Df V  = new TPoint3Df();
        TPoint3Df W1 = new TPoint3Df();
        TPoint3Df W2 = new TPoint3Df();
        PP = get2DDepthCoordinates(QD.VertexA.X, QD.VertexA.Y, QD.VertexA.Z);
        QD.Vertex2DA  =  getScreenCoordinates(PP.X, PP.Y, FDoDestTCanvas);
        PP = get2DDepthCoordinates(QD.VertexB.X, QD.VertexB.Y, QD.VertexB.Z);
        QD.Vertex2DB  =  getScreenCoordinates(PP.X, PP.Y, FDoDestTCanvas);
        PP = get2DDepthCoordinates(QD.VertexC.X, QD.VertexC.Y, QD.VertexC.Z);
        QD.Vertex2DC  =  getScreenCoordinates(PP.X, PP.Y, FDoDestTCanvas);
        PP = get2DDepthCoordinates(QD.VertexD.X, QD.VertexD.Y, QD.VertexD.Z);
        QD.Vertex2DD  =  getScreenCoordinates(PP.X, PP.Y, FDoDestTCanvas);
        //test de visibilité
        U  =  makeTPoint3Df(QD.VertexB.X - QD.VertexA.X, QD.VertexB.Y - QD.VertexA.Y, QD.VertexB.Z - QD.VertexA.Z);
        V  =  makeTPoint3Df(QD.VertexC.X - QD.VertexA.X, QD.VertexC.Y - QD.VertexA.Y, QD.VertexC.Z - QD.VertexA.Z);
        W1 = produitVectoriel(U,V, false);
        U  =  makeTPoint3Df(QD.VertexC.X - QD.VertexA.X, QD.VertexC.Y - QD.VertexA.Y, QD.VertexC.Z - QD.VertexA.Z);
        V  =  makeTPoint3Df(QD.VertexD.X - QD.VertexA.X, QD.VertexD.Y - QD.VertexA.Y, QD.VertexD.Z - QD.VertexA.Z);
        W2 = produitVectoriel(U,V, false);
        W1.X  =  W1.X + W2.X;
        W1.Y  =  W1.Y + W2.Y;
        W1.Z  =  W1.Z + W2.Z;
        NW = 1e-12 + hypot3D(W1.X, W1.Y, W1.Z);
        PS = W1.X * FLookAt.X +
             W1.Y * FLookAt.Y +
             W1.Z * FLookAt.Z;
        QD.Visible = ((PS / NW) > 0);
        // calcul de profondeur
        M  =  makeTPoint3Df(0.25*(QD.VertexA.X + QD.VertexB.X + QD.VertexC.X + QD.VertexD.X),
                            0.25*(QD.VertexA.Y + QD.VertexB.Y + QD.VertexC.Y + QD.VertexD.Y),
                            0.25*(QD.VertexA.Z + QD.VertexB.Z + QD.VertexC.Z + QD.VertexD.Z));        
        QD.Depth  =  -(M.X + FOffset.X) * FMatAux[7]
                     -(M.Y + FOffset.Y) * FMatAux[8] 
                     -(M.Z + FOffset.Z) * FMatAux[2];
        FTableQuads.set(Idx, QD);
    }       
    
    private void precalculVolumesCavite(boolean b)
    {
        int Nb = myTableEntites.getNbEntites();
        int iq = 0;
        for (int i = LOWINDEX; i < Nb; i++)
        {
            int n = i - 1;
            iq = NBFACESBYVISEE * n + 1;
            calc3DQuad(iq, FTableQuads.get(iq));
            iq = NBFACESBYVISEE * n + 2;
            calc3DQuad(iq, FTableQuads.get(iq));
            iq = NBFACESBYVISEE * n + 3;
            calc3DQuad(iq, FTableQuads.get(iq));
            iq = NBFACESBYVISEE * n + 4;
            calc3DQuad(iq, FTableQuads.get(iq));
            iq = NBFACESBYVISEE * n + 5;
            calc3DQuad(iq, FTableQuads.get(iq));
            iq = NBFACESBYVISEE * n + 6;
            calc3DQuad(iq, FTableQuads.get(iq));
        }   
    }        
    //--------------------------------------------------------------------------
    // dessin
    // couleurs
    private void setBackColor(Graphics g, Color c)
    {
        this.fBackColor = c;
        g.setColor(c);
    }    
    private void setLineColor(Graphics g, Color c)
    {
        this.fLineColor = c;
        g.setColor(c);
    }        
    private void setFillColor(Graphics g, Color c)
    {
        this.fFillColor = c;
        g.setColor(c); 
    }     
    private void drawPolygonales(Graphics g)
    {
        final int sz = 4;
        int Nb = FPolygonale.size();
        afficherMessage(sprintf("  --> Draw Polygonales: %d entites", Nb));
        
        for (int i = LOWINDEX; i < Nb; i++)
        {
            TViseesTopo2D P = FPolygonale.get(i);
            // TODO: Traitement du P.Drawn 
            if (P.TypeEntite != tgENTRANCE)
            {
                setLineColor(g, Color.BLUE);
                g.drawLine(P.CoordEcrSt1X, P.CoordEcrSt1Y, P.CoordEcrSt2X, P.CoordEcrSt2Y);
            }    
        }    
    } 
    //--------------------------------------------------------------------------
    private void dessinerQuad(Graphics g, TQuad Q)
    {
        if (Q.Visible)
        {    
            Polygon qd = new Polygon();
            qd.addPoint(Q.Vertex2DA.x, Q.Vertex2DA.y);
            qd.addPoint(Q.Vertex2DB.x, Q.Vertex2DB.y);
            qd.addPoint(Q.Vertex2DC.x, Q.Vertex2DC.y);
            qd.addPoint(Q.Vertex2DD.x, Q.Vertex2DD.y);
            qd.addPoint(Q.Vertex2DA.x, Q.Vertex2DA.y);      

            //g.setColor(Color.gray);
            
            g.setColor(Color.gray);
            g.fillPolygon(qd);
            g.setColor(Color.BLACK);
            g.drawPolygon(qd);
        }
    }        
    private void drawVolumes(Graphics g)
    {
        int Nb = myTableEntites.getNbEntites();
        afficherMessage(sprintf("  --> Draw Volumes: %d entites", Nb));
        for (int i = 2; i < Nb; i++)
        {    
            dessinerQuad(g, FTableQuads.get(NBFACESBYVISEE *(i-1) + 1));
            dessinerQuad(g, FTableQuads.get(NBFACESBYVISEE *(i-1) + 2));
            dessinerQuad(g, FTableQuads.get(NBFACESBYVISEE *(i-1) + 3));
            dessinerQuad(g, FTableQuads.get(NBFACESBYVISEE *(i-1) + 4));
            dessinerQuad(g, FTableQuads.get(NBFACESBYVISEE *(i-1) + 5));
            dessinerQuad(g, FTableQuads.get(NBFACESBYVISEE *(i-1) + 6));
        }
    }  
    //--------------------------------------------------------------------------
    private void drawCube(Graphics g) // OK
    {
        afficherMessage("  --> Cube");
        setLineColor(g, Color.RED);
        int s = 0;
        for (int i = 1; i <=8; i++)
        {
            FCubeEnglobant[i].x += fTranslater.x;
            FCubeEnglobant[i].y += fTranslater.y;
        }    
        for (int i = 1; i <= 4; i++) 
        {
            g.drawLine(FCubeEnglobant[i].x, FCubeEnglobant[i].y, 
                       FCubeEnglobant[i+4].x, FCubeEnglobant[i+4].y);
            s = i - 1;
            if (0 == s) s = 4;
            g.drawLine(FCubeEnglobant[s].x, FCubeEnglobant[s].y, FCubeEnglobant[i].x, FCubeEnglobant[i].y);
            s = 4 + (i - 1);
            if (4 == s) s = 8;
            g.drawLine(FCubeEnglobant[s].x, FCubeEnglobant[s].y, FCubeEnglobant[i+4].x, FCubeEnglobant[i+4].y);
        }
    }        
    public void paint(Graphics g)        
    {
        try
        {    
            afficherMessage("paintComponent");
            // Appel de la méthode paintComponent de la classe mère
            //super.paintComponent(g);
            // Conversion en un contexte 2D
            Graphics2D g2 = (Graphics2D) g;
            //Graphics2D g2 = (Graphics2D) g; 
            int h = this.getHeight();
            int w = this.getWidth();

            this.setBackColor(g2, Color.WHITE);
            g2.fillRect(0, 0, w, h);
            // polygonale
            drawPolygonales(g);
            if (fDoToutRegenerer) drawVolumes(g);
            // cube
            drawCube(g);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        } 
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 402, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        curMousePosXY = this.getMousePosition();
    }//GEN-LAST:event_formMouseMoved

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        // TODO add your handling code here:
        int buttonDown  = evt.getButton();
        int shitKey     = evt.getModifiers();
        boolean keyShiftDown = (shitKey & evt.SHIFT_MASK) != 0;
        boolean keyCtrlDown  = (shitKey & evt.CTRL_MASK)  != 0;
        curMousePosXY = this.getMousePosition();
        try
        {    
            afficherMessage(sprintf("Bouton: %d - Masque: %d", buttonDown, (evt.BUTTON1_MASK)));
            boolean sx = (curMousePosXY.x - oldMousePosXY.x) > 0; 
            boolean sy = (curMousePosXY.y - oldMousePosXY.y) > 0; 
            double IncTheta = (sx) ? 1.00 : -1.00; 
            double IncPhi   = (sy) ? 1.00 : -1.00; 
            //fTheta += IncTheta; 
            //fPhi   += IncPhi; 
            fTheta += (double) (curMousePosXY.x - oldMousePosXY.x);
            fPhi   += (double) (curMousePosXY.y - oldMousePosXY.y);
            
            if (fTheta > 360.0) fTheta = 0.0;
            if (fPhi >= 90.0) fPhi = 90.0;
            if (fPhi <   0.0) fPhi =  0.0;
            
            setParamTransformation(fTheta, fPhi, fZoom, fMagnification, FDoDestTCanvas, false);
            this.repaint();
         
//            if ((buttonDown & evt.BUTTON2_MASK) != 0)
//            {
//                int dx = curMousePosXY.x - oldMousePosXY.x; 
//                int dy = curMousePosXY.y - oldMousePosXY.y; 
//                fTranslater.x += dx;
//                fTranslater.y += dy;
//                setParamTransformation(fTheta, fPhi, fZoom, fMagnification, FDoDestTCanvas);
//                //calcProjPolygonale(FDoDestTCanvas);
//                this.repaint();
//            }
        }
        catch (Exception e)
        {
            ;
        }    
        oldMousePosXY = curMousePosXY;
        //afficherMessage(sprintf("%f, %f", fTheta, fPhi));
    }//GEN-LAST:event_formMouseDragged

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        // TODO add your handling code here:
        setParamTransformation(fTheta, fPhi, fZoom, fMagnification, FDoDestTCanvas, true);
        this.repaint();
         
    }//GEN-LAST:event_formMouseReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
