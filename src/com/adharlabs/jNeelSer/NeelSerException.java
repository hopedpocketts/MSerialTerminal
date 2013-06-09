/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adharlabs.jNeelSer;

/**
 * jNeelSer Exception Class
 * To add info about the type of error being sent out
 * @version 1.0_20130625
 * @author Abhijit Bose , A.D.H.A.R Labs Research Bharat(India)
 */
public class NeelSerException extends Exception{
    /**
     *
     */
    public eNeelSerialStatus status;
    /**
     *
     */
    public String message;

    /**
     *
     * @param status
     */
    public NeelSerException(eNeelSerialStatus status) {
        super();
        this.status = status;
    }

    /**
     *
     * @param status
     * @param message
     */
    public NeelSerException(eNeelSerialStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }    
}
