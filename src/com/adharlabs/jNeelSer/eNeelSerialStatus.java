/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adharlabs.jNeelSer;

/**
 * jNeelSer Serial Error Status Enum
 *
 * @version 1.0_20130625
 * @author Abhijit Bose , A.D.H.A.R Labs Research Bharat(India)
 */
public enum eNeelSerialStatus {

    /**
     * All is good
     */
    OK {
        @Override
        public String toString() {
            return "All is good";
        }
    },
    /**
     * Generic Error can be ignored or recovery possible
     */
    ERROR_GEN {
        @Override
        public String toString() {
            return "Generic Error can be ignored or recovery possible";
        }
    },
    /**
     * Unrecoverable error might need reinitialization or exit
     */
    ERROR_FATAL {
        @Override
        public String toString() {
            return "Unrecoverable error might need reinitialization or exit";
        }
    },
    /**
     * Error in opening the port
     */
    ERROR_PORTOPEN{
        @Override
        public String toString() {
            return "Error in opening the port";
        }
    },
    /**
     * Problem with Port List Array access
     */
    ERROR_PORTLISTARRAY_ACCESS{
        @Override
        public String toString() {
            return "Problem with Port List Array access";
        }
    },
    /**
     * Error in setting the Serial port parameters
     */
    ERROR_SERIALPARMSETTING{
       @Override
        public String toString() {
            return "Error in setting the Serial port parameters";
        } 
    },
    /**
     * Error in setting the Flow control parameters for the Serial Port
     */
    ERROR_SERIALFLOWCONTROLSETTING{
        @Override
        public String toString() {
            return "Error in setting the Flow control parameters for the Serial Port";
        }
    },
    /**
     * Problem in closing the Port
     */
    ERROR_PORTCLOSE{
        
    },
    ERROR_CTS {
    }
}
