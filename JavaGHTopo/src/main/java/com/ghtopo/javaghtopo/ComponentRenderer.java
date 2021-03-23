
package com.ghtopo.javaghtopo;

//******************************************************************************
// ComponentRenderer

import static GHTopoOperationnel.Constantes.*;
import static GHTopoOperationnel.GeneralFunctions.*;
import GHTopoOperationnel.TPalette256;
import GHTopoOperationnel.TSerie;
import GHTopoOperationnel.TToporobotStructure;
import GHTopoOperationnel.Types.TCode;
import GHTopoOperationnel.Types.TEntrance;
import GHTopoOperationnel.Types.TExpe;
import GHTopoOperationnel.Types.TReseau;
import GHTopoOperationnel.Types.TSecteur;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

class ComponentRenderer extends JComponent implements ListCellRenderer
{
    // référence vers le document topo
    private TToporobotStructure fDocTopo;
    // palette 256
    private TPalette256 fPalette256;
   
    //----- attributs
    private Color background;
    private Color foreground;
    //----- constantes
    private final static int WIDTH = 100;
    private final static int HEIGHT = 24;
    private int fCurrentIndex = 0;

    private int fModeSelection;

    public  void SetDocTopoAndSelection(TToporobotStructure dt, TPalette256 P, int QMS)
    {
        fDocTopo = dt;
        fPalette256 = P;
        fModeSelection = QMS;        
    }        
    public void SetModeDeSelection(int ms)
    {
        fModeSelection = ms;
    }        

    public ComponentRenderer()
    {
            super();
            setOpaque( true );
            this.setSize( WIDTH, HEIGHT );
    }

    
    @Override 
    
    protected void paintComponent( Graphics g )
    {
            Graphics2D g2d = ( Graphics2D )g.create();

            paintBackground( g2d );
            paintContent( g2d );

            g2d.dispose();
    }


    @Override
    public Dimension getPreferredSize()
    {
            if ( isPreferredSizeSet() )
            {
                return super.getPreferredSize();
            }
            else
            {
                return new Dimension( WIDTH, HEIGHT );
            }
    }

