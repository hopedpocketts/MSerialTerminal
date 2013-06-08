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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
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
        this.jtextpane.setSelectionColor(Color.YELLOW);
        this.addRightClickHandlers(); 
        
        // Set Form at Center of Screen
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        super.setLocation(d.width/2-this.iWidth/2 ,d.height/2-this.iHeight/2 );
        
        //Run the TextPane Thread
        (new Thread(this.xtph,"TextPane Thread")).start();
        
        //Set the final Log message
        this.LOG.log(Level.FINE, "VisualTerminal -{0}- Intialized", sTitle);
    }

    @Override
    public void setTitle(String title) {
        this.sTitle = title;
        super.setTitle(title); 
        LOG.log(Level.FINER, "Title: {0}", title);
    }

    @Override
    public void setSize(int width, int height) {
        this.iHeight = height;
        this.iWidth = width;
        super.setSize(width, height);
        LOG.log(Level.FINER, "Size: Height = {0}", height);
        LOG.log(Level.FINER, "Size: Width = {0}", width);
    }
    
    public void print(String s)
    {
        this.syncqueue.add(new StringWithVisualStyle(s));
        LOG.log(Level.FINER, "Print: {0}", s);
        this.gotoend();
    }
    
    public void print(String s,Color fg)
    {
        this.syncqueue.add(new StringWithVisualStyle(s,fg));
        LOG.log(Level.FINER, "Print Fg["+
                String.format("#%06X",fg.getRGB())+"] : {0}", s);
        this.gotoend();
    }
    
    public void print(String s,Color fg,Color bg)
    {
        this.syncqueue.add(new StringWithVisualStyle(s,fg,bg));
        LOG.log(Level.FINER, "Print Fg["+
                String.format("#%06X",fg.getRGB())+"]"+
                " Bg["+String.format("#%06X",bg.getRGB())+"]"+" : {0}", s);
        this.gotoend();
    }
    
    public void print(String s, SimpleAttributeSet a)
    {
        this.syncqueue.add(new StringWithVisualStyle(s,a));
        LOG.log(Level.FINER, "Print Attibute["+a.toString()+"] : {0}", s);
        this.gotoend();
    }

    public void println()
    {
        this.print(this.sNewLineChar);
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
        LOG.log(Level.FINER, "Clear the Pane");
    }
    
    public void gotoend()
    {
        this.jtextpane.setCaretPosition(
                            this.jtextpane.getStyledDocument().getLength());
    }
    
    private void addRightClickHandlers()
    {
        JMenuItem jmClr = new JMenuItem("Clear");
        jmClr.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        
        this.jpopupmenu.add(jmClr);
        this.jpopupmenu.addSeparator();
        
        JMenuItem jmCut = new JMenuItem("Cut");
        jmCut.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                jtextpane.cut();
            }
        });
        
        this.jpopupmenu.add(jmCut);
        
        JMenuItem jmCopy = new JMenuItem("Copy");
        jmCopy.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                jtextpane.copy();
            }
        });
        
        this.jpopupmenu.add(jmCopy);
        
        JMenuItem jmPaste = new JMenuItem("Paste");
        jmPaste.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                jtextpane.paste();
            }
        });
        
        this.jpopupmenu.add(jmPaste);
        this.jpopupmenu.addSeparator();
        
        JMenuItem jmSelAll = new JMenuItem("Select All");
        jmSelAll.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                jtextpane.selectAll();                
            }
        });
        
        this.jpopupmenu.add(jmSelAll);
    }
    
}
