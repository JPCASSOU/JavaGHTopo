/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ghtopo.javaghtopo;

import static GHTopoOperationnel.Constantes.*;
import static GHTopoOperationnel.GeneralFunctions.*;
import static GHTopoOperationnel.GeneralFunctions.makeTPoint2Df;
import static GHTopoOperationnel.GeneralFunctions.trunc;
import GHTopoOperationnel.TTableDesEntites;
import GHTopoOperationnel.Types.TEntiteEtendue;
import GHTopoOperationnel.Types.TEntrance;
import GHTopoOperationnel.Types.TPoint2Df;
import GHTopoOperationnel.Types.TPoint3Df;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import javax.swing.JLabel;

/**
 *
 * @author jean-pierre.cassou
 */
public class TVue2D extends javax.swing.JPanel {
    // membres privés
    private TTableDesEntites myFTableEntites;
    private long fElementsDrawn = 0; 
    private int  fModeReprReseaux = rgDEPTH;
    // peut-on dessiner ?
    private boolean myFCanDraw;
    // coin bas et haut
    private TPoint3Df cnMini;
    private TPoint3Df cnMaxi;

    // limites de la vue
    private double myFRegionXMini;
    private double myFRegionXMaxi;
    private double myFRegionYMini;
    private double myFRegionYMaxi;
    // paramètres internes de vue
    private double myFRappHLVue;
    private double myFRappScrReal;
    private double myFInvRappScrReal;
    // couleurs
    private Color fBackColor;
    private Color fLineColor;
    private Color fFillColor;
    private Color fQuadrillesColor;
    // quadrillage
    private double fEspcQuadrilles;
    
    // gestion de la souris
    private Point mouseXY;
    private Point oldmouseXY;
    
    private TPoint2Df mouseCoordsReelles;
    
    // conteneurs pour passage de paramètres avec l'extérieur
    private JLabel myLabelExterneCoordsSouris;
    private JLabel myLabelExterneDescStation;
    // MétaFiltre
    public void appliquerMetaFiltre(String filtres)
    {
        myFTableEntites.applyMetaFiltre(filtres);
        repaint();
    }        
    //--------------------------------------------------------------------------
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
    
