package printerserver.server;

import java.io.File;
import java.io.FileWriter;

/**
 * Object to debug of server.
 * 
 * This object turn easy the use of messages, errors and logs.
 *
 * @author Anderson Costa
 * @version 2019/01
 *
 * @see <a href="https://github.com/AsonCS/server_java" target="_blank">Repository on GitHub</a>
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
     * Method for show error, if debug is activated.
     * 
     * @param message Text for show.
     */
    public static void error(String message){
        if(debug) System.err.println(message);
    }
    
    /**
     * Method for show error, if debug is activated.
     * 
     * @param object Object with toString of that.
     */
    public static void error(Object object){
        if(debug) System.err.println(object);
    }
    
    /**
     * Method for show error, if debug is activated.
     * 
     * @param e {@link Exception}.
     */
    public static void error(Exception e){
        if(debug) e.printStackTrace();
    }
    
    /**
     * Method for show message, if debug is activated.
     * 
     * @param object Object with toString of that.
     */
    public static void message(Object object){
        if(debug) System.out.println(object);
    }
    
    /**
     * Method for show message, if debug is activated.
     * 
     * @param message Text for show.
     */
    public static void message(String message){
        if(debug) System.out.println(message);
    }
    
    /**
     * Method for show info, if info is activated.
     * 
     * @param message Text for show.
     */
    public static void info(String message){
        if(info) System.out.println(message);
    }
    
    /**
     * Method for show info, if info is activated.
     * 
     * @param object Object with toString of that.
     */
    public static void info(Object object){
        if(info) System.out.println(object);
    }
    
    /**
     * Method for write log at file, if log is activated.
     * 
     * @param message Text for show.
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
