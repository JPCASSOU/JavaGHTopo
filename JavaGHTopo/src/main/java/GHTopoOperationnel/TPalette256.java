package GHTopoOperationnel;

import static GHTopoOperationnel.GeneralFunctions.*;

import java.lang.String;
import java.util.List;
import java.awt.*;
import java.util.ArrayList;


public class TPalette256 {
    private List<Color> myPalette;
    public int getNbCouleurs()
    {
        return myPalette.size();
    }        
    public void GenerateWeb216Palette()
    {
        myPalette.clear();
        for (int i = 1; i <= 6; i++)
        {
            for (int j = 1; j <= 6; j++)
            {
                for (int k = 1; k <= 6; k++)
                {
                    int idxColor = (i-1) * 36 +
                                   (j-1) * 6  +
                                   (k-1);
                    int r = 255 - (i-1) * 51;
                    int g = 255 - (j-1) * 51;
                    int b = 255 - (k-1) * 51;
                    Color EWE = new Color(r, g, b);
                    myPalette.add(EWE);
                }   
            }
        }     
        // compléter la palette avec des blancs
        for (int i = 216; i < 256; i++)
        {
            Color EWE = new Color(0x80, 0x80, 0x80);
            myPalette.add(EWE);
        }
    }        
    public void addColorByRGB(int R, int G, int B)
    {
        Color C = new Color(R, G, B);
        myPalette.add(C);
    }        
   
    public void generateGrayScalePalette()
    {
        myPalette.clear();
        for (int i = 0; i < 256; i++) 
        {
            Color EWE = new Color(i,i,i);
            myPalette.add(EWE);
        }
    }        

