/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GHTopoOperationnel;

import static GHTopoOperationnel.CalculMatriciel3x3.produitVectoriel;
import static GHTopoOperationnel.Constantes.*;
import GHTopoOperationnel.Types.TEntiteEtendue;
import GHTopoOperationnel.Types.TLongAzPente;
import GHTopoOperationnel.Types.TPoint2Df;
import GHTopoOperationnel.Types.TPoint3Df;
import GHTopoOperationnel.Types.TStation;
import java.awt.Color;
import java.io.File;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import org.hsqldb.lib.StringUtil;

/**
 * Created by jean-pierre.cassou on 16/05/14.
 */

// En Java, les fonctions utilitaires font partie d'une classe conteneur
// Dans le module appelant: mettre import static GeneralFunctions.*
// puis les fonctions contenues dans la classe s'utilisent comme des fonctions ordinaires
public class GeneralFunctions 
{
    // constantes
    public static final double TWOPI   = 2 * Math.PI;
    public static final double PI_2   = Math.PI / 2.0;
    public static final double PI_200 = Math.PI / 200.0;
    /**
     * Equivalent de la format Format du Pascal
     * ou de la fonction sprintf() du PHP
     * @param fmt String Chaîne de format
     * @param args Object ... : liste des valeurs à formatter
     * @return 
     */
    public static String sprintf(String fmt, Object... args)
    {
        return String.format(fmt, args);
    }
    /**
     * Affiche un texte sur la console standard
     * @param msg 
     */
    public static void afficherMessage(String msg)
    {
        System.out.print(msg + "\n");
    }
    /**
     * Equivalent de printf
     * @param fmt
     * @param args 
     */
    public static void afficherMessageFmt(String fmt, Object... args)
    {
        afficherMessage(String.format(fmt, args));
    }        
    /**
     * Affiche un avertissement (stub)
     * @param msg 
     */
    public static void writeWarning(String msg)
    {
        System.out.print(msg + "\n");
    }
    /**
     * Equivalent de la fonction Trunc du Pascal, pour garantir le même 
     * fonctionnement
     * @param x double
     * @return int
     */
    public static int trunc(double x)
    {
        return (int) Math.floor(x);
    }
    // opérations sur les 'ensembles'
    /**
     * Détermine si le Flag de l'ensemble SetOf est armé
     *  
     * @param Flag  int
     * @param SetOf long
     * @return 
     */
    public static boolean IsInSet(int Flag, long SetOf)
    {
        return (Flag & SetOf) != 0; // if (vValeur and vVariable) <> 0  then .. (1101 and 0001 = 0001)
    }  
    /** Arme le Flag dans SetOf
     * 
     * @param Flag
     * @param SetOf
     * @return long
     */ 
    public static long Include(int Flag, long SetOf) 
    {
        return Flag | SetOf;       // vVariable OR vValeur  (1100 or 0001 1101)
    }   
    /** Désarme le Flag dans SetOf
     * 
     * @param Flag
     * @param SetOf
     * @return 
     */
    public static long Exclude(int Flag, long SetOf)
    {
        return Flag ^ SetOf;       // vVariable XOR vValeur (1101 xor 0001 1100) 
    }        
    /**
     * Arme ou désarme le Flag appartenant à SetOf
     * @param Flag   int
     * @param SetOf  long
     * @param FlagValue boolean
     * @return 
     */
    public static long setOnOffFlagInSet(int Flag, long SetOf, boolean FlagValue)
    {
        long R = 0;
        if (FlagValue) 
            R = Include(Flag, SetOf);
        else
            R = Exclude(Flag, SetOf);
        return R;
    }      
    /**
     * Bascule l'état du Flag dans l'ensemble SetOf
     * @param Flag  int
     * @param SetOf long
     * @return 
     */
    public static long basculerOnOffFlagInSet(int Flag, long SetOf)
    {
        long R = 0;
        boolean EWE = ! IsInSet(Flag, SetOf);
        if (EWE)
            R = Include(Flag, SetOf);
        else
            R = Exclude(Flag, SetOf);
        return R;
    } 
    /**
     * Construit un point 2D à partir de ses coordonnées
     * @param qx
     * @param qy
     * @return TPoint2Df
     */
    public static TPoint2Df makeTPoint2Df(double qx, double qy)
    {
        TPoint2Df Result = new TPoint2Df();
        Result.X = qx;
        Result.Y = qy;
        return Result;
    }
    /**
     * Construit un point 3D à partir de ses coordonnées
     * @param qx
     * @param qy
     * @param qz
     * @return TPoint3Df
     */
    public static TPoint3Df makeTPoint3Df(double qx, double qy, double qz)
    {
        TPoint3Df Result = new TPoint3Df();
        Result.X = qx;
        Result.Y = qy;
        Result.Z = qz;
        return Result;
    }
    // -------------------------------------------------------------
    /**
     * Conversion sécurisée d'un texte en nombre entier
     * 
     * @param qValeur
     * @param qDefault
     * @return int
     */
    public static int strToIntDef(String qValeur, int qDefault)
    {
        try {
            return Integer.parseInt(qValeur.trim());
        }
        catch (Exception E) // aucun besoin de traiter l'exception. On retourne la valeur par défaut et on décarre
        {
            return qDefault;
        }
    }
    /**
     * Conversion sécurisée d'un texte en un réel
     * @param qValeur
     * @param qDefault
     * @return double
     */
    public static double strToFloatDef(String qValeur, double qDefault)
    {
        try {
            return Double.parseDouble(qValeur.trim());
        }
        catch (Exception E)
        {
            return qDefault;
        }
    }
    /**
     * Conversion degré - radian
     * @param ang
     * @return double
     */
    public static double convDegToRad(double ang)
    {
        return ang * Math.PI / 180.00;
    }
    /**
     * Conversion grades - radian
     * @param ang
     * @return double
     */
    public static double convGradToRad(double ang)
    {
        return ang * Math.PI / 200.00;
    }
    /**
     * Retourne l'azimut dans l'unité précisée
     * @param dx double
     * @param dy double
     * @param fUB Unité de la boussole: 360 = degré, 400 = grades
     * @return double
     */
    public static double getAzimut(double dx, double dy, double fUB)
    {
        double a = Math.atan2(dy, dx + 1E-12);
        if (a < 0.00) a += 2 * Math.PI;
        a = 0.50 * Math.PI - a;
        if (a < 0.00) a += 2 * Math.PI;
        return a * 0.50 * fUB / Math.PI;
    }
    /**
     * Long, azimut et pente depuis dx, dy, dz
     * @param dx double
     * @param dy double
     * @param dz double
     * @param fUB Unité boussole selon spécs Toporobot
     * @param fUC (360 = degrés, 400 = grades)
     * @return TLongAzPente 
     */
    public static TLongAzPente getBearingInc(double dx, double dy, double dz, double fUB, double fUC)
    {
        TLongAzPente Result = new TLongAzPente();
        double dp = Math.hypot(dx, dy);
        Result.aLongueur = Math.hypot(dp, dz);
        Result.aPente    = Math.atan2(dz, dp) * 0.50 * fUC / Math.PI;
        Result.aAzimut   = getAzimut(dx, dy, fUB);
        return Result;
    }
    /**
     * Construit une chaîne contenant l'ID de série et ID de station séparés par un point
     * @param n  int
     * @param st TStation
     * @return String
     */
    public static String getIDLitteralStation(int n, TStation st)
    {
        String miaou = st.IDTerrain.trim();
        if (miaou.length() == 0)
            return String.format("%d.%d", n, st.IDStation);
        else
            return miaou;
    }
    /**
     * Hypoténuse 3D
     * @param dx double
     * @param dy double
     * @param dz double
     * @return double
     */
    public static double hypot3D(double dx, double dy, double dz)
    {
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }        
    /**
     * Calcul des accroissements d'une visée topo
     * @param maVisee  TStation
     * @param qUAzimut double (360 = degré, 400 = grade)
     * @param qUPente double
     * @param qDeclinaison double - Déclinaison magnétique
     * @param qInclinaison double - Correction sur inclinaisons
     * @return 
     */
    public static TStation calculerUneVisee(TStation maVisee, double qUAzimut, double qUPente, double qDeclinaison, double qInclinaison)
    {
        // variables
        @SuppressWarnings("UnusedAssignment")
        double CorrectionPenteInRadians = 0.0;

        int ubb = 0;
        int ucc = 0;
        double udd = 0.00;

        double rx = 0.00;
        double ry = 0.00;
        double rz = 0.00;

        double ub = 0.00;
        double uc = 0.00;
        double lp = 0.00;
        double azm = 0.00;
        double pentem = 0.00;
        //--------------------------
        TStation Result = maVisee;
        //--------------------------
        ubb = trunc(qUAzimut);
        ucc = trunc(qUPente);
        udd = 400.00;
        switch (ubb) {
            case 359: // visées directes en degrés
            case 360: // Nota: Un switch étant un GOTO particulier, la superposition de plusieurs case sans instruction équivaut au  'x, y, z, ...:' du Pascal
                ub = TWOPI / 360.00;
                azm = maVisee.Azimut * ub + qDeclinaison * PI_200;
                break;
            case 399:
            case 400: // visées directes en grades
                ub = TWOPI / 400.00;
                azm = maVisee.Azimut * ub + qDeclinaison * PI_200;
                break;
            case 349:
            case 350: // visées inverses en degrés
                ub = TWOPI / 360.00;
                azm = maVisee.Azimut * ub + qDeclinaison * PI_200;
                azm = Math.PI + azm;
                break;
            case 389:
            case 390: // visées inverses en grades
                ub = TWOPI / 400.00;
                azm = maVisee.Azimut * ub + qDeclinaison * PI_200;
                azm = Math.PI + azm;
                break;
            default: // protection = supposé direct en degré
                ub = TWOPI / 360.00;
                azm = maVisee.Azimut * ub + qDeclinaison * PI_200;
                break;
        } //  switch (ubb)
        // Déterminer si on travaille en zénithal*)
        ucc = trunc(qUPente);
        // Correction erreurs systématiques des pentes
        CorrectionPenteInRadians = convGradToRad(qInclinaison / 10.00);
        switch (ucc) {
            case 360:
            case 400: // zéro à l'horizontale
                uc = TWOPI / ucc;
                pentem = CorrectionPenteInRadians + maVisee.Pente * uc;
                lp = maVisee.Longueur * Math.cos(pentem);
                rx = lp * Math.sin(azm);
                ry = lp * Math.cos(azm);
                rz = maVisee.Longueur * Math.sin(pentem);
                break;
            case 361:
            case 401: // zéro zénithal
                uc = TWOPI / (ucc - 1);
                pentem = PI_2 - (CorrectionPenteInRadians + maVisee.Pente * uc);
                lp = maVisee.Longueur * Math.cos(pentem);
                rx = lp * Math.sin(azm);
                ry = lp * Math.cos(azm);
                rz = maVisee.Longueur * Math.sin(pentem);
                break;
            case 359:
            case 399: // zéro nadiral
                uc = TWOPI / (ucc + 1);
                lp = maVisee.Longueur * Math.cos(-(PI_2 - (maVisee.Pente * uc)));
                rx = lp * Math.sin(azm);
                ry = lp * Math.cos(azm);
                pentem = -(PI_2 - (CorrectionPenteInRadians + maVisee.Pente * uc));
                rz = maVisee.Longueur * Math.sin(pentem); // Fixé le 31/05.
                break;
            case 370: // DONE: Pourcentages sont exprimés sous la forme 59.11 pour 59.11%
                udd = Math.atan(maVisee.Pente / 100.0);
                lp = maVisee.Longueur * Math.cos(udd);
                rx = lp * Math.sin(azm);
                ry = lp * Math.cos(azm);
                rz = maVisee.Longueur * Math.sin(udd);
                break;
            case 380: // dans ce mode, les données sont 'Longueur VISEE et DENIVELEE
                // différences d'altitude, stockées ds Visee.Pente
                // DONE: Formule de calcul de lp corrigée le 15/10/2010
                lp = Math.sqrt(maVisee.Longueur * maVisee.Longueur - maVisee.Pente * maVisee.Pente);
                rx = lp * Math.sin(azm);
                ry = lp * Math.cos(azm);
                rz = maVisee.Pente;
                break;
            case 350:
            case 390: // visées inverses en degrés/grades (ucc = UniteClino -10);
                uc = TWOPI / (ucc + 10);
                pentem = CorrectionPenteInRadians - maVisee.Pente * uc;
                lp = maVisee.Longueur * Math.cos(pentem);
                rx = lp * Math.sin(azm);
                ry = lp * Math.cos(azm);
                rz = maVisee.Longueur * Math.sin(pentem);
                break;
            case 790:
            case 800: // Lasermètre Stanley TLM330
                lp = maVisee.Longueur; // longueur projetée est dans Long
                rx = lp * Math.sin(azm);
                ry = lp * Math.cos(azm);
                rz = maVisee.Pente; // dénivelée est dans Pente
                // visée inverse ?
                if (ucc == 790) rz = -rz;
                break;
            default: // zéro horizontal par défault
                uc = TWOPI / ubb;
                pentem = CorrectionPenteInRadians + maVisee.Pente * uc;
                lp = maVisee.Longueur * Math.cos(pentem);
                rx = lp * Math.sin(azm);
                ry = lp * Math.cos(azm);
                rz = maVisee.Longueur * Math.sin(pentem);
                break;

        } // switch (ucc)
        Result.dX = rx;
        Result.dY = ry;
        Result.dZ = rz;
        lp = Math.hypot(rx, ry);
        Result.LDev = Math.hypot(lp, rz); // Autorisé ici: Lp et lp sont deux variables différentes (case sensitive)
        return Result;
    }
    /**
     * Retourne True si x est entre a et b, bornes comprises
     * @param x 
     * @param a
     * @param b
     * @return 
     */
    public static boolean IsInRangeInt(int x, int a, int b)
    {
        return (x >= a) && (x <= b);
    }  
    /**
     * Retourne True si x est entre a et b, bornes comprises
     * @param x
     * @param a
     * @param b
     * @return 
     */
    public static boolean IsInRangeDbl(double x, double a, double b)
    {
        return (x >= a) && (x <= b);
    }
    /**
     * Convertit un entier en texte
     * @param v
     * @return 
     */
    public static String intToStr(int v)
    {
        return String.format("%d", v);
    }        
    
