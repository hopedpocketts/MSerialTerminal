/* jNeelSer Option and Data Storage class
 * This class encloses the data related to the serial port and
 * data interation options. It also provides a wrapper for the
 * Serial port related functions
 * @copy © Abhijit Bose , A.D.H.A.R Labs Research Bharat(India), 2013.
 *
 * NeelSerOptions is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NeelSerOptions is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jNeelSer.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @email info@adharlabs.in
 * @website http://adharlabs.in
 *
 */
package com.adharlabs.jNeelSer;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JRadioButtonMenuItem;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 * jNeelSer Option and Data Storage class
 *
 * @version 1.0_20130625
 * @author Abhijit Bose , A.D.H.A.R Labs Research Bharat(India)
 */
public class NeelSerOptions {

    /**
     *
     */
    public SerialPort xPort;
    /**
     *
     */
    public boolean isPortOpen = false;
    /**
     *
     */
    public String[] arsPortList;
    /**
     *
     */
    public int iSelectedPortIndex;
    /**
     *
     */
    public String sPortName;
    /**
     *
     */
    public int iBaudrate;
    /**
     *
     */
    public int iParity;
    /**
     *
     */
    public int iStopBits;
    /**
     *
     */
    public int iDataBits;
    /**
     *
     */
    public int iFlowControl;
    /**
     *
     */
    public boolean bCTS;
    /**
     *
     */
    public boolean bRTS;
    /**
     *
     */
    public boolean bDSR;
    /**
     *
     */
    public boolean bDTR;
    /**
     *
     */
    public boolean bRI;
    /**
     *
     */
    public boolean bRLSD;
    /**
     *
     */
    public boolean param_bEnableLoopBack;
    /**
     *
     */
    public boolean param_bQueueCommunications;
    //<editor-fold defaultstate="collapsed" desc="Queues for User application Communication">
    /**
     *
     */
    public BlockingQueue<byte[]> xRxQueue;
    /**
     *
     */
    public BlockingQueue<byte[]> xTxQueue;
    /**
     *
     */
    public BlockingQueue<Boolean> xbCTSqueue;
    /**
     *
     */
    public BlockingQueue<Boolean> xbDSRqueue;
    /**
     *
     */
    public BlockingQueue<Boolean> xbRIqueue;
    /**
     *
     */
    public BlockingQueue<Boolean> xbRLSDqueue;
    public BlockingQueue<Boolean> xbRxBreakqueue;
    /**
     *
     */
    public BlockingQueue<eNeelSerialStatus> xErrorqueue;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Interface for User Application Communication">
    /**
     *
     */
    public INeelSerialInterface iface;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Old Thread Implementation now not used">
    /*private class TxThread implements Runnable {
     * 
     * private final SerialPort xSerial;
     * private final Logger LOG;
     * private final BlockingQueue<Byte> que;
     * public final Thread th;
     * private boolean en = false;
     * 
     * public TxThread(Logger log, SerialPort ser, BlockingQueue<Byte> xbk) {
     * this.xSerial = ser;
     * this.LOG = log;
     * this.que = xbk;
     * this.th = new Thread(this);
     * this.LOG.finer("TxThread Initialized");
     * }
     * 
     * public void Start() {
     * this.en = true;
     * this.th.start();
     * this.LOG.finer("TxThread Started");
     * }
     * 
     * public void Stop() {
     * this.en = false;
     * this.th.interrupt();
     * this.LOG.finer("TxThread Interrupted");
     * }
     * 
     * @Override
     * public void run() {
     * Byte dt;
     * while (en) {
     * synchronized (this.que) {
     * try {
     * dt = this.que.take();
     * try {
     * if (xSerial != null) {
     * if (xSerial.isOpened()) {
     * this.xSerial.writeByte(dt);
     * }
     * }
     * } catch (SerialPortException ex) {
     * this.LOG.log(Level.SEVERE,
     * "Error in Sending data byte:" + dt.intValue()
     * + "\n Exception: ", ex.toString());
     * }
     * } catch (InterruptedException ex) {
     * this.LOG.log(Level.WARNING,
     * "Thread Interrupted in middle of operation "
     * + "\n Exception: {0}", ex.toString());
     * }
     * }
     * 
     * }
     * this.LOG.finer("Finished TxThread");
     * }
     * }*/
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Constuctor">
    /**
     * Constructor
     *
     * @note If Queue based is needed then manual initialization must be
     * performed all the queues. Also presently not much of Queue based
     * functionality is implemented
     * @param bInterfaceBased True if user application binds using Interface.
     * False if user application directly uses the queues.
     * @param log Logging class used internally
     */
    public NeelSerOptions(boolean bInterfaceBased, Logger log) {
        // Assign the Primary Things
        NeelSerOptions.LOG = log;

        // Initialize the Parameters
        this.arsPortList = SerialPortList.getPortNames();
        this.sPortName = this.arsPortList[0];
        this.iSelectedPortIndex = 0;
        this.isPortOpen = false;
        this.iBaudrate = SerialPort.BAUDRATE_9600;
        this.iParity = SerialPort.PARITY_NONE;
        this.iStopBits = SerialPort.STOPBITS_1;
        this.iDataBits = SerialPort.DATABITS_8;
        this.iFlowControl = SerialPort.FLOWCONTROL_NONE;
        this.bCTS = false;
        this.bRTS = false;
        this.bDSR = false;
        this.bDTR = false;
        this.bRI = false;
        this.bRLSD = false;
        this.param_bEnableLoopBack = false;
        this.iface = null;
        this.xErrorqueue = null;
        this.xRxQueue = null;
        this.xTxQueue = null;
        this.xbCTSqueue = null;
        this.xbDSRqueue = null;
        this.xbRIqueue = null;
        this.xbRLSDqueue = null;
        this.xbRxBreakqueue = null;
        // Check if initialization for Interface based implementations
        if (bInterfaceBased) {
            //Enable Interface based communications
            this.param_bQueueCommunications = false;
        } else { //Or Queue based implementations
            //Enable Queue based communications
            this.param_bQueueCommunications = true;
        }

        //Finally done
        NeelSerOptions.LOG.fine("Done Initialization");
    }
    //</editor-fold>

