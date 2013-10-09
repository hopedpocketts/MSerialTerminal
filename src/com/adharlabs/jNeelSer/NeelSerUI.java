/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adharlabs.jNeelSer;

import com.adharlabs.UI.StringWithVisualStyle;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.adharlabs.UI.ThreadedTextPaneHandler;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * jNeelSer Serial User Interface
 *
 * @version 1.0_20130625
 * @author Abhijit Bose , A.D.H.A.R Labs Research, Bharat(India)
 */
public class NeelSerUI extends javax.swing.JFrame implements INeelSerialInterface {

    //<editor-fold defaultstate="collapsed" desc="Constant Data">
    public final String sProduct = "jNeelSerial";
    public final String sCompany = "A.D.H.A.R Labs Research, Bharat(India)";
    //</editor-fold>
    
    @SuppressWarnings("NonConstantLogger")
    private static Logger LOG;
    
    //<editor-fold defaultstate="collapsed" desc="Private Data Vars">
    private NeelSerOptions nso;
    private BlockingQueue<StringWithVisualStyle> RxDispQue;
    private ThreadedTextPaneHandler RxPaneHandler;
    private Thread RxPaneThread;
    private String sEndl = "\r\n"; //Default CRLF
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Creates new form NeelSerUI
     */
    @SuppressWarnings("CallToThreadStartDuringObjectConstruction")
    public NeelSerUI(Logger log) {
        NeelSerUI.LOG = log;
        initComponents();
        
        //Set the title
        super.setTitle(sProduct + " by " + sCompany);
        
        //Initializae the Options base
        nso = new NeelSerOptions(true, NeelSerUI.LOG);
        
        //Get Ports
        this.v_updatePortList();
        
        // Create the RX Queue and Assign the Handler
        this.RxDispQue = new LinkedBlockingQueue<StringWithVisualStyle>();
        this.RxPaneHandler = new ThreadedTextPaneHandler(xTP_RX, RxDispQue, log);
        
        // Run the RX TextPane Thread
        this.RxPaneThread = new Thread(this.RxPaneHandler, "RX TextPane Thread");
        this.RxPaneThread.start();
        
        //Initialize items
        this.xL_CTS.setOpaque(false);
        this.xL_DSR.setOpaque(false);
        this.xL_RI.setOpaque(false);
        this.xL_RSLD.setOpaque(false);
        this.v_DisableDataEnableControls();
        
        // Print Some Messages
        this.println("Initialization Done! ... let the fun begin", Color.BLUE);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Internal Functions">
    /**
     * Function to update the Port List Combo box
     */
    private void v_updatePortList() {
        // Refresh Port List
        try {
            this.nso.v_refreshPortList();
        } catch (Exception e) {
            NeelSerUI.LOG.log(Level.SEVERE,
                    "Got Exception in Updating port list: {0}", e.toString());
            this.println("Error: Could not Update Port List ", Color.RED);
            return;
        }
        this.xCB_Port.removeAllItems();
        for (Object object : nso.arsPortList) {
            this.xCB_Port.addItem(object);
        }
        this.xCB_Port.addItem("Update");
    }
    
    private void v_DisableDataEnableControls() {
        this.xCB_Port.setEnabled(true);
        this.xCB_Baud.setEnabled(true);
        this.xCB_DataLen.setEnabled(true);
        this.xCB_Parity.setEnabled(true);
        this.xCB_HandShake.setEnabled(true);
        this.xB_UpdatePorts.setEnabled(true);
        this.xT_CustBaud.setEnabled(true);
        this.xB_Data1_Send.setEnabled(false);
        this.xB_Data2_Send.setEnabled(false);
        this.xB_Data3_Send.setEnabled(false);
        this.xB_Data4_Send.setEnabled(false);
    }
    
    private void v_EnableDataDisableControls() {
        this.xCB_Port.setEnabled(false);
        this.xCB_Baud.setEnabled(false);
        this.xCB_DataLen.setEnabled(false);
        this.xCB_Parity.setEnabled(false);
        this.xCB_HandShake.setEnabled(false);
        this.xB_UpdatePorts.setEnabled(false);
        this.xT_CustBaud.setEnabled(false);
        this.xB_Data1_Send.setEnabled(true);
        this.xB_Data2_Send.setEnabled(true);
        this.xB_Data3_Send.setEnabled(true);
        this.xB_Data4_Send.setEnabled(true);
    }
    
    private void v_InterpretValues(){
        this.nso.iBaudrate = Integer.parseInt(
                this.xCB_Baud.getSelectedItem().toString());
        this.nso.iDataBits = Integer.parseInt(
                this.xCB_DataLen.getSelectedItem().toString());
        this.print("BAUD: "+this.nso.iBaudrate,Color.LIGHT_GRAY);
        this.print("DataLen: "+this.nso.iDataBits,Color.LIGHT_GRAY);
        this.print("Parity: "+
                this.xCB_Parity.getSelectedItem().toString(),Color.LIGHT_GRAY);
        this.print("HandShake: "+
                this.xCB_HandShake.getSelectedItem().toString(),Color.LIGHT_GRAY);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Internal Prints">
    /**
     *
     * @param s
     * @param fgColor
     */
    private void print(String s, Color fgColor) {
        this.RxDispQue.add(
                new StringWithVisualStyle(s, fgColor));
        NeelSerUI.LOG.log(Level.FINER, "Got String: {0}", s);
    }

    /**
     *
     * @param s
     * @param fgColor
     * @param bgColor
     */
    private void print(String s, Color fgColor, Color bgColor) {
        this.RxDispQue.add(
                new StringWithVisualStyle(s, fgColor, bgColor));
        NeelSerUI.LOG.log(Level.FINER, "Got String: {0}", s);
    }

    /**
     *
     */
    private void println() {
        this.print("\r\n", Color.BLACK, Color.WHITE);
    }

    /**
     *
     * @param s
     */
    private void println(String s) {
        this.print(s + "\r\n", Color.BLACK);
    }

    /**
     *
     * @param s
     * @param fgColor
     */
    private void println(String s, Color fgColor) {
        this.print(s + "\r\n", fgColor);
    }

    /**
     *
     * @param s
     * @param fgColor
     * @param bgColor
     */
    private void println(String s, Color fgColor, Color bgColor) {
        this.print(s + "\r\n", fgColor, bgColor);
    }

    /**
     *
     * @param arr
     * @return
     */
    private String hexstr(byte[] arr) {
        String s = "";
        if (arr != null && arr.length > 0) {
            for (int i = 0; i < arr.length; i++) {
                s += String.format("[%02X]", arr[i]);
            }
        }
        return s;
    }

    /**
     *
     * @param arr
     * @return
     */
    private String asciistr(byte[] arr, boolean special) {
        String s = "";
        if (arr != null && arr.length > 0) {
            for (int i = 0; i < arr.length; i++) {
                if ((arr[i] >= 32) && (arr[i] <= 126)) {
                    s += String.format("%c", arr[i]);
                } else if (arr[i] == 0x0D || arr[i] == 0x0A) {
                    if ((i + 1) < arr.length && (arr[i] == 0x0D)) {
                        if (arr[i + 1] == 0x0A) {
                            ++i;
                        }
                    }
                    s += "\r\n";
                } else if (arr[i] == 0x08 || arr[i] == 0x7F) {
                    s += "\b";
                } else if (special) {
                    s += String.format("[%02X]", arr[i]);
                }
            }
        }
        return s;
    }

    /**
     *
     * @param s
     * @return
     */
    private String inlinehex(String s) {
        String str = s;
        int i = 0, k = 0;
        while (i >= 0) {
            i = str.indexOf("\\x", k);
            if (i >= 0) {
                Matcher m = Pattern.compile("\\\\x([\\da-fA-F]{2})").matcher(str.substring(i));
                if (m.find()) {
                    /*System.out.println("Matched: "+m.group(1)+ " Str :" + str );
                     * System.out.println("Hex: "+Integer.parseInt(m.group(1), 16)+" Char: "+
                     * Character.toString((char)Integer.parseInt(m.group(1), 16)));*/
                    str = str.replaceFirst("\\\\x([\\da-fA-F]{2})",
                            Character.toString((char) Integer.parseInt(m.group(1), 16)));
                }
            }
            k = i + 1;
        }
        return str;
    }

    /**
     *
     * @param data
     * @param isHex
     * @param needEndl
     * @return
     */
    private String processSendData(String data, boolean isHex,
            boolean needEndl) throws Exception {
        String s = data;
        // Hex Processing
        if (isHex) {
            // Filter out unnessary characters
            s = s.replaceAll("[\\s\\\\x]|(0x)", "");
            s = s.replaceAll("[^a-fA-F0-9]", "");
            if ((s.length() % 2) != 0) {
                this.println();
                this.print("Error the Hex Input has invalid lenght", Color.RED);
                throw new Exception("Error the Hex Input has invalid lenght");
            }
            char[] arr = s.toCharArray();
            s = "";
            for (int i = 0; i < arr.length; i++) {
                s += Character.toString((char) Integer.parseInt(
                        String.format("%c%c", arr[i], arr[i + 1]), 16));
                ++i;
            }
            this.println(this.hexstr(s.getBytes()), Color.BLACK);
        } else { // Text Processing
            //Default Processing for Escape Chars
            s = s.replaceAll("\\\\r", Character.toString('\r'));
            s = s.replaceAll("\\\\n", Character.toString('\n'));
            s = s.replaceAll("\\\\b", Character.toString('\b'));
            s = s.replaceAll("\\\\t", Character.toString('\t'));
            s = s.replaceAll("\\\\f", Character.toString('\f'));
            s = s.replaceAll("\\\\0", Character.toString('\0'));
            s = s.replaceAll("\\\\\"", Character.toString('\"'));
            s = s.replaceAll("\\\\\'", Character.toString('\''));
            // Inline Hex
            if (s.contains("\\x")) {
                s = this.inlinehex(s);
            }
            // Endl if needed
            if (needEndl) {
                s += this.sEndl;
            }
            this.println(s, Color.BLACK);
        }
        return s;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Neel Serial Interface Overrides">
    @Override
    public void gotRxData(byte[] arb) {
        if (this.nso.isPortOpen) {
            if (this.xRB_RXmode_Ascii.isSelected()) {
                this.print(this.asciistr(arb, false), Color.BLACK);
            } else if (this.xRB_RXmode_Mixed.isSelected()) {
                this.print(this.asciistr(arb, true), Color.BLACK);
            } else if (this.xRB_RXmode_Hex.isSelected()) {
                this.print(this.hexstr(arb), Color.BLACK);
            }
        }
    }

    @Override
    public byte[] needTxData() {
        if (this.nso.isPortOpen) {
            return new byte[1];
        }
        return null;
    }

    @Override
    public void changeDSR(boolean state) {
        this.println("DSR: " + state, Color.ORANGE);
        this.xL_DSR.setOpaque(state);
    }

    @Override
    public void changeRI(boolean state) {
        this.println("RI: " + state, Color.ORANGE);
        this.xL_RI.setOpaque(state);
    }

    @Override
    public void changeCTS(boolean state) {
        this.println("CTS: " + state, Color.ORANGE);
        this.xL_CTS.setOpaque(state);
    }

    @Override
    public void changeRLSD(boolean state) {
        this.println("RSLD: " + state, Color.ORANGE);
        this.xL_RSLD.setOpaque(state);
    }

    @Override
    public void gotBreak(boolean state) {
        this.println("Got break Condiion on TX line", Color.ORANGE);
    }

    @Override
    public void notifyError(eNeelSerialStatus lvl, String from, String error) {
        this.println("Error: \nfrom: " + from
                + "\ninfo: " + error
                + "\n Level: " + lvl.name() + " = " + lvl.toString(), Color.RED);
    }
    //</editor-fold>

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        xTP_SettingPane = new javax.swing.JTabbedPane();
        xP_BasicSettings = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        xCB_Port = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        xCB_Baud = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        xCB_DataLen = new javax.swing.JComboBox();
        xCB_Parity = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        xCB_HandShake = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        xP_RXmodeSel = new javax.swing.JPanel();
        xRB_RXmode_Ascii = new javax.swing.JRadioButton();
        xRB_RXmode_Hex = new javax.swing.JRadioButton();
        xRB_RXmode_Mixed = new javax.swing.JRadioButton();
        xP_TXmodeSel = new javax.swing.JPanel();
        xRB_TXmode_Ascii = new javax.swing.JRadioButton();
        xRB_TXmode_Hex = new javax.swing.JRadioButton();
        xCB_TXmode_Endl = new javax.swing.JComboBox();
        xB_OpenClose = new javax.swing.JButton();
        xP_ModemStatus = new javax.swing.JPanel();
        xCK_DTR = new javax.swing.JCheckBox();
        xCK_RTS = new javax.swing.JCheckBox();
        xL_DSR = new javax.swing.JLabel();
        xL_CTS = new javax.swing.JLabel();
        xL_RI = new javax.swing.JLabel();
        xL_RSLD = new javax.swing.JLabel();
        xB_SendBreak = new javax.swing.JButton();
        xB_UpdatePorts = new javax.swing.JButton();
        xT_CustBaud = new javax.swing.JFormattedTextField();
        xCK_CustomBaud = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        xCB_StopBits = new javax.swing.JComboBox();
        xSP_IO = new javax.swing.JSplitPane();
        xSCP_Rx = new javax.swing.JScrollPane();
        xTP_RX = new javax.swing.JTextPane();
        xSCP_Tx = new javax.swing.JScrollPane();
        xP_Tx = new javax.swing.JPanel();
        xT_Data1 = new javax.swing.JTextField();
        xCK_Data1_Hex = new javax.swing.JCheckBox();
        xB_Data1_Send = new javax.swing.JButton();
        xCK_Data1_Endl = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        xT_Data2 = new javax.swing.JTextField();
        xCK_Data2_Hex = new javax.swing.JCheckBox();
        xCK_Data2_Endl = new javax.swing.JCheckBox();
        xB_Data2_Send = new javax.swing.JButton();
        xB_Data3_Send = new javax.swing.JButton();
        xCK_Data3_Endl = new javax.swing.JCheckBox();
        xCK_Data3_Hex = new javax.swing.JCheckBox();
        xT_Data3 = new javax.swing.JTextField();
        xCK_Data4_Endl = new javax.swing.JCheckBox();
        xB_Data4_Send = new javax.swing.JButton();
        xCK_Data4_Hex = new javax.swing.JCheckBox();
        xT_Data4 = new javax.swing.JTextField();
        xCB_Seq = new javax.swing.JComboBox();
        xB_LoadSeq = new javax.swing.JButton();
        xB_SendSingleSeq = new javax.swing.JButton();
        xB_SendCompleteSeq = new javax.swing.JButton();
        xMb_Main = new javax.swing.JMenuBar();
        xMn_File = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(580, 600));
        setName("NeelSerialUI"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        xTP_SettingPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        xTP_SettingPane.setPreferredSize(new java.awt.Dimension(580, 200));

        xP_BasicSettings.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel1.setText("Port");

        jLabel2.setText("Baud");

        xCB_Baud.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "600", "1200", "2400", "4800", "9600", "14400", "19200", "38400", "56000", "57600", "115200" }));
        xCB_Baud.setSelectedIndex(4);