    public Color getColorByIdx(int idx)
    {
        return myPalette.get(idx);
    }        
    // constructeur
    public TPalette256()
    {
        myPalette = new ArrayList<Color>();
        this.generateTOPOROBOTPalette();
        afficherMessage(sprintf("Palette Toporobot OK: %d valeurs", this.getNbCouleurs()));
    }
    public void generateTOPOROBOTPalette()
    {
        // méthode passablement bourrin mais qui a le mérite de marcher
        // et on ne va pas se casser les cacahuètes
        this.addColorByRGB(255, 255, 255);
        this.addColorByRGB(0, 0, 0);
        this.addColorByRGB(119, 119, 119);
        this.addColorByRGB(85, 85, 85);
        this.addColorByRGB(255, 255, 0);
        this.addColorByRGB(255, 102, 0);
        this.addColorByRGB(221, 0, 0);
        this.addColorByRGB(255, 0, 153);
        this.addColorByRGB(102, 0, 153);
        this.addColorByRGB(0, 0, 221);
        this.addColorByRGB(0, 153, 255);
        this.addColorByRGB(0, 238, 0);
        this.addColorByRGB(0, 102, 0);
        this.addColorByRGB(102, 51, 0);
        this.addColorByRGB(153, 102, 51);
        this.addColorByRGB(187, 187, 187);
        this.addColorByRGB(255, 255, 204);
        this.addColorByRGB(255, 255, 153);
        this.addColorByRGB(255, 255, 102);
        this.addColorByRGB(255, 255, 51);
        this.addColorByRGB(255, 204, 255);
        this.addColorByRGB(255, 204, 204);
        this.addColorByRGB(255, 204, 153);
        this.addColorByRGB(255, 204, 102);
        this.addColorByRGB(255, 204, 51);
        this.addColorByRGB(255, 204, 0);
        this.addColorByRGB(255, 153, 255);
        this.addColorByRGB(255, 153, 204);
        this.addColorByRGB(255, 153, 153);
        this.addColorByRGB(255, 153, 102);
        this.addColorByRGB(255, 153, 51);
        this.addColorByRGB(255, 153, 0);
        this.addColorByRGB(255, 102, 255);
        this.addColorByRGB(255, 102, 204);
        this.addColorByRGB(255, 102, 153);
        this.addColorByRGB(255, 102, 102);
        this.addColorByRGB(255, 102, 51);
        this.addColorByRGB(255, 51, 255);
        this.addColorByRGB(255, 51, 204);
        this.addColorByRGB(255, 51, 153);
        this.addColorByRGB(255, 51, 102);
        this.addColorByRGB(255, 51, 51);
        this.addColorByRGB(255, 51, 0);
        this.addColorByRGB(255, 0, 255);
        this.addColorByRGB(255, 0, 204);
        this.addColorByRGB(255, 0, 102);
        this.addColorByRGB(255, 0, 51);
        this.addColorByRGB(255, 0, 0);
        this.addColorByRGB(204, 255, 255);
        this.addColorByRGB(204, 255, 204);
        this.addColorByRGB(204, 255, 153);
        this.addColorByRGB(204, 255, 102);
        this.addColorByRGB(204, 255, 51);
        this.addColorByRGB(204, 255, 0);
        this.addColorByRGB(204, 204, 255);
        this.addColorByRGB(204, 204, 204);
        this.addColorByRGB(204, 204, 153);
        this.addColorByRGB(204, 204, 102);
        this.addColorByRGB(204, 204, 51);
        this.addColorByRGB(204, 204, 0);
        this.addColorByRGB(204, 153, 255);
        this.addColorByRGB(204, 153, 204);
        this.addColorByRGB(204, 153, 153);
        this.addColorByRGB(204, 153, 102);
        this.addColorByRGB(204, 153, 51);
        this.addColorByRGB(204, 153, 0);
        this.addColorByRGB(204, 102, 255);
        this.addColorByRGB(204, 102, 204);
        this.addColorByRGB(204, 102, 153);
        this.addColorByRGB(204, 102, 102);
        this.addColorByRGB(204, 102, 51);
        this.addColorByRGB(204, 102, 0);
        this.addColorByRGB(204, 51, 255);
        this.addColorByRGB(204, 51, 204);
        this.addColorByRGB(204, 51, 153);
        this.addColorByRGB(204, 51, 102);
        this.addColorByRGB(204, 51, 51);
        this.addColorByRGB(204, 51, 0);
        this.addColorByRGB(204, 0, 255);
        this.addColorByRGB(204, 0, 204);
        this.addColorByRGB(204, 0, 153);
        this.addColorByRGB(204, 0, 102);
        this.addColorByRGB(204, 0, 51);
        this.addColorByRGB(204, 0, 0);
        this.addColorByRGB(153, 255, 255);
        this.addColorByRGB(153, 255, 204);
        this.addColorByRGB(153, 255, 153);
        this.addColorByRGB(153, 255, 102);
        this.addColorByRGB(153, 255, 51);
        this.addColorByRGB(153, 255, 0);
        this.addColorByRGB(153, 204, 255);
        this.addColorByRGB(153, 204, 204);
        this.addColorByRGB(153, 204, 153);
        this.addColorByRGB(153, 204, 102);
        this.addColorByRGB(153, 204, 51);
        this.addColorByRGB(153, 204, 0);
        this.addColorByRGB(153, 153, 255);
        this.addColorByRGB(153, 153, 204);
        this.addColorByRGB(153, 153, 153);
        this.addColorByRGB(153, 153, 102);
        this.addColorByRGB(153, 153, 51);
        this.addColorByRGB(153, 153, 0);
        this.addColorByRGB(153, 102, 255);
        this.addColorByRGB(153, 102, 204);
        this.addColorByRGB(153, 102, 153);
        this.addColorByRGB(153, 102, 102);
        this.addColorByRGB(153, 102, 0);
        this.addColorByRGB(153, 51, 255);
        this.addColorByRGB(153, 51, 204);
        this.addColorByRGB(153, 51, 153);
        this.addColorByRGB(153, 51, 102);
        this.addColorByRGB(153, 51, 51);
        this.addColorByRGB(153, 51, 0);
        this.addColorByRGB(153, 0, 255);
        this.addColorByRGB(153, 0, 204);
        this.addColorByRGB(153, 0, 153);
        this.addColorByRGB(153, 0, 102);
        this.addColorByRGB(153, 0, 51);
        this.addColorByRGB(153, 0, 0);
        this.addColorByRGB(102, 255, 255);
        this.addColorByRGB(102, 255, 204);
        this.addColorByRGB(102, 255, 153);
        this.addColorByRGB(102, 255, 102);
        this.addColorByRGB(102, 255, 51);
        this.addColorByRGB(102, 255, 0);
        this.addColorByRGB(102, 204, 255);
        this.addColorByRGB(102, 204, 204);
        this.addColorByRGB(102, 204, 153);
        this.addColorByRGB(102, 204, 102);
        this.addColorByRGB(102, 204, 51);
        this.addColorByRGB(102, 204, 0);
        this.addColorByRGB(102, 153, 255);
        this.addColorByRGB(102, 153, 204);
        this.addColorByRGB(102, 153, 153);
        this.addColorByRGB(102, 153, 102);
        this.addColorByRGB(102, 153, 51);
        this.addColorByRGB(102, 153, 0);
        this.addColorByRGB(102, 102, 255);
        this.addColorByRGB(102, 102, 204);
        this.addColorByRGB(102, 102, 153);
        this.addColorByRGB(102, 102, 102);
        this.addColorByRGB(102, 102, 51);
        this.addColorByRGB(102, 102, 0);
        this.addColorByRGB(102, 51, 255);
        this.addColorByRGB(102, 51, 204);
        this.addColorByRGB(102, 51, 153);
        this.addColorByRGB(102, 51, 102);
        this.addColorByRGB(102, 51, 51);
        this.addColorByRGB(102, 0, 255);
        this.addColorByRGB(102, 0, 204);
        this.addColorByRGB(102, 0, 102);
        this.addColorByRGB(102, 0, 51);
        this.addColorByRGB(102, 0, 0);
        this.addColorByRGB(51, 255, 255);
        this.addColorByRGB(51, 255, 204);
        this.addColorByRGB(51, 255, 153);
        this.addColorByRGB(51, 255, 102);
        this.addColorByRGB(51, 255, 51);
        this.addColorByRGB(51, 255, 0);
        this.addColorByRGB(51, 204, 255);
        this.addColorByRGB(51, 204, 204);
        this.addColorByRGB(51, 204, 153);
        this.addColorByRGB(51, 204, 102);
        this.addColorByRGB(51, 204, 51);
        this.addColorByRGB(51, 204, 0);
        this.addColorByRGB(51, 153, 255);
        this.addColorByRGB(51, 153, 204);
        this.addColorByRGB(51, 153, 153);
        this.addColorByRGB(51, 153, 102);
        this.addColorByRGB(51, 153, 51);
        this.addColorByRGB(51, 153, 0);
        this.addColorByRGB(51, 102, 255);
        this.addColorByRGB(51, 102, 204);
        this.addColorByRGB(51, 102, 153);
        this.addColorByRGB(51, 102, 102);
        this.addColorByRGB(51, 102, 51);
        this.addColorByRGB(51, 102, 0);
        this.addColorByRGB(51, 51, 255);
        this.addColorByRGB(51, 51, 204);
        this.addColorByRGB(51, 51, 153);
        this.addColorByRGB(51, 51, 102);
        this.addColorByRGB(51, 51, 51);
        this.addColorByRGB(51, 51, 0);
        this.addColorByRGB(51, 0, 255);
        this.addColorByRGB(51, 0, 204);
        this.addColorByRGB(51, 0, 153);
        this.addColorByRGB(51, 0, 102);
        this.addColorByRGB(51, 0, 51);
        this.addColorByRGB(51, 0, 0);
        this.addColorByRGB(0, 255, 255);
        this.addColorByRGB(0, 255, 204);
        this.addColorByRGB(0, 255, 153);
        this.addColorByRGB(0, 255, 102);
        this.addColorByRGB(0, 255, 51);
        this.addColorByRGB(0, 255, 0);
        this.addColorByRGB(0, 204, 255);
        this.addColorByRGB(0, 204, 204);
        this.addColorByRGB(0, 204, 153);
        this.addColorByRGB(0, 204, 102);
        this.addColorByRGB(0, 204, 51);
        this.addColorByRGB(0, 204, 0);
        this.addColorByRGB(0, 153, 204);
        this.addColorByRGB(0, 153, 153);
        this.addColorByRGB(0, 153, 102);
        this.addColorByRGB(0, 153, 51);
        this.addColorByRGB(0, 153, 0);
        this.addColorByRGB(0, 102, 255);
        this.addColorByRGB(0, 102, 204);
        this.addColorByRGB(0, 102, 153);
        this.addColorByRGB(0, 102, 102);
        this.addColorByRGB(0, 102, 51);
        this.addColorByRGB(0, 51, 255);
        this.addColorByRGB(0, 51, 204);
        this.addColorByRGB(0, 51, 153);
        this.addColorByRGB(0, 51, 102);
        this.addColorByRGB(0, 51, 51);
        this.addColorByRGB(0, 51, 0);
        this.addColorByRGB(0, 0, 255);
        this.addColorByRGB(0, 0, 204);
        this.addColorByRGB(0, 0, 153);
        this.addColorByRGB(0, 0, 102);
        this.addColorByRGB(0, 0, 51);
        this.addColorByRGB(238, 0, 0);
        this.addColorByRGB(187, 0, 0);
        this.addColorByRGB(170, 0, 0);
        this.addColorByRGB(136, 0, 0);
        this.addColorByRGB(119, 0, 0);
        this.addColorByRGB(85, 0, 0);
        this.addColorByRGB(68, 0, 0);
        this.addColorByRGB(34, 0, 0);
        this.addColorByRGB(17, 0, 0);
        this.addColorByRGB(0, 221, 0);
        this.addColorByRGB(0, 187, 0);
        this.addColorByRGB(0, 170, 0);
        this.addColorByRGB(0, 136, 0);
        this.addColorByRGB(0, 119, 0);
        this.addColorByRGB(0, 85, 0);
        this.addColorByRGB(0, 68, 0);
        this.addColorByRGB(0, 34, 0);
        this.addColorByRGB(0, 17, 0);
        this.addColorByRGB(0, 0, 238);
        this.addColorByRGB(0, 0, 187);
        this.addColorByRGB(0, 0, 170);
        this.addColorByRGB(0, 0, 136);
        this.addColorByRGB(0, 0, 119);
        this.addColorByRGB(0, 0, 85);
        this.addColorByRGB(0, 0, 68);
        this.addColorByRGB(0, 0, 34);
        this.addColorByRGB(0, 0, 17);
        this.addColorByRGB(238, 238, 238);
        this.addColorByRGB(221, 221, 221);
        this.addColorByRGB(170, 170, 170);
        this.addColorByRGB(136, 136, 136);
        this.addColorByRGB(68, 68, 68);
        this.addColorByRGB(34, 34, 34);
        this.addColorByRGB(17, 17, 17);
    }    
}

