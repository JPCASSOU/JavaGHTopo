//------------------------------------------------------------------------------
// DistoX Calibration Utilities                          
// PROPRIETARY UNIT - (c) Beat HEEB heeb@speleo.ch - Adapted by JP CASSOU
//------------------------------------------------------------------------------

package GHTopoOperationnel;

import static GHTopoOperationnel.GeneralFunctions.*;
import static GHTopoOperationnel.CalculMatriciel3x3.*;
import GHTopoOperationnel.Types.TPoint3Df;

import java.util.ArrayList;

public class TCalibrationDistoX 
{     
    private final double FV = 24000.00;
    private final double FM = 16384.00;
    private final double EPS = 1.0E-6F;
    private final int    MAX_IT = 200; 
    private double fDelta = 0;
    private byte[] fDataForDistoX;
    // liste des valeurs
    ArrayList<TPoint3Df> fListeMesuresDeG;
    ArrayList<TPoint3Df> fListeMesuresDeM;
    // listes temp
    private TPoint3Df[] fgr;
    private TPoint3Df[] fmr;
    private TPoint3Df[] fgx;
    private TPoint3Df[] fmx;
    
    private TPoint3Df OutTurnVectors_gx;
    private TPoint3Df OutTurnVectors_mx;
    private TPoint3Df OutOptVectors_gx;
    private TPoint3Df OutOptVectors_mx;
    //result
    private TMatrix3x3 aG, aM;
    private TPoint3Df  bG, bM;
    /**
     * Retourne la valeur du delta (doit être inférieur à 0.50)
     * @return double 
     */
    public double getDelta()
    {
        return fDelta;
    }        
    /**
     * Retourne la matrice de calibration gravimétrique aG
     * @return TMatrix3x3
     */
    public TMatrix3x3 getMatrixG()
    {
        return aG;
    } 
    /**
     * Retourne la matrice de calibration magnétométrique aM
     * @return TMatrix3x3
     */
    public TMatrix3x3 getMatrixM()
    {
        return aM;
    }  
    public TPoint3Df getVecteurG()
    {
        return bG;
    }  
    public TPoint3Df getVecteurM()
    {
        return bM;
    }  
    /**
     * Retourne le nombre de mesures (56 minimum)
     * @return int
     */
    public int getNbMesures()
    {
        return this.fListeMesuresDeG.size();
    }    
    /**
     * Ajoute une mesure de gravité 3 axes
     * @param mg TPoint3Df
     */
    private void AddMesureG(TPoint3Df mg)
    {
        fListeMesuresDeG.add(mg);
    }    
    /**
     * Afoute une mesure de magnétisme 3 axes
     * @param mm TPoint3Df
     */
    private void AddMesureM(TPoint3Df mm)
    {
        fListeMesuresDeM.add(mm);
    }
    /**
     * Ajoute un couple de mesures gravimétriques + magnétométriques
     * @param gx double
     * @param gy
     * @param gz
     * @param mx double
     * @param my
     * @param mz 
     */
    public  void AddValues(int gx, int gy, int gz, int mx, int my, int mz) 
    {
        this.AddMesureG(makeTPoint3Df(gx / FV, gy / FV, gz / FV));
        this.AddMesureM(makeTPoint3Df(mx / FV, my / FV, mz / FV));
    }
    private TPoint3Df getMesureG(int Idx)
    {
        return fListeMesuresDeG.get(Idx);
    }    
    private TPoint3Df getMesureM(int Idx)
    {
        return fListeMesuresDeM.get(Idx);
    }
    private void OptVectors(TPoint3Df gr, TPoint3Df mr, double alpha) 
    {
        TPoint3Df no = produitVectoriel(gr, mr, true); //Vector no = Vector.Normalized(gr % mr);  // plane normal
        double s = Math.sin(alpha);
        double c = Math.cos(alpha);
        
        // gx = Vector.Normalized(mr * c + (mr % no) * s + gr);
        TPoint3Df s1 = new TPoint3Df();
        TPoint3Df s2 = new TPoint3Df();
        TPoint3Df s4 = new TPoint3Df();
        
        s1 = MultVectorByScalar(mr, c); // mr * c
        s2 = MultVectorByScalar(produitVectoriel(mr, no, false), s);// (mr % no) * s
        s4 = AddVecteurs(s1, s2, gr);
        this.OutOptVectors_gx = NormaliseVecteur(s4);
        // mx = gx * c + (no % gx) * s;
        s1 = MultVectorByScalar(this.OutOptVectors_gx, c); // gx * c + 
        s2 = MultVectorByScalar(produitVectoriel(no, this.OutOptVectors_gx, false), s);// (no % gx) * s;
        
        this.OutOptVectors_mx = AddVecteurs(s1, s2);                             
    }