    /**
     * Construit de manière sécurisée une date GHTopo
     * @param d int
     * @return 
     */
    public static int ensureMakeGHTopoDateFromInt(int d)
    {
        int YYYY = trunc(d / 10000);
        int MMDD = d % 10000;
        int MM   = trunc (MMDD / 100);
        int DD   = MMDD % 100; 
        return ensureMakeGHTopoDateFromYMD(YYYY, MM, DD);
    } 
    /**
     * Construction sécurisée d'une date GHTopo
     * L'année pivot est 1950; peut être fournie sur deux ou quatre chiffres
     * @param YYYY
     * @param MM
     * @param DD
     * @return 
     */
    public static int ensureMakeGHTopoDateFromYMD(int YYYY, int MM, int DD)
    {
        //afficherMessageFmt("Annee %04d - Mois %02d- Jour %02d", YYYY, MM, DD);
        int YY = 0;
        if (YYYY < 100) // années sur deux chiffres ?
        {
            YY = (YYYY < 50) ? 2000 + YYYY : 1900 + YYYY;
        }   
        else  // sinon, année supposée au format (Y)YYY
        {
            YY = YYYY;
        }    
        //afficherMessageFmt("%04d-%02d-%02d", YY, MM, DD);
        return YY * 10000 + MM * 100 + DD; 
    }
    /**
     * Retourne une date GHTopo depuis une date au format français
     * Format de date: JJ/MM/AAAA -- Validé
     * @param d
     * @return 
     */
    public static int ensureMakeGHTopoDateFromFrenchDateStr(String d)
    {
        String[] EWE = StringUtil.split(d.trim(), "/");
        int JJ   = strToIntDef(EWE[0].trim(), 1);
        int MM   = strToIntDef(EWE[1].trim(), 1);
        int AAAA = strToIntDef(EWE[2].trim(), 1970);
        return ensureMakeGHTopoDateFromYMD(AAAA, MM, JJ);               
    }        
    // traitement des dates sans passer par l'objet Date
    /**
     * Retourne l'année sur 4 chiffres depuis une date GHTopo
     * @param myDate
     * @return 
     */
    public static int getYear(int myDate)
    {
        //afficherMessageFmt("Date: %d - Année: %d", myDate, trunc(myDate/10000.0));
        return trunc(myDate / 10000.0);
    }
    /**
     * Retourne le mois (1.. 12) depuis une date GHTopo
     * @param myDate
     * @return 
     */
    public static int getMonth(int myDate)
    {
        int EWE = myDate % 10000;
        return trunc(EWE / 100.0);
    }
    /**
     * Retourne le jour depuis une date GHTopo
     * @param myDate
     * @return 
     */
    public static int getDay(int myDate)
    {
        return myDate % 100;
    }
    /**
     * Retourne l'index d'un texte dans une liste, ou -1 si échec
     * @param S
     * @param b
     * @param args
     * @return 
     */
    public static int indexOfString(String S, boolean b, String args[])
    {
        int nb = args.length;
        //afficherMessageFmt("indexofstring: %d", nb);
        if (nb == 0) return -1;
        for (int i = 0; i < nb; i++)
        {
            //afficherMessage(S + " " + args[i]);
            if (S.equals(args[i])) return i;
        } 
        return -1;
    }   
    /**
     * Retourne l'item à l'index i du tableau de chaînes
     * @param idx
     * @param args
     * @return 
     */
    public static String ChooseString(int idx, String args[])
    {
        return args[idx];
    }
    
