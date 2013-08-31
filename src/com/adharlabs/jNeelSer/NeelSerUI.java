/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adharlabs.jNeelSer;

import com.adharlabs.UI.StringWithVisualStyle;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import com.adharlabs.UI.ThreadedTextPaneHandler;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * jNeelSer Serial User Interface
 *
 * @version 1.0_20130625
 * @author Abhijit Bose , A.D.H.A.R Labs Research, Bharat(India)
 */
public class NeelSerUI extends javax.swing.JFrame implements INeelSerialInterface {

    public final String sProduct = "jNeelSerial";
    public final String sCompany = "A.D.H.A.R Labs Research, Bharat(India)";
    @SuppressWarnings("NonConstantLogger")
    private static Logger LOG;
    private NeelSerOptions nso;
    private BlockingQueue<StringWithVisualStyle> RxDispQue;
    private ThreadedTextPaneHandler RxPaneHandler;
    private Thread RxPaneThread;

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

        // Print Some Messages
        this.println("Initialization Done! ... let the fun begin",Color.BLUE);
    }

    /**
     * Function to update the Port List Combo box
     */
    private void v_updatePortList() {
        // Refresh Port List
        this.nso.v_refreshPortList();
        this.xCB_Port.removeAllItems();
        for (Object object : nso.arsPortList) {
            this.xCB_Port.addItem(object);
        }
        this.xCB_Port.addItem("Update");
    }

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
        this.print("\n", Color.BLACK, Color.WHITE);
    }

    /**
     *
     * @param s
     */
    private void println(String s) {
        this.print(s, Color.BLACK);
        this.println();
    }

    /**
     *
     * @param s
     * @param fgColor
     */
    private void println(String s, Color fgColor) {
        this.print(s, fgColor);
        this.println();
    }

    /**
     *
     * @param s
     * @param fgColor
     * @param bgColor
     */
    private void println(String s, Color fgColor, Color bgColor) {
        this.print(s, fgColor, bgColor);
        this.println();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Neel Serial Interface Overrides">
    @Override
    public void gotRxData(byte[] arb) {
        if (this.nso.isPortOpen) {
            this.println(new String(arb));
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
        this.xL_DSR.setOpaque(state);
    }

    @Override
    public void changeRI(boolean state) {
        this.xL_RI.setOpaque(state);
    }

    @Override
    public void changeCTS(boolean state) {
        this.xL_CTS.setOpaque(state);
    }

    @Override
    public void changeRLSD(boolean state) {
        this.xL_RSLD.setOpaque(state);
    }

    @Override
    public void gotBreak(boolean state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notifyError(eNeelSerialStatus lvl, String from, String error) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        xRB_TXmode_Endl = new javax.swing.JComboBox();
        xB_OpenClose = new javax.swing.JButton();
        xP_ModemStatus = new javax.swing.JPanel();
        xCK_DTR = new javax.swing.JCheckBox();
        xCK_RTS = new javax.swing.JCheckBox();
        xL_DSR = new javax.swing.JLabel();
        xL_CTS = new javax.swing.JLabel();
        xL_RI = new javax.swing.JLabel();
        xL_RSLD = new javax.swing.JLabel();
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

        xTP_SettingPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        xTP_SettingPane.setPreferredSize(new java.awt.Dimension(580, 200));

        xP_BasicSettings.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel1.setText("Port");

        xCB_Port.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xCB_PortActionPerformed(evt);
            }
        });

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

        xRB_TXmode_Endl.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "CrLf", "Cr", "Lf" }));

        javax.swing.GroupLayout xP_TXmodeSelLayout = new javax.swing.GroupLayout(xP_TXmodeSel);
        xP_TXmodeSel.setLayout(xP_TXmodeSelLayout);
        xP_TXmodeSelLayout.setHorizontalGroup(
            xP_TXmodeSelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xP_TXmodeSelLayout.createSequentialGroup()
                .addComponent(xRB_TXmode_Hex)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xRB_TXmode_Ascii)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xRB_TXmode_Endl, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        xP_TXmodeSelLayout.setVerticalGroup(
            xP_TXmodeSelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xP_TXmodeSelLayout.createSequentialGroup()
                .addGroup(xP_TXmodeSelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xRB_TXmode_Ascii)
                    .addComponent(xRB_TXmode_Hex)
                    .addComponent(xRB_TXmode_Endl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        xCK_RTS.setText("RTS");

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

        javax.swing.GroupLayout xP_ModemStatusLayout = new javax.swing.GroupLayout(xP_ModemStatus);
        xP_ModemStatus.setLayout(xP_ModemStatusLayout);
        xP_ModemStatusLayout.setHorizontalGroup(
            xP_ModemStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xP_ModemStatusLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(xP_ModemStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(xP_ModemStatusLayout.createSequentialGroup()
                        .addComponent(xL_DSR, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(xL_CTS, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(xL_RI, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, xP_ModemStatusLayout.createSequentialGroup()
                        .addComponent(xCK_DTR)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(xCK_RTS)
                        .addGap(18, 18, 18)
                        .addComponent(xL_RSLD, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        xP_ModemStatusLayout.setVerticalGroup(
            xP_ModemStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xP_ModemStatusLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(xP_ModemStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xCK_DTR, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xCK_RTS)
                    .addComponent(xL_RSLD))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(xP_ModemStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xL_DSR)
                    .addComponent(xL_CTS)
                    .addComponent(xL_RI))
                .addContainerGap())
        );

        javax.swing.GroupLayout xP_BasicSettingsLayout = new javax.swing.GroupLayout(xP_BasicSettings);
        xP_BasicSettings.setLayout(xP_BasicSettingsLayout);
        xP_BasicSettingsLayout.setHorizontalGroup(
            xP_BasicSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xP_BasicSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(xP_BasicSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xP_RXmodeSel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xP_TXmodeSel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(xP_BasicSettingsLayout.createSequentialGroup()
                        .addGroup(xP_BasicSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(xP_BasicSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(xP_BasicSettingsLayout.createSequentialGroup()
                                    .addGroup(xP_BasicSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, xP_BasicSettingsLayout.createSequentialGroup()
                                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                                        .addGroup(xP_BasicSettingsLayout.createSequentialGroup()
                                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(8, 8, 8)))
                                    .addGroup(xP_BasicSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(xCB_Baud, 0, 152, Short.MAX_VALUE)
                                        .addComponent(xCB_Port, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(xP_BasicSettingsLayout.createSequentialGroup()
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(xCB_DataLen, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(xP_BasicSettingsLayout.createSequentialGroup()
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(xCB_Parity, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, xP_BasicSettingsLayout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(xCB_HandShake, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(xB_OpenClose))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(xP_ModemStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        xP_BasicSettingsLayout.setVerticalGroup(
            xP_BasicSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xP_BasicSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(xP_BasicSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(xCB_Port, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(xP_BasicSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(xCB_Baud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(xP_BasicSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(xCB_DataLen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(xP_BasicSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(xCB_Parity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(xP_BasicSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(xCB_HandShake, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xP_RXmodeSel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xP_TXmodeSel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xB_OpenClose)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xP_ModemStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(131, Short.MAX_VALUE))
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

        xTP_RX.setMinimumSize(new java.awt.Dimension(315, 235));
        xTP_RX.setPreferredSize(new java.awt.Dimension(315, 330));
        xSCP_Rx.setViewportView(xTP_RX);

        xSP_IO.setTopComponent(xSCP_Rx);

        xSCP_Tx.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        xSCP_Tx.setMinimumSize(new java.awt.Dimension(340, 50));
        xSCP_Tx.setPreferredSize(new java.awt.Dimension(340, 235));

        xP_Tx.setMinimumSize(new java.awt.Dimension(315, 235));
        xP_Tx.setPreferredSize(new java.awt.Dimension(315, 235));

        xB_Data1_Send.setText("Send");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("Data");

        jLabel7.setText("HEX");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("EndLine");

        xB_Data2_Send.setText("Send");

        xB_Data3_Send.setText("Send");

        xB_Data4_Send.setText("Send");

        xCB_Seq.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Not Implemented" }));

        xB_LoadSeq.setText("Load Seq");

        xB_SendSingleSeq.setText("Send Single");

        xB_SendCompleteSeq.setText("Send Sequence");

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
            this.xCB_Port.setEnabled(true);
            this.xCB_Baud.setEnabled(true);
            this.xCB_DataLen.setEnabled(true);
            this.xCB_Parity.setEnabled(true);
            this.xCB_HandShake.setEnabled(true);
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
                // Refresh Port List
                this.v_updatePortList();
            }
        } else //Open the Port
        {
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
                    this.xCB_Port.setEnabled(false);
                    this.xCB_Baud.setEnabled(false);
                    this.xCB_DataLen.setEnabled(false);
                    this.xCB_Parity.setEnabled(false);
                    this.xCB_HandShake.setEnabled(false);
                    this.xB_OpenClose.setText("Port Close");
                }
            } catch (NeelSerException ser) {
                NeelSerUI.LOG.log(Level.SEVERE,
                        "Got Exception in Opening port: {0}", ser.toString());
                this.println("Error: Could not Open Port "
                        + this.nso.sPortName, Color.RED);
                // Refresh Port List
                this.v_updatePortList();
            }
        }
    }//GEN-LAST:event_xB_OpenCloseActionPerformed

    private void xCB_PortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xCB_PortActionPerformed
        if((!this.nso.isPortOpen)&&
                "Update".equals(xCB_Port.getSelectedItem().toString()))
        {
            this.v_updatePortList();
            this.println("PortList updated",Color.BLUE);
        }
    }//GEN-LAST:event_xCB_PortActionPerformed

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
    private javax.swing.JButton xB_Data1_Send;
    private javax.swing.JButton xB_Data2_Send;
    private javax.swing.JButton xB_Data3_Send;
    private javax.swing.JButton xB_Data4_Send;
    private javax.swing.JButton xB_LoadSeq;
    private javax.swing.JButton xB_OpenClose;
    private javax.swing.JButton xB_SendCompleteSeq;
    private javax.swing.JButton xB_SendSingleSeq;
    private javax.swing.JComboBox xCB_Baud;
    private javax.swing.JComboBox xCB_DataLen;
    private javax.swing.JComboBox xCB_HandShake;
    private javax.swing.JComboBox xCB_Parity;
    private javax.swing.JComboBox xCB_Port;
    private javax.swing.JComboBox xCB_Seq;
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
    private javax.swing.JComboBox xRB_TXmode_Endl;
    private javax.swing.JRadioButton xRB_TXmode_Hex;
    private javax.swing.JScrollPane xSCP_Rx;
    private javax.swing.JScrollPane xSCP_Tx;
    private javax.swing.JSplitPane xSP_IO;
    private javax.swing.JTextPane xTP_RX;
    private javax.swing.JTabbedPane xTP_SettingPane;
    private javax.swing.JTextField xT_Data1;
    private javax.swing.JTextField xT_Data2;
    private javax.swing.JTextField xT_Data3;
    private javax.swing.JTextField xT_Data4;
    // End of variables declaration//GEN-END:variables
}
