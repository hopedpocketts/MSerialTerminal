/**
 * jNeelSer - Java Full loaded Serial Port Terminal
 * 
 */
package com.adharlabs.jNeelSer;

import java.io.IOException;

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
        // TODO code application logic here
    }
}
