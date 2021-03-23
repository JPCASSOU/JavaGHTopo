package GHTopoOperationnel.Types;

// ------------------------------------------------------------

import java.awt.Color;

// structure pour les entités (résultats du calcul)
public class TEntiteEtendue 
{
    //  serie et point
    public int EntiteSerie;   //  Série
    public int EntiteStation;   //  Station
    //  secteurs, type de visée, réseaux, codes, expés
    public int TypeEntite;
    public int eSecteur;
    public int eReseau;
    public int eCode;
    public int eExpe;
    public int DateLeve; // la date est au format YYYYMMDD
    //  drapeaux
    public boolean Drawn;
    //  valeurs initiales: Long, Az, P
    public double oLongueur;
    public double oAzimut;
    public double oPente;
    public double oLG;
    public double oLD;
    public double oHZ;
    public double oHN;
    //  valeurs calculées: centerline
    public double UneStation1X;
    public double UneStation1Y;
    public double UneStation1Z;

    public double UneStation2X;
    public double UneStation2Y;
    public double UneStation2Z;
    //  valeurs calculées: silhouette
    public double X1PD; //X point droit contour
    public double Y1PD; //Y point gauche contour
    public double X1PG; //X point droit contour
    public double Y1PG; //Y point gauche contour

    public double X2PD; //X point droit contour
    public double Y2PD; //Y point gauche contour
    public double X2PG; //X point droit contour
    public double Y2PG; //Y point gauche contour

    public double Z1PH; //Z point haut contour
    public double Z1PB; //Z point bas contour
    public double Z2PH; //Z point haut contour
    public double Z2PB; //Z point bas contour
    // NOUVEAU: La visée est une jonction
    public boolean IsJonction = false;
    //  valeur calculée: Couleur en fonction de la profondeur
    //  stockée ici en raison du grand nombre de calculs pour le dégradé
    public Color ColourByDepth;
    //  champs texte => en fin de ligne
    public String oIDLitteral;
    public String oCommentaires;
    // comparaison
    /*
    public int CompareTo(TEntiteEtendue autreEntite)
    {
      int resultat;
      if (this.UneStation2Z > autreEntite.UneStation2Z)
         resultat = 1;
      if (this.point < autrejoueur.point)
         resultat = -1;
      if (this.point == autrejoueur.point)
         resultat = 0;
      return resultat;
   }
   //*/ 
}