    /**
     * Interfaced based Open Port function use to perform the following
     * <ul><li>Initialize the Port parameters</li>
     * <li>Open the serial port</li>
     * <li>Clear the port</li>
     * <li>Add the User Application binding for events</li></ul>
     *
     * @note iSelectedPortIndex is used to initialize the port name so this
     * needs to be updated before calling port open
     *
     * @param iface Interface used to transact with the User application
     * @return True if all initialization was alright. Else in case the port was
     * already opened then returns False
     * @throws NeelSerException Throws exception in case of the following causes
     * <ul><li>Wrong user application binding type - in case the initialization
     * was for a Interface base implementation</li>
     * <li>If the interface supplied is Null</li>
     * <li>If port open operation fails</li>
     * <li>If parameter setting operation fails</li>
     * <li>If Flow control setting operation fails</li>
     * <li>If purging operation fails</li>
     * <li>If the Registration of the Serial Events fails</li></ul>
     */
    public boolean b_openport(
            INeelSerialInterface iface) throws NeelSerException {
        // In Future add Semaphore Lock (Relese Sem in Exceptions)

        //Check if port is already opened
        if (this.isPortOpen) {
            NeelSerOptions.LOG.log(Level.FINE, "Port Already Opened, Need to"
                    + " close the port first Index:{0}", /*index*/
                    this.iSelectedPortIndex);
            return false;
        }

        // Check if the correct function is called
        if (this.param_bQueueCommunications) {
            String s = "Error: Wrong function called - "
                    + "need to call Queue based function ";
            NeelSerOptions.LOG.log(Level.WARNING, s);
            throw new NeelSerException(eNeelSerialStatus.ERROR_GEN, s);
        }

        //Check for the Interface to be Null
        if (iface == null) {
            String s = "Error: Interface Supplied was Null";
            NeelSerOptions.LOG.log(Level.WARNING, s);
            throw new NeelSerException(eNeelSerialStatus.ERROR_GEN, s);
        }

        //Get Interface
        this.iface = iface;

        // Get the serial port name using the index
        try {
            this.sPortName = this.arsPortList[this.iSelectedPortIndex];
        } catch (ArrayIndexOutOfBoundsException ex1) {
            String s = "Access to Port list array caused "
                    + "issue due to invalid index("
                    + this.iSelectedPortIndex + ")\n" + ex1.toString();
            NeelSerOptions.LOG.log(Level.WARNING, s);
            throw new NeelSerException(
                    eNeelSerialStatus.ERROR_PORTLISTARRAY_ACCESS, s);
        }

        synchronized (this) {
            //Create the Serial Port
            this.xPort = new SerialPort(this.sPortName);

            //Try to open the port
            try {
                this.isPortOpen = this.xPort.openPort();
            } catch (SerialPortException ser1) {
                String s = "Unable to open the port for the "
                        + "following Reason\n Exception:" + ser1.toString();
                NeelSerOptions.LOG.log(Level.WARNING, s);
                this.isPortOpen = false;
                this.notifyAll();
                throw new NeelSerException(eNeelSerialStatus.ERROR_PORTOPEN, s);
            }

            //Set the Parameters
            try {
                this.xPort.setParams(this.iBaudrate, this.iDataBits, this.iStopBits,
                        this.iParity, this.bRTS, this.bDTR);
            } catch (SerialPortException ser2) {
                String s = "Unable to Set Parameters for following "
                        + "Reason\n Exception:" + ser2.toString();
                NeelSerOptions.LOG.log(Level.WARNING, s);
                this.notifyAll();
                throw new NeelSerException(
                        eNeelSerialStatus.ERROR_SERIALPARMSETTING, s);
            }

            //Set Flow Control
            try {
                this.xPort.setFlowControlMode(this.iFlowControl);
            } catch (SerialPortException ser3) {
                String s = "Unable to set Flow control for the  "
                        + "following Reason\n Exception:" + ser3.toString();
                NeelSerOptions.LOG.log(Level.WARNING, s);
                this.notifyAll();
                throw new NeelSerException(
                        eNeelSerialStatus.ERROR_SERIALFLOWCONTROLSETTING, s);
            }

            //Clear the port
            try {
                this.xPort.purgePort(SerialPort.PURGE_RXCLEAR | SerialPort.PURGE_TXCLEAR);
            } catch (SerialPortException ser4) {
                String s = "Unable to Purge the Serial port for the "
                        + "following Reason\n Exception:" + ser4.toString();
                NeelSerOptions.LOG.log(Level.WARNING, s);
                this.notifyAll();
                throw new NeelSerException(eNeelSerialStatus.ERROR_GEN, s);
            }

            //Register for Serial Events
            try {
                this.xPort.addEventListener(new SerialPortEventListener() {
                    @Override
                    public void serialEvent(SerialPortEvent serialPortEvent) {
                        Action_SerialPortListen(serialPortEvent);
                    }
                }, SerialPort.MASK_RXCHAR
                        | SerialPort.MASK_BREAK);
            } catch (SerialPortException ser5) {
                String s = "Unable to register events for the "
                        + "Serial port due to "
                        + "following Reason\n Exception:" + ser5.toString();
                NeelSerOptions.LOG.log(Level.WARNING, s);
                this.notifyAll();
                throw new NeelSerException(eNeelSerialStatus.ERROR_FATAL, s);
            }
            //Notify that we are all done
            this.notifyAll();
        }
        // In Future add Semaphore Release
        return true;
    }