    private void paintBackground( Graphics2D g2d )
    {
            if ( this.isOpaque() )
            {
                g2d.setColor( this.background );
                g2d.fillRect( 0, 0, this.getWidth(), this.getHeight());
            }
    }
    // équivalent de TListBox.OnDrawItem 
    public Component getListCellRendererComponent( JList list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus )
    {
            //----- définition couleurs
            this.background = ( isSelected ? list.getSelectionBackground() : list.getBackground() );
            this.foreground = ( isSelected ? list.getSelectionForeground() : list.getForeground() );
            fCurrentIndex = index;            
            return this;
    }
    private void drwTab(Graphics2D g2d, int x)
    {
        g2d.setColor(Color.GRAY);
        g2d.drawLine(x - 4, 0, x - 4, getHeight());
    }
    private void paintContent( Graphics2D g2d )
    {
        final int MARGE = 4;
        int posYTexte = getHeight() - MARGE;
        int widthBoxColor = 50;
        int heightBoxColor = getHeight() - 2 * MARGE;
        int yPosBoxColor  = MARGE;
        int xTabIdx       = 5;
        int xTabBoxColor  = 40;
        int xTabNom       = xTabIdx + xTabBoxColor + widthBoxColor + 5;
        // tabs des séries
        int largeurNomSerie = 280;
        int largeurSerPt = 50;
        int xTabNomSerie = 40;
        
        int xTabSerPtDep =  xTabNomSerie + largeurNomSerie;
        int xTabSerPtArr =  xTabSerPtDep + largeurSerPt;
        int xTabExpeDate =  xTabBoxColor + widthBoxColor + 8;
        int xTabExpeOperateurs = xTabExpeDate + 70;
        
        try
        {
            g2d.setColor( foreground );
            switch(fModeSelection)
            {
                case mslENTRANCES:
                    if (fDocTopo.getNbEntrances() > 0)
                    {    
                        TEntrance fCurrEntree = fDocTopo.getEntrance(fCurrentIndex);
                        g2d.drawString(sprintf("%d", fCurrEntree.eNumEntree), xTabIdx, posYTexte);
                         // lignes verticales
                        drwTab(g2d, xTabNom);
                        g2d.drawString(sprintf("%s", fCurrEntree.eNomEntree), xTabNom, posYTexte);
                    }
                    break;                
                case mslRESEAUX:
                    if (fDocTopo.getNbReseaux() > 0)
                    {    
                        TReseau fCurrReseau = fDocTopo.getReseau(fCurrentIndex);
                        Color RColor = new Color(fCurrReseau.CouleurReseauR, fCurrReseau.CouleurReseauG, fCurrReseau.CouleurReseauB);
                        g2d.setColor(RColor);
                        g2d.fillRect( xTabBoxColor, yPosBoxColor, widthBoxColor, heightBoxColor);
                        // lignes verticales
                        drwTab(g2d, xTabBoxColor);
                        drwTab(g2d, xTabNom);
                        // textes
                        g2d.setColor( foreground );
                        g2d.drawRect( xTabBoxColor, yPosBoxColor, widthBoxColor , heightBoxColor);
                        g2d.drawString(sprintf("%d", fCurrReseau.IdxReseau), xTabIdx, posYTexte);
                        //g2d.clipRect(xTabNom, posYTexte, 300, heightBoxColor);
                        g2d.drawString(sprintf("%s", fCurrReseau.NomReseau), xTabNom, posYTexte);
                        //g2d.cli
                    }
                    break;
                case mslSECTEURS:
                    if (fDocTopo.getNbSecteurs() > 0)
                    {    
                        TSecteur fCurrSecteur = fDocTopo.getSecteur(fCurrentIndex);
                        Color SColor = new Color(fCurrSecteur.CouleurSecteurR, fCurrSecteur.CouleurSecteurG, fCurrSecteur.CouleurSecteurB);
                        g2d.setColor(SColor);
                        g2d.fillRect( xTabBoxColor, yPosBoxColor, widthBoxColor, heightBoxColor);
                        // lignes verticales
                        drwTab(g2d, xTabBoxColor);
                        drwTab(g2d, xTabNom);
                        // textes
                        g2d.setColor( foreground );
                        g2d.drawRect( xTabBoxColor, yPosBoxColor, widthBoxColor , heightBoxColor);
                        g2d.drawString(sprintf("%d", fCurrSecteur.IdxSecteur), xTabIdx, posYTexte);
                        g2d.drawString(sprintf("%s", fCurrSecteur.NomSecteur), xTabNom, posYTexte);
                    }
                    break;
                case mslCODES:
                    if (fDocTopo.getNbCodes() > 0)
                    {    
                        TCode fCurrCode = fDocTopo.getCode(fCurrentIndex);
                        // lignes verticales
                        // textes
                        g2d.setColor( foreground );
                        g2d.drawString(sprintf("%d", fCurrCode.IdxCode), xTabIdx, posYTexte);                        
                    }
                    break;      
                case mslEXPES:
                    if (fDocTopo.getNbExpes() > 0)
                    {    
                        TExpe fCurrExpe = fDocTopo.getExpe(fCurrentIndex);
                        Color ExColor = fPalette256.getColorByIdx(fCurrExpe.IdxColor);
                        g2d.setColor(ExColor);
                        g2d.fillRect( xTabBoxColor, yPosBoxColor, widthBoxColor, heightBoxColor);
                        // lignes verticales
                        drwTab(g2d, xTabBoxColor);
                        // textes
                        g2d.setColor( foreground );
                        g2d.drawRect( xTabBoxColor, yPosBoxColor, widthBoxColor , heightBoxColor);
                        g2d.drawString(sprintf("%d", fCurrExpe.IdxExpe), xTabIdx, posYTexte);
                        drwTab(g2d, xTabExpeDate);
                        int JJ   = getDay(fCurrExpe.DateLeve);
                        int MM   = getMonth(fCurrExpe.DateLeve);
                        int AAAA = getYear(fCurrExpe.DateLeve);
                        g2d.drawString(sprintf("%02d/%02d/%04d", JJ, MM, AAAA), xTabExpeDate, posYTexte);
                        drwTab(g2d, xTabExpeOperateurs);
                        g2d.drawString(sprintf("%s - %s", fCurrExpe.Speleometre, fCurrExpe.Speleographe), xTabExpeOperateurs, posYTexte);
                    }
                    break;  
                case mslSERIES:
                    if (fDocTopo.getNbSeries() > 0)
                    {                           
                        TSerie fCurrSerie = fDocTopo.getSerie(fCurrentIndex); 
                        afficherMessageFmt("Passe dans les séries: %d - %d.%d", fCurrSerie.IdxSerie, fCurrSerie.SerDep, fCurrSerie.PtDep);
                        // lignes verticales
                        drwTab(g2d, xTabNomSerie); 
                        drwTab(g2d, xTabSerPtDep);
                        drwTab(g2d, xTabSerPtArr);
                        // textes
                        g2d.setColor( foreground );
                        g2d.drawString(sprintf("%d", fCurrSerie.IdxSerie), xTabIdx, posYTexte);
                        g2d.drawString(sprintf("%s", fCurrSerie.NomSerie), xTabNomSerie, posYTexte);
                        g2d.drawString(sprintf("%d.%d", fCurrSerie.SerDep, fCurrSerie.PtDep), xTabSerPtDep, posYTexte);
                        g2d.drawString(sprintf("%d.%d", fCurrSerie.SerArr, fCurrSerie.PtArr), xTabSerPtArr, posYTexte);
                    }
                    break;  

                default:
                    g2d.drawString(sprintf("%d", fCurrentIndex), xTabIdx, posYTexte);

                    break;
            }
            
        } 
        catch (Exception e)
        {
            afficherMessage("toto");
        }    
    }	
}