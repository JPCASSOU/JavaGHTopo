/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ghtopo.javaghtopo;

import static GHTopoOperationnel.GeneralFunctions.afficherMessage;
import static GHTopoOperationnel.GeneralFunctions.trunc;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author jean-pierre.cassou
 */
public class TPanelQRCode extends javax.swing.JPanel 
{
    private boolean    fDoDrawQRCode = false;
    private ByteMatrix fMatrixQR;
    /**
     * Creates new form TPanelQRCode
     */
    //-------------------------------------------------------------------------------------------------------
    private void genererImageQRCode(String msg, int size, String filename) throws WriterException, IOException
    {
            final ErrorCorrectionLevel level = ErrorCorrectionLevel.Q;

            // encode
            final ByteMatrix matrix = generateByteMatrix(msg, level);
            
            // dessin dans le panel
           
            // write in a file
            writeByteImage(filename, "png", matrix, size);        
    } 
    private boolean genererQRCode(String s)
    {
        try
        {
            final ErrorCorrectionLevel level = ErrorCorrectionLevel.Q;
           // encode
            fMatrixQR = generateByteMatrix(s, level);
            return true;
        }  
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }    
    }    
    //******** MODE COMPLET ****************************************************
    private static ByteMatrix generateByteMatrix(final String data, final ErrorCorrectionLevel level) throws WriterException {
        final QRCode qr = new QRCode();
        Encoder.encode(data, level, qr);
        final ByteMatrix matrix = qr.getMatrix();
        return matrix;
    }
    private static void writeByteImage(final String outputFileName, final String imageFormat, final ByteMatrix matrix, final int size)
            throws FileNotFoundException, IOException {

        /**
         * Java 2D Traitement de Area
         */
        Area a = new Area(); // les futurs modules
        //Area module = new Area(new Rectangle2D.Float(0.05f, 0.05f, 0.9f, 0.9f));
        //Area module = new Area(new Rectangle2D.Float(0.01f, 0.01f, 0.98f, 0.98f));
        Area module = new Area(new Rectangle2D.Float(0.00f, 0.00f, 1.00f, 1.00f));

        AffineTransform at = new AffineTransform(); // pour déplacer le module
        int width = matrix.getWidth();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                if (matrix.get(j, i) == 1) {
                    a.add(module); // on ajoute le module
                }
                at.setToTranslation(1, 0); // on décale vers la droite
                module.transform(at);
            }
            at.setToTranslation(-width, 1); // on saute une ligne on revient au
            module.transform(at);
        }
        // agrandissement de l'Area pour le remplissage de l'image
        double ratio = size / (double) width;
        // il faut respecter la Quietzone : 4 modules de bordures autour du QR
        // Code
        double adjustment = width / (double) (width + 8);
        ratio = ratio * adjustment;

        at.setToTranslation(4, 4); // à cause de la quietzone
        a.transform(at);

        at.setToScale(ratio, ratio); // on agrandit
        a.transform(at);

        //Java 2D Traitement l'image
        BufferedImage im = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) im.getGraphics();
        Color couleur1 = new Color(0x000000);
        g.setPaint(couleur1);
        g.setBackground(new Color(0xFFFFFF));
        g.clearRect(0, 0, size, size); // pour le fond blanc
        g.fill(a); // remplissage des modules

        // Ecriture sur le disque
        File f = new File(outputFileName);
        f.setWritable(true);
        try {
            ImageIO.write(im, imageFormat, f);
            f.createNewFile();
        } catch (Exception e) 
        {
            ;
        }
    }
    //------------------------------------------------------------------------------------------------------
    public void Initialiser(String MsgQR)
    {        
        fDoDrawQRCode = genererQRCode(MsgQR);
        this.repaint();
    }        
    public TPanelQRCode() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 443, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 422, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    public void paint(Graphics g)        
    {
        final int MARGIN = 2;
        String EWE = fDoDrawQRCode ? "OK" : "KO";
        try
        {    
            afficherMessage("paintComponent: " + EWE);
            if (fDoDrawQRCode)
            {
                int m = fMatrixQR.getWidth();
                int n = fMatrixQR.getHeight();
            
                // Appel de la méthode paintComponent de la classe mère
                //super.paintComponent(g);
                // Conversion en un contexte 2D
                Graphics2D g2 = (Graphics2D) g;
                //Graphics2D g2 = (Graphics2D) g; 
                int h = this.getHeight();
                int w = this.getWidth();
                g2.setColor(Color.white);
                g2.fillRect(0, 0, w, h);
                int fcx = trunc(w / (m + 2 * MARGIN));
                int fcy = trunc(h / (n + 2 * MARGIN));
                for (int i = 0; i < m; i++)
                    for (int j = 0; j < n; j++)
                    {
                        Color C = (fMatrixQR.get(i, j) == 0) ? Color.WHITE : Color.BLACK;
                        g2.setColor(C);
                        g2.fillRect(fcx * (i + MARGIN), fcy * (j + MARGIN), fcx, fcy);
                    }
            }    
        }
        catch (Exception e)
        {
            e.printStackTrace();
        } 
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
