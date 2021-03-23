/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ghtopo.javaghtopo;

import static GHTopoOperationnel.Constantes.*;
import static GHTopoOperationnel.GeneralFunctions.*;
import GHTopoOperationnel.TCalibrationDistoX;
import static GHTopoOperationnel.TCallDialogs.*;
import GHTopoOperationnel.TMatrix3x3;
import GHTopoOperationnel.Types.TPoint3Df;
import UtilsComposants.TBluetoothDiscoveryDevicesAndServices;
import java.awt.Color;
import java.awt.EventQueue;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import javax.obex.SessionNotifier;
import javax.swing.DefaultListModel;

public class TdlgCalibrerDistoX extends javax.swing.JDialog 
{
    private int NbServicesBlueToothOK = 0;
    private boolean fFullyListed = false;
    private ResourceBundle fMyResourceBundle = null;
    private boolean fBlueToothIsEnable = false;
    private int fModalResult = mrNONE;
    TBluetoothDiscoveryDevicesAndServices fBluetoothContext;
    private TCalibrationDistoX myCalibrationDistoX;
    // liste des appareils
    private ArrayList<RemoteDevice> fListeDevices;
    private void addADevice(RemoteDevice D)
    {
        fListeDevices.add(D);
    }    
    private RemoteDevice getADevice(int idx)
    {
        return fListeDevices.get(idx);
    }   
    private int getNbDevices()
    {
        return fListeDevices.size();
    }        

    //- I18N
    private String getResourceByKey(String k)
    {
        return fMyResourceBundle.getString(k);
    }      
    public void initialiserDlg()
    {
        
        this.setTitle("Calibration d'un DistoX");
        myCalibrationDistoX = new TCalibrationDistoX();
        myCalibrationDistoX.setJeuDeDonneesExemple();
        myCalibrationDistoX.Optimise();
        setResultCalibration();
        myCalibrationDistoX.setCoeffToDistoX();
        String WU = myCalibrationDistoX.getDistoXDataAsHex();
        afficherMessageFmt("Chaîne à envoyer au Disto: %s (%d bytes)", WU, WU.getBytes().length);
        
    }   
    private void SetQualiteCalibration(double d)
    {
        editDelta.setText(sprintf("%.6f", d));
        if (d < 0.25) 
        {
            lbQualiteCalibration.setText("Excellent");
            lbQualiteCalibration.setBackground(Color.GREEN);
        }    
        else if (IsInRangeDbl(d, 0.25, 0.50))
        {
            lbQualiteCalibration.setText("Good");
            lbQualiteCalibration.setBackground(Color.YELLOW);
        }   
        else if (IsInRangeDbl(d, 0.50, 0.75))
        {   
            lbQualiteCalibration.setText("Poor quality");
            lbQualiteCalibration.setBackground(Color.ORANGE);
        }  
        else
        {
            lbQualiteCalibration.setText("Bad quality");
            lbQualiteCalibration.setBackground(Color.RED);
        }
    }        
    public void setResultCalibration()
    {
        TMatrix3x3 MM = myCalibrationDistoX.getMatrixM();
        TMatrix3x3 MG = myCalibrationDistoX.getMatrixG();
        TPoint3Df  VG = myCalibrationDistoX.getVecteurG();
        TPoint3Df  VM = myCalibrationDistoX.getVecteurM();
        editGX.setText(sprintf("%.7f", VG.X));
        editGY.setText(sprintf("%.7f", VG.Y));
        editGZ.setText(sprintf("%.7f", VG.Z));
        editMX.setText(sprintf("%.7f", VM.X));
        editMY.setText(sprintf("%.7f", VM.Y));
        editMZ.setText(sprintf("%.7f", VM.Z));
        
        editG11.setText(sprintf("%.7f", MG.getValue(1, 1)));
        editG12.setText(sprintf("%.7f", MG.getValue(1, 2)));
        editG13.setText(sprintf("%.7f", MG.getValue(1, 3)));
        editG21.setText(sprintf("%.7f", MG.getValue(2, 1)));
        editG22.setText(sprintf("%.7f", MG.getValue(2, 2)));
        editG23.setText(sprintf("%.7f", MG.getValue(2, 3)));
        editG31.setText(sprintf("%.7f", MG.getValue(3, 1)));
        editG32.setText(sprintf("%.7f", MG.getValue(3, 2)));
        editG33.setText(sprintf("%.7f", MG.getValue(3, 3)));
        
        editM11.setText(sprintf("%.7f", MM.getValue(1, 1)));
        editM12.setText(sprintf("%.7f", MM.getValue(1, 2)));
        editM13.setText(sprintf("%.7f", MM.getValue(1, 3)));
        editM21.setText(sprintf("%.7f", MM.getValue(2, 1)));
        editM22.setText(sprintf("%.7f", MM.getValue(2, 2)));
        editM23.setText(sprintf("%.7f", MM.getValue(2, 3)));
        editM31.setText(sprintf("%.7f", MM.getValue(3, 1)));
        editM32.setText(sprintf("%.7f", MM.getValue(3, 2)));
        editM33.setText(sprintf("%.7f", MM.getValue(3, 3)));
        SetQualiteCalibration(myCalibrationDistoX.getDelta());
    }
    // messages
    private void DispMessage(String msg)
    {
        afficherMessage(msg);
        lbProcessing.setText(msg);
    }        
    //--------------------------------------------------------------------------
    // Gestion du BlueTooth
    private boolean OpenBlueTooth()
    {
        return false;
    }      
    private void listerProprietesDeviceByIdx(int Idx)
    {
        RemoteDevice EWE = getADevice(Idx);
        listerProprietesDevice(EWE);
    }
    private void listerProprietesDevice(RemoteDevice dev)
    {        
        try
        {    
            lbNomAppareil.setText(dev.getFriendlyName(false));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }    
    }   
    
