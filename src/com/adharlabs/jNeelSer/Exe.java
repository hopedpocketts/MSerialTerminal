/**
 * jNeelSer - Java Full loaded Serial Port Terminal
 * 
 */
package com.adharlabs.jNeelSer;

import com.adharlabs.UI.VisualTerminal;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bose
 */
public class Exe {

    @SuppressWarnings("NonConstantLogger")
    public static Logger LOG;
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
        // Initialize the Local logger
        Exe.LOG = com.adharlabs.log.LogCustomHandler.LOG;
        Exe.LOG.log(Level.INFO,"Begin Exe");
        Exe.LOG.setLevel(Level.ALL);//Enable Debug Logs
        // Create the Visual Terminal
        VisualTerminal vs = new VisualTerminal(200, 200, "jNeelSer", 
                Exe.LOG);
        
        // Add some dummy lines
        vs.println("Hello This is the first lines\n\n\n\n\n\n\n\n\n\n\n\n"+
                "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"+
                "\n\n\n\n\n\n\n\n\n\n");
        
        // Start Execution
        vs.setVisible(true);
    }
}
