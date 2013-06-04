/* Adharlab's Custom Log Handler
 * Logger Class to Initialize the Log Handler and provide addtional functions
 * @copy © Abhijit Bose , A.D.H.A.R Labs Research Bharat(India), 2013.
 * 
 * LogCustomHandler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LogCustomHandler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * @email info@adharlabs.in
 * @website http://adharlabs.in
 * 
 */
package com.adharlabs.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;

/**
 * Swing based Visual Terminal that can be used as required
 * 
 * @version 1.0_20130605
 * @author Abhijit Bose , A.D.H.A.R Labs Research Bharat(India)
 */
public class VisualTerminal extends JFrame{
    public int iHeight;
    public int iWidth;
    public String sTitle;
    @SuppressWarnings("NonConstantLogger")
    public Logger LOG;
    public JTextPane jtextpane;
    public StyledDocument styledoc;
    public JScrollPane jscrollpane;
    public JPopupMenu jpopupmenu;    
    public BlockingQueue<StringWithVisualStyle> syncqueue;
    private ThreadedTextPaneHandler xtph;
    public String sNewLineChar = "\n";
    
    @SuppressWarnings("CallToThreadStartDuringObjectConstruction")
    public VisualTerminal(int iHeight,int iWidth,String sTitle,Logger log)
    {
        // Get all the vars initialized into the internal context
        this.iHeight = iHeight;
        this.iWidth = iWidth;
        this.sTitle = sTitle;
        this.LOG = log;
       
        //Create the Visual Objects
        this.jtextpane = new JTextPane();
        this.jscrollpane = new JScrollPane(jtextpane, 
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.jpopupmenu = new JPopupMenu();
        this.syncqueue = new LinkedBlockingQueue<StringWithVisualStyle>();
        this.xtph = new ThreadedTextPaneHandler(jtextpane, syncqueue, this.LOG);
        
        //Assign Properties
        super.setTitle(this.sTitle);
        super.setMinimumSize(new Dimension(this.iWidth, this.iHeight));
        super.setPreferredSize(new Dimension(this.iWidth, this.iHeight));
        super.add(this.jscrollpane);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jtextpane.setComponentPopupMenu(this.jpopupmenu);
        this.styledoc = this.jtextpane.getStyledDocument();
        this.jpopupmenu.add(this.sTitle+" Menu");
        this.jpopupmenu.addSeparator();        
        
        //Run the TextPane Thread
        (new Thread(this.xtph,"TextPane Thread")).start();
        
        //Set the final Log message
        this.LOG.log(Level.INFO, "VisualTerminal -{0}- Intialized", sTitle);
    }

    @Override
    public void setTitle(String title) {
        this.sTitle = title;
        super.setTitle(title); 
    }

    @Override
    public void setSize(int width, int height) {
        this.iHeight = height;
        this.iWidth = width;
        super.setSize(width, height);
    }
    
    public void print(String s)
    {
        this.syncqueue.add(new StringWithVisualStyle(s));
    }
    
    public void print(String s,Color fg)
    {
        this.syncqueue.add(new StringWithVisualStyle(s,fg));
    }
    
    public void print(String s,Color fg,Color bg)
    {
        this.syncqueue.add(new StringWithVisualStyle(s,fg,bg));
    }
    
    public void print(String s, SimpleAttributeSet a)
    {
        this.syncqueue.add(new StringWithVisualStyle(s,a));
    }
    
    public void println(String s)
    {
        this.print(s+this.sNewLineChar);
    }
    
    public void println(String s,Color fg)
    {
        this.print(s+this.sNewLineChar,fg);
    }
    
    public void println(String s,Color fg,Color bg)
    {
        this.print(s+this.sNewLineChar,fg,bg);
    }
    
    public void println(String s,SimpleAttributeSet a)
    {
        this.print(s+this.sNewLineChar,a);
    }
    
    public void clear()
    {
        this.jtextpane.setText("");
    }
}