    /**
     * Close port function for both type of user application bindings Also
     * clears the User application binding after closing of the port
     *
     * @return true if close is successful false if the port is already closed
     * @throws NeelSerException if the serial port close was unsuccessful
     */
    public boolean b_closePort() throws NeelSerException {

        // Check if the port was open
        if (!this.isPortOpen) {
            NeelSerOptions.LOG.log(Level.FINE, "Port Already Closed, Need to"
                    + " open the port first");
            return false;
        }

        synchronized (this) {
            // Close the port
            try {
                //Remove the Port open                
                this.isPortOpen = false;
                //Actually close the port
                this.xPort.closePort();
                // Deint all the User Application connectors
                if (!this.param_bQueueCommunications) {
                    this.iface = null;
                } else {
                    this.xErrorqueue = null;
                    this.xRxQueue = null;
                    this.xTxQueue = null;
                    this.xbCTSqueue = null;
                    this.xbDSRqueue = null;
                    this.xbRIqueue = null;
                    this.xbRLSDqueue = null;
                }
                this.xPort = null;//Delete the port also
            } catch (SerialPortException ser) {
                String s = "Unable to Close the Serial port for the "
                        + "following Reason\n Exception:" + ser.toString();
                NeelSerOptions.LOG.log(Level.WARNING, s);
                throw new NeelSerException(
                        eNeelSerialStatus.ERROR_PORTCLOSE, s);
            }
        }
        return true;
    }

