/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adharlabs.UI;

import java.awt.Color;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 *
 * @author nxp
 */
public class StringWithVisualStyle {
    public String s;
    public SimpleAttributeSet a;
    
    public StringWithVisualStyle(String s)
    {
        this.s = s;
        this.a = new SimpleAttributeSet();
    }
    
    public StringWithVisualStyle(String s,Color fgColor)
    {
        this.s = s;
        this.a = new SimpleAttributeSet();
        StyleConstants.setForeground(a, fgColor);
    }
    
    public StringWithVisualStyle(String s,Color fgColor,Color bgColor)
    {
        this.s = s;
        this.a = new SimpleAttributeSet();
        StyleConstants.setForeground(a, fgColor);
        StyleConstants.setBackground(a, bgColor);
    }
    
    public StringWithVisualStyle(String s, SimpleAttributeSet as)
    {
        this.s = s;
        this.a = as;
    }
}
