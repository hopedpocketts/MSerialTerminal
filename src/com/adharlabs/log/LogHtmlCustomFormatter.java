/* Adharlab's Custom Log HTML formatter
 * Log Formatter Class to help in HTML formatting of Log output
 * @copy © Abhijit Bose , A.D.H.A.R Labs Research Bharat(India), 2013.
 * 
 * LogHtmlCustomFormatter is free software: you can redistribute it 
 * and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation, either version 3 
 * of the License, or (at your option) any later version.
 *
 * LogHtmlCustomFormatter is distributed in the hope that it will be useful,
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
package com.adharlabs.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Class to extend the Log Formatter functionality and implement a customized
 * HTML formatter for handling the log output
 *
 * @author Abhijit Bose , A.D.H.A.R Labs Research Bharat(India)
 */
public class LogHtmlCustomFormatter extends Formatter {

    /**
     * Function is called when the log record is added
     *
     * @param record currently added record in the logger
     * @return formatted string to be used for HTML output
     */
    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        // Start the Log Row
        sb.append("<tr>\n");

        // Add the Log Level
        int recv = record.getLevel().intValue();
        if (recv == Level.SEVERE.intValue()) {
            sb.append(" <td style=\"color:#F00\"><b>\n");
            sb.append(record.getLevel().getName());
            sb.append("</b></td>");
        } else if (recv == Level.WARNING.intValue()) {
            sb.append(" <td><b>\n");
            sb.append(record.getLevel().getName());
            sb.append("</b></td>");
        } else if (recv == Level.INFO.intValue()) {
            sb.append(" <td style=\"color:#00F\">\n");
            sb.append(record.getLevel().getName());
            sb.append("</td>");
        } else if (recv == Level.CONFIG.intValue()) {
            sb.append(" <td style=\"color:#0F0\">\n");
            sb.append(record.getLevel().getName());
            sb.append("</td>");
        } else // In case of normal logs
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

    /**
     * Called when the Logger is initialized and first Heading is created
     * for the log file
     * 
     * @param h basic handler for the log output
     * @return formatted HTML string making the Header of the HTML file
     */
    @Override
    public String getHead(Handler h) {
        //return super.getHead(h); //To change body of generated methods, choose Tools | Templates.
        return "<HTML>\n<HEAD>\n" + (new Date())
                + "\n</HEAD>\n<BODY>\n<PRE>\n"
                + "<table width=\"100%\" border>\n  "
                + "<tr><th>Level</th>"
                + "<th>Time</th>"
                + "<th>Log Message</th>"
                + "</tr>\n";
    }

    /**
     * Called when the Logger closes and ending of the log file need to be
     * placed
     * 
     * @param h basic handler for log output
     * @return formatted HTML string making the Footer of the HTML file
     */
    @Override
    public String getTail(Handler h) {
        //return super.getTail(h); //To change body of generated methods, choose Tools | Templates.
        return "</table>\n  </PRE></BODY>\n</HTML>\n";
    }
}
