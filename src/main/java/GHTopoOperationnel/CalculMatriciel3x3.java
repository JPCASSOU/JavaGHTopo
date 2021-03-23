package GHTopoOperationnel;
import static GHTopoOperationnel.GeneralFunctions.*;
import GHTopoOperationnel.Types.TPoint3Df;

public class CalculMatriciel3x3 {
    /**
     * Retourne le nombre de Kronecker Delta(i,j)
     * @param i int
     * @param j int
     * @return double 1 si i == j, 0 sinon 
     */
    public static double DeltaKronecker(int i, int j)
    {
        return (i == j) ? 1.00 : 0.00;
    } 
    /**
     * Retourne le déterminant de la matrice 3x3
     * @param M  TMatrix3x3
     * @return double
     */
    public static double DetMat3x3(TMatrix3x3 M)
    {
        return M.getValue(1, 1) * (M.getValue(3, 3) * M.getValue(2, 2) - M.getValue(3, 2) * M.getValue(2, 3)) +      // a11(a33a22-a32a23)
              -M.getValue(2, 1) * (M.getValue(3, 3) * M.getValue(1, 2) - M.getValue(3, 2) * M.getValue(1, 3)) +      //-a21(a33a12-a32a13)
               M.getValue(3, 1) * (M.getValue(2, 3) * M.getValue(1, 2) - M.getValue(2, 2) * M.getValue(1, 3));       //+a31(a23a12-a22a13)
    }
    /**
     * Inverse de la matrice 3x3 
     * @param M  TMatrix3x3
     * @return TMatrix3x3
     */
    public static TMatrix3x3 InvMat3x3(TMatrix3x3 M)
    {
        double ted = 1 / DetMat3x3(M);
        TMatrix3x3 R = new TMatrix3x3( M.getValue(3,3) * M.getValue(2,2) - M.getValue(3,2) * M.getValue(2,3), 
                                     -(M.getValue(3,3) * M.getValue(1,2) - M.getValue(3,2) * M.getValue(1,3)), 
                                      (M.getValue(2,3) * M.getValue(1,2) - M.getValue(2,2) * M.getValue(1,3)),
                                     -(M.getValue(3,3) * M.getValue(2,1) - M.getValue(3,1) * M.getValue(2,3)), 
                                      (M.getValue(3,3) * M.getValue(1,1) - M.getValue(3,1) * M.getValue(1,3)), 
                                     -(M.getValue(2,3) * M.getValue(1,1) - M.getValue(2,1) * M.getValue(1,3)),
                                      (M.getValue(3,2) * M.getValue(2,1) - M.getValue(3,1) * M.getValue(2,2)), 
                                     -(M.getValue(3,2) * M.getValue(1,1) - M.getValue(3,1) * M.getValue(1,2)), 
                                      (M.getValue(2,2) * M.getValue(1,1) - M.getValue(2,1) * M.getValue(1,2))
                                     );
        for (int i = 1; i <=3; i++)
            for (int j = 1; j <=3; j++)
            {
                double EWE = R.getValue(i, j) * ted; 
                R.setValue(i, j, EWE);
            }
        return R;                  
    }   
    /**
     * Produit matriciel des matrices A et B
     * @param A TMatrix3x3
     * @param B TMatrix3x3
     * @return TMatrix3x3
     */
    public static TMatrix3x3 ProduitMat3x3(TMatrix3x3 A, TMatrix3x3 B)
    {
        TMatrix3x3 R = new TMatrix3x3();
        for (int i = 1; i <=3; i++)
            for (int j = 1; j <=3; j++)
            {
                double EWE = 0.00;
                for (int k = 1; k <=3; k++)
                    EWE += A.getValue(i, k) * B.getValue(k, j);
                R.setValue(i, j, EWE);
            }
        return R;
    } 
    /**
     * Addition des matrices A et B
     * @param A TMatrix3x3
     * @param B TMatrix3x3
     * @return TMatrix3x3
     */
    public static TMatrix3x3 AddMatrix3x3(TMatrix3x3 A, TMatrix3x3 B)
    {
        TMatrix3x3 R = new TMatrix3x3();
        for (int i = 1; i <=3; i++)
            for (int j = 1; j <=3; j++)
                R.setValue(i, j, (A.getValue(i, j) + B.getValue(i, j)));           
        return R;
    }        
    /**
     * Soustraction des matrices A et B tq A - B
     * @param A TMatrix3x3
     * @param B TMatrix3x3
     * @return TMatrix3x3
     */
    public static TMatrix3x3 SubstractMatrix3x3(TMatrix3x3 A, TMatrix3x3 B)
    {
        TMatrix3x3 R = new TMatrix3x3();
        for (int i = 1; i <=3; i++)
            for (int j = 1; j <=3; j++)
                R.setValue(i, j, (A.getValue(i, j) - B.getValue(i, j)));           
        return R;
    }           
    /**
     * Multiplication par un réel
     * @param A TMatrix3x3
     * @param k double
     * @return TMatrix3x3
     */
    public static TMatrix3x3 MultMatrix3x3ByScalar(TMatrix3x3 A, double k)
    {
        TMatrix3x3 R = new TMatrix3x3();
        for (int i = 1; i <=3; i++)
            for (int j = 1; j <=3; j++)
                R.setValue(i, j, (A.getValue(i, j) * k));           
        return R;
    }   
    /**
     * Transpose la matrice A
     * @param A TMatrix3x3
     * @return TMatrix3x3
     */
    public static TMatrix3x3 TransposeMatrix3x3 (TMatrix3x3 A)
    {
        TMatrix3x3 R = new TMatrix3x3();
        for (int i = 1; i <=3; i++)
            for (int j = 1; j <=3; j++)
                R.setValue(i, j, (A.getValue(j, i)));           
        return R;
    }   
    /**
     * Calcule le produit extérieur (produit tensoriel) des vecteurs A et B
     * Retourne une matrice 3x3 et non un vecteur
     * A (x) B = A x Bt
     * @param A TPoint3Df
     * @param B TPoint3Df
     * @return TMatrix3x3
     */    
    public static TMatrix3x3 ProduitKroneckerVecteurs(TPoint3Df A, TPoint3Df B)
    {
        TMatrix3x3 R = new TMatrix3x3(A.X * B.X, A.X * B.Y, A.X * B.Z,
                                      A.Y * B.X, A.Y * B.Y, A.Y * B.Z,
                                      A.Z * B.X, A.Z * B.Y, A.Z * B.Z);                
        return R;
    } 
    /**
     * Calcul du triple produit des vecteurs A, B, C
     * tel que R = (A x B).C, l'opérateur x désigne le produit vectoriel
     * @param a TPoint3Df
     * @param b TPoint3Df
     * @param c TPoint3Df
     * @return double
     */
    public static double TripleProduitScalaire(TPoint3Df a, TPoint3Df b, TPoint3Df c)
    {
        TPoint3Df avb = new TPoint3Df();
        avb = produitVectoriel(a, b, false);
        return produitScalaire(avb, c);
    } 
    /**
     * Retourne la valeur maximale des termes de la matrice M
     * @param M TMatrix3x3
     * @return double
     */
    public static double  MaxNormeMatrix3x3(TMatrix3x3 M)
    {
        double r = -1e100;
        for (int i = 1; i <=3; i++)
            for (int j = 1; j <=3; j++)
            {
                double WU = M.getValue(i, j);
                if (WU > r) r = WU;
            }    
        return r;
    }
    /**
     * Norme euclidienne du vecteur V
     * @param V TPoint3Df
     * @return double
     */
    public static double NormeVecteur(TPoint3Df V)
    {
        return hypot3D(V.X, V.Y, V.Z);
    }
    /**
     * Retourne un vecteur ayant la même direction que V mais 
     * dont la norme est égale à 1
     * @param V TPoint3Df
     * @return TPoint3Df
     */
    public static TPoint3Df NormaliseVecteur(TPoint3Df V)
    {
        double r = NormeVecteur(V);
        TPoint3Df Result = new TPoint3Df();
        Result.X = V.X / r;
        Result.Y = V.Y / r;
        Result.Z = V.Z / r;
        return Result;
    }  
    /**
     * Retourne l'angle en radians formé par les vecteurs A et B 
     * @param a TPoint3Df
     * @param b TPoint3Df
     * @return double
     */
    public static double AngleBetweenVectors(TPoint3Df a, TPoint3Df b)
    {
        TPoint3Df V1 = new TPoint3Df();
        TPoint3Df V2 = new TPoint3Df();
        V1 = NormaliseVecteur(a);
        V2 = NormaliseVecteur(b);
        return Math.acos(produitScalaire(V1, V2));
    }  
    /**
     * Crée et construit la matrice identité I3
     * @return TMatrix3x3
     */
    public static TMatrix3x3 makeIdentity()
    {
        TMatrix3x3 R = new TMatrix3x3();
        R.loadIdentity();
        return R;
    }
    /**
     * Addition de deux ou plusieurs vecteurs
     * @param V TPoint3Df
     * @return TPoint3Df
     */
    public static TPoint3Df AddVecteurs(TPoint3Df ... V) // fonction variadique
    {
        TPoint3Df result = new TPoint3Df();
        result = makeTPoint3Df(0.00, 0.00, 0.00);
        for(TPoint3Df arg : V) 
        {
           result.X += arg.X;
           result.Y += arg.Y;
           result.Z += arg.Z;
        }
        return result;
    } 
    /**
     * Soustraction de deux vecteurs: V1 - V2
     * @param V1 TPoint3Df
     * @param V2 TPoint3Df
     * @return TPoint3Df
     */
    public static TPoint3Df SubstractVecteurs(TPoint3Df V1, TPoint3Df V2)
    {
        TPoint3Df result = new TPoint3Df();
        result.X = V1.X - V2.X;
        result.Y = V1.Y - V2.Y;
        result.Z = V1.Z - V2.Z;
        return result;
    } 
    /**
     * Produit d'un vecteur par un nombre
     * @param V TPoint3Df
     * @param k double
     * @return TPoint3Df 
     */
    public static TPoint3Df MultVectorByScalar(TPoint3Df V, double k) 
    {
        TPoint3Df R = new TPoint3Df();
        R = makeTPoint3Df(V.X * k, V.Y * k, V.Z * k);
        return R;
    }
    /**
     * Produit de la matrice M par le vecteur V
     * @param M TMatrix3x3
     * @param V TPoint3Df
     * @return TPoint3Df
     */
    public static TPoint3Df MultMat3x3ByVector(TMatrix3x3 M, TPoint3Df V)
    {
        TPoint3Df R = new TPoint3Df();
        R = makeTPoint3Df(M.getValue(1, 1) * V.X + M.getValue(1, 2) * V.Y + M.getValue(1, 3) * V.Z,
                          M.getValue(2, 1) * V.X + M.getValue(2, 2) * V.Y + M.getValue(2, 3) * V.Z,
                          M.getValue(3, 1) * V.X + M.getValue(3, 2) * V.Y + M.getValue(3, 3) * V.Z);
        return R;
    } 
    /**
     * Retourne la différence maxi terme à terme des vecteurs a et b
     * @param a TPoint3Df
     * @param b TPoint3Df
     * @return double
     */
    public static double MaxDiffVector(TPoint3Df a, TPoint3Df b) 
    {
        return Math.max(Math.abs(a.X - b.X), Math.max(Math.abs(a.Y - b.Y), Math.abs(a.Z - b.Z)));
    }
    /**
     *  Retourne la différence maxi terme à terme des matrices 3x3 a et b
     * @param A TMatrix3x3
     * @param B TMatrix3x3
     * @return double
     */
    public static double MaxDiffMatrix(TMatrix3x3 A, TMatrix3x3 B)
    {
        double qMax = -1e100;
        for (int i = 1; i <= 3; i++)
            for (int j = 1; j <= 3; j++)
            {    
                double v = Math.abs(A.getValue(i, j) - B.getValue(i, j));
                //afficherMessageFmt("-- %.6f - %.6f", v, qMax);
                if (v > qMax) qMax = v;
            }  
        return qMax;
    }  
    /**
     * Crée et renvoie un vecteur nul
     * @return TPoint3Df 
     */
    public static TPoint3Df makeVecteurNul()
    {
        return makeTPoint3Df(0.0, 0.0, 0.0);
    }
   