    /**
     * Function to perform the action associated with the serial events received
     *
     * @param se Serial port event received
     */
    private void Action_SerialPortListen(SerialPortEvent se) {
        // check if port is open
        if (this.isPortOpen) {

            if (se.isRXCHAR()) {// !!!! Event Received Characters !!!!
                @SuppressWarnings("UnusedAssignment")
                byte[] bt = null; //Var to get the data

                synchronized (this) {
                    //Obtain data from port
                    try {
                        bt = this.xPort.readBytes();
                    } catch (SerialPortException ex) {
                        NeelSerOptions.LOG.log(Level.SEVERE,
                                "Could not obtain the CTS status\n {0}",
                                ex.toString());
                        this.notifyAll();
                        return;
                    }

                    //Check if the received array is null or not
                    if (bt != null) {
                        //Send it out as per the interface
                        if (this.param_bQueueCommunications) {
                            this.xRxQueue.add(bt);
                        } else {
                            this.iface.gotRxData(bt);
                        }
                        NeelSerOptions.LOG.log(Level.FINEST,
                                "Received change {0}bytes", bt.length);
                    }
                    this.notifyAll();
                }
            } else if (se.isCTS()) { // !!!!Event CTS Status Change!!!!
                synchronized (this) {
                    try {
                        this.bCTS = this.xPort.isCTS();
                    } catch (SerialPortException ex) {
                        NeelSerOptions.LOG.log(Level.SEVERE,
                                "Could not obtain the CTS status\n {0}",
                                ex.toString());
                        this.notifyAll();
                        return;
                    }
                    if (this.param_bQueueCommunications) {
                        this.xbCTSqueue.add(this.bCTS);
                    } else {
                        this.iface.changeCTS(this.bCTS);
                    }
                    NeelSerOptions.LOG.log(Level.FINEST,
                            "CTS change: {0}", this.bCTS);
                    this.notifyAll();
                }
            } else if (se.isDSR()) {// !!!!Event DSR Status Change!!!!
                synchronized (this) {
                    try {
                        this.bDSR = this.xPort.isDSR();
                    } catch (SerialPortException ex) {
                        NeelSerOptions.LOG.log(Level.SEVERE,
                                "Could not obtain the DSR status\n {0}",
                                ex.toString());
                        this.notifyAll();
                        return;
                    }
                    if (this.param_bQueueCommunications) {
                        this.xbDSRqueue.add(this.bDSR);
                    } else {
                        this.iface.changeDSR(this.bDSR);
                    }
                    NeelSerOptions.LOG.log(Level.FINEST,
                            "DSR change: {0}", this.bDSR);
                    this.notifyAll();
                }
            } else if (se.isRING()) {// !!!!Event RING Status Change!!!!
                synchronized (this) {
                    try {
                        this.bRI = this.xPort.isRING();
                    } catch (SerialPortException ex) {
                        NeelSerOptions.LOG.log(Level.SEVERE,
                                "Could not obtain the RING status\n {0}",
                                ex.toString());
                        this.notifyAll();
                        return;
                    }
                    if (this.param_bQueueCommunications) {
                        this.xbRIqueue.add(this.bRI);
                    } else {
                        this.iface.changeRI(this.bRI);
                    }
                    NeelSerOptions.LOG.log(Level.FINEST,
                            "RING change: {0}", this.bRI);
                    this.notifyAll();
                }
            } else if (se.isRLSD()) {
                synchronized (this) {
                    try {
                        this.bRLSD = this.xPort.isRLSD();
                    } catch (SerialPortException ex) {
                        NeelSerOptions.LOG.log(Level.SEVERE,
                                "Could not obtain the RLSD status\n {0}",
                                ex.toString());
                        this.notifyAll();
                        return;
                    }
                    if (this.param_bQueueCommunications) {
                        this.xbRLSDqueue.add(this.bRLSD);
                    } else {
                        this.iface.changeRLSD(this.bRLSD);
                    }
                    NeelSerOptions.LOG.log(Level.FINEST,
                            "RLSD change: {0}", this.bRLSD);
                    this.notifyAll();
                }

            } else if (se.isTXEMPTY()) {
            } else if (se.isBREAK()) {
                synchronized (this) {
                    if (this.param_bQueueCommunications) {
                        this.xbRxBreakqueue.add(true);
                    } else {
                        this.iface.gotBreak(true);
                    }
                    NeelSerOptions.LOG.log(Level.FINEST,
                            "Got Serail Break condition on Receiver");
                    this.notifyAll();
                }
            } else if (se.isERR()) {
            }
        }

    }