    /**
     * Construit une date JJ/MM/AAAA depuis une date GHTopo
     * @param d
     * @return 
     */
    public static String makeFormattedDateFromEnsuredInt(int d)
    {
        int YYYY = trunc(d / 10000);
        int MMDD = d % 10000;
        int MM   = trunc (MMDD / 100);
        int DD   = MMDD % 100; 
        return String.format("%02d/%02d/%04d", DD, MM, YYYY);
    }   
    /**
     * retourne la description de la visée de type int idx
     * @param idx
     * @return 
     */
    public static String getDescTypeReseau(int idx)
    {
        ResourceBundle BD = java.util.ResourceBundle.getBundle("com/ghtopo/javaghtopo/Bundle");
		
	String[] s = 
        {
           BD.getString("rsCDR_RESEAU_CMB_CAVITE_NATURELLE"),
           BD.getString("rsCDR_RESEAU_CMB_CAVITE_ARTIFICIELLE"),
           BD.getString("rsCDR_RESEAU_CMB_TOPO_SURFACE"),
           BD.getString("rsCDR_RESEAU_CMB_THALWEG"),
           BD.getString("rsCDR_RESEAU_CMB_PATH"),
           BD.getString("rsCDR_RESEAU_CMB_SENTIER"),
           BD.getString("rsCDR_RESEAU_CMB_AUTRE")
        };
        return s[idx];
    }
    // pour remplacer la première occurrence 
    
