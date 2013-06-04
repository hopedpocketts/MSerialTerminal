/**
 * jNeelSer - Java Full loaded Serial Port Terminal
 * 
 */
package com.adharlabs.jNeelSer;

import com.adharlabs.UI.VisualTerminal;
import java.awt.Dimension;
import java.io.IOException;
import java.util.logging.Logger;

/**
 *
 * @author bose
 */
public class Exe {

    /**
     * Static block to Configure the logger by default
     */
    static{
        try {
            com.adharlabs.log.LogCustomHandler.setup(Exe.class.getName());
        } catch (IOException ex) {
            System.err.println("Error could not intialize the logger:\n"
                    +ex.toString());
            System.exit(1);            
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        VisualTerminal vs = new VisualTerminal(200, 200, "jNeelSer", 
                com.adharlabs.log.LogCustomHandler.LOG);
        vs.println("Hello This is the first lines\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        vs.setVisible(true);
    }
}