    // coordonnées
    private Point getCoordsPlan(TPoint2Df PM)
    {
        Point result = new Point();
        if (myFCanDraw)
        {    
            result.x = trunc((PM.X - this.myFRegionXMini) * this.myFRappScrReal);
            result.y = trunc((this.myFRegionYMaxi - PM.Y) * this.myFRappScrReal);
            return result;
        }
        else
        {
            result.x = 0;
            result.y = 0;  //'512 720
            return result;
        }    
    }
    private TPoint2Df getCoordsMonde(Point PP)
    {
        TPoint2Df result = new TPoint2Df();
        if (myFCanDraw)
        {    
            result.X =  this.myFInvRappScrReal * PP.x + this.myFRegionXMini;
            result.Y = -this.myFInvRappScrReal * PP.y + this.myFRegionYMaxi;
            return result;
        }
        else
        {
            result = makeTPoint2Df(0.00, 0.00);
            return result;
        }    
    }
    //--------------------------------------------------------------------------
    public TPoint2Df getCoordsReellesSouris()
    {
        return mouseCoordsReelles;
    }        
    //--------------------------------------------------------------------------
    private double getRYMaxi()
    {
        // calcul du rapport Hauteur/largeur de vueint qw = this.getHeight();
        int qh = this.getWidth(); 
        int qw = this.getHeight();
        this.myFRappHLVue = (double) qh / (double) qw;       
        afficherMessage(sprintf("GetYMaxi: w= %d - h = %d",  qw, qh ));
        
        // calcul du rapport Ecran/Réel
        this.myFRappScrReal = qw / (this.myFRegionXMaxi - this.myFRegionXMini);
        this.myFInvRappScrReal = 1 / this.myFRappScrReal;
        // calcul de la hauteur de visualisation
        afficherMessage(sprintf("GetYMaxi: %f - %f, %f, R =%.3f", this.myFRegionYMini, this.myFRegionXMaxi, this.myFRegionXMini, this.myFRappHLVue));
        return this.myFRegionYMini + (this.myFRegionXMaxi - this.myFRegionXMini) * this.myFRappHLVue;
    }        
    public void setViewLimits(double x1, double y1, double x2, double y2)
    {
        afficherMessage(sprintf("-- SetViewLimits(%.2f, %.2f, %.2f, %.2f", x1, y1, x2, y2));
        double  qX1 = x1;
        double  qY1 = y1;
        double  qX2 = x2;
        double  qY2 = y2;
        // TODO: Sens du rectangle de sélection
        
        this.myFRegionXMini = qX1;
        this.myFRegionXMaxi = qX2;
        this.myFRegionYMini = qY1;
        this.myFRegionYMaxi = qY2;
        // Redéfinition de la hauteur maxi
        this.myFRegionYMaxi = this.getRYMaxi();
        afficherMessage(sprintf("-- SetViewLimits(%.2f, %.2f, %.2f, %.2f",  this.myFRegionXMini,  this.myFRegionYMini,  this.myFRegionXMaxi,  this.myFRegionYMaxi));
        this.repaint(); 
    }  
    public void resetVue()
    {        
       double m = 0.00;
       
       if (this.myFCanDraw)
       {            
            try
            {
                cnMini = myFTableEntites.getCoinBasGauche();
                cnMaxi = myFTableEntites.getCoinHautDroit();// marge périmétrique
                myFTableEntites.calcCouleursByDepth();
                cnMini.X += (-m);
                cnMini.Y += (-m);
                cnMaxi.X +=   m;
                cnMaxi.Y +=   m;
                double DX = (cnMaxi.X - cnMini.X);
                double DY = (cnMaxi.Y - cnMini.Y);
                afficherMessageFmt("ResetVue: %f, %f -> %f, %f", cnMini.X, cnMini.Y, cnMaxi.X, cnMaxi.Y);
                double L = (DX > DY) ? DX : DY;
                //L *= (this.getWidth() / this.getHeight());
                //this.setViewLimits(cnMini.X, cnMini.Y, cnMini.X + L, cnMini.Y+L);
                this.setViewLimits(cnMini.X, cnMini.Y, cnMaxi.X, cnMaxi.Y);
                this.repaint();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }   
       }
    }
   
   
    private void drawLigne(Graphics G, double x1, double y1, double x2, double y2)
    {
        TPoint2Df pm1 = makeTPoint2Df(x1, y1);
        TPoint2Df pm2 = makeTPoint2Df(x2, y2);
        Point p1 = this.getCoordsPlan(pm1);
        Point p2 = this.getCoordsPlan(pm2);
        G.drawLine(p1.x, p1.y, p2.x, p2.y);
    } 
    private void drawCircle(Graphics G, double x1, double y1, int r)
    {
        TPoint2Df pm1 = makeTPoint2Df(x1, y1);
        Point p1 = this.getCoordsPlan(pm1);
        G.setColor(Color.BLUE);
        G.fillOval(p1.x - r, p1.y - r, 2 * r, 2 * r);
        G.setColor(Color.RED);
        G.drawOval(p1.x - r, p1.y - r, 2 * r, 2 * r);
    }        
    private void drawFillVisee(Graphics G, TEntiteEtendue v, boolean DoDrawParois, int moderep)
    {
        if (v.Drawn)
        {   
            Polygon qd = new Polygon();
            Point P0 = this.getCoordsPlan(makeTPoint2Df(v.X1PD, v.Y1PD));
            Point P1 = this.getCoordsPlan(makeTPoint2Df(v.X2PD, v.Y2PD));
            Point P2 = this.getCoordsPlan(makeTPoint2Df(v.X2PG, v.Y2PG));
            Point P3 = this.getCoordsPlan(makeTPoint2Df(v.X1PG, v.Y1PG));
            qd.addPoint(P0.x, P0.y);
            qd.addPoint(P1.x, P1.y);
            qd.addPoint(P2.x, P2.y);
            qd.addPoint(P3.x, P3.y);

            //setFillColor(G, Color.MAGENTA);
            Color C = myFTableEntites.getColorViseeFromModeRepresentation(moderep, v);
            C = new Color(C.getRed(), C.getGreen(), C.getBlue(), 192); // transparence
            setFillColor(G, C);
            G.fillPolygon(qd);
            if (DoDrawParois)
            {
                setLineColor(G, Color.BLACK);
                G.drawLine(qd.xpoints[0], qd.ypoints[0], qd.xpoints[1], qd.ypoints[1]);
                G.drawLine(qd.xpoints[3], qd.ypoints[3], qd.xpoints[2], qd.ypoints[2]);
            }
        }
    }  
    public void setQuadrillageSpc(double Spacing)
    {
        fEspcQuadrilles = Spacing;
    }      
    public double getQuadrillageSpc()
    {
        return fEspcQuadrilles;
    }      
    public void setQuadrillageColor(Color c)
    {
        fQuadrillesColor = c;
    }      
    public Color  getQuadrillageColor()
    {
        return fQuadrillesColor;
    }      
 
