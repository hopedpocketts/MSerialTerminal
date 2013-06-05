/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adharlabs.jNeelSer;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import jssc.SerialPortList;

/**
 *
 * @author bose
 */
public class NeelSerOptions {

    @SuppressWarnings("NonConstantLogger")
    private static Logger LOG;
    public boolean isPortOpen = false;
    public String[] arsPortList;
    public String sPortName;
    public int iBaudrate;
    public int iParity;
    public int iStopBits;
    public int iFlowControl;

    public NeelSerOptions(Logger log) {
        // Assign the Primary Things
        NeelSerOptions.LOG = log;

        // Initialize the Parameters
        this.arsPortList = SerialPortList.getPortNames();
        this.sPortName = this.arsPortList[0];

        //Finally done
        NeelSerOptions.LOG.fine("Done Initialization");
    }

    public void refreshPortList(JMenu jm) throws Exception {
        // Get the Port List
        this.arsPortList = SerialPortList.getPortNames();
        // Clear the menue
        jm.removeAll();
        // Variable to check if the port thats currently open 
        //   is present in the list
        boolean foundopenport = false;
        // Cycle through the Port Names
        for (String s : arsPortList) {
            // Set the Check box in case the port currently opened is found
            if (this.isPortOpen && this.sPortName.equals(s)) {
                jm.add(new JCheckBoxMenuItem(s, null, true));
                foundopenport = true;
            } else { //In normal case none would be selected
                jm.add(new JCheckBoxMenuItem(s, null, false));
            }
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
                ((JCheckBoxMenuItem) jm.getItem(0)).setSelected(true);
            }
        }
    }
}
