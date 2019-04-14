package printerserver.server;

import java.io.File;
import java.io.FileWriter;

public class Debug {
    
    public static final String LOGSDIRETORY = "logs";
    public static final String LOGSFILE = LOGSDIRETORY + "/error.txt";
    public static boolean debug = false;
    public static boolean log = false;
    public static boolean info = false;
    
    public static void error(String mensage){
        if(debug) System.err.println(mensage);
    }
    
    public static void error(Object object){
        if(debug) System.err.println(object);
    }
    
    public static void mensage(Object object){
        if(debug) System.out.println(object);
    }
    
    public static void mensage(String mensage){
        if(debug) System.out.println(mensage);
    }
    
    public static void info(String mensage){
        if(info) System.out.println(mensage);
    }
    
    public static void info(Object object){
        if(info) System.out.println(object);
    }
    
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
