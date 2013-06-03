/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adharlabs.log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author bose
 */
public class LogCustomHandler {
    private static FileHandler fh_Text,fh_Html;
    private static SimpleFormatter fmt_Text;
    private static LogHtmlCustomFormatter fmt_Html;
    
    public static void setup() throws IOException{
        LogCustomHandler.setup(Logger.GLOBAL_LOGGER_NAME,
                true,true,
                "log.txt","log.htm");
    }
    public static void setup(String logname) throws IOException{
        LogCustomHandler.setup(logname,true,true,
                "log.txt","log.htm");
    }
    public static void setup(String logname,
            boolean btxtlog,boolean bhtmllog) throws IOException{
        LogCustomHandler.setup(logname,btxtlog,bhtmllog,
                "log.txt","log.htm");
    }
    public static void setup(String logname,
            boolean btxtlog,boolean bhtmllog,
            String name) throws IOException{
        LogCustomHandler.setup(logname,btxtlog,bhtmllog,
                name+".txt",name+".htm");
    }
    public static void setup(String logname,
            boolean btxtlog,boolean bhtmllog,
            String txtname,String htmlname) throws IOException{
        Logger lg = Logger.getLogger(logname);
        
        if(btxtlog)
        {
            fh_Text = new FileHandler(txtname);
            fmt_Text = new SimpleFormatter();
            fh_Text.setFormatter(fmt_Text);
            lg.addHandler(fh_Text);
        }
        
        if(bhtmllog)
        {
            fh_Html = new FileHandler(htmlname);
            fmt_Html = new LogHtmlCustomFormatter();
            fh_Html.setFormatter(fmt_Html);
            lg.addHandler(fh_Html);
        }       
        
        lg.info("Intialization of Logger Done ...");
    }
}
