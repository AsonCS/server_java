package printerserver.server;

import java.io.File;
import java.io.FileWriter;

/**
 * Object debug of server.
 * This class turn easy the use of messages, errors and logs.
 *
 * @author Anderson Costa
 * @version 1.0
 *
 * @see <a href="https://github.com/AsonCS" target="_blank">My repository on GitHub</a>
 */
public class Debug {
    
    /**
     * Directory for logs.
     */
    public static final String LOGSDIRETORY = "logs";

    /**
     * File name for logs.
     */
    public static final String LOGSFILE = LOGSDIRETORY + "/error.txt";

    /**
     * Parameter for control of debug.
     */
    public static boolean debug = false;

    /**
     * Parameter for control of debug.
     */
    public static boolean log = false;

    /**
     * Parameter for control of debug.
     */
    public static boolean info = false;
    
    /**
     * Method for print error, if debug atived.
     * 
     * @param message Text for print.
     */
    public static void error(String message){
        if(debug) System.err.println(message);
    }
    
    /**
     * Method for print error, if debug atived.
     * 
     * @param object Object with toString of this.
     */
    public static void error(Object object){
        if(debug) System.err.println(object);
    }
    
    /**
     * Method for print error, if debug atived.
     * 
     * @param e Object exception with error.
     */
    public static void error(Exception e){
        if(debug) e.printStackTrace();
    }
    
    /**
     * Method for print message, if debug atived.
     * 
     * @param object Object with toString of this.
     */
    public static void message(Object object){
        if(debug) System.out.println(object);
    }
    
    /**
     * Method for print message, if debug atived.
     * 
     * @param message Text for print.
     */
    public static void message(String message){
        if(debug) System.out.println(message);
    }
    
    /**
     * Method for print info, if info atived.
     * 
     * @param message Text for print.
     */
    public static void info(String message){
        if(info) System.out.println(message);
    }
    
    /**
     * Method for print info, if info atived.
     * 
     * @param object Object with toString of this.
     */
    public static void info(Object object){
        if(info) System.out.println(object);
    }
    
    /**
     * Method for record log in file with , if log atived.
     * 
     * @param message Text for print.
     */
    public static void logFile(String message){
        if(log){
            try{
                new File(LOGSDIRETORY).mkdirs();
                File file = new File(LOGSFILE);
                if(!file.exists()) file.createNewFile();
                FileWriter writer = new FileWriter(file, true);
                writer.write(message + "\r\n");
                writer.close();
            }catch (Exception e){}
        }
    }
    
}
