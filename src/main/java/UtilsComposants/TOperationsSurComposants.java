// Fonctions utilisant des composants

package UtilsComposants;

import static GHTopoOperationnel.GeneralFunctions.*;
import GHTopoOperationnel.TConvertisseurCoordonnees;
import GHTopoOperationnel.TConvertisseurCoordonnees;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 *
 * @author jean-pierre.cassou
 */
public class TOperationsSurComposants {
    
    /**
     * Peuple un JCombo avec les systèmes de coordonnées supportés
     * par le TConvertisseurCoordonnees passé en paramètre
     * @param lsb JList
     * @param myConvertisseur TConvertisseurCoordonnees
     */
    public static void remplirCombosListesSystem(JComboBox cmb, TConvertisseurCoordonnees myConvertisseur)
    {
        int Nb = myConvertisseur.getNbSystCoords();
       
        if (Nb > 0)
        {    
            cmb.removeAllItems();
            for (int i = 0; i < Nb; i++)
            {
               CoordinateReferenceSystem c = myConvertisseur.getCoordSystem(i);
               int epsg = myConvertisseur.getCodeEPSG(c);
               cmb.addItem(sprintf("EPSG:%d - %s", epsg, c.getName()));
            }    
        }       
    }   
     /**
     * Peuple un JList avec les systèmes de coordonnées supportés
     * par le TConvertisseurCoordonnees passé en paramètre
     * @param lsb JList
     * @param myConvertisseur TConvertisseurCoordonnees
     */
    public static void remplirListBoxSystem(JList lsb, TConvertisseurCoordonnees myConvertisseur)
    {
        DefaultListModel ldm = new DefaultListModel();
        lsb.setModel(ldm);
        int Nb = myConvertisseur.getNbSystCoords();
        if (Nb > 0)
        {    
            lsb.removeAll();
            for (int i = 0; i < Nb; i++)
            {
               CoordinateReferenceSystem c = myConvertisseur.getCoordSystem(i);
               int epsg = myConvertisseur.getCodeEPSG(c);
               ldm.addElement(sprintf("EPSG:%d - %s", epsg, c.getName()));
            } 
             lsb.setSelectedIndex(0);     
        }
    }   
    /**
     * Peuple un combobox et positionne son index 
     * @param cmb JComboBox
     * @param items String[]
     * @param Idx int
     */
    public static void preparerComboBox(JComboBox cmb, String[] items, int Idx)
    {
        cmb.removeAllItems();
        int nb = items.length;
        for (int i = 0; i < nb; i++) cmb.addItem(items[i]);
        cmb.setSelectedIndex(Idx);
    }     
    /**
    
    /**
     * retourne l'index du bouton sélectionné dans un RadioGroup
     * @param RadioBtnGrp ButtonGroup
     * @return int
     */
    public static int getRadioButtonSelected(ButtonGroup RadioBtnGrp)
    {
        Enumeration<AbstractButton> Btns = RadioBtnGrp.getElements();
        int Nb = RadioBtnGrp.getButtonCount();
        for (int i = 0; i < Nb; i++)
        {
            AbstractButton B = Btns.nextElement();
            if (B.isSelected()) return i;
        }    
        return -1;
    }        
}    