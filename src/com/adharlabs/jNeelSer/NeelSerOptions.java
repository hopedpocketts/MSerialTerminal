/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adharlabs.jNeelSer;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 *
 * @author bose
 */
public class NeelSerOptions {
    @SuppressWarnings("NonConstantLogger")
    private static Logger LOG;
    public boolean isPortOpen = false;
    public ArrayList<String> arPortList;
    public NeelSerOptions(Logger log)
    {
        NeelSerOptions.LOG = log;
        this.arPortList = new ArrayList<String>();
        
        NeelSerOptions.LOG.fine("Done Initialization");
    }
}