        jLabel3.setText("Data");

        xCB_DataLen.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "8", "7" }));

        xCB_Parity.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "none", "even", "odd", "mark" }));

        jLabel4.setText("Parity");

        xCB_HandShake.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "off", "CTS/RTS", "Xon/Xoff" }));

        jLabel5.setText("Handskake");

        xP_RXmodeSel.setBorder(javax.swing.BorderFactory.createTitledBorder("RX Mode"));

        buttonGroup1.add(xRB_RXmode_Ascii);
        xRB_RXmode_Ascii.setSelected(true);
        xRB_RXmode_Ascii.setText("Ascii");

        buttonGroup1.add(xRB_RXmode_Hex);
        xRB_RXmode_Hex.setText("Hex");

        buttonGroup1.add(xRB_RXmode_Mixed);
        xRB_RXmode_Mixed.setText("Mixed");

        javax.swing.GroupLayout xP_RXmodeSelLayout = new javax.swing.GroupLayout(xP_RXmodeSel);
        xP_RXmodeSel.setLayout(xP_RXmodeSelLayout);
        xP_RXmodeSelLayout.setHorizontalGroup(
            xP_RXmodeSelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xP_RXmodeSelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xRB_RXmode_Ascii)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xRB_RXmode_Hex)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xRB_RXmode_Mixed)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        xP_RXmodeSelLayout.setVerticalGroup(
            xP_RXmodeSelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xP_RXmodeSelLayout.createSequentialGroup()
                .addGroup(xP_RXmodeSelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xRB_RXmode_Ascii)
                    .addComponent(xRB_RXmode_Hex)
                    .addComponent(xRB_RXmode_Mixed))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        xP_TXmodeSel.setBorder(javax.swing.BorderFactory.createTitledBorder("TX Mode"));

        buttonGroup2.add(xRB_TXmode_Ascii);
        xRB_TXmode_Ascii.setSelected(true);
        xRB_TXmode_Ascii.setText("Ascii");

        buttonGroup2.add(xRB_TXmode_Hex);
        xRB_TXmode_Hex.setText("Hex");

        xCB_TXmode_Endl.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "CrLf", "Cr", "Lf" }));
        xCB_TXmode_Endl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xCB_TXmode_EndlActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout xP_TXmodeSelLayout = new javax.swing.GroupLayout(xP_TXmodeSel);
        xP_TXmodeSel.setLayout(xP_TXmodeSelLayout);
        xP_TXmodeSelLayout.setHorizontalGroup(
            xP_TXmodeSelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xP_TXmodeSelLayout.createSequentialGroup()
                .addComponent(xRB_TXmode_Hex)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xRB_TXmode_Ascii)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xCB_TXmode_Endl, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        xP_TXmodeSelLayout.setVerticalGroup(
            xP_TXmodeSelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xP_TXmodeSelLayout.createSequentialGroup()
                .addGroup(xP_TXmodeSelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xRB_TXmode_Ascii)
                    .addComponent(xRB_TXmode_Hex)
                    .addComponent(xCB_TXmode_Endl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        xB_OpenClose.setText("Port Open");
        xB_OpenClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xB_OpenCloseActionPerformed(evt);
            }
        });

        xP_ModemStatus.setBorder(javax.swing.BorderFactory.createTitledBorder("Modem Status"));

        xCK_DTR.setText("DTR");
        xCK_DTR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xCK_DTRActionPerformed(evt);
            }
        });

        xCK_RTS.setText("RTS");
        xCK_RTS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xCK_RTSActionPerformed(evt);
            }
        });

        xL_DSR.setBackground(new java.awt.Color(0, 255, 0));
        xL_DSR.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        xL_DSR.setText("DSR");
        xL_DSR.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        xL_CTS.setBackground(new java.awt.Color(0, 255, 0));
        xL_CTS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        xL_CTS.setText("CTS");
        xL_CTS.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        xL_RI.setBackground(new java.awt.Color(0, 255, 0));
        xL_RI.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        xL_RI.setText("RI");
        xL_RI.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        xL_RSLD.setBackground(new java.awt.Color(0, 255, 0));
        xL_RSLD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        xL_RSLD.setText("RSLD");
        xL_RSLD.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        xB_SendBreak.setText("Break");

        javax.swing.GroupLayout xP_ModemStatusLayout = new javax.swing.GroupLayout(xP_ModemStatus);
        xP_ModemStatus.setLayout(xP_ModemStatusLayout);
        xP_ModemStatusLayout.setHorizontalGroup(
            xP_ModemStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xP_ModemStatusLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(xP_ModemStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(xP_ModemStatusLayout.createSequentialGroup()
                        .addComponent(xL_DSR, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xL_CTS, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xL_RI, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xL_RSLD, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(xP_ModemStatusLayout.createSequentialGroup()
                        .addComponent(xCK_DTR)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xCK_RTS)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xB_SendBreak)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        xP_ModemStatusLayout.setVerticalGroup(
            xP_ModemStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xP_ModemStatusLayout.createSequentialGroup()
                .addGroup(xP_ModemStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xCK_DTR, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xCK_RTS)
                    .addComponent(xB_SendBreak))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(xP_ModemStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xL_DSR)
                    .addComponent(xL_CTS)
                    .addComponent(xL_RI)
                    .addComponent(xL_RSLD))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        xB_UpdatePorts.setText("Update Ports");
        xB_UpdatePorts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xB_UpdatePortsActionPerformed(evt);
            }
        });

        xT_CustBaud.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("0"))));
        xT_CustBaud.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N

        xCK_CustomBaud.setText("Custom Baud");

        jLabel9.setText("Stop Bits");

        xCB_StopBits.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "1.5", "2" }));

        javax.swing.GroupLayout xP_BasicSettingsLayout = new javax.swing.GroupLayout(xP_BasicSettings);
        xP_BasicSettings.setLayout(xP_BasicSettingsLayout);
        xP_BasicSettingsLayout.setHorizontalGroup(
            xP_BasicSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xP_BasicSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(xP_BasicSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xP_RXmodeSel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xP_TXmodeSel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xP_ModemStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 186, Short.MAX_VALUE)
                    .addGroup(xP_BasicSettingsLayout.createSequentialGroup()
                        .addGroup(xP_BasicSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(xP_BasicSettingsLayout.createSequentialGroup()
                                .addComponent(xB_OpenClose)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(xB_UpdatePorts))
                            .addGroup(xP_BasicSettingsLayout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(xCB_HandShake, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(xP_BasicSettingsLayout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(xCB_Parity, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(xP_BasicSettingsLayout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(xCB_DataLen, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(xP_BasicSettingsLayout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(xCB_Baud, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(xP_BasicSettingsLayout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(xCB_Port, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(xP_BasicSettingsLayout.createSequentialGroup()
                                .addComponent(xT_CustBaud, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(xCK_CustomBaud))
                            .addGroup(xP_BasicSettingsLayout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(xCB_StopBits, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        xP_BasicSettingsLayout.setVerticalGroup(
            xP_BasicSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xP_BasicSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(xP_BasicSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(xCB_Port, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(xP_BasicSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(xCB_Baud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(xP_BasicSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xT_CustBaud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xCK_CustomBaud))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(xP_BasicSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(xCB_StopBits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(xP_BasicSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(xCB_DataLen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(xP_BasicSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(xCB_Parity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(xP_BasicSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(xCB_HandShake, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xP_RXmodeSel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xP_TXmodeSel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(xP_BasicSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xB_OpenClose)
                    .addComponent(xB_UpdatePorts))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xP_ModemStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(108, Short.MAX_VALUE))
        );

        xTP_SettingPane.addTab("Basic Settings", xP_BasicSettings);

        xSP_IO.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        xSP_IO.setDividerLocation(340);
        xSP_IO.setDividerSize(8);
        xSP_IO.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        xSP_IO.setPreferredSize(new java.awt.Dimension(355, 575));

        xSCP_Rx.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        xSCP_Rx.setMinimumSize(new java.awt.Dimension(340, 320));
        xSCP_Rx.setPreferredSize(new java.awt.Dimension(340, 320));

        xTP_RX.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        xTP_RX.setMinimumSize(new java.awt.Dimension(315, 235));
        xTP_RX.setPreferredSize(new java.awt.Dimension(315, 330));
        xSCP_Rx.setViewportView(xTP_RX);

        xSP_IO.setTopComponent(xSCP_Rx);

        xSCP_Tx.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        xSCP_Tx.setMinimumSize(new java.awt.Dimension(340, 50));
        xSCP_Tx.setPreferredSize(new java.awt.Dimension(340, 235));

        xP_Tx.setMinimumSize(new java.awt.Dimension(315, 235));
        xP_Tx.setPreferredSize(new java.awt.Dimension(315, 235));

        xT_Data1.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N

        xB_Data1_Send.setText("Send");
        xB_Data1_Send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xB_Data1_SendActionPerformed(evt);
            }
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("Data");

        jLabel7.setText("HEX");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("EndLine");

        xT_Data2.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N

        xB_Data2_Send.setText("Send");
        xB_Data2_Send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xB_Data2_SendActionPerformed(evt);
            }
        });

        xB_Data3_Send.setText("Send");
        xB_Data3_Send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xB_Data3_SendActionPerformed(evt);
            }
        });

        xT_Data3.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N

        xB_Data4_Send.setText("Send");
        xB_Data4_Send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xB_Data4_SendActionPerformed(evt);
            }
        });

        xT_Data4.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N

        xCB_Seq.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Not Implemented" }));
        xCB_Seq.setEnabled(false);

        xB_LoadSeq.setText("Load Seq");
        xB_LoadSeq.setEnabled(false);

        xB_SendSingleSeq.setText("Send Single");
        xB_SendSingleSeq.setEnabled(false);

        xB_SendCompleteSeq.setText("Send Sequence");
        xB_SendCompleteSeq.setEnabled(false);

        javax.swing.GroupLayout xP_TxLayout = new javax.swing.GroupLayout(xP_Tx);
        xP_Tx.setLayout(xP_TxLayout);
        xP_TxLayout.setHorizontalGroup(
            xP_TxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xP_TxLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(xP_TxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(xP_TxLayout.createSequentialGroup()
                        .addGroup(xP_TxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(xP_TxLayout.createSequentialGroup()
                                .addComponent(xT_Data1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(xCK_Data1_Hex))
                            .addGroup(xP_TxLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7))
                            .addGroup(xP_TxLayout.createSequentialGroup()
                                .addComponent(xT_Data2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(xCK_Data2_Hex))
                            .addGroup(xP_TxLayout.createSequentialGroup()
                                .addComponent(xT_Data3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(xCK_Data3_Hex))
                            .addGroup(xP_TxLayout.createSequentialGroup()
                                .addComponent(xT_Data4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(xCK_Data4_Hex)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(xP_TxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(xP_TxLayout.createSequentialGroup()
                                .addComponent(xCK_Data1_Endl)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(xB_Data1_Send, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8)
                            .addGroup(xP_TxLayout.createSequentialGroup()
                                .addComponent(xCK_Data2_Endl)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(xB_Data2_Send, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(xP_TxLayout.createSequentialGroup()
                                .addComponent(xCK_Data3_Endl)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(xB_Data3_Send, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(xP_TxLayout.createSequentialGroup()
                                .addComponent(xCK_Data4_Endl)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(xB_Data4_Send, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(8, 8, 8))
                    .addGroup(xP_TxLayout.createSequentialGroup()
                        .addComponent(xCB_Seq, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xB_LoadSeq, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(xP_TxLayout.createSequentialGroup()
                        .addComponent(xB_SendSingleSeq)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xB_SendCompleteSeq)
                        .addGap(0, 122, Short.MAX_VALUE))))
        );
        xP_TxLayout.setVerticalGroup(
            xP_TxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xP_TxLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(xP_TxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(xCK_Data1_Hex, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(xP_TxLayout.createSequentialGroup()
                        .addGroup(xP_TxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xT_Data1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(xCK_Data1_Endl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xB_Data1_Send))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(xP_TxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(xCK_Data2_Hex, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xT_Data2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xCK_Data2_Endl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xB_Data2_Send))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(xP_TxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(xCK_Data3_Hex, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xT_Data3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xCK_Data3_Endl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xB_Data3_Send))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(xP_TxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(xCK_Data4_Hex, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xT_Data4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xCK_Data4_Endl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xB_Data4_Send))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(xP_TxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xCB_Seq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xB_LoadSeq))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(xP_TxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xB_SendSingleSeq)
                    .addComponent(xB_SendCompleteSeq))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        xSCP_Tx.setViewportView(xP_Tx);

        xSP_IO.setBottomComponent(xSCP_Tx);

        xMn_File.setText("File");
        xMb_Main.add(xMn_File);

        setJMenuBar(xMb_Main);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(xTP_SettingPane, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xSP_IO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(xSP_IO, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
                    .addComponent(xTP_SettingPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        xTP_SettingPane.getAccessibleContext().setAccessibleName("");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void xB_OpenCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xB_OpenCloseActionPerformed
        if (this.nso.isPortOpen)//Close the Port
        {
            // Enable Controls            
            this.v_DisableDataEnableControls();
            this.xB_OpenClose.setText("Port Open");
            try {
                if (this.nso.b_closePort()) {
                    NeelSerUI.LOG.log(Level.ALL, "Port {0} Closed",
                            this.nso.sPortName);
                    this.println("Port "
                            + this.nso.sPortName + " Closed",
                            Color.MAGENTA);
                }
            } catch (NeelSerException ser) {
                NeelSerUI.LOG.log(Level.SEVERE,
                        "Got Exception in Closing port: {0}", ser.toString());
                this.nso.isPortOpen = false;//Force Close
                this.println("Error: Could not Close Port "
                        + this.nso.sPortName, Color.RED);
            }
        } else //Open the Port
        {
            // Interpret Values
            this.v_InterpretValues();
            // Get the Current Selected Port
            this.nso.iSelectedPortIndex = this.xCB_Port.getSelectedIndex();
            try {
                if (this.nso.b_openport(this)) {
                    NeelSerUI.LOG.log(Level.ALL, "Port {0} opened",
                            this.nso.sPortName);
                    this.println("Port "
                            + this.nso.sPortName + " Opened",
                            Color.MAGENTA);
                    // Disable All controls and change the Name
                    this.v_EnableDataDisableControls();
                    this.xB_OpenClose.setText("Port Close");
                }
            } catch (NeelSerException ser) {
                NeelSerUI.LOG.log(Level.SEVERE,
                        "Got Exception in Opening port: {0}", ser.toString());
                this.println("Error: Could not Open Port "
                        + this.nso.sPortName, Color.RED);
            }
        }
    }//GEN-LAST:event_xB_OpenCloseActionPerformed

    private void xCK_DTRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xCK_DTRActionPerformed
        if (this.nso.isPortOpen) {
            try {
                this.nso.b_DTR(this.xCK_DTR.isSelected());
            } catch (NeelSerException ser) {
                NeelSerUI.LOG.log(Level.SEVERE,
                        "Got Exception in Changing DTR state: {0}", ser.toString());
                this.println("Error: Could not change DTR state "
                        + this.xCK_DTR.isSelected(), Color.RED);
            }
        }
    }//GEN-LAST:event_xCK_DTRActionPerformed

    private void xCK_RTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xCK_RTSActionPerformed
        if (this.nso.isPortOpen) {
            try {
                this.nso.b_RTS(this.xCK_RTS.isSelected());
            } catch (NeelSerException ser) {
                NeelSerUI.LOG.log(Level.SEVERE,
                        "Got Exception in Changing RTS state: {0}", ser.toString());
                this.println("Error: Could not change RTS state "
                        + this.xCK_DTR.isSelected(), Color.RED);
            }
        }
    }//GEN-LAST:event_xCK_RTSActionPerformed

    private void xB_Data1_SendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xB_Data1_SendActionPerformed
        String s = this.xT_Data1.getText();
        if (s.length() > 0) {
            // Process the Data
            try {
                s = this.processSendData(s,
                        this.xCK_Data1_Hex.isSelected(),
                        this.xCK_Data1_Endl.isSelected());
            } catch (Exception ex) {
                NeelSerUI.LOG.log(Level.SEVERE, "Got Exception Processing data [1]: {0}", ex.toString());
                this.println("Error: Could not Process Data[1] ", Color.RED);
                return;
            }
            // Send the Data
            try {
                this.nso.b_SendBytes(s.getBytes());
            } catch (NeelSerException ser) {
                NeelSerUI.LOG.log(Level.SEVERE, "Got Exception Sending data [1]: {0}", ser.toString());
                this.println("Error: Could Send Data[1] ", Color.RED);
            }
        }
    }//GEN-LAST:event_xB_Data1_SendActionPerformed

    private void xB_Data2_SendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xB_Data2_SendActionPerformed
        String s = this.xT_Data2.getText();
        if (s.length() > 0) {
            // Process the Data
            try {
                s = this.processSendData(s,
                        this.xCK_Data2_Hex.isSelected(),
                        this.xCK_Data2_Endl.isSelected());
            } catch (Exception ex) {
                NeelSerUI.LOG.log(Level.SEVERE, "Got Exception Processing data [2]: {0}", ex.toString());
                this.println("Error: Could not Process Data[2] ", Color.RED);
                return;
            }
            // Send the Data
            try {
                this.nso.b_SendBytes(s.getBytes());
            } catch (NeelSerException ser) {
                NeelSerUI.LOG.log(Level.SEVERE, "Got Exception Sending data [2]: {0}", ser.toString());
                this.println("Error: Could Send Data[2] ", Color.RED);
            }
        }
    }//GEN-LAST:event_xB_Data2_SendActionPerformed

    private void xB_Data3_SendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xB_Data3_SendActionPerformed
        String s = this.xT_Data3.getText();
        if (s.length() > 0) {
            // Process the Data
            try {
                s = this.processSendData(s,
                        this.xCK_Data3_Hex.isSelected(),
                        this.xCK_Data3_Endl.isSelected());
            } catch (Exception ex) {
                NeelSerUI.LOG.log(Level.SEVERE, "Got Exception Processing data [3]: {0}", ex.toString());
                this.println("Error: Could not Process Data[3] ", Color.RED);
                return;
            }
            // Send the Data
            try {
                this.nso.b_SendBytes(s.getBytes());
            } catch (NeelSerException ser) {
                NeelSerUI.LOG.log(Level.SEVERE, "Got Exception Sending data [3]: {0}", ser.toString());
                this.println("Error: Could Send Data[3] ", Color.RED);
            }
        }
    }//GEN-LAST:event_xB_Data3_SendActionPerformed

    private void xB_Data4_SendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xB_Data4_SendActionPerformed
        String s = this.xT_Data4.getText();
        if (s.length() > 0) {
            // Process the Data
            try {
                s = this.processSendData(s,
                        this.xCK_Data4_Hex.isSelected(),
                        this.xCK_Data4_Endl.isSelected());
            } catch (Exception ex) {
                NeelSerUI.LOG.log(Level.SEVERE, "Got Exception Processing data [4]: {0}", ex.toString());
                this.println("Error: Could not Process Data[4] ", Color.RED);
                return;
            }
            // Send the Data
            try {
                this.nso.b_SendBytes(s.getBytes());
            } catch (NeelSerException ser) {
                NeelSerUI.LOG.log(Level.SEVERE, "Got Exception Sending data [4]: {0}", ser.toString());
                this.println("Error: Could Send Data[4] ", Color.RED);
            }
        }
    }//GEN-LAST:event_xB_Data4_SendActionPerformed

    private void xCB_TXmode_EndlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xCB_TXmode_EndlActionPerformed
        if ("Cr".equalsIgnoreCase(this.xCB_TXmode_Endl.getSelectedItem().toString())) {
            this.sEndl = "\r";
        } else if ("Lf".equalsIgnoreCase(this.xCB_TXmode_Endl.getSelectedItem().toString())) {
            this.sEndl = "\n";
        } else { //Default Case
            this.sEndl = "\r\n";
        }
    }//GEN-LAST:event_xCB_TXmode_EndlActionPerformed

    private void xB_UpdatePortsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xB_UpdatePortsActionPerformed
        this.v_updatePortList();
    }//GEN-LAST:event_xB_UpdatePortsActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if(this.nso.isPortOpen)
            this.xB_OpenClose.doClick();
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JButton xB_Data1_Send;
    private javax.swing.JButton xB_Data2_Send;
    private javax.swing.JButton xB_Data3_Send;
    private javax.swing.JButton xB_Data4_Send;
    private javax.swing.JButton xB_LoadSeq;
    private javax.swing.JButton xB_OpenClose;
    private javax.swing.JButton xB_SendBreak;
    private javax.swing.JButton xB_SendCompleteSeq;
    private javax.swing.JButton xB_SendSingleSeq;
    private javax.swing.JButton xB_UpdatePorts;
    private javax.swing.JComboBox xCB_Baud;
    private javax.swing.JComboBox xCB_DataLen;
    private javax.swing.JComboBox xCB_HandShake;
    private javax.swing.JComboBox xCB_Parity;
    private javax.swing.JComboBox xCB_Port;
    private javax.swing.JComboBox xCB_Seq;
    private javax.swing.JComboBox xCB_StopBits;
    private javax.swing.JComboBox xCB_TXmode_Endl;
    private javax.swing.JCheckBox xCK_CustomBaud;
    private javax.swing.JCheckBox xCK_DTR;
    private javax.swing.JCheckBox xCK_Data1_Endl;
    private javax.swing.JCheckBox xCK_Data1_Hex;
    private javax.swing.JCheckBox xCK_Data2_Endl;
    private javax.swing.JCheckBox xCK_Data2_Hex;
    private javax.swing.JCheckBox xCK_Data3_Endl;
    private javax.swing.JCheckBox xCK_Data3_Hex;
    private javax.swing.JCheckBox xCK_Data4_Endl;
    private javax.swing.JCheckBox xCK_Data4_Hex;
    private javax.swing.JCheckBox xCK_RTS;
    private javax.swing.JLabel xL_CTS;
    private javax.swing.JLabel xL_DSR;
    private javax.swing.JLabel xL_RI;
    private javax.swing.JLabel xL_RSLD;
    private javax.swing.JMenuBar xMb_Main;
    private javax.swing.JMenu xMn_File;
    private javax.swing.JPanel xP_BasicSettings;
    private javax.swing.JPanel xP_ModemStatus;
    private javax.swing.JPanel xP_RXmodeSel;
    private javax.swing.JPanel xP_TXmodeSel;
    private javax.swing.JPanel xP_Tx;
    private javax.swing.JRadioButton xRB_RXmode_Ascii;
    private javax.swing.JRadioButton xRB_RXmode_Hex;
    private javax.swing.JRadioButton xRB_RXmode_Mixed;
    private javax.swing.JRadioButton xRB_TXmode_Ascii;
    private javax.swing.JRadioButton xRB_TXmode_Hex;
    private javax.swing.JScrollPane xSCP_Rx;
    private javax.swing.JScrollPane xSCP_Tx;
    private javax.swing.JSplitPane xSP_IO;
    private javax.swing.JTextPane xTP_RX;
    private javax.swing.JTabbedPane xTP_SettingPane;
    private javax.swing.JFormattedTextField xT_CustBaud;
    private javax.swing.JTextField xT_Data1;
    private javax.swing.JTextField xT_Data2;
    private javax.swing.JTextField xT_Data3;
    private javax.swing.JTextField xT_Data4;
    // End of variables declaration//GEN-END:variables
}
