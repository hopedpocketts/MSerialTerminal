/**
 * jNeelSer - Java Full loaded Serial Port Terminal
 *
 */
package com.adharlabs.jNeelSer;

import com.adharlabs.UI.VisualTerminal;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 *
 * @author bose
 */
public class Exe {

    @SuppressWarnings("NonConstantLogger")
    private static Logger LOG;
    /*private NeelSerOptions opt;
    private JRadioButtonMenuItem[] arck_port;
    private VisualTerminal vs;
    private JMenuItem jmi_OpenClose;
    private StringBuilder sRxStream;*/

    /**
     * Static block to Configure the logger by default
     */
    static {
        try {
            com.adharlabs.log.LogCustomHandler.setup(Exe.class.getName());
        } catch (IOException ex) {
            System.err.println("Error could not intialize the logger:\n"
                    + ex.toString());
            System.exit(1);
        }
    }
/*
    private int getSelectPort() {
        int i;
        for (i = 0; i < arck_port.length; i++) {
            if (arck_port[i].isSelected()) {
                return i;
            }
        }
        return -1;
    }

    private void Action_setPortName(ActionEvent e) {
        String s = ((javax.swing.AbstractButton) e.getSource()).getText();
        if (opt.indexOfPortName(s) != -1 && (!opt.isPortOpen)) {
            //Set the port Index as per the string
            opt.iSelectedPortIndex = opt.indexOfPortName(s);
        } else {
            //Restore the Older Selection
            for (int i = 0; i < arck_port.length; i++) {
                if (i == opt.iSelectedPortIndex) {
                    arck_port[i].setSelected(true);
                }
            }
        }
    }

    private void Action_serialevent(SerialPortEvent se) {
        if (se.isRXCHAR() || se.isRXFLAG()) {
            String s;
            try {
                s = opt.xPort.readString();
                if (s != null) {
                    if (s.indexOf("\b") != -1) {
                        try {
                            if (vs.styledoc.getLength() > 0) {
                                vs.styledoc.remove(vs.styledoc.getLength() - 1, 1);
                                //Fix Set the default style at the last char location
                                SimpleAttributeSet aset = new SimpleAttributeSet();
                                StyleConstants.setForeground(aset, Color.black);
                                StyleConstants.setBackground(aset, Color.WHITE);
                                vs.jtextpane.setCharacterAttributes(aset, false);
                            }
                        } catch (BadLocationException ex) {
                            Exe.LOG.log(Level.SEVERE, "Error: ", ex);
                        } catch (Exception ex) {
                            Exe.LOG.log(Level.SEVERE, "Error: ", ex);
                        }
                    } else {
                        try {
                            vs.styledoc.insertString(vs.styledoc.getLength(), s, null);
                        } catch (BadLocationException ex) {
                            
                        }
                    }
                    if (opt.param_bEnableLoopBack) {
                        opt.xPort.writeString(s);
                    }
                    vs.gotoend();
                }
            } catch (SerialPortException ex) {
                Exe.LOG.log(Level.SEVERE, "Problem in Receiving data", ex);
            }
        }
    }

    private void Action_openclose(ActionEvent e) {
        if (getSelectPort() != -1) {
            if (!opt.isPortOpen) {
                opt.iSelectedPortIndex = getSelectPort();
                try {
                    opt.openPort();
                    jmi_OpenClose.setText("Close");
                    vs.println("Opened Port: " + opt.sPortName, Color.MAGENTA);
                    opt.xPort.addEventListener(new SerialPortEventListener() {
                        @Override
                        public void serialEvent(SerialPortEvent serialPortEvent) {
                            Action_serialevent(serialPortEvent);
                        }
                    },
                            SerialPort.MASK_RXCHAR | SerialPort.MASK_RXFLAG);
                } catch (Exception ex) {
                    Exe.LOG.log(Level.INFO, "Port Open Failed\n{0}", ex.toString());
                }
            } else {
                try {
                    opt.closePort();
                    jmi_OpenClose.setText("Open");
                    vs.println("Closed Port: " + opt.sPortName, Color.MAGENTA);
                } catch (Exception ex) {
                    Exe.LOG.log(Level.INFO, "Port Close Failed \n{0}", ex);
                }
            }
        }
    }
*/
    public Exe() {
        // Initialize the Local logger
        Exe.LOG = com.adharlabs.log.LogCustomHandler.LOG;
        Exe.LOG.log(Level.INFO, "Begin Exe");
        Exe.LOG.setLevel(Level.ALL);//Enable Debug Logs

 /*       // Create the Visual Terminal
        vs = new VisualTerminal(400, 600, "jNeelSer",
                Exe.LOG);
        opt = new NeelSerOptions(Exe.LOG);
        sRxStream = new StringBuilder();

        //Create Menu Bar
        JMenuBar jmb = new JMenuBar();
        //Add Menu Bar to the Visual Terminal
        vs.setJMenuBar(jmb);

        //Create Port List Setting
        JMenu jPort = new JMenu("Port Sel");
        jmb.add(jPort);
        ButtonGroup bg1 = new ButtonGroup();
        try {
            arck_port = opt.refreshPortList_menu();
            jPort.removeAll();
            for (int i = 0; i < arck_port.length; i++) {
                jPort.add(arck_port[i]);
                bg1.add(arck_port[i]);
                if (i == opt.iSelectedPortIndex) {
                    arck_port[i].setSelected(true);
                }
                arck_port[i].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Action_setPortName(e);
                    }
                });
            }
        } catch (Exception e) {
            vs.println(e.toString(), Color.red);
        }

        jmi_OpenClose = new JMenuItem("Open");
        jmi_OpenClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Action_openclose(e);
            }
        });
        jmb.add(jmi_OpenClose);
        KeyStroke xks_Send = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,
                java.awt.event.InputEvent.CTRL_DOWN_MASK);
        vs.jtextpane.getInputMap().put(xks_Send, "actionMapKey");

        vs.jtextpane.getActionMap().put("actionMapKey",
                new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (opt.isPortOpen) {
                    vs.println();//Go to a New Line To indicate Sending
                    //Get the Final Buffer
                    String s = sRxStream.toString().trim() + "\r\n";
                    // Message fir the Text being sent
                    vs.println("Sending Text: " + s, Color.PINK);

                    try {
                        //Attempt to Really send the string
                        opt.xPort.writeString(s);
                        //Clear the Buffer
                        sRxStream.delete(0, sRxStream.capacity());
                    } catch (SerialPortException ex) {
                        Exe.LOG.log(Level.SEVERE, "Failed to send String: "
                                + s + "\n{0}", ex);
                    }
                }
            }
        });

        vs.jtextpane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char ch = (char) e.getKeyChar();
                // Only add incase any special keys are not pressed
                if (!e.isAltDown() && !e.isAltGraphDown()
                        && !e.isControlDown() && !e.isMetaDown()
                        && opt.isPortOpen) {
                    if ((ch >= 32) && (ch < 127)) {
                        sRxStream.append(ch);
                    } else if (ch == '\n') {
                        sRxStream.append('\n');
                    }
                }
            }
        });
        //opt.param_bEnableLoopBack = true;
        opt.iBaudrate = SerialPort.BAUDRATE_115200;
        vs.jtextpane.setAutoscrolls(true);
        // Start Execution
        vs.setVisible(true);*/
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Exe exe;
        exe = new Exe();
    }
}