    private void listerLesDevices()
    {
        // lister les services
        DispMessage(sprintf("%d devices found", getNbDevices()));
        
        
        int Nb = getNbDevices();
        for (int i = 0; i < Nb; i++)
        {
            try
            {
                RemoteDevice EWE = this.getADevice(i);            
                String Auth = EWE.isAuthenticated() ? "Authentifie" : "Anonyme";
                afficherMessageFmt("%d: %s - %s - %s ", i, EWE.getBluetoothAddress(), Auth, EWE.getFriendlyName(false));
                //EventQueue.invokeLater(ru);
                
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }      
        }            
    }   
    private void listerLesServices()
    {
        int Nb = fBluetoothContext.getNbServicesFound();
        NbServicesBlueToothOK = Nb;
        DispMessage(sprintf("%d services", Nb));
        fFullyListed = false;
        DefaultListModel ldm = new DefaultListModel();
        lsbBluetoothServices.setModel(ldm);
        lsbBluetoothServices.removeAll();
        if (Nb > 0)
        {
            for (int i = 0; i < Nb; i++)
            {
                try
                {
                    ServiceRecord EWE = fBluetoothContext.getService(i);
                    String url = EWE.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                    DataElement serviceName = EWE.getAttributeValue(0x0100);
                    String WU = (serviceName != null) ? (String) serviceName.getValue() : "Anonyme";
                    RemoteDevice QAT = EWE.getHostDevice();
                    ldm.addElement(sprintf("%d: dev: %s - serv: %s - url: %s",i, QAT.getFriendlyName(false), WU, url));                
                }
                catch (Exception e)
                {
                    ;
                }    
            }
            lsbBluetoothServices.setSelectedIndex(0);
            //listerProprietesDeviceByIdx(0);
            lbNombreServices.setText(sprintf("%d services", Nb));  
        }    
        else
        {            
            showMessage("Bluetooth", "Aucun service");
        }
        fFullyListed = true;
    }
    private ServiceRecord getServiceRecord(int idx)
    {
        return fBluetoothContext.getService(idx);
    }        
    private void rescannerBluetoothDevices()
    {
        CloseBlueTooth();
        try
        {
            btnRescanDevices.setEnabled(false);
            fListeDevices.clear();
            lbNombreServices.setText(sprintf("%d appareils - Recherche en cours", 0)); 
            
            fBluetoothContext.DiscoveryDevices();
            fBluetoothContext.DiscoveryServices();
            
            int Nb = fBluetoothContext.getNbDevicesFound();                           
            if (Nb > 0) 
            {                
                // lister les appareils
                fFullyListed = false;
                DefaultListModel ldm = new DefaultListModel();
                lsbBluetoothServices.setModel(ldm);
                lsbBluetoothServices.removeAll();
                for (int i = 0; i < Nb; i++)
                {    
                    RemoteDevice EWE = fBluetoothContext.getDevice(i);
                    this.addADevice(EWE);
                    
                } 
                listerLesDevices();
                listerLesServices();
            } 
            else
            {
                showMessage("Bluetooth", "Aucun appareil détecté");
            }    
            btnRescanDevices.setEnabled(true);
        }
        catch (Exception e)
        {
            showMessage("Bluetooth", sprintf("Erreur BlueTooth: %s", e.getMessage()));
            e.printStackTrace();
            btnRescanDevices.setEnabled(true);
        }            
    }
    private void CloseBlueTooth()
    {
        try
        {
            try
            {
                
            }  
            catch (Exception e)
            {
                e.printStackTrace();
            }    
        }
        finally
        {
            fBlueToothIsEnable = false;
        }     
    }      
    public int getModalResult()
    {
        return fModalResult;
    }        
     
