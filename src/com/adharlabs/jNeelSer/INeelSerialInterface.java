/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adharlabs.jNeelSer;

/**
 * jNeelSer Serial interface
 *
 * @version 1.0_20130625
 * @author Abhijit Bose , A.D.H.A.R Labs Research Bharat(India)
 */
public interface INeelSerialInterface {

    /**
     *
     * @param arb
     */
    void gotRxData(byte[] arb);
    /**
     *
     * @return
     */
    byte[] needTxData();
    /**
     *
     * @param state
     */
    void changeDSR(boolean state);
    /**
     *
     * @param state
     */
    void changeRI(boolean state);
    /**
     *
     * @param state
     */
    void changeCTS(boolean state);
    /**
     *
     * @param state
     */
    void changeRLSD(boolean state);
    /**
     * 
     * @param state 
     */
    void gotBreak(boolean state);
    /**
     *
     * @param lvl
     * @param from
     * @param error
     */
    void notifyError(eNeelSerialStatus lvl,String from,String error);
    
}
