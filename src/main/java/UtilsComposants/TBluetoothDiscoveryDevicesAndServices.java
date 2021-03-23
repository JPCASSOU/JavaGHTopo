package UtilsComposants;
import GHTopoOperationnel.GeneralFunctions;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import javax.bluetooth.*;

/**
 *
 * @author jean-pierre.cassou
 */
public class TBluetoothDiscoveryDevicesAndServices 
{
    public static final UUID OBEX_OBJECT_PUSH = new UUID(0x1105);
    public static final UUID OBEX_FILE_TRANSFER = new UUID(0x1106);
    public static final Vector devicesDiscovered = new Vector();
    public static final Vector servicesFound = new Vector();
    
    private Object inquiryCompletedEvent;
    private Object serviceSearchCompletedEvent;
    
    public int getNbDevicesFound()
    {
        return devicesDiscovered.size(); 
    }   
    public RemoteDevice getDevice(int idx)
    {
        return (RemoteDevice) devicesDiscovered.get(idx);
    }        
    
    public int getNbServicesFound()
    {
        return servicesFound.size(); 
    }   
    public void addService(ServiceRecord S)
    {
        servicesFound.add(S);
    }        
    public ServiceRecord getService(int idx)
    {
        return (ServiceRecord) servicesFound.get(idx);
    }        
    //*/
    private DiscoveryListener listener = new DiscoveryListener() {
            //@Override
            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                System.out.println("Device " + btDevice.getBluetoothAddress() + " found");
                devicesDiscovered.addElement(btDevice);
                try 
                {
                    System.out.println("     name " + btDevice.getFriendlyName(false));
                } 
                catch (IOException cantGetDeviceName) 
                {
                }
            }
            //@Override
            public void inquiryCompleted(int discType) {
                System.out.println("Device Inquiry completed!");
                synchronized(inquiryCompletedEvent){
                    inquiryCompletedEvent.notifyAll();
                }
            }
            //@Override
            public void serviceSearchCompleted(int transID, int respCode) {
                System.out.println("service search completed!");
                synchronized(serviceSearchCompletedEvent){
                             serviceSearchCompletedEvent.notifyAll();
                }
            }
            //@Override
            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
                for (int i = 0; i < servRecord.length; i++) 
                {
                    // on ajoute le service
                    ServiceRecord EWE = servRecord[i]; 
                   
                    //String url = servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                    String url = EWE.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                    if (url == null) {
                        continue;
                    } 
                    addService(EWE);
                    EWE = getService(i);
                    url = EWE.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                    //servicesFound.add(url);
                    //addService(servRecord[i]);
                    //DataElement serviceName = servRecord[i].getAttributeValue(0x0100);
                    DataElement serviceName = EWE.getAttributeValue(0x0100);
                    
                    
                    if (serviceName != null) {
                        System.out.println("service " + serviceName.getValue() + " trouvé " + url);
                    } else {
                        System.out.println("service anonyme trouvé: " + url);
                    }
                }
            }
        };
    
    public void DiscoveryDevices()
    {
        GeneralFunctions.afficherMessage("TBluetoothDiscoveryDevices: Entre dans DiscoveryDevices");
        
        devicesDiscovered.clear();
        try
        {    
            synchronized(inquiryCompletedEvent) {
                boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
                if (started) {
                    System.out.println("wait for device inquiry to complete...");
                    inquiryCompletedEvent.wait();
                    System.out.println(devicesDiscovered.size() +  " device(s) found");
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }    
        
    }      
    public void DiscoveryServices()
    {
        GeneralFunctions.afficherMessage("TBluetoothDiscoveryDevices: Entre dans DiscoveryServices");
        serviceSearchCompletedEvent = new Object();
        UUID serviceUUID = OBEX_OBJECT_PUSH;
        UUID[] searchUuidSet = new UUID[] { serviceUUID };
        int[] attrIDs =  new int[] {0x0100}; // Service name
        servicesFound.clear();
        int Nb = getNbDevicesFound();
        for (int i = 0; i < Nb; i++)
        {
            RemoteDevice btDevice = getDevice(i);            
            try
            {    
                GeneralFunctions.afficherMessageFmt("Recherche des services pour le device: %d - %s", i, btDevice.getFriendlyName(false));
                synchronized(serviceSearchCompletedEvent) {
                    System.out.println("search services on " + btDevice.getBluetoothAddress() + " " + btDevice.getFriendlyName(false));
                    LocalDevice.getLocalDevice().getDiscoveryAgent().searchServices(attrIDs, searchUuidSet, btDevice, listener);
                    serviceSearchCompletedEvent.wait();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }    
        }
        GeneralFunctions.afficherMessage("Recherche des services terminée");
    }
    public TBluetoothDiscoveryDevicesAndServices() throws IOException, InterruptedException
    {
        inquiryCompletedEvent       = new Object();
        serviceSearchCompletedEvent = new Object();
        
    }           
}
