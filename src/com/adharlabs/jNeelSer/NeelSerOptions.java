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
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @email info@adharlabs.in
 * @website http://adharlabs.in
 *
 */
package com.adharlabs.jNeelSer;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 * jNeelSer Option and Data Storage class
 *
 * @version 1.0_20130625
 * @author Abhijit Bose , A.D.H.A.R Labs Research Bharat(India)
 */
public class NeelSerOptions {

    @SuppressWarnings("NonConstantLogger")
    private static Logger LOG;
    public SerialPort xPort;
    public boolean isPortOpen = false;
    public String[] arsPortList;
    public int iSelectedPortIndex;
    public String sPortName;
    public int iBaudrate;
    public int iParity;
    public int iStopBits;
    public int iDataBits;
    public int iFlowControl;
    public boolean bCTS;
    public boolean bRTS;
    public boolean bDSR;
    public boolean bDTR;
    public boolean bRI;
    public boolean bRSLD;
    public boolean param_bEnableLoopBack;

    public NeelSerOptions(Logger log) {
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
        this.bRSLD = false;
        this.param_bEnableLoopBack = false;
        
        //Finally done
        NeelSerOptions.LOG.fine("Done Initialization");
    }

    public int indexOfPortName(String s) {
        boolean b = false;
        int i;
        for (i = 0; i < this.arsPortList.length; i++) {
            if (this.arsPortList[i].equalsIgnoreCase(s)) {
                b = true;
                break;
            }
        }
        if (b) {
            return i;
        }
        return -1;
    }

    public boolean openPort(/*int index*/) throws Exception {
        // In Future add Semaphore Lock (Relese Sem in Exceptions)
        //Check if port is already opened
        if (this.isPortOpen) {
            NeelSerOptions.LOG.log(Level.FINE, "Port Already Opened, Need to"
                    + " close the port first Index:{0}", /*index*/
                    this.iSelectedPortIndex);
            return false;
        }

        // Get the serial port name using the index
        this.sPortName = this.arsPortList[this.iSelectedPortIndex];
        /*
         try {
         this.sPortName = this.arsPortList[index];
         } catch (ArrayIndexOutOfBoundsException aiob) {
         String s = "Index " + index + " provided for "
         + "Serial port selection is outside the current"
         + " SerialPort Names list\n Exception: "
         + aiob.toString();
         NeelSerOptions.LOG.log(Level.WARNING, s);
         throw new Exception(s);
         }
         */
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
            throw new Exception(s);
        }

        //Set the Parameters
        try {
            this.xPort.setParams(this.iBaudrate, this.iDataBits, this.iStopBits,
                    this.iParity, this.bRTS, this.bDTR);
        } catch (SerialPortException ser2) {
            String s = "Unable to Set Parameters for following "
                    + "Reason\n Exception:" + ser2.toString();
            NeelSerOptions.LOG.log(Level.WARNING, s);
            throw new Exception(s);
        }

        //Set Flow Control
        try {
            this.xPort.setFlowControlMode(this.iFlowControl);
        } catch (SerialPortException ser3) {
            String s = "Unable to set Flow control for the  "
                    + "following Reason\n Exception:" + ser3.toString();
            NeelSerOptions.LOG.log(Level.WARNING, s);
            throw new Exception(s);
        }

        //Clear the port
        try {
            this.xPort.purgePort(SerialPort.PURGE_RXCLEAR | SerialPort.PURGE_TXCLEAR);
        } catch (SerialPortException ser4) {
            String s = "Unable to Purge the Serial port for the "
                    + "following Reason\n Exception:" + ser4.toString();
            NeelSerOptions.LOG.log(Level.WARNING, s);
            this.isPortOpen = false;
            throw new Exception(s);
        }
        // In Future add Semaphore Release
        return true;
    }

    public boolean closePort() throws Exception {

        // Check if the port was open
        if (!this.isPortOpen) {
            NeelSerOptions.LOG.log(Level.FINE, "Port Already Closed, Need to"
                    + " open the port first");
            return false;
        }

        // Close the port
        try {
            this.isPortOpen = false;
            this.xPort.closePort();
        } catch (SerialPortException ser) {
            String s = "Unable to Close the Serial port for the "
                    + "following Reason\n Exception:" + ser.toString();
            NeelSerOptions.LOG.log(Level.WARNING, s);
            throw new Exception(s);
        }

        return true;
    }

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
}