    public TdlgCalibrerDistoX() {
        this.fMyResourceBundle = java.util.ResourceBundle.getBundle("com/ghtopo/javaghtopo/Bundle");
        initComponents();
        this.setLocationRelativeTo(null); // centrée
        fListeDevices = new ArrayList<RemoteDevice>();
        fListeDevices.clear();
        try
        {    
            fBluetoothContext = new TBluetoothDiscoveryDevicesAndServices();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }                    
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        lsbBluetoothServices = new javax.swing.JList();
        lbNombreServices = new javax.swing.JLabel();
        btnRescanDevices = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lbNomAppareil = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        editM11 = new javax.swing.JTextField();
        editM12 = new javax.swing.JTextField();
        editM13 = new javax.swing.JTextField();
        editMX = new javax.swing.JTextField();
        editM21 = new javax.swing.JTextField();
        editM22 = new javax.swing.JTextField();
        editM23 = new javax.swing.JTextField();
        editMY = new javax.swing.JTextField();
        editM31 = new javax.swing.JTextField();
        editM32 = new javax.swing.JTextField();
        editM33 = new javax.swing.JTextField();
        editMZ = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        editG11 = new javax.swing.JTextField();
        editG12 = new javax.swing.JTextField();
        editG13 = new javax.swing.JTextField();
        editG21 = new javax.swing.JTextField();
        editG22 = new javax.swing.JTextField();
        editG23 = new javax.swing.JTextField();
        editGX = new javax.swing.JTextField();
        editGY = new javax.swing.JTextField();
        editG31 = new javax.swing.JTextField();
        editG32 = new javax.swing.JTextField();
        editG33 = new javax.swing.JTextField();
        editGZ = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        editDelta = new javax.swing.JTextField();
        lbQualiteCalibration = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        btnArmerAsServer = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        lbLocalDevice = new javax.swing.JLabel();
        btnClose = new javax.swing.JButton();
        btnArmerAsClient = new javax.swing.JButton();
        lbProcessing = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lsbBluetoothServices.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lsbBluetoothServices.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lsbBluetoothServicesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(lsbBluetoothServices);

        lbNombreServices.setText("jLabel1");

        btnRescanDevices.setText("Rescan");
        btnRescanDevices.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRescanDevicesActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbNomAppareil.setText("jLabel1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbNomAppareil)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbNomAppareil)
                .addContainerGap(101, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("aG");

        editM11.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        editM11.setText("0.000");

        editM12.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        editM12.setText("0.000");

        editM13.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        editM13.setText("0.000");

        editMX.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        editMX.setText("0.000");

        editM21.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        editM21.setText("0.000");

        editM22.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        editM22.setText("0.000");

        editM23.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        editM23.setText("0.000");

        editMY.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        editMY.setText("0.000");

        editM31.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        editM31.setText("0.000");

        editM32.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        editM32.setText("0.000");

        editM33.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        editM33.setText("0.000");

        editMZ.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        editMZ.setText("0.000");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("bG");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(editM11, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(editM31, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(editM21, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(editM12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(editM32, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(editM22, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(editM13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(editM33, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(editM23, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editMX, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editMY, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editMZ, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(editM11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editM21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editM31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(editM12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editM22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editM32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(editM13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(editMX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(editM23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(editMY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(editM33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(editMZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Gravimétrie", jPanel3);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        editG11.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        editG11.setText("0.000");

        editG12.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        editG12.setText("0.000");

        editG13.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        editG13.setText("0.000");

        editG21.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        editG21.setText("0.000");

        editG22.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        editG22.setText("0.000");

        editG23.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        editG23.setText("0.000");

        editGX.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        editGX.setText("0.000");

        editGY.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        editGY.setText("0.000");

        editG31.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        editG31.setText("0.000");

        editG32.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        editG32.setText("0.000");

        editG33.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        editG33.setText("0.000");

        editGZ.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        editGZ.setText("0.000");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("aM");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("bM");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(editG31, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editG32, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editG33, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addComponent(editGZ, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(editG21, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(editG22, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(editG23, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(editGY, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(editG11, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editG12, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editG13, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(editGX, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(editG11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(editG12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(editGX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(editG13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(editG21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(editG22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(editGY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(editG23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editG33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(editG32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(editG31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(editGZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(293, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Magnétisme", jPanel2);

        jLabel3.setText("Delta");

        editDelta.setEditable(false);
        editDelta.setText("0");
        editDelta.setToolTipText("");

        lbQualiteCalibration.setBackground(new java.awt.Color(102, 255, 51));
        lbQualiteCalibration.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbQualiteCalibration.setText("---");
        lbQualiteCalibration.setName("lbQualityDelta"); // NOI18N
        lbQualiteCalibration.setOpaque(true);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(editDelta, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbQualiteCalibration, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(editDelta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbQualiteCalibration, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3))
                .addContainerGap(365, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Autres valeurs", jPanel4);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 363, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 424, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnArmerAsServer.setText("Armer comme serveur");
        btnArmerAsServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArmerAsServerActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jButton1.setText("Recevoir des données");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        lbLocalDevice.setText("jLabel7");
        lbLocalDevice.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnClose.setText("Fermer");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        btnArmerAsClient.setText("Armer comme client");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(174, 174, 174)
                        .addComponent(lbLocalDevice, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(btnArmerAsServer, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnArmerAsClient, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnArmerAsServer)
                    .addComponent(btnArmerAsClient))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(lbLocalDevice, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(220, 220, 220))
        );

        lbProcessing.setText("--");
        lbProcessing.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbNombreServices, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRescanDevices, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbProcessing, javax.swing.GroupLayout.PREFERRED_SIZE, 657, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 710, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbNombreServices)
                        .addComponent(btnRescanDevices))
                    .addComponent(lbProcessing, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        fModalResult = mrCLOSE;
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnRescanDevicesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRescanDevicesActionPerformed
        // TODO add your handling code here:
        DispMessage("Scan bluetooth");
        rescannerBluetoothDevices();
    }//GEN-LAST:event_btnRescanDevicesActionPerformed

    private void lsbBluetoothServicesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lsbBluetoothServicesValueChanged
        // TODO add your handling code here:
        if (fFullyListed) // liste complète ?
        {
            listerProprietesDeviceByIdx(lsbBluetoothServices.getSelectedIndex());
        }    
    }//GEN-LAST:event_lsbBluetoothServicesValueChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        lbLocalDevice.setText("No device available");
          
        // Device/service local
        try
        {    
            //BTS
           // BTServer btServer = new BTServer();
           // btServer.startServer();
            afficherMessage("Local device");
            LocalDevice DV = LocalDevice.getLocalDevice();
            afficherMessage("Discovery Agent");
            DiscoveryAgent AG = DV.getDiscoveryAgent();
            afficherMessage("Address: " + DV.getBluetoothAddress());
            afficherMessage("Name: " + DV.getFriendlyName());
            lbLocalDevice.setText(sprintf("%s - %s", DV.getFriendlyName(), DV.getBluetoothAddress()));
        }
        catch (Exception e)
        {
            
            e.printStackTrace();
        }  
        if (NbServicesBlueToothOK == 0) 
        {
            showMessage("Bluetooth", "Aucun service disponible");
            return;
        }  
        // SORTIE PROVISOIRE
        if (true) return;
        // Service distant
        ServiceRecord SR = getServiceRecord(lsbBluetoothServices.getSelectedIndex());
        //-------------------------------------------
        // let's name our variables

        StreamConnectionNotifier notifier  = null;
        StreamConnection         connexion = null;
        LocalDevice              localdevice = null;
        
        
        InputStream input;
        OutputStream output;

        // let's create a URL that contains a UUID that 
        // has a very low chance of conflicting with anything
        String url = "btspp://localhost:00112233445566778899AABBCCDDEEFF;name=serialconn";
        // let's open the connection with the url and
        // cast it into a StreamConnectionNotifier         
        try
        {    
            notifier = (StreamConnectionNotifier) Connector.open(url);

            // block the current thread until a client responds
            connexion = notifier.acceptAndOpen();

            // the client has responded, so open some streams
            input  = connexion.openInputStream();
            output = connexion.openOutputStream();
            // now that the streams are open, send and
            // receive some data
            for (;;)
            {
                char c = (char) input.read();
                if (c == -1) break;
            }    
            
                
        }
        catch (Exception e)
        {
            ;
        }         
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnArmerAsServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArmerAsServerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnArmerAsServerActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TdlgCalibrerDistoX.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TdlgCalibrerDistoX.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TdlgCalibrerDistoX.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TdlgCalibrerDistoX.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TdlgCalibrerDistoX().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnArmerAsClient;
    private javax.swing.JButton btnArmerAsServer;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnRescanDevices;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField editDelta;
    private javax.swing.JTextField editG11;
    private javax.swing.JTextField editG12;
    private javax.swing.JTextField editG13;
    private javax.swing.JTextField editG21;
    private javax.swing.JTextField editG22;
    private javax.swing.JTextField editG23;
    private javax.swing.JTextField editG31;
    private javax.swing.JTextField editG32;
    private javax.swing.JTextField editG33;
    private javax.swing.JTextField editGX;
    private javax.swing.JTextField editGY;
    private javax.swing.JTextField editGZ;
    private javax.swing.JTextField editM11;
    private javax.swing.JTextField editM12;
    private javax.swing.JTextField editM13;
    private javax.swing.JTextField editM21;
    private javax.swing.JTextField editM22;
    private javax.swing.JTextField editM23;
    private javax.swing.JTextField editM31;
    private javax.swing.JTextField editM32;
    private javax.swing.JTextField editM33;
    private javax.swing.JTextField editMX;
    private javax.swing.JTextField editMY;
    private javax.swing.JTextField editMZ;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lbLocalDevice;
    private javax.swing.JLabel lbNomAppareil;
    private javax.swing.JLabel lbNombreServices;
    private javax.swing.JLabel lbProcessing;
    private javax.swing.JLabel lbQualiteCalibration;
    private javax.swing.JList lsbBluetoothServices;
    // End of variables declaration//GEN-END:variables
}