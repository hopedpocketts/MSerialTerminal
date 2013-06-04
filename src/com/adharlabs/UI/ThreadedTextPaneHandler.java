/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adharlabs.UI;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

/**
 *
 * @author nxp
 */
public class ThreadedTextPaneHandler implements Runnable{

    private final JTextPane jp;
    private final BlockingQueue<StringWithVisualStyle> q;
    public boolean isEnabled;
    @SuppressWarnings("NonConstantLogger")
    private static Logger LOG;
    
    public ThreadedTextPaneHandler(JTextPane j,
            BlockingQueue<StringWithVisualStyle> queue,
            Logger log)
    {
        this.jp = j;
        this.q = queue;
        this.isEnabled = true;
        LOG = log;
    }
    
    @Override
    public void run() {
        while(isEnabled)
        {
            try {
                StringWithVisualStyle as = this.q.take();
                StyledDocument sd = this.jp.getStyledDocument();
                try {
                    sd.insertString(sd.getLength(), as.s, as.a);
                } catch (BadLocationException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            } catch (InterruptedException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
}