    private void drawQuadrilles(Graphics G, Color c, int qWidth, int qHeight)
    {
        afficherMessageFmt("DrawQuadrilles: Espacement: %.0f", fEspcQuadrilles);
        TPoint2Df C1VueCourante = makeTPoint2Df(0.00, 0.00);
        TPoint2Df C2VueCourante = makeTPoint2Df(0.00, 0.00);
        TPoint2Df C1 = makeTPoint2Df(0.00, 0.00);
        TPoint2Df C2 = makeTPoint2Df(0.00, 0.00);
        G.setColor(c);
        Point PP1 = new Point(0, 0);
        Point PP2 = new Point(0, 0);
        PP1.x = 0;
        PP1.y = 0;
        C1VueCourante = getCoordsMonde(PP1);
        PP2.x = qWidth;
        PP2.y = qHeight;
        C2VueCourante = getCoordsMonde(PP2);
        int t = 0;
        double A = 0.00;
        t = trunc(C1VueCourante.X / fEspcQuadrilles);
        A = fEspcQuadrilles * t;
        afficherMessage(" |  |  |  |");
        afficherMessageFmt("- C1: %.0f, %.0f --  C2:  %.0f, %.0f - A = %.0f", C1.X, C1.Y, C2.X, C2.Y, A);
        while (A < C2VueCourante.X) 
        {
            C1 = makeTPoint2Df(A, C1VueCourante.Y);
            C2 = makeTPoint2Df(A, C2VueCourante.Y);
            drawLigne(G, C1.X, C1.Y, C2.X, C2.Y);
            afficherMessageFmt("- %.0f, %.0f, %.0f, %.0f", C1.X, C1.Y, C2.X, C2.Y);
            //-------- Coordonnées en rive
            /*S:=Format('%.0f',[A]);
            q:=P1.X - TmpBuffer.Canvas.TextWidth(S) div 2;
            TmpBuffer.Canvas.TextOut(q, 1, S);
            //---------------------------- */
            A += fEspcQuadrilles;
        }
        t = trunc(C1VueCourante.Y / fEspcQuadrilles);
        A = fEspcQuadrilles * t;
        afficherMessage("-----------");
        afficherMessageFmt("- C1: %.0f, %.0f --  C2:  %.0f, %.0f - A = %.0f", C1.X, C1.Y, C2.X, C2.Y, A);
        while (A > C2VueCourante.Y) 
        {
            C1 = makeTPoint2Df(C1VueCourante.X, A);
            C2 = makeTPoint2Df(C2VueCourante.X, A);
            drawLigne(G, C1.X, C1.Y, C2.X, C2.Y);
            afficherMessageFmt("- %.0f, %.0f, %.0f, %.0f", C1.X, C1.Y, C2.X, C2.Y);
             /*/-------- Coordonnées en rive
            S:=Format('%.0f',[A]);
            q:=P1.Y - TmpBuffer.Canvas.TextHeight(S) div 2;
            TmpBuffer.Canvas.TextOut(2, q, S);
            //----------------------------*/
            A -= fEspcQuadrilles;
        }
    }        
    public void setElementsDrawn(long qElementsDrawn)
    {
        fElementsDrawn = qElementsDrawn;
        this.repaint();
    }
    public long getElementsDrawn()
    {
        return fElementsDrawn;
    }   
    public void setModeRepresentationReseau(int m)
    {
        fModeReprReseaux = m;
        this.repaint();
    }  
    public int getModeRepresentationReseau()
    {
        return fModeReprReseaux;
    }  
    
