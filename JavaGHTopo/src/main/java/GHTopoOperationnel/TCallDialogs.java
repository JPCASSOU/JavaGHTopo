package GHTopoOperationnel;

import com.ghtopo.javaghtopo.NewClass;
import com.ghtopo.javaghtopo.TdlgAboutBox;
import com.ghtopo.javaghtopo.TdlgCalculette;
import com.ghtopo.javaghtopo.TdlgCalibrerDistoX;
import com.ghtopo.javaghtopo.TdlgExportGIS;
import com.ghtopo.javaghtopo.TdlgSelectDansListes;
import com.ghtopo.javaghtopo.TdlgStatistiques;
import com.ghtopo.javaghtopo.TdlgToporobotPalette;
import com.ghtopo.javaghtopo.TdlgVue2D;
import com.ghtopo.javaghtopo.TdlgVue3D;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.FileFilter;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


        
public class TCallDialogs {
    public static String getGHTopoDirectory()
    {        
        return System.getProperty("user.dir" );
    }        
    // Validé
    public static void showMessage(String titre, String msg)
    {
       
        JOptionPane pane = new JOptionPane(msg);
        JDialog dialog = pane.createDialog(new JFrame(), titre);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }  
    public static boolean questionOuiNon(JFrame parent, String titre, String msg)
    {
        int result = JOptionPane.showConfirmDialog(parent, msg, titre, JOptionPane.YES_NO_OPTION);
        return (JOptionPane.YES_OPTION == result);
    }   
    public static int selectionnerDansListe(TToporobotStructure t, int selectionMode, int elementSelected)
    {
        int result = elementSelected;
        int dlgReturnVal = 0;
        TdlgSelectDansListes dlg = new TdlgSelectDansListes();
        
        dlg.initialiserDlg(t, selectionMode, elementSelected);
        dlg.setModal(true);   // ceci est indispensable: boite de dialogue modale
        dlg.setVisible(true);      
        // Méthode de récupération des valeurs type Lazarus:
        // - Une variable interne de dlg est définie à mrCANCEL
        // - Les boutons [OK] et [Annuler] de dlg positionnent cette variable à mrOK ou mrCANCEL puis ferment le dialogue
        // - Un getter récupère cette valeur (dlg.getModalResult)
        // - D'autres getters récupèrent les valeurs demandées
   
        switch (dlg.getModalResult())  // similaire au ModalResult du TMessageDlg           
        {
            case Constantes.mrOK:
                result = dlg.getIdxElementSelected(); // on récupère les valeurs à obtenir
                
                //showMessage("OK est cliqué - Element sélectionné", String.format("Element %d sélectionné", result));
                break;
            case Constantes.mrCANCEL:
                //showMessage("", "CANCEL est cliqué");
                break;
        }
        return result;
    }
    public static int selectCouleurTopo(int elementSelected)
    {
        int result = elementSelected;
        int dlgReturnVal = 0;
        TdlgToporobotPalette dlg = new TdlgToporobotPalette();
        
        dlg.initialiserDlg(elementSelected);
        dlg.setModal(true);   // ceci est indispensable: boite de dialogue modale
        dlg.setVisible(true);      
        // Méthode de récupération des valeurs type Lazarus:
        // - Une variable interne de dlg est définie à mrCANCEL
        // - Les boutons [OK] et [Annuler] de dlg positionnent cette variable à mrOK ou mrCANCEL puis ferment le dialogue
        // - Un getter récupère cette valeur (dlg.getModalResult)
        // - D'autres getters récupèrent les valeurs demandées
        switch (dlg.getModalResult())  // similaire au ModalResult du TMessageDlg           
        {
            case Constantes.mrOK:
                result = dlg.getIdxCouleur(); // on récupère les valeurs à obtenir
                GeneralFunctions.afficherMessageFmt("Idx sélectionné: %d", result);
                break;
            case Constantes.mrCANCEL:
                break;
        }
        return result;
    }
    public static void afficherVue3D(TTableDesEntites te)
    {
        TdlgVue3D dlg = new TdlgVue3D();
        dlg.initialiserDlg(te);
        dlg.setModal(true);
        dlg.setVisible(true);
    }
     public static void afficherVue2D(TTableDesEntites te)
    {
        TdlgVue2D dlg = new TdlgVue2D();
        dlg.initialiserLaVue2D(te);
        dlg.setModal(true);
        dlg.setVisible(true);
    }
    public static void afficherCalculette()
    {
        TdlgCalculette dlg = new TdlgCalculette();
        dlg.initialiserDlg();
        dlg.setModal(true);
        dlg.setVisible(true);
    }       
    public static void afficherExportGIS(TTableDesEntites t)
    {
        TdlgExportGIS dlg = new TdlgExportGIS();
        dlg.initialiserDlg(t);
        dlg.setModal(true);
        dlg.setVisible(true);
    }      
    public static void afficherAboutBox()
    {
        TdlgAboutBox dlg = new TdlgAboutBox();
        dlg.initialiserDlg();
        dlg.setModal(true);
        dlg.setVisible(true);
    }
    public static void afficherOSMContext(TTableDesEntites t)
    {
        //TdlgOSMVue  dlg = new TdlgOSMVue();
        //dlg.initialiserDlg(t);
        //dlg.setModal(true);
        //dlg.setVisible(true);
    }
    public static Color ChooseColor(Component Parent, String Titre, Color c)
    {
        
        final JColorChooser dlgJC = new JColorChooser(c);
        Color returnVal = dlgJC.showDialog(Parent, Titre, c);
        return returnVal;
    }  
    public static void calibrerDistoX()
    {
        TdlgCalibrerDistoX dlg = new TdlgCalibrerDistoX();
        dlg.initialiserDlg();
        dlg.setModal(true);
        dlg.setVisible(true);
    }
    public static void afficherStatistiques(TTableDesEntites t)
    {
        TdlgStatistiques dlg = new TdlgStatistiques();
        dlg.initialiserDlg(t);
        dlg.setModal(true);
        dlg.setVisible(true);
    }
    public static void afficherVue3DOpenGL(TTableDesEntites t)
    {
        //GLAutoDrawable GLAD = null;
        //TVisu3DOpenGL MyGL = new TVisu3DOpenGL();
        //MyGL.init(GLAD);
        NewClass OGL = new NewClass();
        showMessage("GHTopo", "OpenGL unimplemented");
    }        
}