    /**
     * Remplace la première occurrence, et seulement la première
     * (sinon, utiliser String.replace(char, char) pour remplacer toutes les occurrences)
     * @param s
     * @param oldChar
     * @param newChar
     * @return 
     */
    public static String replaceFirstCharFound(String s, char oldChar, char newChar)
    {
        int P = s.indexOf(oldChar);
        if (P == -1) return s;
        char [] chars = s.toCharArray();
        int nb = chars.length;
        for (int i = 0; i < nb; i++)
        {
            if (chars[i] == oldChar)
            {
                chars[i] = newChar;
                break;
            }    
        }    
        s = String.copyValueOf(chars);
        return s;
    }
   
    /**
     * Fonction Now() formattée
     * @return 
     */
    public static String dateMaintenant()
    {
        // todo: Implementer
        Date maintenant = new Date();
        String result = maintenant.toString();
	return result;
    }
    // fonctions de couleurs
    public static String javaColorToHTMLColor(Color c)
    {
        return String.format("#%.2X%.2X%.2X", c.getRed(), c.getGreen(),c.getBlue());
    } 
   
    
    /**
     * Retourne une couleur KML à partir de ses composantes
     * @param R
     * @param G
     * @param B
     * @param A
     * @return 
     */
    public static String KMLColor(int R, int G, int B, int A)
    {        
        return String.format("<color>%02X%02X%02X%02X</color>\n", A, B, G, R);
    }
    public static String javaColorToKMLColor(Color c)
    {
        return KMLColor(c.getRed(), c.getGreen(),c.getBlue(), c.getAlpha());
    } 
    // TODO: Voir si les R et B sont inversés
    public static int javaColorToPasColor(Color C)
    {
        return C.getBlue()  * 65536 +
               C.getGreen() * 256 +
               C.getRed();
    }        
    /**
     * Ligne d'entête XML
     * @param version
     * @param encodage
     * @return 
     */
    public static String FormatterVersionEncodageXML(String version, String encodage)
    {        
        return String.format("<?xml version=\"%s\" encoding=\"%s\"?>\n", version, encodage);
    }
    /**
     * Retourne True si les séries attributaires des entités E1 et E2 sont identiques
     * @param E1
     * @param E2
     * @return 
     */
    public static boolean IsSameSerie(TEntiteEtendue E1, TEntiteEtendue E2)
    {
        return (E1.EntiteSerie   == E2.EntiteSerie) &&
               (E1.EntiteStation == (E2.EntiteStation + 1));
    }
    /**
     *  Formatter longitudes et latitudes pour GE et OSM
     *  de manière à ne pas être gêné par le séparateur décimal
     * @param v
     * @return 
     */
    public static String ensureConvertLongOrLatToXML(double v)
    {
        return String.format(Locale.US, "%.8f", v);
    }
    /**
     * Formatage sécurisé des nombres
     * Peut à l'avenir être différent de ensureConvertLongOrLatToStr()
     * @param v
     * @return 
     */
    public static String ensureConvertNumberToStr(double v)
    {
        return String.format(Locale.US, "%.8f", v);
    }   
    /**
     * Retourne un nombre formaté pour GHCaveDraw
     * @param v
     * @return 
     */
    public static String ensureConvertCoordToGCD(double v)
    {
        return String.format(Locale.US, "%.2f", v);
    }   
    /**
     * Convertir un texte en nombre avec support indifférencié de la virgule ou du point
     * @param s
     * @param qdefault
     * @return 
     */
    public static double ensureConvertStrToNumber(String s, double qdefault)
    {
        String toto = s.trim().replace(',', '.');
        afficherMessageFmt("ensureConvertStrToNumber %s - %s", s, toto);
        return strToFloatDef(toto, qdefault);
    }  
    /**
     * Suggère une équidistance de grille en fonction de l'étendue du réseau
     * @param C1
     * @param C2
     * @param Default
     * @return 
     */
    public static double proposerEquidistanceDef(TPoint3Df C1, TPoint3Df C2, double Default)
    {
        double result = Default;
        try
        {
            double d = Math.hypot(C2.X - C1.X, C2.Y - C1.Y);
            d = d / 10.0f;
            result = d;
            if (IsInRangeDbl(d,    0.00,   10.00)) result = 10.00;
            if (IsInRangeDbl(d,   10.00,   25.00)) result = 25.00;
            if (IsInRangeDbl(d,   25.00,   50.00)) result = 50.00;
            if (IsInRangeDbl(d,   50.00,  100.00)) result = 100.00;
            if (IsInRangeDbl(d,  100.00,  200.00)) result = 200.00;
            if (IsInRangeDbl(d,  200.00,  250.00)) result = 250.00;
            if (IsInRangeDbl(d,  250.00,  500.00)) result = 500.00;
            if (IsInRangeDbl(d,  500.00, 1000.00)) result = 1000.00;
            if (IsInRangeDbl(d, 1000.00, 2000.00)) result = 2000.00;
            if (IsInRangeDbl(d, 2000.00, 5000.00)) result = 5000.00;
            if (IsInRangeDbl(d, 5000.00,10000.00)) result = 10000.00;
            if (IsInRangeDbl(d,10000.00,20000.00)) result = 20000.00;
             // protection 'anti-nul'
            if (result < 5.00) result = 5.00;
            return result;
        }   
        catch (Exception e)
        {
            e.printStackTrace();
            return result;
        }    
    }
    /**
    * Incrémentation lexicographique
    * Si le string ne comporte que des lettres, on ajoute un indice à la fin
    * Si le string comporte un préfixe et un indice, on incrémente l'indice
    * Validée.
    * @param S0
    * @return 
    */
    public static String autoIncrementerLbTerrain(String S0)
    {
        String S = " " + S0 + " "; // protection par des espaces
        String groupe = "";
        String prefix = "";    
        int    Index  = 0;
        // décomposition
        char Seps[]  = {'.', ':', '/', '-', ';', ','};
        char CurrSep = 0; // un char est un nombre.
        int nbc = Seps.length;
        int p = -1;
        for (int i = 0; i < nbc; i++)
        {  
            p = S.indexOf(Seps[i]);
            if (p >= 0)
            {
                CurrSep = Seps[i];
                break;
            }    
        }
        
        if (p > 0) groupe = S.substring(0, p); // première forme de substring: début et fin
        groupe = groupe.trim(); // nettoyage
        String reste = (groupe.equals("")) ? S.trim() : S.substring(p); //  deuxième forme de substring: début seulement
        reste = " " + reste.trim() + " ";
        // rechercher un chiffre
        nbc = reste.length();
        p = -1;
        for (int i = 0; i < nbc; i++)
        {
            char c = reste.charAt(i);
            if (IsInRangeInt(c, '0', '9'))
            { 
                p = i;
                break;
            }    
        }   
        if (p > 0)
        {
            prefix = reste.substring(0, p).trim();
            String t = reste.substring(p).trim();
            Index  = 1 + strToIntDef(t, 0);
        }
        else
        {
            prefix = reste.trim();
            Index  = 0;
        }    
        return sprintf("%s%s%d", groupe, prefix, Index);
    }
    // dégradé de couleurs
    // Validé à raison des composantes RGB
    public static Color getColorDegrade(double z, double zmin, double zmax, Color Coul1, Color Coul2)
    {
        Color result = new Color(80, 80, 80);     
        try
        {    
            double d = zmax - zmin;
            if (Math.abs(d) < 1e-8) return Coul1; // pour éviter une div/0
            double h = (z - zmin) / d;
            int dR = Coul2.getRed()   - Coul1.getRed();
            int dG = Coul2.getGreen() - Coul1.getGreen();
            int dB = Coul2.getBlue()  - Coul1.getBlue();        
            int qR = trunc(Coul1.getRed()   + h * dR);
            int qG = trunc(Coul1.getGreen() + h * dG);
            int qB = trunc(Coul1.getBlue()  + h * dB);
            result = new Color(qR, qG, qB);        
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return result;
        }    
    }
  