    /**
     * Creates new form TVue2D
     */
    public TVue2D() 
    {
        myFCanDraw = false;
        fEspcQuadrilles = 100.00;
        fQuadrillesColor = new Color(80,80,80);
        initComponents();  
        // éléments dessinés
        fElementsDrawn = Include(edPolygonals    , fElementsDrawn);
        //fElementsDrawn = Include(edFillGalerie   , fElementsDrawn);
        //fElementsDrawn = Include(edWalls         , fElementsDrawn);
        fElementsDrawn = Include(edCrossSections , fElementsDrawn);
        fElementsDrawn = Include(edQuadrilles    , fElementsDrawn);
        fElementsDrawn = Include(edStations      , fElementsDrawn);
        fElementsDrawn = Include(edENTRANCES     , fElementsDrawn);
        
    }
    // dessin des éléments du plan
    private void drawCrossSections(Graphics2D g)
    {
        int nb = this.myFTableEntites.getNbEntites();
        TEntiteEtendue EE;
        this.setLineColor(g, Color.GRAY);
        for (int i = 1; i < nb; i++)
        {
            EE = this.myFTableEntites.getEntite(i);
            if (EE.TypeEntite != tgENTRANCE) drawLigne(g, EE.X2PD, EE.Y2PD, EE.X2PG, EE.Y2PG);
        }
        
    }  
    private void drawSilhouette(Graphics2D g)
    {
        //myFTableEntites.calcCouleursByDepth();
        int nb = this.myFTableEntites.getNbEntites();
        TEntiteEtendue EE;
        for (int i = 0; i < nb; i++)            
        {
            EE = this.myFTableEntites.getEntite(i);
            if (EE.TypeEntite != tgENTRANCE) drawFillVisee(g, EE, true, fModeReprReseaux);
        }
    } 
    private void drawCenterLines(Graphics2D g)
    {
        int nb = this.myFTableEntites.getNbEntites();
        TEntiteEtendue EE;
        this.setLineColor(g, Color.RED);
        // attributs de lignes
        BasicStroke stroke = new BasicStroke(0.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 12.0f); 
        g.setStroke(stroke);
        EE = this.myFTableEntites.getEntite(0);
        for (int i = 0; i < nb; i++)            
        {
            EE = this.myFTableEntites.getEntite(i);
            if (EE.TypeEntite != tgENTRANCE) drawLigne(g, EE.UneStation1X, EE.UneStation1Y, EE.UneStation2X, EE.UneStation2Y);
        }
    } 
    private void drawStations(Graphics2D g)
    {
        afficherMessage("drawStations()");
        int nb = this.myFTableEntites.getNbEntites();
        TEntiteEtendue EE;
        for (int i = 0; i < nb; i++)            
        {
            EE = this.myFTableEntites.getEntite(i);
            if (EE.TypeEntite != tgENTRANCE) drawCircle(g, EE.UneStation2X, EE.UneStation2Y, 2);
        }
    }  
    private void drawTexte(Graphics2D g, double x, double y, String texte)
    {
        Point P = getCoordsPlan(makeTPoint2Df(x, y));
        g.drawString(texte, P.x + 3, P.y - 3);
    }        
    private void drawJonctions(Graphics2D g)
    {
        afficherMessage("drawJonctions()");
        int nb = this.myFTableEntites.getNbEntites();
        TEntiteEtendue EE;
        g.setColor(Color.BLACK);
        Font f = new Font("Arial", 0, 10);       
        g.setFont(f);
        for (int i = 0; i < nb; i++)            
        {
            EE = this.myFTableEntites.getEntite(i);
            if (EE.TypeEntite != tgENTRANCE) 
            {
                if (EE.IsJonction)
                {    
                    drawCircle(g, EE.UneStation2X, EE.UneStation2Y, 4);
                    String EWE = sprintf("%d.%d", EE.EntiteSerie, EE.EntiteStation);
                    drawTexte(g, EE.UneStation2X, EE.UneStation2Y, EWE);
                }
            }
        }
    } 
    
