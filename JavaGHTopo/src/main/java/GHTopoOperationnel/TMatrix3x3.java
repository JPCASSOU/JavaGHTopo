/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GHTopoOperationnel;

public class TMatrix3x3 
{
    private double[][] fM;
    private void initMatrix()
    {
        fM = new double [4][4]; // pour que les index aillent de 1 à 3
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                fM [i][j] = 0.00;
    }
    public void loadIdentity()
    {
        for (int i = 0; i < 4; i++)  fM [i][i] = 1.00;
    }    
    public void loadZero()
    {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                fM [i][j] = 0.00;
    }    
    public double getValue(int i, int j)
    {
        return fM[i][j];
    }    
    public void setValue(int i, int j, double Value)
    {
        fM[i][j] = Value;
    }    
    public TMatrix3x3()
    {
        initMatrix();
    } 
    // init avec des valeurs
    public TMatrix3x3(double V11, double V12, double V13,
                      double V21, double V22, double V23, 
                      double V31, double V32, double V33)
    {
        initMatrix();       
        fM[1][1] = V11; fM[1][2] = V12; fM[1][3] = V13;
        fM[2][1] = V21; fM[2][2] = V22; fM[2][3] = V23;
        fM[3][1] = V31; fM[3][2] = V32; fM[3][3] = V33;
    }
    
}