    //--------------------------------------------------------------------------
    /**
     * Angle bissecteur de deux segments
     * @param dX1
     * @param dY1
     * @param dX2
     * @param dY2
     * @return 
     */
    public static double calculerAngleBissecteur(double dX1, double dY1, double dX2, double dY2)
    {
        TPoint3Df v1 = makeTPoint3Df(dX1, dY1, 0.00);
        TPoint3Df v2 = makeTPoint3Df(dX2, dY2, 0.00);
        TPoint3Df w  = makeTPoint3Df(0.00, 0.00, 1.00); 
        // cross products
        v1 = produitVectoriel(v1, w, true);
        v2 = produitVectoriel(v2, w, true);
        // composition Vectorielle
        w.X = v1.X + v2.X;
        w.Y = v1.Y + v2.Y;
        w.Z = v1.Z + v2.Z;
        // angles
        return Math.atan2(w.Y + 1E-12, w.X + 1E-12);
    }
    public static boolean IsViseetInNaturalCave(TEntiteEtendue E)
    {
        boolean Result = (E.TypeEntite == tgDEFAULT) ||
                         (E.TypeEntite == tgFOSSILE) ||    
                         (E.TypeEntite == tgVADOSE) ||    
                         (E.TypeEntite == tgENNOYABLE) ||    
                         (E.TypeEntite == tgSIPHON);
        return Result;
    }
    public static boolean FileExists(String filename)
    {
        File fp = new File(filename);
        return fp.exists();
    }        
  
}