    private void drawIDStations(Graphics2D g)
    {
        afficherMessage("drawIDStations()");
        int nb = this.myFTableEntites.getNbEntites();
        TEntiteEtendue EE;
        for (int i = 0; i < nb; i++)            
        {
            EE = this.myFTableEntites.getEntite(i);
            
        }
    } 
    private void drawEntrances(Graphics2D g)
    {
        afficherMessage("drawEntrances()");
        int nb = this.myFTableEntites.getNbEntrances();
        TEntrance EE;
        int R = 4;
        for (int i = 0; i < nb; i++)            
        {
            EE = this.myFTableEntites.getEntrance(i);
            Point PP = getCoordsPlan(makeTPoint2Df(EE.eXEntree, EE.eYEntree));
            g.setColor(Color.BLUE);
            g.fillRect(PP.x - R, PP.y - R, 2*R, 2*R);               
        }
    } 
    private void drawCotation(Graphics2D g)
    {
        afficherMessage("drawStations()");
        int nb = this.myFTableEntites.getNbEntites();
        TEntiteEtendue EE;
        //g.draw
        for (int i = 0; i < nb; i++)            
        {
            EE = this.myFTableEntites.getEntite(i);
            TPoint3Df WU = this.myFTableEntites.getDeltaXYZFromPositionSt0(EE);
        }
    } 
    private void drawAltitudes(Graphics2D g)
    {
        afficherMessage("drawStations()");
        int nb = this.myFTableEntites.getNbEntites();
        TEntiteEtendue EE;
        for (int i = 0; i < nb; i++)            
        {
            EE = this.myFTableEntites.getEntite(i);
        }
    } 
    // surcharge de la méthode paint (et non paintComponent) afin de dessiner notre vue personnalisée
    // En AWT, la méthode paintComponent n'existe pas et c'est la méthode paint qui doit être redéfinie pour ajouter du dessin. 
    // En Swing, la méthode paint existe encore mais elle ne doit pas être redéfinie.
    @Override
    //public void paintComponent(Graphics g)
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
            // quadrilles
            if (IsInSet(edQuadrilles, fElementsDrawn))
            {
                this.setQuadrillageColor(new Color(128,128,128));
                drawQuadrilles(g2, fQuadrillesColor,w, h);
            }
            if (IsInSet(edCrossSections , fElementsDrawn)) drawCrossSections(g2);
            if (IsInSet(edFillGalerie   , fElementsDrawn)) drawSilhouette(g2);
            // centerline, en dernier
            if (IsInSet(edPolygonals    , fElementsDrawn)) drawCenterLines(g2);
            if (IsInSet(edStations      , fElementsDrawn)) drawStations(g2);
            if (IsInSet(edJonctions     , fElementsDrawn)) drawJonctions(g2);
            if (IsInSet(edIDStations    , fElementsDrawn)) drawIDStations(g2);
            if (IsInSet(edENTRANCES     , fElementsDrawn)) drawEntrances(g2);
            if (IsInSet(edAltitudes     , fElementsDrawn)) drawAltitudes(g2);
            if (IsInSet(edCotes         , fElementsDrawn)) drawCotation(g2);
        }
        catch (Exception E)
        {
            E.printStackTrace();
        }    
    }
     //--------------------------------------------------------------------------
    public void initialiseVue2D(TTableDesEntites E, JLabel lbSouris, JLabel lbDescStation)
    {
        
        this.myFTableEntites = E;
        this.myLabelExterneCoordsSouris = lbSouris;
        this.myLabelExterneDescStation  = lbDescStation;
        this.myFCanDraw = true;
        afficherMessage(sprintf("TVisualisateur2DExt est initialisé avec %d données", this.myFTableEntites.getNbEntites()));
        this.myFTableEntites.setMiniMaxi();
        this.myFTableEntites.applyMetaFiltre("");
        this.fEspcQuadrilles = proposerEquidistanceDef(myFTableEntites.getCoinBasGauche(), myFTableEntites.getCoinHautDroit(), 100.00);
        this.resetVue();
        this.repaint();
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
            public void mouseExited(java.awt.event.MouseEvent evt) {
                formMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
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
            .addGap(0, 511, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 497, Short.MAX_VALUE)
        );

        getAccessibleContext().setAccessibleDescription("");
        getAccessibleContext().setAccessibleParent(this);
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        // TODO add your handling code here:
        if (myFCanDraw)
        {   
            mouseXY = this.getMousePosition();
            TPoint2Df p1 = getCoordsMonde(oldmouseXY);
            TPoint2Df p2 = getCoordsMonde(mouseXY);
            double dx = -(p2.X - p1.X);
            double dy = -(p2.Y - p1.Y);
            setViewLimits(myFRegionXMini + dx, myFRegionYMini + dy, myFRegionXMaxi + dx, myFRegionYMaxi + dy);
            this.repaint();
            oldmouseXY = mouseXY;
        }    
    }//GEN-LAST:event_formMouseDragged

    private void formMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseExited

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        // TODO add your handling code here:
        if (myFCanDraw)
        {    
            mouseXY = this.getMousePosition();
            mouseCoordsReelles = getCoordsMonde(mouseXY);
            oldmouseXY = this.getMousePosition();
            myLabelExterneCoordsSouris.setText(sprintf("%.2f, %.2f", mouseCoordsReelles.X, mouseCoordsReelles.Y));
        }
    }//GEN-LAST:event_formMouseMoved

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        // TODO add your handling code here:
        int buttonDown  = evt.getButton();
        int shitKey     = evt.getModifiers();
        boolean keyShiftDown = (shitKey & evt.SHIFT_MASK) != 0;
        boolean keyCtrlDown  = (shitKey & evt.CTRL_MASK)  != 0;
        
        int wu = myFTableEntites.getIdxEntiteNearToPoint(mouseCoordsReelles);
        //int wu = -1;
        String EWE = "";
        if (wu > 0)
        {
            TEntiteEtendue beuh = myFTableEntites.getEntite(wu);
            EWE = sprintf("%d.%d - %s", beuh.EntiteSerie, beuh.EntiteStation, beuh.oIDLitteral);
        }
        else
        {
            EWE = "-- Not found --";
        } 
        myLabelExterneDescStation.setText(EWE);
        // Maj + click = zoom +
        double diag = Math.hypot(myFRegionXMaxi - myFRegionXMini, myFRegionYMaxi - myFRegionYMini) * 0.05;
        if ((buttonDown == evt.BUTTON1) && keyShiftDown)  
        {
            setViewLimits(myFRegionXMini + diag, myFRegionYMini + diag, myFRegionXMaxi - diag, myFRegionYMaxi - diag);
            this.repaint();
        }   
        else if ((buttonDown == evt.BUTTON1) && keyCtrlDown) 
        {
            setViewLimits(myFRegionXMini - diag, myFRegionYMini - diag, myFRegionXMaxi + diag, myFRegionYMaxi + diag);
            this.repaint();
        }    
    }//GEN-LAST:event_formMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
