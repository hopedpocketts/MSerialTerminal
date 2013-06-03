/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adharlabs.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 *
 * @author bose
 */
public class LogHtmlCustomFormatter extends Formatter{
    
    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        // Start the Log Row
        sb.append("<tr>\n");
        
        // Add the Log Level
        int recv = record.getLevel().intValue();
        if(recv == Level.SEVERE.intValue())
        {
            sb.append(" <td style=\"color:#F00\"><b>\n");
            sb.append(record.getLevel().getName());
            sb.append("</b></td>");
        }
        else if(recv == Level.WARNING.intValue())
        {
            sb.append(" <td><b>\n");
            sb.append(record.getLevel().getName());
            sb.append("</b></td>");
        }
        else if(recv == Level.INFO.intValue())
        {
            sb.append(" <td style=\"color:#00F\">\n");
            sb.append(record.getLevel().getName());
            sb.append("</td>");
        }
        else if(recv == Level.CONFIG.intValue())
        {
            sb.append(" <td style=\"color:#0F0\">\n");
            sb.append(record.getLevel().getName());
            sb.append("</td>");
        }
        else // In case of normal logs
        {
            sb.append(" <td>\n");
            sb.append(record.getLevel().getName());
            sb.append("</td>");
        }
        
        // Now add the Date Field
        sb.append("<td>\n");
        sb.append((new SimpleDateFormat("dd-MMM-yyyy HH:mm"))
                .format(new Date(record.getMillis())));
        sb.append("</td>\n");
        
        // Add the Message
        sb.append("<td>\n");
        sb.append(formatMessage(record));
        sb.append("</td>\n");
        
        // End the Row 
        sb.append("</tr>\n");
        return sb.toString();
    }

    @Override
    public String getHead(Handler h) {
        //return super.getHead(h); //To change body of generated methods, choose Tools | Templates.
        return "<HTML>\n<HEAD>\n" + (new Date()) 
        + "\n</HEAD>\n<BODY>\n<PRE>\n"
        + "<table width=\"100%\" border>\n  "
        + "<tr><th>Level</th>" +
        "<th>Time</th>" +
        "<th>Log Message</th>" +
        "</tr>\n";
    }

    @Override
    public String getTail(Handler h) {
        //return super.getTail(h); //To change body of generated methods, choose Tools | Templates.
        return "</table>\n  </PRE></BODY>\n</HTML>\n";
    }
}
