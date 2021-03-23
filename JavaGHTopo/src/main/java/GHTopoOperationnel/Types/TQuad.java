package GHTopoOperationnel.Types;
import java.awt.Color;
import java.awt.Point;

public class TQuad
{
    public boolean Visible;
    public boolean Drawn;
    public TPoint3Df VertexA;
    public TPoint3Df VertexB;
    public TPoint3Df VertexC;
    public TPoint3Df VertexD;
    public TPoint3Df Normale;
    public double    Depth;
    public Point     Vertex2DA;
    public Point     Vertex2DB;
    public Point     Vertex2DC;
    public Point     Vertex2DD;
    public Color     Couleur;
    public int       TypeEntite;
    public int       IdxReseau;
    public int       IdxSecteur;
    public int       IdxExpe;
}