    //private static void TurnVectors(Vector gxp, Vector mxp, Vector gr, Vector mr, out Vector gx, out Vector mx) 
    private void TurnVectors(TPoint3Df gxp, TPoint3Df mxp, TPoint3Df gr, TPoint3Df mr) 
    {
        double s = gr.Z * gxp.Y - gr.Y * gxp.Z + mr.Z * mxp.Y - mr.Y * mxp.Z;               //float s = gr.z * gxp.y - gr.y * gxp.z + mr.z * mxp.y - mr.y * mxp.z;
        double c = gr.Y * gxp.Y + gr.Z * gxp.Z + mr.Y * mxp.Y + mr.Z * mxp.Z;               //float c = gr.y * gxp.y + gr.z * gxp.z + mr.y * mxp.y + mr.z * mxp.z;
        double a = Math.atan2(s, c);                                                        //float a = (float)Math.Atan2(s, c);    
        this.OutTurnVectors_gx = TurnX(gxp, a);                         // gx = gxp.TurnX(a);
        this.OutTurnVectors_mx = TurnX(mxp, a);                         // mx = mxp.TurnX(a);                
    }
    // passage par référence;
    private void CheckOverflow(TMatrix3x3 m, TPoint3Df v)            
    {
        TMatrix3x3 Mo = new TMatrix3x3();
        TPoint3Df  Vo = makeTPoint3Df(0.00, 0.00, 0.00);
        double qmax = Math.max(MaxDiffMatrix(m, Mo) * FM,
                               MaxDiffVector(v, Vo) * FV);
        if (qmax > Short.MAX_VALUE) 
        {
            TMatrix3x3 qM = new TMatrix3x3();
            TPoint3Df  qV = new TPoint3Df();
            qM = MultMatrix3x3ByScalar(m, Short.MAX_VALUE / qmax); //           m = m * (short.MaxValue / max);
            qV = MultVectorByScalar(v, Short.MAX_VALUE / qmax);            
        }
    }
    /** Lance le calcul de calibration 
     * 
     */
    public void Optimise()
    {
        afficherMessage("Optimisation");
        int Nb = getNbMesures();
        double invNb = 1.0F / Nb;                   // float invNum = 1.0F / num;  
        this.fgr = new TPoint3Df[Nb];               // Vector[] gr = new Vector[num];
        this.fmr = new TPoint3Df[Nb];               // Vector[] mr = new Vector[num];
        this.fgx = new TPoint3Df[Nb];               // Vector[] gx = new Vector[num];
        this.fmx = new TPoint3Df[Nb];               // Vector[] mx = new Vector[num];
        // init
        for (int i = 0; i < Nb; i++)
        {
            this.fgr[i] = makeVecteurNul(); 
            this.fmr[i] = makeVecteurNul();            
            this.fgx[i] = makeVecteurNul(); 
            this.fmx[i] = makeVecteurNul();            
        }    
        TMatrix3x3 aG0 = new TMatrix3x3();          // Matrix aG0,                                             
        TMatrix3x3 aM0 = new TMatrix3x3();          //        aM0;
        TPoint3Df sumG = makeTPoint3Df(0F, 0F, 0F);  //Vector sumG = Vector.zero;
        TPoint3Df sumM = makeTPoint3Df(0F, 0F, 0F);  //Vector sumM = Vector.zero; 
        TMatrix3x3 sumG2 = new TMatrix3x3();         //Matrix sumG2 = Matrix.zero;
        TMatrix3x3 sumM2 = new TMatrix3x3();         //Matrix sumM2 = Matrix.zero;
        sumG2.loadZero();
        sumM2.loadZero();
        TMatrix3x3 CarreKronecker = new TMatrix3x3();
        double sa = 0F;                              //float sa = 0, ca = 0;
        double ca = 0F;                              //float sa = 0, ca = 0;
        //Boucle 001
        for (int i = 0; i < Nb; i++)                           // for (int i = 0; i < num; i++) 
        {                                                      // { 
            // sum up g x m for initial alpha                  //     sum up g x m for initial alpha 
            TPoint3Df G = this.getMesureG(i);        
            TPoint3Df M = this.getMesureM(i);            
            sa += NormeVecteur(produitVectoriel(G, M, false)); //     Vector.Abs(g[i] % m[i]); // cross product
            ca += produitScalaire(G, M);                       //     g[i] * m[i]; // dot product               
            sumG = AddVecteurs(sumG, G);
            sumM = AddVecteurs(sumM, M);
            CarreKronecker = ProduitKroneckerVecteurs(G, G);     
            sumG2 = AddMatrix3x3(sumG2, CarreKronecker) ;      //     sumG2 += (g[i] & g[i]); // outer product
            CarreKronecker = ProduitKroneckerVecteurs(M, M);   
            sumM2 = AddMatrix3x3(sumM2, CarreKronecker);       //     sumM2 += (m[i] & m[i]); // outer product
        } 
        double alpha = Math.atan2(sa, ca);
        TPoint3Df avG = MultVectorByScalar(sumG, invNb);         // Vector avG = sumG * invNum; // average g
        TPoint3Df avM = MultVectorByScalar(sumM, invNb);         // Vector avM = sumM * invNum; 
        // average Matrix invG = Matrix.Inverse(sumG2 - (sumG & avG));
        TMatrix3x3 WU = new TMatrix3x3();
        CarreKronecker = ProduitKroneckerVecteurs(sumG, avG);
        WU = SubstractMatrix3x3(sumG2, CarreKronecker);
        TMatrix3x3 invG = InvMat3x3(WU);     // Matrix invG = Matrix.Inverse(sumG2 - (sumG & avG));
        // average Matrix invM = Matrix.Inverse(sumM2 - (sumM & avM));
        CarreKronecker = ProduitKroneckerVecteurs(sumM, avM);
        WU = SubstractMatrix3x3(sumM2, CarreKronecker);
        TMatrix3x3 invM = InvMat3x3(WU);     // Matrix invM = Matrix.Inverse(sumM2 - (sumM & avM));
        // OK jusqu'ici
        afficherMatrix3x3("invG", invG);
        afficherMatrix3x3("invM", invM);
        
        aG = makeIdentity(); // aG = aM = Matrix.one;
        aM = makeIdentity();
        TPoint3Df oO = makeVecteurNul();
        bG = SubstractVecteurs(oO, avG); //bG = Vector.zero - avG; // use negative average as initial offset
        bM = SubstractVecteurs(oO, avM); //bM = Vector.zero - avM;
        int it = 0; // number of iterations
        do 
        {
            //afficherMessage("Boucle 000");
            for (int i = 0; i < Nb; i++)                                              // for (int i = 0; i < num; i++) 
            {                                                                         // { 
                fgr[i] = AddVecteurs(MultMat3x3ByVector(aG, this.getMesureG(i)), bG); //     gr[i] = (aG * g[i]) + bG;
                fmr[i] = AddVecteurs(MultMat3x3ByVector(aM, this.getMesureM(i)), bM); //     mr[i] = (aM * m[i]) + bM;       
            }                                                                         // }
            //afficherMessage("Boucle 001");
            sa = 0F; ca = 0F;                                                         // sa = ca = 0;  
            for (int i = 0; i < Nb; i++)                                              // for (int i = 0; i < num; i++) 
            {                                                                         // {  
                // get optimal gx & mx from gr & mr                                   //     get optimal gx & mx from gr & mr
                if (i < 16) // equidirectional sample groups                          //     if (i < 16)  // equidirectional sample groups        
                {                                                                     //     {      
                    TPoint3Df grp = makeVecteurNul();                                 //         Vector grp = Vector.zero;
                    TPoint3Df mrp = makeVecteurNul();                                 //         Vector mrp = Vector.zero;
                    int first = i;                                                    //         int first = i;                     
                    for (; i < first + 4; i++)                                        //         for (; i < first + 4; i++)           
                    {                                                                 //         { 
                        // match gr & mr to first gr & mr                             //             // match gr & mr to first gr & mr  
                        this.OutTurnVectors_gx = makeVecteurNul();                    //             Vector gt;
                        this.OutTurnVectors_mx = makeVecteurNul();                    //             Vector mt;
                        TurnVectors(fgr[i], fmr[i], fgr[first], fmr[first]);          //             TurnVectors(gr[i], mr[i], gr[first], mr[first], out gt, out mt);
                        grp = AddVecteurs(grp, this.OutTurnVectors_gx);               //             grp += gt;
                        mrp = AddVecteurs(mrp, this.OutTurnVectors_mx);               //             mrp += mt;
                    }                                                                 //         } 
                    // get optimal matched gx & mx from sum of matched gr & mr        //         // get optimal matched gx & mx from sum of matched gr & mr                                                                                     
                    this.OutOptVectors_gx = makeVecteurNul();                         //          Vector gxp, mxp;
                    this.OutOptVectors_mx = makeVecteurNul();
                    this.OptVectors(grp, mrp, alpha);                                 //          OptVectors(grp, mrp, alpha, out gxp, out mxp);
                    // alpha calculation                                              //          alpha calculation
                    sa += NormeVecteur(produitVectoriel(mrp, this.OutOptVectors_gx, false));  // sa += Vector.Abs(mrp % gxp);
                    ca += produitScalaire(mrp, this.OutOptVectors_gx);                        // ca += mrp * gxp;          
                    // Boucle0004
                    for (i = first; i < first + 4; i++)                                       // for (i = first; i < first + 4; i++) {                    
                    {                                                                         // { // get optimal gx & mx from matched gx & mx
                        this.OutTurnVectors_gx = makeVecteurNul();   
                        this.OutTurnVectors_mx = makeVecteurNul();  
                        TurnVectors(this.OutOptVectors_gx, this.OutOptVectors_mx, fgr[i], fmr[i]);   //  TurnVectors(gxp, mxp, gr[i], mr[i], out gx[i], out mx[i]);
                        fgx[i] = this.OutTurnVectors_gx; 
                        fmx[i] = this.OutTurnVectors_mx;  
                    } 
                    i--;                                                                                                      //i--;
                }                                                                             //}
                else                                                                          //else
                { // individual sample                                                        // {
                    this.OutOptVectors_gx = makeVecteurNul();
                    this.OutOptVectors_mx = makeVecteurNul();
                    OptVectors(fgr[i], fmr[i], alpha);                                         //    OptVectors(gr[i], mr[i], alpha, out gx[i], out mx[i]);
                    fgx[i] = this.OutOptVectors_gx;
                    fmx[i] = this.OutOptVectors_mx;
                    // alpha calculation                                                       //    alpha calculation
                    sa += NormeVecteur(produitVectoriel(fmr[i], fgx[i], false));               //     sa += Vector.Abs(mr[i] % gx[i]);
                    ca += produitScalaire(fmr[i], fgx[i]);                                     //    ca += mr[i] * gx[i];
                }                                                                             // }
            }                                                                                // }    
            alpha = Math.atan2(sa, ca);                     // float alpha = (float)Math.Atan2(sa, ca);
            
            // get aG & aM from g, m, gx, & mx              // get aG & aM from g, m, gx, & mx
            TPoint3Df avGx = makeVecteurNul();              // Vector avGx = Vector.zero;
            TPoint3Df avMx = makeVecteurNul();              // Vector avMx = Vector.zero;
            TMatrix3x3 sumGxG = new TMatrix3x3();           // Matrix sumGxG = Matrix.zero;
            TMatrix3x3 sumMxM = new TMatrix3x3();           // Matrix sumMxM = Matrix.zero;
            sumGxG.loadZero();
            sumMxM.loadZero();
            for (int i = 0; i < Nb; i++)                    // for (int i = 0; i < num; i++) 
            {                                               // {
                avGx = AddVecteurs(avGx, fgx[i]);           //    avGx += gx[i];
                avMx = AddVecteurs(avMx, fmx[i]);           //    avMx += mx[i];
                TPoint3Df GG = getMesureG(i);
                TPoint3Df MM = getMesureM(i);
                TMatrix3x3 pk = new TMatrix3x3();                
                pk = ProduitKroneckerVecteurs(fgx[i], GG);
                sumGxG = AddMatrix3x3(sumGxG, pk);          //    sumGxG += (gx[i] & g[i]); // outer product
                pk = ProduitKroneckerVecteurs(fmx[i], MM); 
                sumMxM = AddMatrix3x3(sumMxM, pk);          //    sumMxM += (mx[i] & m[i]);       
            }                                               //}
            // get new aG & aM                                     // // get new aG & aM
            aG0 = CopyMatrix3x3(aG); 
            aM0 = CopyMatrix3x3(aM);          
            //afficherMatrix3x3("-- aM", aM);// aG0 = aG; aM0 = aM;
            avGx = MultVectorByScalar(avGx, invNb);                // avGx = avGx * invNum; // average gx
            avMx = MultVectorByScalar(avMx, invNb);                // avMx = avMx * invNum;
            TMatrix3x3 QQ = new TMatrix3x3();
            CarreKronecker = ProduitKroneckerVecteurs(avGx, sumG); // aG = (sumGxG - (avGx & sumG)) * invG;
            QQ = SubstractMatrix3x3(sumGxG, CarreKronecker);
            aG = ProduitMat3x3(QQ, invG);
            // aM = (sumMxM - (avMx & sumM)) * invM;               // (sumMxM - (avMx & sumM)) * invM; 
            CarreKronecker = ProduitKroneckerVecteurs(avMx, sumM); //           (avMx & sumM)            
            //afficherMatrix3x3("Kronecker aM", CarreKronecker);
            QQ = SubstractMatrix3x3(sumMxM, CarreKronecker);       // (sumMxM - (avMx & sumM))  
            aM = ProduitMat3x3(QQ, invM);                          // (sumMxM - (avMx & sumM)) * invM;       
            // enforce symmetric aG(y,z)
            //aG.y.z = aG.z.y = (aG.y.z + aG.z.y) * 0.5F;
            // Rappel: M.getValue(i,j) = M[i][j]
            double moy = (aG.getValue(2, 3) + aG.getValue(3, 2)) * 0.5F;
            aG.setValue(2, 3, moy);
            aG.setValue(3, 2, moy);
            // get new bG & bM
            bG = SubstractVecteurs(avGx, MultMat3x3ByVector(aG, avG)); // bG = avGx - (aG * avG);
            bM = SubstractVecteurs(avMx, MultMat3x3ByVector(aM, avM)); // bM = avMx - (aM * avM);             
            it++;            
        } while (it < MAX_IT && Math.max(MaxDiffMatrix(aG, aG0), MaxDiffMatrix(aM, aM0)) > EPS);
        //afficherMessageFmt("%d itérations", it);
        CheckOverflow(aG, bG);
        CheckOverflow(aM, bM);
        double delta = 0;
        for (int i = 0; i < Nb; i++) 
        {
            TPoint3Df dg = SubstractVecteurs(fgx[i], fgr[i]);
            TPoint3Df dm = SubstractVecteurs(fmx[i], fmr[i]);            //Vector dm = mx[i] - mr[i];           
            delta += produitScalaire(dg, dg) + produitScalaire(dm, dm);  //delta += (dg * dg) + (dm * dm);
        }
        delta = Math.sqrt(delta / Nb) * 100;
        fDelta = delta;
        afficherMessageFmt("%d itérations - Delta %.5f", it, fDelta); // delta = 0.28
        afficherMatrix3x3("aG", aG);
        afficherVecteur("", "bG", bG);
        afficherMatrix3x3("aM", aM);
        afficherVecteur("", "bM", bM);
        
        /* Valeurs attendues
        n: 56  it: 55  delta: 0.28

                   aG              bG
            1.770 -0.016  0.001    0.031
            0.015  1.761  0.003   -0.088
            0.001  0.003  1.769   -0.047
                   aM              bM
            1.092 -0.019 -0.037   -0.059
            0.008  1.053  0.024   -0.028
            0.015 -0.029  1.041    0.023

                    Cg
            29001   -256     17      745
              238  28854     56    -2105
               17     56  28989    -1120
                    Cm
            17889   -317   -600    -1418
              132  17255    388     -660
              247   -477  17050      550
        
        Cg: [
 [29001,46823228 -255,55037466 17,29640399  ]
 [237,99119499 28853,61114796 55,92677292  ]
 [17,08753484 55,92677292 28989,15259306  ]
]
Cm: [
 [17889,19081893 -317,12917291 -599,79349032  ]
 [131,81642173 17254,91718621 388,18843072  ]
 [247,37279324 -476,57098584 17050,19374816  ]
]
  -- : Vg = 744,88304667, -2105,10875326, -1120,48222250
  -- : Vm = -967,86798417, -450,84687749, 375,49952156

        

        */
        //return it;
    }
    /**
     * Construit un jeu de données exemple
     * fourni par l'auteur du Disto X2
     */
    public void setJeuDeDonneesExemple()
    {
        //this.AddValues(  Gx,  Gy,   Gz,   Mx,   My,   Mz); // Azim,  Incl,  Roll,  |G|,  |M|,   a,   d
        this.AddValues(3,109, 14158, 10445, -7506, 18917); //   36.2,    -1.8,    -4.5,   1.001,   1.002,    26.8,    0.28
        this.AddValues(169,14774, 105, 10475, 20752, 5286); //   36.3,    -1.9,    92.1,   0.998,   1.002,    26.7,    0.34
        this.AddValues(47,2512, -12877, 9405, 8914, -20075); //   36.0,    -1.8,   174.6,   1.001,   1.000,    26.7,    0.09
        this.AddValues(-88,-12335, -791, 9275, -18631, -9399); //   35.9,    -1.9,   -96.1,   0.999,   1.003,    26.6,    0.34
        this.AddValues(-862,1478, 14217, -6522, 6721, 19965); //  216.2,     1.9,     1.3,   1.002,   1.001,    26.7,    0.34
        this.AddValues(-705,14790, 788, -7153, 20965, -5638); //  215.8,     1.8,    89.2,   0.998,   1.000,    26.8,    0.26
        this.AddValues(-826,998, -12943, -8110, -5143, -20801); //  215.7,     1.8,  -179.0,   1.001,   1.000,    26.6,    0.21
        this.AddValues(-966,-12411, 394, -7530, -19607, 4711); //  215.9,     1.8,   -91.1,   1.000,   1.000,    26.7,    0.12
        this.AddValues(-2269,1578, 14051, -6838, -7320, 19180); //  127.5,     7.9,     1.7,   0.999,   1.002,    26.7,    0.19
        this.AddValues(-2356,-12192, 2108, -7947, -19299, -6990); //  127.8,     7.8,   -83.8,   0.998,   1.003,    26.7,    0.39
        this.AddValues(-2283,672, -12772, -7864, 8468, -20059); //  127.2,     8.0,  -177.6,   0.999,   1.001,    26.7,    0.24
        this.AddValues(-2151,14674, 106, -6762, 20132, 7609); //  127.6,     7.9,    92.1,   0.998,   1.001,    26.7,    0.26
        this.AddValues(1416,1979, 14067, 10762, 9360, 18618); //  308.0,    -7.7,     3.5,   1.002,   1.001,    26.7,    0.19
        this.AddValues(1529,14658, -233, 9945, 19543, -9505); //  307.8,    -7.7,    93.6,   1.000,   0.999,    26.7,    0.19
        this.AddValues(1426,132, -12782, 9153, -8529, -19752); //  308.1,    -7.8,  -175.4,   1.001,   0.999,    26.6,    0.25
        this.AddValues(1301,-12242, 1929, 9910, -18115, 8792); //  307.9,    -7.8,   -84.6,   0.999,   0.999,    26.6,    0.20
        this.AddValues(-13966,734, 935, -18632, 10043, -1414); //  196.8,    87.3,   -62.7,   1.001,   1.001,    26.8,    0.15
        this.AddValues(-13965,1711, 1054, -19057, -1034, -9666); //  209.1,    87.6,    43.9,   1.001,   1.001,    26.7,    0.15
        this.AddValues(-13961,1811, -78, -18921, -8598, 35); //  226.1,    86.3,   145.2,   1.002,   1.002,    26.7,    0.25
        this.AddValues(-13932,703, 144, -18427, 2085, 9096); //  216.8,    86.7,  -129.4,   0.999,   1.000,    26.7,    0.14
        this.AddValues(13076,1335, -430, 21856, 3865, 7470); //   31.3,   -85.4,   166.9,   0.998,   1.000,    26.7,    0.23
        this.AddValues(13016,2738, 685, 21897, -6662, 2658); //   22.5,   -83.0,    87.7,   0.997,   1.001,    26.7,    0.35
        this.AddValues(13050,1302, 2094, 21571, -2582, -8377); //   11.9,   -83.7,     8.4,   0.999,   1.002,    26.8,    0.23
        this.AddValues(13034,-196, 716, 21736, 8068, -4382); //   18.1,   -84.6,   -86.0,   0.997,   1.000,    26.7,    0.30
        this.AddValues(-9490,1217, 10687, -13615, 10039, 13038); //  251.1,    42.1,    -0.2,   0.998,   0.999,    26.6,    0.21
        this.AddValues(-9412,11414, 922, -14300, 14112, -9325); //  250.9,    42.0,    88.3,   1.002,   1.000,    26.7,    0.21
        this.AddValues(-9502,1287, -9425, -14847, -8669, -13613); //  251.3,    42.1,  -180.0,   1.000,   1.001,    26.7,    0.10
        this.AddValues(-9513,-8900, 708, -14152, -12330, 9147); //  250.3,    41.7,   -89.7,   1.000,   1.000,    26.7,    0.06
        this.AddValues(7913,9350, 7639, 11143, 20569, 4494); //  250.6,   -37.4,    49.4,   1.002,   1.000,    26.7,    0.22
        this.AddValues(7951,8031, -7745, 9936, 5291, -21004); //  249.5,   -37.5,   140.6,   1.005,   1.000,    26.5,    0.52
        this.AddValues(7699,-6757, -6746, 9813, -19233, -6981); //  249.1,   -37.2,  -133.2,   0.998,   1.000,    26.5,    0.30
        this.AddValues(7645,-6359, 8576, 10708, -5566, 19444); //  248.7,   -36.8,   -43.2,   1.001,   0.999,    26.8,    0.22
        this.AddValues(-9092,758, 11042, -7062, -9533, 17966); //   60.4,    39.8,    -2.7,   0.999,   1.000,    26.8,    0.15
        this.AddValues(-9105,11649, 1085, -7045, 18771, 9955); //   60.6,    40.4,    87.4,   1.001,   0.998,    26.7,    0.19
        this.AddValues(-9095,1406, -9744, -8019, 10262, -19061); //   60.6,    40.0,   179.4,   0.998,   0.999,    26.8,    0.23
        this.AddValues(-9092,-9260, 300, -8056, -17696, -10533); //   60.4,    39.3,   -91.9,   1.000,   0.999,    26.6,    0.14
        this.AddValues(7456,1015, 11630, 15208, -9738, 13998); //   74.1,   -35.6,    -0.5,   0.998,   0.999,    26.6,    0.28
        this.AddValues(7445,12368, 613, 15181, 15466, 9639); //   74.8,   -34.8,    90.0,   1.003,   0.997,    26.6,    0.45
        this.AddValues(7362,465, -10491, 14283, 9771, -16343); //   74.6,   -34.9,  -176.5,   1.002,   0.998,    26.7,    0.28
        this.AddValues(7240,-10092, 1614, 14085, -15201, -9746); //   74.1,   -34.7,   -85.1,   1.005,   0.999,    26.7,    0.52
        this.AddValues(-8871,8519, 8273, -18098, 5385, 8559); //  166.5,    39.0,    43.4,   0.999,   0.998,    26.7,    0.25
        this.AddValues(-8849,8836, -6856, -18468, 9476, -5228); //  166.4,    38.8,   134.8,   1.003,   1.001,    26.5,    0.39
        this.AddValues(-8840,-7572, -5334, -18781, -5812, -8079); //  166.2,    38.1,  -124.2,   0.998,   1.001,    26.8,    0.25
        this.AddValues(-8816,-6548, 7995, -18351, -8290, 4604); //  165.9,    38.0,   -46.6,   0.999,   1.001,    26.7,    0.17
        this.AddValues(8749,1689, 10586, 8788, -3017, 20940); //  154.5,   -42.6,     3.4,   0.999,   1.000,    26.3,    0.45
        this.AddValues(8630,11393, -474, 7842, 22272, 2084); //  154.9,   -41.1,    96.0,   1.005,   0.998,    26.9,    0.58
        this.AddValues(8315,-310, -9598, 6439, 2280, -22733); //  155.3,   -40.2,  -171.9,   0.997,   0.999,    26.6,    0.31
        this.AddValues(8324,-9072, 2251, 6894, -21223, -2419); //  153.5,   -40.7,   -81.0,   0.999,   0.999,    26.8,    0.22
        this.AddValues(-8692,1264, 11355, -2286, 2618, 22065); //  346.3,    37.7,     0.1,   0.999,   1.001,    26.8,    0.23
        this.AddValues(-8663,11976, 542, -2841, 22873, -2412); //  346.7,    38.1,    90.4,   0.998,   1.000,    26.9,    0.32
        this.AddValues(-8740,923, -10032, -3863, -1974, -22865); //  346.6,    38.0,  -178.1,   0.999,   0.998,    27.1,    0.55
        this.AddValues(-8671,-9581, 1375, -3135, -21434, 2871); //  346.3,    37.1,   -86.2,   0.999,   0.999,    26.8,    0.13
        this.AddValues(7555,619, 11593, 21153, -245, 9692); //    0.2,   -36.0,    -2.6,   1.000,   0.999,    26.8,    0.10
        this.AddValues(7642,12197, 1101, 20946, 10980, -11); //    0.2,   -35.8,    87.4,   1.003,   0.998,    26.6,    0.32
        this.AddValues(7578,581, -10323, 20446, 224, -11374); //    0.2,   -36.1,  -177.0,   1.000,   0.999,    26.6,    0.10
        this.AddValues(7552,-9887, 596, 20676, -9827, -1094); //  359.7,   -36.3,   -90.3,   1.004,   1.000,    26.7,    0.36
        afficherMessage("   Gx     Gy     Gz     Mx     My     Mz    Azim   Incl   Roll   |G|    |M|     a      d");
        // on liste
        int Nb = this.getNbMesures();
        for (int i = 0; i < Nb; i++)
        {
            TPoint3Df G = getMesureG(i);
            TPoint3Df M = getMesureM(i);
            afficherMessageFmt("%.6f, %.6f, %.6f, %.6f, %.6f, %.6f", G.X * FV, G.Y * FV, G.Z * FV, M.X * FV, M.Y * FV, M.Z * FV);
        }    
    }   
     
