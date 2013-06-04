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
package com.adharlabs.log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Class to handle the custom intialization of the logger
 * @version 1.0_20130605
 * @author Abhijit Bose , A.D.H.A.R Labs Research Bharat(India)
 */
public class LogCustomHandler {

    /**
     * File handlers for Text and HTML log files
     */
    private static FileHandler fh_Text, fh_Html;
    /**
     * Formatter for Text files
     */
    private static SimpleFormatter fmt_Text;
    /**
     * Custom HTML formatter for HTML log files
     */
    private static LogHtmlCustomFormatter fmt_Html;
    /**
     * Generic global instance of a Logger Variable that can be either used by
     * direct or indirect reference
     */
    @SuppressWarnings("NonConstantLogger")
    public static Logger LOG;

    /**
     * Default Logger Setup function that create the a Global logger entity and
     * also creates both log.htm and log.txt files
     *
     * @throws IOException
     */
    public static void setup() throws IOException {
        LogCustomHandler.setup(Logger.GLOBAL_LOGGER_NAME,
                true, true,
                "log.txt", "log.htm");
    }

    /**
     * Logger Setup to create the Logger instance under a specified logging name
     * and create both log.htm and log.txt files
     *
     * @param logname the logger name on which the Logger instance is
     * initialized
     * @throws IOException
     */
    public static void setup(String logname) throws IOException {
        LogCustomHandler.setup(logname, true, true,
                "log.txt", "log.htm");
    }

    /**
     * Logger Setup to create the Logger instance under a specified logging name
     * and create log.htm and/or log.txt files depending on user choices
     *
     * @param logname the logger name on which the Logger instance is
     * initialized
     * @param btxtlog states if the Text mode log files is needed or not
     * @param bhtmllog states if the Html mode log files is needed or not
     * @throws IOException
     */
    public static void setup(String logname,
            boolean btxtlog, boolean bhtmllog) throws IOException {
        LogCustomHandler.setup(logname, btxtlog, bhtmllog,
                "log.txt", "log.htm");
    }

    /**
     * Logger Setup to create the Logger instance under a specified logging name
     * and create Html mode and/or Text mode files depending on user choices.
     * The file names used in Html log file have a file extention of ".htm" and
     * in Text log file have a file extention of ".txt". The user can specify
     * the path prefix and a common file name used during the logging.
     *
     * @param logname the logger name on which the Logger instance is
     * initialized
     * @param btxtlog states if the Text mode log files is needed or not
     * @param bhtmllog states if the Html mode log files is needed or not
     * @param name prefix for the log file names - possibly might contain a
     * special path in case needed
     * @throws IOException
     */
    public static void setup(String logname,
            boolean btxtlog, boolean bhtmllog,
            String name) throws IOException {
        LogCustomHandler.setup(logname, btxtlog, bhtmllog,
                name + ".txt", name + ".htm");
    }

    /**
     * Main Logger Setup function will all the bells and whistles User can
     * create files for both Text mode and Html mode. Here the error handling
     * has been added to avoid any mistakes.
     *
     * @param logname the logger name on which the Logger instance is
     * initialized
     * @param btxtlog states if the Text mode log files is needed or not
     * @param bhtmllog states if the Html mode log files is needed or not
     * @param txtname custom name along with path for the Text mode log file
     * @param htmlname custom name along with path for the Html mode log file
     * @throws IOException
     */
    public static void setup(String logname,
            boolean btxtlog, boolean bhtmllog,
            String txtname, String htmlname) throws IOException {
        LOG = Logger.getLogger(logname);
        //Text Logger
        if (btxtlog) {
            //Autmatically throw exception in case error in path for file name
            fh_Text = new FileHandler(txtname);
            fmt_Text = new SimpleFormatter();
            fh_Text.setFormatter(fmt_Text);
            LOG.addHandler(fh_Text);
        }
        //Html Logger
        if (bhtmllog) {
            //Autmatically throw exception in case error in path for file name
            fh_Html = new FileHandler(htmlname);
            fmt_Html = new LogHtmlCustomFormatter();
            fh_Html.setFormatter(fmt_Html);
            LOG.addHandler(fh_Html);
        }
        
        //Print a final Log Go message
        LOG.info("Intialization of Logger Done ...");
    }
}