    /**
     * Function to update the Port List array
     */
    public void v_refreshPortList() {
        this.arsPortList = SerialPortList.getPortNames();
    }

    public boolean b_SendBytes(byte[] barr) throws NeelSerException {
        // Check if port is opened
        if (this.isPortOpen) {
            //Check if the Port Rally exists and parameter is not null
            if (this.xPort != null && barr != null) {
                synchronized (this) {
                    try {
                        this.xPort.writeBytes(barr);
                    } catch (SerialPortException ex) {
                        String s = "Error in Sending bytes\n" + ex.toString();
                        NeelSerOptions.LOG.warning(s);
                        this.notifyAll();
                        throw new NeelSerException(eNeelSerialStatus.ERROR_GEN, s);
                    }
                    this.notifyAll();
                }
                NeelSerOptions.LOG.log(Level.FINEST, "Sent {0}bytes", barr.length);
            } else {
                NeelSerOptions.LOG.warning(
                        "Unable to Send Bytes as Serial port is not "
                        + "initialized or parameter supplied is Null ");
            }
        } else {
            NeelSerOptions.LOG.warning(
                    "Unable to Send Bytes as Port is not open ");
        }
        return false;
    }

    public boolean b_SendBreak(int delayms) throws NeelSerException {
        //Check if port is Open
        if (this.isPortOpen) {
            synchronized (this) {
                try {
                    this.xPort.sendBreak(delayms);
                    return true;
                } catch (SerialPortException ex) {
                    String s = "Error in Sending Break Signal\n" + ex.toString();
                    NeelSerOptions.LOG.warning(s);
                    this.notifyAll();
                    throw new NeelSerException(eNeelSerialStatus.ERROR_GEN, s);
                }
            }
        } else {
            NeelSerOptions.LOG.warning(
                    "Unable to Send Break Signal as Port is not open ");
        }
        return false;
    }

    /**
     *
     * @return @throws Exception
     */
    public JRadioButtonMenuItem[] refreshPortList_menu() throws Exception {
        // Get the Port List
        this.arsPortList = SerialPortList.getPortNames();
        // Clear the menue
        JRadioButtonMenuItem[] arck = new JRadioButtonMenuItem[this.arsPortList.length];
        // Variable to check if the port thats currently open
        //   is present in the list
        boolean foundopenport = false;
        int i = 0;
        // Cycle through the Port Names
        for (String s : arsPortList) {
            // Set the Check box in case the port currently opened is found
            if (this.isPortOpen && this.sPortName.equals(s)) {
                arck[i] = new JRadioButtonMenuItem(s, null, true);
                foundopenport = true;
                NeelSerOptions.LOG.log(Level.FINER,
                        "Found Currently opened port: "
                        + "{0} in the port list", this.sPortName);
            } else { //In normal case none would be selected
                arck[i] = (new JRadioButtonMenuItem(s, null, false));
            }
            ++i;
        }

        if (this.isPortOpen) // If the Port currently open is not found in the list Raise Exeption
        {
            if (!foundopenport) {
                NeelSerOptions.LOG.log(Level.WARNING,
                        "Unable to find currently open port: "
                        + "{0} in the port list", this.sPortName);
                throw new Exception("Unable to find currently open port: "
                        + this.sPortName + " in the port list");
            } else // In case there is just a refresh and no ports are open
            //  Make default selection
            {
                arck[0].setSelected(true);
            }
        }
        return arck;
    }

    /**
     * Function to search the Port Name Array to find the index of a given port
     * name
     *
     * @param s Port name to be searched in the array
     * @return index of the Port Name array if the given Port Name is found -1
     * is returned to indicate error
     */
    public int indexOfPortName(String s) {
        boolean b = false;
        int i;
        //Check if the array is null
        if (this.arsPortList == null) {
            return -1;
        }
        //Check if the array is empty
        if (this.arsPortList.length < 1) {
            return -1;
        }
        //Perform the Search
        for (i = 0; i < this.arsPortList.length; i++) {
            if (this.arsPortList[i].equalsIgnoreCase(s)) {
                b = true;
                break;
            }
        }
        //If found then return true
        if (b) {
            return i;
        }
        return -1;
    }
    /**
     * Local Logger instance
     */
    @SuppressWarnings("NonConstantLogger")
    private static Logger LOG;
}
