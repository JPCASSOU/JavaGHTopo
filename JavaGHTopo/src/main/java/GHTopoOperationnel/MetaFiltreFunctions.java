/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GHTopoOperationnel;

import static GHTopoOperationnel.Constantes.*;
import static GHTopoOperationnel.GeneralFunctions.*;
import GHTopoOperationnel.Types.TFiltre;
import GHTopoOperationnel.Types.TEntiteEtendue;
import java.util.ResourceBundle;
import java.util.ArrayList;

public class MetaFiltreFunctions 
{
    private static int ChooseFilter(String s)
    {
        String S = s.trim().toUpperCase();
        ResourceBundle EWE = java.util.ResourceBundle.getBundle("com/ghtopo/javaghtopo/Bundle");
        String[] ListeFilters = 
        {
            EWE.getString("rsMETAFILTRE_KWD_0"), //kFLT_NIL       -- rsMETAFILTRE_KWD_0=RIEN
            EWE.getString("rsMETAFILTRE_KWD_1"), //kFLT_ALL -- rsMETAFILTRE_KWD_1=TOUT
            EWE.getString("rsMETAFILTRE_KWD_2"), //kFLT_ID -- rsMETAFILTRE_KWD_2=IDSTATION
            EWE.getString("rsMETAFILTRE_KWD_3"), //kFLT_LONGUEUR -- rsMETAFILTRE_KWD_3=LONGUEUR
            EWE.getString("rsMETAFILTRE_KWD_4"), //kFLT_AZIMUT -- rsMETAFILTRE_KWD_4=AZIMUT
            EWE.getString("rsMETAFILTRE_KWD_5"), //kFLT_PENTE -- rsMETAFILTRE_KWD_5=PENTE
            EWE.getString("rsMETAFILTRE_KWD_6"), //kFLT_DATE -- rsMETAFILTRE_KWD_6=DATE
            EWE.getString("rsMETAFILTRE_KWD_7"), //kFLT_COULEUR -- rsMETAFILTRE_KWD_7=COULEUR
            EWE.getString("rsMETAFILTRE_KWD_8"), //kFLT_X -- rsMETAFILTRE_KWD_8=COORD_X
            EWE.getString("rsMETAFILTRE_KWD_9"), //kFLT_Y -- rsMETAFILTRE_KWD_9=COORD_Y
            EWE.getString("rsMETAFILTRE_KWD_10"), //kFLT_Z -- rsMETAFILTRE_KWD_10=COORD_Z
            EWE.getString("rsMETAFILTRE_KWD_11"), //kFLT_LARGEUR -- rsMETAFILTRE_KWD_11=LARGEUR
            EWE.getString("rsMETAFILTRE_KWD_12"), //kFLT_HAUTEUR -- rsMETAFILTRE_KWD_12=HAUTEUR
            EWE.getString("rsMETAFILTRE_KWD_13"), //kFLT_DATES -- rsMETAFILTRE_KWD_13=DATES
            EWE.getString("rsMETAFILTRE_KWD_14"), //kFLT_COULEURS -- rsMETAFILTRE_KWD_14=COULEURS
            EWE.getString("rsMETAFILTRE_KWD_15"), //kFLT_SERIE -- rsMETAFILTRE_KWD_15=SERIE
            EWE.getString("rsMETAFILTRE_KWD_16"), //kFLT_RESEAU -- rsMETAFILTRE_KWD_16=RESEAU
            EWE.getString("rsMETAFILTRE_KWD_17"), //kFLT_CODE  -- rsMETAFILTRE_KWD_17=CODE
            EWE.getString("rsMETAFILTRE_KWD_18"), //kFLT_EXPE -- rsMETAFILTRE_KWD_18=EXPE
            EWE.getString("rsMETAFILTRE_KWD_19"), //kFLT_TYPEVISEE -- rsMETAFILTRE_KWD_19=TYPEVISEE
            EWE.getString("rsMETAFILTRE_KWD_20"), //kFLT_SECTEUR -- rsMETAFILTRE_KWD_20=SECTEUR
        };
        return indexOfString(S, true, ListeFilters);
    }        
    public static boolean DoDrawVisee(TFiltre F, TEntiteEtendue E)
    {
        boolean result = false;
        double qLong   = 0;
        String s       = "";
        int p          = 0;
        int q          = 0;
        int iA         = 0; // bornes entières
        int iB         = 0; 
        double dA      = 0.00; // bornes réelles
        double dB      = 0.00; 
        double d       = 0.00;
        double h       = 0.00;
        double v       = 0.00;
        int q666       = tgDEFAULT;
        switch (F.TypeFiltre)
        {    
            case ftINVALID:
                break;
            case ftSCALAR:
                //afficherMessageFmt("Le filtre est scalaire: %d ", F.Filter);
                switch(F.Filter)
                {    
                    case -1:
                    case kFLT_NIL:
                        break;
                    case kFLT_ALL:
                        result = true;
                        break;
                    case kFLT_ID:                        
                        result = F.Valeur.trim().equals(E.oIDLitteral.trim());
                        break;
                    case kFLT_LONGUEUR:
                        qLong = hypot3D(E.UneStation2X - E.UneStation1X,
                                        E.UneStation2Y - E.UneStation1Y,
                                        E.UneStation2Z - E.UneStation1Z);
                        switch(F.Operateur)
                        {
                            case 1 : result = (qLong <= strToFloatDef(F.Valeur, 0.00)); break;
                            case 3 : result = (qLong >= strToFloatDef(F.Valeur, 0.00));break;
                            default: result = false; break;
                        } 
                        break;
                    case kFLT_DATE:     // TODO: Dates
                        iA = ensureMakeGHTopoDateFromFrenchDateStr(F.Valeur);
                        switch(F.Operateur)
                        {
                            case 1 : result = (E.DateLeve <= iA); 
                                break;
                            case 2 : result = (E.DateLeve == iA);
                                break;
                            case 3 : result = (E.DateLeve >= iA);
                                break;
                            default: result = false; break;
                        } 
                        break;
                    case kFLT_COULEUR:  // TODO: Modifications à faire; inertée
                        break;
                    case kFLT_X:
                        d = strToFloatDef(F.Valeur, 0.00);
                        switch(F.Operateur)
                        {
                            case 1 : result = ((E.UneStation1X <= d) || (E.UneStation2X <= d)); break;
                            case 3 : result = ((E.UneStation1X >= d) || (E.UneStation2X >= d)); break;
                            default: result = false; break;    
                        }
                        break;
                    case kFLT_Y:
                        d = strToFloatDef(F.Valeur, 0.00);
                        switch(F.Operateur)
                        {
                            case 1 : result = ((E.UneStation1Y <= d) || (E.UneStation2Y <= d)); break;
                            case 3 : result = ((E.UneStation1Y >= d) || (E.UneStation2Y >= d)); break;
                            default: result = false; break;    
                        }
                        break;
                    case kFLT_Z:
                        d = strToFloatDef(F.Valeur, 0.00);
                        switch(F.Operateur)
                        {
                            case 1 : result = ((E.UneStation1Z <= d) || (E.UneStation2Z <= d)); break;
                            case 3 : result = ((E.UneStation1Z >= d) || (E.UneStation2Z >= d)); break;
                            default: result = false; break;    
                        }
                        break;
                    case kFLT_LARGEUR: // filtres sur largeur
                        d = strToFloatDef(F.Valeur, 0.00);
                        h = Math.hypot(E.X1PG - E.X1PD, E.Y1PG - E.Y1PD);
                        v = Math.hypot(E.X2PG - E.X2PD, E.Y2PG - E.Y2PD);
                        switch(F.Operateur)
                        {
                            case 1 : result = ((v <= d) || (h <= d)); break;
                            case 3 : result = ((h >= d) || (v >= d)); break;
                            default: result = false; break;    
                        }
                        break;
                    case kFLT_HAUTEUR: // filtres sur hauteurs
                        d = strToFloatDef(F.Valeur, 0.00);
                        h = E.Z1PH - E.Z1PB;
                        v = E.Z2PH - E.Z2PB;
                        switch(F.Operateur)
                        {
                            case 1 : result = ((v <= d) || (h <= d)); break;
                            case 3 : result = ((h >= d) || (v >= d)); break;
                            default: result = false; break;    
                        }
                        break;
                    case kFLT_SERIE: // filtre sur séries
                        //afficherMessageFmt(" -- Filtre sur série: %d - %d", E.EntiteSerie, p);
                        p = strToIntDef(F.Valeur, 0);
                        switch(F.Operateur)
                        {
                            case 1 : result = (E.EntiteSerie <= p); break;
                            case 2 : result = (E.EntiteSerie == p); break;
                            case 3 : result = (E.EntiteSerie >= p); break;
                            default: result = false; break;    
                        }
                        break;
                    case kFLT_RESEAU: // filtre sur réseaux p = strToIntDef(F.valeur, 0);
                        p = strToIntDef(F.Valeur, 0);
                        switch(F.Operateur)
                        {
                            case 1 : result = (E.eReseau <= p); break;
                            case 2 : result = (E.eReseau == p); break;
                            case 3 : result = (E.eReseau >= p); break;
                            default: result = false; break;    
                        }
                        break;
                    case kFLT_SECTEUR: // filtre sur secteurp = strToIntDef(F.valeur, 0);
                        p = strToIntDef(F.Valeur, 0);
                        switch(F.Operateur)
                        {
                            case 1 : result = (E.eSecteur <= p); break;
                            case 2 : result = (E.eSecteur == p); break;
                            case 3 : result = (E.eSecteur >= p); break;
                            default: result = false; break;    
                        }
                        break;
                    case kFLT_CODE: // filtre sur codes
                        p = strToIntDef(F.Valeur, 0);
                        switch(F.Operateur)
                        {
                            case 1 : result = (E.eCode <= p); break;
                            case 2 : result = (E.eCode == p); break;
                            case 3 : result = (E.eCode >= p); break;
                            default: result = false; break;    
                        }
                        break;
                    case kFLT_EXPE: // filtre sur expés
                        p = strToIntDef(F.Valeur, 0);
                        switch(F.Operateur)
                        {
                            case 1 : result = (E.eExpe <= p); break;
                            case 2 : result = (E.eExpe == p); break;
                            case 3 : result = (E.eExpe >= p); break;
                            default: result = false; break;    
                        }
                        break;
                    case kFLT_TYPEVISEE: // filtre sur type de visée
                        p = strToIntDef(F.Valeur, 0); 
                        result = (p == E.TypeEntite);
                        break;                        
                    default:
                        result = false;
                        break;
                } // switch(F.Filter)
                break;
            case ftINTERVAL:  
                switch(F.Filter)
                {
                    case -1:                        
                    case kFLT_NIL:
                        result = false; 
                        break;
                    case kFLT_AZIMUT:
                        dA = strToFloatDef(F.BorneInf, 0.00);
                        dB = strToFloatDef(F.BorneSup, 0.00);
                        v  = getAzimut(E.UneStation2X - E.UneStation1X,
                                       E.UneStation2Y - E.UneStation1Y,
                                       360.00);
                        result = IsInRangeDbl(v, dA, dB) || IsInRangeDbl(v + 180, dA, dB);                                
                        break;
                    case kFLT_PENTE:
                        dA = strToFloatDef(F.BorneInf, 0.00);
                        dB = strToFloatDef(F.BorneSup, 0.00);
                        qLong = 1e-12 + hypot3D(E.UneStation2X - E.UneStation1X,
                                                E.UneStation2Y - E.UneStation1Y,
                                                E.UneStation2Z - E.UneStation1Z);
                        h = E.UneStation2Z - E.UneStation1Z;
                        v = Math.asin(h / qLong) * INV_DEGREES;
                        result = ((v > dA) || (v < dB));
                        break;   
                    case kFLT_DATE: // TODO: Dates à implémenter
                        // conversion des dates en entier
                        iA = ensureMakeGHTopoDateFromFrenchDateStr(F.BorneInf);
                        iB = ensureMakeGHTopoDateFromFrenchDateStr(F.BorneSup);
                        result = IsInRangeDbl(E.DateLeve, iA, iB);                        
                        break;
                    case kFLT_COULEUR: // TODO: Couleurs   
                        break; 
                    case kFLT_X:
                        dA = strToFloatDef(F.BorneInf, 0.00);
                        dB = strToFloatDef(F.BorneSup, 0.00);
                        v = E.UneStation1X;
                        result = IsInRangeDbl(v, dA, dB);
                        v = E.UneStation2X;
                        result = result || IsInRangeDbl(v, dA, dB);
                        break;    
                    case kFLT_Y:
                        dA = strToFloatDef(F.BorneInf, 0.00);
                        dB = strToFloatDef(F.BorneSup, 0.00);
                        v = E.UneStation1Y;
                        result = IsInRangeDbl(v, dA, dB);
                        v = E.UneStation2Y;
                        result = result || IsInRangeDbl(v, dA, dB);
                        break;    
                    case kFLT_Z:
                        dA = strToFloatDef(F.BorneInf, 0.00);
                        dB = strToFloatDef(F.BorneSup, 0.00);
                        v = E.UneStation1Z;
                        result = IsInRangeDbl(v, dA, dB);
                        v = E.UneStation2Z;
                        result = result || IsInRangeDbl(v, dA, dB);
                        break;    
                    case kFLT_SERIE: // filtre sur séries OK
                        
                        iA = strToIntDef(F.BorneInf, 0);
                        iB = strToIntDef(F.BorneSup, 0);
                        afficherMessageFmt("Intervalle séries: %d < %d < %d", E.EntiteSerie, iA, iB);
                        result = IsInRangeInt(E.EntiteSerie, iA, iB);
                        break;
                    case kFLT_RESEAU: // filtre sur reseaux
                        iA = strToIntDef(F.BorneInf, 0);
                        iB = strToIntDef(F.BorneSup, 0);
                        result = IsInRangeInt(E.eReseau, iA, iB);
                        break;
                    case kFLT_SECTEUR: // filtre sur secteurs
                        iA = strToIntDef(F.BorneInf, 0);
                        iB = strToIntDef(F.BorneSup, 0);
                        result = IsInRangeInt(E.eSecteur, iA, iB);
                        break;
                    case kFLT_CODE: // filtre sur codes
                        iA = strToIntDef(F.BorneInf, 0);
                        iB = strToIntDef(F.BorneSup, 0);
                        result = IsInRangeInt(E.eCode, iA, iB);
                        break;
                    case kFLT_EXPE: // filtre sur expes
                        iA = strToIntDef(F.BorneInf, 0);
                        iB = strToIntDef(F.BorneSup, 0);
                        result = IsInRangeInt(E.eExpe, iA, iB);
                        break;
                    default:
                        result = false; 
                        break;
                } // switch(F.Filter)   
                break;
            case ftFLAGS: // Désactivé (peu utilisé)
                break;
            default:
                break;
                    //*****************
        } // switch (F.TypeFiltre)
        return result;
    } // DoDrawvisee(TFiltre F, TEntiteEtendue E)
    //--------------------------------------------------------------------------
    private static int getConnector(String s)
    {
        
        if (s.length() == 0) return ftERROR;
       
        char c = s.charAt(0);
        afficherMessageFmt("GetConnector: %s: %s", s, c);
        switch (c)
        {    
            case 'U': // (U)nion
                afficherMessage("-- getconnector: ftOR");
                return ftOR;
            case 'N': // i(N)tersection
                afficherMessage("-- getconnector: ftAND");
                return ftAND;
            default:
                afficherMessage("-- getconnector: ftError");
                return ftERROR;
        }   
        
    }        
    private static TFiltre makeInvalidFiltre(String s)
    {
        TFiltre result = new TFiltre();
        result.Caption   = s;
        result.Filter    = -1;
        result.Operateur =  0;
        result.Valeur    = "";
        return result;
    }        
    private static TFiltre makeFiltreScalaire(String S, int codeOp, int ConnecteurPrevious)
    {
        afficherMessageFmt("makeFiltreScalaire: %s, %d", S, codeOp);
        TFiltre result = new TFiltre();
        String q = "";
        switch (codeOp)
        {
            case ftLT : q = "<"; break;
            case ftEQ : q = "="; break;
            case ftGT : q = ">"; break;
            default:    q = "="; break;
        }    
        String [] PB = S.split(q);
        if (PB.length != 2) return makeInvalidFiltre(S);
        result.Caption    = S;
        result.TypeFiltre = ftSCALAR;
        result.Filter    = ChooseFilter(PB[0]);
        result.Operateur = codeOp;
        result.Valeur    = PB[1].trim();
        result.ConnectedWithPrevious = ConnecteurPrevious;
        return result;
    }        
    public static TFiltre setFiltre(String F44)
    {
        afficherMessage("SetFiltre: " + F44);
        TFiltre result = makeInvalidFiltre(F44);
        result.Caption = F44;
        int P = 0;
        int Q = 0;
        String S1 = "";
        String S2 = "";
        // filtres "Tout" ou "Rien"
	ResourceBundle EWE = java.util.ResourceBundle.getBundle("com/ghtopo/javaghtopo/Bundle");
        String qfALL = EWE.getString("rsMETAFILTRE_KWD_0"); //kFLT_NIL       -- rsMETAFILTRE_KWD_0=RIEN
        String qfNIL = EWE.getString("rsMETAFILTRE_KWD_1"); //kFLT_ALL -- r
        if (F44.contains(qfALL))
        {
            result.Filter     = 1;
            result.Operateur  = 0;
            result.Valeur     = "";
            return result;
        }    
        if (F44.contains(qfNIL))
        {
            result.Filter     = 0;
            result.Operateur  = 0;
            result.Valeur     = "";
            return result;            
        }    
        // autres cas
        result.ConnectedWithPrevious = getConnector(F44);
        afficherMessageFmt("--- Autres cas: Connecteur: %d", result.ConnectedWithPrevious);
        // supprimer le caractère de tête
        F44 = F44.substring(1);
        if (result.ConnectedWithPrevious == ftERROR) result.ConnectedWithPrevious = ftOR;
        // rechercher un ensemble de drapeaux: DESACTIVE
        // rechercher un spécificateur d'intervalle
        // Formats acceptés:
        // <Filtre>=(A, B);
        //Q =;
        afficherMessage(F44);
        if (F44.contains("("))
        {
            afficherMessage("Filtre intervalle");
            // Résultat en entrée: F44 = "FILTRE = (123456.8   , 654321.09)"
            F44 = replaceFirstCharFound(F44, '(', ' '); // virer la parenthèse ouvrante
            F44 = replaceFirstCharFound(F44, ')', ' '); // virer la parenthèse fermante
            // Résultat attendu: F44 = "FILTRE = 123456.8  , 654321.09"
            F44 = replaceFirstCharFound(F44, '=', ';'); // séparateur pour les trois valeurs
            F44 = replaceFirstCharFound(F44, ',', ';'); // re
            // Résultat attendu: F44 = "FILTRE ; 123456.8  ; 654321.09"
            //rechercher la donnée à filtrer
            String PA[] = F44.split(";");                     // découper le filtre 
            // le split doit renvoyer 3 éléments
            if (PA.length != 3) return makeInvalidFiltre(F44);  
            result.Filter = ChooseFilter(PA[0]);
            result.BorneInf = PA[1];
            result.BorneSup = PA[2];
            // définir le type de filtre comme intervalle et quitter
            result.TypeFiltre = ftINTERVAL;
            afficherMessageFmt("%d - %d: %s - %s", result.Filter, result.TypeFiltre, result.BorneInf, result.BorneSup);
            return result;
        }    
        // rechercher une valeur scalaire
        if (F44.contains("<")) result = makeFiltreScalaire(F44, ftLT, result.ConnectedWithPrevious);
        if (F44.contains("=")) result = makeFiltreScalaire(F44, ftEQ, result.ConnectedWithPrevious);
        if (F44.contains(">")) result = makeFiltreScalaire(F44, ftGT, result.ConnectedWithPrevious);
        //* rechercher un point d'exclamation              *)
        //(* et mettre le flag d'inversion à True si trouvé *)
        result.Basculed =  (F44.contains("!"));
        return result;      
    }
    private static int FoundSeparator(String s2)
    {
        int result = -1;
        int nb = s2.length();
        for (int i = 0; i < nb; i++)
        {
            if ((s2.charAt(i) == '&') ||
                (s2.charAt(i) == '*') ||
                (s2.charAt(i) == '|') ||
                (s2.charAt(i) == '+'))
            {    
                result = i;
                break;
            }
        } 
        return result;   
    }        
    public static ArrayList<TFiltre> recenserFiltres(String qFiltre)
    {
        TFiltre f = new TFiltre();
        afficherMessage("-- RecenserFiltres: " + qFiltre);
        ArrayList<TFiltre> result = new ArrayList<TFiltre>();
        result.clear();
        int P = 0;
        int nbtours = 0;
        String S1 = "";
        String S2 = "N" + qFiltre;
        P = FoundSeparator(S2);
        while (P >= 0)
        {   
            afficherMessage("============================================");
            afficherMessageFmt("Passe: %d", nbtours);
            afficherMessageFmt("--- Avant ttt: S2 = '%s', Sep: %d", S2, P);           
            S1 = S2.substring(0, P).trim();
            afficherMessage("--- S1 = " + S1);
            S2 = S2.substring(P, S2.length()).trim();
            afficherMessage("--- S2 = " + S2);            
            if (S2.length() > 0)
            {
                char Q = S2.charAt(0);
                switch (Q)
                {
                    case '&':
                    case '*':
                        S2 = replaceFirstCharFound(S2, Q, 'N'); 
                        break;
                    case '|':
                    case '+':
                        S2 = replaceFirstCharFound(S2, Q, 'U');
                        break;                            
                }    
            }
            //*/        
            if (S1.length() > 0)
            {
                f = setFiltre(S1);
                result.add(f);
            }
            nbtours++;
            P = FoundSeparator(S2);
        }
        if (S2.length() > 0)
        {
            f = setFiltre(S2);
            result.add(f);
        }
        afficherMessage("====================================================");
        return result;
    }        
}