    /**
     * Construit la matrice de rotation d'angle Angle en radians
     * autour de l'axe X
     * @param Angle Angle de rotation en radians
     * @return TMatrix3x3
     */
    public static TMatrix3x3 makeMatRotationX(double Angle)
    {
        double cosa = Math.cos(Angle);
        double sina = Math.sin(Angle);
        TMatrix3x3 r = new TMatrix3x3(1F, 0F   , 0F,
                                      0F, cosa , -sina,
                                      0F, sina, cosa);
        return r;
    }
    /**
     * Construit la matrice de rotation d'angle Angle en radians
     * autour de l'axe Y
     * @param Angle Angle de rotation en radians
     * @return TMatrix3x3
     */
    public static TMatrix3x3 makeMatRotationY(double Angle)
    {
        double cosa = Math.cos(Angle);
        double sina = Math.sin(Angle); 
        TMatrix3x3 r = new TMatrix3x3(cosa , 0F, sina,
                                      0F   , 1F, 0F,
                                      -sina, 0F, cosa);
        return r; 
    }    
    /**
     * Construit la matrice de rotation d'angle Angle en radians
     * autour de l'axe Z
     * @param Angle double Angle de rotation en radians
     * @return TMatrix3x3
     */
    public static TMatrix3x3 makeMatRotationZ(double Angle)
    {
        double cosa = Math.cos(Angle);
        double sina = Math.sin(Angle);  
        TMatrix3x3 r = new TMatrix3x3(cosa, -sina, 0F,
                                      sina,  cosa, 0F,
                                      0F   ,  0F   , 1F);
        return r;
    }    
    /**
     * Construit le vecteur de rotation de V autour de l'axe sélectionné
     * @param Axis int Axe de rotation: 1 = X, 2 = Y, 3 = Z
     * @param V TPoint3Df
     * @param angle double Angle en radians
     * @return TPoint3Df
     */
    public static TPoint3Df RotationVecteur(int Axis, TPoint3Df V,double angle)
    {
        TMatrix3x3 M;
        switch (Axis)
        {
            case 1: M = makeMatRotationX(angle);
            case 2: M = makeMatRotationY(angle);
            case 3: M = makeMatRotationZ(angle);
            default: M = makeIdentity();
        }        
        return MultMat3x3ByVector(M, V);
    }   
    /**
     * Affiche sur la console le contenu de la matrice M
     * @param Nom String Nom de la variable à afficher
     * @param M TMatrix3x3
     */
    public static void afficherMatrix3x3(String Nom, TMatrix3x3 M)
    {
        String EWE = Nom + ": [\n";
        for (int i = 1; i <=3; i++)
        {    
            EWE +=" [";
            for (int j = 1; j <=3; j++) 
                EWE += sprintf("%.8f ", M.getValue(i, j));
            EWE +=" ]\n";
        }
        EWE +="]";
        afficherMessage(EWE);
    }
     /**
     * Affiche sur la console le contenu du vecteur V
     * @param func String Nom de la fonction utilisant le vecteur
     * @param Nom String Nom de la variable à afficher
     * @param M TMatrix3x3
     */
    public static void afficherVecteur (String func, String nom, TPoint3Df v)
    {
        afficherMessageFmt("  -- %s: %s = %.8f, %.8f, %.8f", func, nom, v.X, v.Y, v.Z);           
    }
    /**
     * Retourne une copie de la matrice M
     * @param M TMatrix3x3
     * @return TMatrix3x3
     */
    public static TMatrix3x3 CopyMatrix3x3(TMatrix3x3 M)
    {
        TMatrix3x3 R = new TMatrix3x3();
        for (int i = 1; i <=3; i++)
            for (int j = 1; j <=3; j++)
            {
                double WU = M.getValue(i, j);
                R.setValue(i, j, WU);
            }
        return R;
    }
    /**
     * Produit vectoriel 
     * @param vect1
     * @param vect2
     * @param normalized Si oui, normalise le résultat
     * @return TPoint3Df
     */
    public static TPoint3Df produitVectoriel(TPoint3Df vect1, TPoint3Df vect2, boolean normalized)
    {
        TPoint3Df v = makeTPoint3Df(vect1.Y * vect2.Z - vect1.Z * vect2.Y,
                                    vect1.Z * vect2.X - vect1.X * vect2.Z,
                                    vect1.X * vect2.Y - vect1.Y * vect2.X);

        if (normalized) {
            double r = Math.sqrt(v.X * v.X + v.Y * v.Y + v.Z * v.Z) + 1E-24;
            v.X /= r;
            v.Y /= r;
            v.Z /= r;
        }
        return v;
    }
    /**
     * Produit scalaire
     * @param vect1
     * @param vect2
     * @return double
     */
    public static double produitScalaire(TPoint3Df vect1, TPoint3Df vect2)
    {
        return vect1.X * vect2.X + vect1.Y * vect2.Y + vect1.Z * vect2.Z;
    }
            
    //----------------------------------------------------------------------------------------------------------
    /**
     * Retourne la rotation du vecteur V autour de l'axe X
     * @param V
     * @param Angle
     * @return TPoint3Df
     */
    public static TPoint3Df TurnX(TPoint3Df V, double Angle)
    {
        TPoint3Df R = new TPoint3Df();
        double    s = Math.sin(Angle);
        double    c = Math.cos(Angle);
        R.X = V.X;
        R.Y = c * V.Y - s * V.Z;
        R.Z = s * V.Y + c * V.Z;
        return R;
    }            
}