    private void PutCoeff(int index, double value) 
    {
        // /!\ Semble ressembler à des nombres 32 couilles avec formatage BigEndian
        short coeff = (short)Math.round(value);
        fDataForDistoX[index] = (byte)coeff;
        fDataForDistoX[index + 1] = (byte)(coeff >> 8); // * 256 pour BigEndian
    }
    /**
     * Retourne la chaîne hexadécimale à envoyer au DistoX
     * @return String
     */
    public String getDistoXDataAsHex()
    {
        String R = "";
        int n = fDataForDistoX.length;
        //afficherMessageFmt("Longueur: %d", n);
        for (int i = 0; i < n; i++)         
            R += sprintf("%02X", fDataForDistoX[i]);
        return R;
    }        
    /**
     * Construit le tableau d'octets pour la calibration du DistoX
     */
    public void setCoeffToDistoX() 
    {
        TMatrix3x3 Cg = new TMatrix3x3();
        TMatrix3x3 Cm = new TMatrix3x3();
        TPoint3Df  Vg = makeTPoint3Df(bG.X * FV, bG.Y * FV, bG.Z * FV);
        TPoint3Df  Vm = makeTPoint3Df(bM.X * FM, bM.Y * FM, bM.Z * FM);
        Cg = MultMatrix3x3ByScalar(aG, FM);
        Cm = MultMatrix3x3ByScalar(aM, FM);
        afficherMatrix3x3("Cg", Cg);
        afficherMatrix3x3("Cm", Cm);
        afficherVecteur("", "Vg", Vg);
        afficherVecteur("", "Vm", Vm);
        
        fDataForDistoX = new byte[48];
        PutCoeff(0, Vg.X);
        PutCoeff(2, Cg.getValue(1, 1)); // OK
        PutCoeff(4, Cg.getValue(1, 2)); // aG.x.y
        PutCoeff(6, Cg.getValue(1, 3)); // aG.x.z
        PutCoeff(8, Vg.Y);
        PutCoeff(10, Cg.getValue(2, 1)); // aG.y.x
        PutCoeff(12, Cg.getValue(2, 2)); // aG.y.y OK
        PutCoeff(14, Cg.getValue(2, 3)); // aG.y.z 
        PutCoeff(16, Vg.Z);
        PutCoeff(18, Cg.getValue(3, 1)); // aG.z.x
        PutCoeff(20, Cg.getValue(3, 2)); // aG.z.y
        PutCoeff(22, Cg.getValue(3, 3)); // aG.z.z 
        PutCoeff(24, Vm.X);
        PutCoeff(26, Cm.getValue(1, 1));
        PutCoeff(28, Cm.getValue(1, 2)); // aM.x.y
        PutCoeff(30, Cm.getValue(1, 3)); // aM.x.z
        PutCoeff(32, Vm.Y);
        PutCoeff(34, Cm.getValue(2, 1)); // aM.y.x 
        PutCoeff(36, Cm.getValue(2, 2));
        PutCoeff(38, Cm.getValue(2, 3)); //aM.y.z 
        PutCoeff(40, Vm.Z);
        PutCoeff(42, Cm.getValue(3, 1)); // aM.z.x 
        PutCoeff(44, Cm.getValue(3, 2)); // aM.z.y  
        PutCoeff(46, Cm.getValue(3, 3));
        
    }
    //--------------------------------------------------------------------------
    public TCalibrationDistoX()
    {
        afficherMessage("Calibration initialisée");
        fListeMesuresDeG = new ArrayList<TPoint3Df>();
        fListeMesuresDeM = new ArrayList<TPoint3Df>();
        fListeMesuresDeG.clear();
        fListeMesuresDeM.clear();  
        
        this.OutTurnVectors_gx = new TPoint3Df();
        this.OutTurnVectors_mx = new TPoint3Df();
        this.OutOptVectors_gx = new TPoint3Df();
        this.OutOptVectors_mx  = new TPoint3Df();
    }
}
/*******************************************************************************
using System;

namespace PocketTopo {
    class CalibAlgorithm {

        private const float FV = 24000;
        private const float FM = 16384;
        private const float EPS = 1.0E-6F;
        private const int MAX_IT = 200;

        //result
        private static Matrix aG, aM;
        private static Vector bG, bM;


        public static void AddValues(int gx, int gy, int gz, int mx, int my, int mz,
                                     Vector[] g, Vector[] m, int idx) {
            g[idx] = new Vector(gx / FV, gy / FV, gz / FV);
            m[idx] = new Vector(mx / FV, my / FV, mz / FV);
        }


        // helpers
        private static void OptVectors(Vector gr, Vector mr, float alpha, out Vector gx, out Vector mx) {
            Vector no = Vector.Normalized(gr % mr);  // plane normal
            float s = (float)Math.Sin(alpha);
            float c = (float)Math.Cos(alpha);
            gx = Vector.Normalized(mr * c + (mr % no) * s + gr);
            mx = gx * c + (no % gx) * s;
        }

        private static void TurnVectors(Vector gxp, Vector mxp, Vector gr, Vector mr, out Vector gx, out Vector mx) {
            float s = gr.z * gxp.y - gr.y * gxp.z + mr.z * mxp.y - mr.y * mxp.z;
            float c = gr.y * gxp.y + gr.z * gxp.z + mr.y * mxp.y + mr.z * mxp.z;
            float a = (float)Math.Atan2(s, c);
            gx = gxp.TurnX(a);
            mx = mxp.TurnX(a);
        }

        private static void CheckOverflow(ref Matrix m, ref Vector v) {
            float max = Math.Max(Matrix.MaxDiff(m, Matrix.zero) * FM,
                                 Vector.MaxDiff(v, Vector.zero) * FV);
            if (max > short.MaxValue) {
                m = m * (short.MaxValue / max);
                v = v * (short.MaxValue / max);
            }
        }

        //*
        // * g: the gravity sensor values divided by FV
        // * m: the magnetic sensor values divided by FV
        // * delta: the average error in %
        // * return value: the number of iterations used
        
        private static int Optimize(Vector[] g, Vector[] m, out float delta) {
            int num = g.Length;
            Vector[] gr = new Vector[num];
            Vector[] mr = new Vector[num];
            Vector[] gx = new Vector[num];
            Vector[] mx = new Vector[num];
            Matrix aG0, aM0;
            float invNum = 1.0F / num;
            Vector sumG = Vector.zero;
            Vector sumM = Vector.zero;
            Matrix sumG2 = Matrix.zero;
            Matrix sumM2 = Matrix.zero;
            float sa = 0, ca = 0;
            //Boucle 001
            for (int i = 0; i < num; i++) {
                // sum up g x m for initial alpha
                sa += Vector.Abs(g[i] % m[i]); // cross product
                ca += g[i] * m[i]; // dot product
                // sum up g, m, g^2, & m^2
                sumG += g[i];
                sumM += m[i];
                sumG2 += (g[i] & g[i]); // outer product
                sumM2 += (m[i] & m[i]);
            }
            float alpha = (float)Math.Atan2(sa, ca);
            Vector avG = sumG * invNum; // average g
            Vector avM = sumM * invNum; // average m
            Matrix invG = Matrix.Inverse(sumG2 - (sumG & avG));
            Matrix invM = Matrix.Inverse(sumM2 - (sumM & avM));
            aG = aM = Matrix.one;
            bG = Vector.zero - avG; // use negative average as initial offset
            bM = Vector.zero - avM;
            int it = 0; // number of iterations
            do {
                // get gr & mr from g, m, aG, aM, bG, & bM
                for (int i = 0; i < num; i++) {
                    gr[i] = (aG * g[i]) + bG;
                    mr[i] = (aM * m[i]) + bM;
                }
                sa = ca = 0;
                // Boucle002
                for (int i = 0; i < num; i++) {
                    // get optimal gx & mx from gr & mr
                    if (i < 16) { // equidirectional sample groups
                        Vector grp = Vector.zero;
                        Vector mrp = Vector.zero;
                        int first = i;
                        for (; i < first + 4; i++) {
                            // match gr & mr to first gr & mr
                            Vector gt, mt;
                            TurnVectors(gr[i], mr[i], gr[first], mr[first], out gt, out mt);
                            grp += gt;
                            mrp += mt;
                        }
                        // get optimal matched gx & mx from sum of matched gr & mr
                        Vector gxp, mxp;
                        OptVectors(grp, mrp, alpha, out gxp, out mxp);
                        // alpha calculation
                        sa += Vector.Abs(mrp % gxp);
                        ca += mrp * gxp;
                        // Boucle0004
                        for (i = first; i < first + 4; i++) {
                            // get optimal gx & mx from matched gx & mx
                            TurnVectors(gxp, mxp, gr[i], mr[i], out gx[i], out mx[i]);
                        }
                        i--;
                    } 
                    else 
                    { // individual sample
                        OptVectors(gr[i], mr[i], alpha, out gx[i], out mx[i]);
                        // alpha calculation
                        sa += Vector.Abs(mr[i] % gx[i]);
                        ca += mr[i] * gx[i];
                    }
                }
                alpha = (float)Math.Atan2(sa, ca);
                // get aG & aM from g, m, gx, & mx
                Vector avGx = Vector.zero;
                Vector avMx = Vector.zero;
                Matrix sumGxG = Matrix.zero;
                Matrix sumMxM = Matrix.zero;
                for (int i = 0; i < num; i++) {
                    avGx += gx[i];
                    avMx += mx[i];
                    sumGxG += (gx[i] & g[i]); // outer product
                    sumMxM += (mx[i] & m[i]);
                }
                // get new aG & aM
                aG0 = aG; aM0 = aM;
                avGx = avGx * invNum; // average gx
                avMx = avMx * invNum;
                aG = (sumGxG - (avGx & sumG)) * invG;
                aM = (sumMxM - (avMx & sumM)) * invM;
                // enforce symmetric aG(y,z)
                aG.y.z = aG.z.y = (aG.y.z + aG.z.y) * 0.5F;
                // get new bG & bM
                bG = avGx - (aG * avG);
                bM = avMx - (aM * avM);
                it++;
            } while (it < MAX_IT && Math.Max(Matrix.MaxDiff(aG, aG0), Matrix.MaxDiff(aM, aM0)) > EPS);
            CheckOverflow(ref aG, ref bG);
            CheckOverflow(ref aM, ref bM);
            delta = 0;
            for (int i = 0; i < num; i++) {
                Vector dg = gx[i] - gr[i];
                Vector dm = mx[i] - mr[i];
                delta += (dg * dg) + (dm * dm);
            }
            delta = (float)Math.Sqrt(delta / num) * 100;
            return it;
        }


        private static void PutCoeff(byte[] data, int index, float value) {
            int coeff = (int)Math.Round(value);
            data[index] = (byte)coeff;
            data[index + 1] = (byte)(coeff >> 8);
        }

        public static byte[] GetCoeff() {
            byte[] data = new byte[48];
            PutCoeff(data, 0, bG.x * FV);
            PutCoeff(data, 2, aG.x.x * FM);
            PutCoeff(data, 4, aG.x.y * FM);
            PutCoeff(data, 6, aG.x.z * FM);
            PutCoeff(data, 8, bG.y * FV);
            PutCoeff(data, 10, aG.y.x * FM);
            PutCoeff(data, 12, aG.y.y * FM);
            PutCoeff(data, 14, aG.y.z * FM);
            PutCoeff(data, 16, bG.z * FV);
            PutCoeff(data, 18, aG.z.x * FM);
            PutCoeff(data, 20, aG.z.y * FM);
            PutCoeff(data, 22, aG.z.z * FM);
            PutCoeff(data, 24, bM.x * FV);
            PutCoeff(data, 26, aM.x.x * FM);
            PutCoeff(data, 28, aM.x.y * FM);
            PutCoeff(data, 30, aM.x.z * FM);
            PutCoeff(data, 32, bM.y * FV);
            PutCoeff(data, 34, aM.y.x * FM);
            PutCoeff(data, 36, aM.y.y * FM);
            PutCoeff(data, 38, aM.y.z * FM);
            PutCoeff(data, 40, bM.z * FV);
            PutCoeff(data, 42, aM.z.x * FM);
            PutCoeff(data, 44, aM.z.y * FM);
            PutCoeff(data, 46, aM.z.z * FM);
            return data;
        }
    }
}
//*/
        /* get data from Disto */
/*
	Vector g[56], m[56];
        for (int i = 0; i < 56; i++) {
            int gx, gy, gz, mx, my, mz;
            // read G calibration data packet to gx, gy, gz
            // read M calibration data packet to mx, my, mz
            CalibAlgorithm.AddValues(gx, gy, gz, mx, my, mz, g, m, i);
        }
	
	//* evaluate data 
        float delta;
        int iterations = CalibAlgorithm.Optimize(g, m, &delta);
        // show iterations & delta
	
	//* write coefficients back to Disto 
        byte[] coeff = CalibAlgorithm.GetCoeff();
        int address = 0x8010;
        for (int i = 0; i < 48; i += 4) {
            byte command[7];
            command[0] = 0x39;
            command[1] = (byte)address;
            command[2] = (byte)(address >> 8);
            command[3] = coeff[i];
            command[4] = coeff[i+1];
            command[5] = coeff[i+2];
            command[6] = coeff[i+3];
            // send command and check reply
            address += 4;
        }
*/