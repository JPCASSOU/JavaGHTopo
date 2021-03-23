package GHTopoOperationnel;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.Month;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.temporal.Temporal;
import org.threeten.bp.temporal.TemporalAccessor;
/**
 *
 * @author jean-pierre.cassou
 */
public class TDateTime 
{
    // offset à ajouter pour passer à la représentation OOo (T0 = 01/01/1900 soit 25569 jours)    
    private static final long OOO_EPOCH  = 25569L * 86400L; // suffixe L indispensable 
    private LocalDateTime fCurrentDateTime = null;
    public int getYear()
    {
        return fCurrentDateTime.getYear();
    }    
    public int getMonth()
    {
        return fCurrentDateTime.getMonthValue();
    }    
    public int getDay()
    {
        return fCurrentDateTime.getDayOfMonth();        
    }
    public int getHour()
    {
        return fCurrentDateTime.getHour();
    }
    public int getMinute()
    {
        return fCurrentDateTime.getMinute();
    }
    public int getSeconds()
    {
        return fCurrentDateTime.getSecond();
    }
    public int getMillisecond()
    {
        return 0; // rien à battre des millisecondes
    }
    
     
    public void setDateTime(int YYYY, int MM, int DD, int HH, int MN, int SS, int MS)
    {
        fCurrentDateTime = LocalDateTime.of(YYYY, MM, DD, HH, MN, SS, MS);
    }
    public long getDateSerialInSeconds()
    {
        return fCurrentDateTime.toEpochSecond(ZoneOffset.UTC);
    } 
    public void setDateSerialInSeconds(long t)
    {
        Instant qt = Instant.ofEpochMilli(t * 1000);
        fCurrentDateTime = LocalDateTime.ofInstant(qt, ZoneOffset.UTC);
    }        
    public void setDateFromPasTDateTime(double t)
    {
        ; 
    } 
    // renvoie une date LibreOffice 
    // nombre de jours depuis 01/01/1900
    public double getDateAsOOoFormat()
    {
        GeneralFunctions.afficherMessageFmt("%d ** %d", OOO_EPOCH, getDateSerialInSeconds());
        return (double) (OOO_EPOCH + this.getDateSerialInSeconds()) / 86400;
    }
    //--------------------------------------------------------------------------
    // constructeurs
    public TDateTime()
    {
        fCurrentDateTime = LocalDateTime.now();
    }       
    public TDateTime(int YYYY, int MM, int DD, int HH, int MN, int SS, int MS)
    {
        this.setDateTime(YYYY, MM, DD, HH, MN, SS, MS);
    }
    public TDateTime(long t) // par date en secondes depuis EPOCH
    {
        this.setDateSerialInSeconds(t);
    }
    public TDateTime(double t) // depuis une date Pascal TDateTime
    {
        
        //fCurrentDateTime = LocalDateTime.of(YYYY, MM, DD, HH, MN, SS, MS);
    }     
}
