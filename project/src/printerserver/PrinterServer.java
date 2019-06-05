package printerserver;

import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import printerserver.server.Server;
import javax.swing.ImageIcon;
import printerserver.server.Debug;

/**
 * Server main object with initial configurations.
 *
 * @author Anderson Costa
 * @version 1.0
 *
 * @see <a href="https://github.com/AsonCS/server_java" target="_blank">Repository on GitHub</a>
 */
public class PrinterServer {
    
    private static Server server;
    private static Thread shutdown;
    private static SystemTray systemTray;
    private static TrayIcon trayIcon;
    
    /**
     * Main method which launch application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args){
        //Debug.debug = true;
        //Debug.log = true;
        //Debug.info = true;
        for(String a : args){
            if(a.toLowerCase().equals("debug")) Debug.debug = true;
            if(a.toLowerCase().equals("log")) Debug.log = true;
            if(a.toLowerCase().equals("info")) Debug.info = true;
        }
        show();
        addIcon();
        addShutdown();
        if(server == null){
            try {
                server = new CreateRoutes().getServer().init();
            } catch (IOException ex) {
                String error = String.format(
                        "Error occurred in initialize modules\n%s / Error type %s\nError message - %s", 
                        new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()),
                        "IOException",
                        ex.getMessage()
                );
                System.err.println(error);
                Debug.logFile(error);
                System.exit(1);
            }
        }
        //start();
    }
    
    /**
     * Show server panel.
     */
    public static void show(){
        try {
            WebScreen.init(new File("templates/main_screen.html"), new JSObj());
        } catch (MalformedURLException ex) {
            String error = String.format(
                    "Error occurred in initialize modules\n%s / Error type %s\nError message - %s", 
                    new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()),
                    "MalformedURLException",
                    ex.getMessage()
            );
            System.err.println(error);
            Debug.logFile(error);
            System.exit(1);
        }
    }
    
    /**
     * Starts server.
     */
    public static void start(){
        PrinterServer.server.start();
        WebScreen.putJs("setAddress('http://" + Server.getIp() + ":" + Server.PORT + "/');");
        WebScreen.putJs("setStatus('Running');");
    }
    
    /**
     * Stops server.
     */
    public static void stop(){
        PrinterServer.server.pause();
        WebScreen.putJs("setAddress('http://" + Server.getIp() + ":" + Server.PORT + "/');");
        WebScreen.putJs("setStatus('Paused');");
    }
    
    /**
     * Turns off server.
     */
    public static void quit(){
        WebScreen.close();
        server.stop();
        if(systemTray != null) systemTray.remove(trayIcon);
        System.exit(0);
    }
    
    private static boolean addIcon(){
        if (SystemTray.isSupported()) {   
            systemTray = SystemTray.getSystemTray();
            trayIcon = new TrayIcon(new ImageIcon("src/image.png", "omt").getImage(), "Printer Server");
            trayIcon.setImageAutoSize(true);// Autosize icon base on space

            MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(WebScreen.isVisible()){
                        WebScreen.close();
                    }else{
                        WebScreen.show();
                    }
                }
            };
            trayIcon.addMouseListener(mouseAdapter);
            try {
                systemTray.add(trayIcon);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
    
    private static void addShutdown(){
        if(shutdown == null){
            shutdown = new Thread(){
                @Override
                public void run() {
                    try{
                        server.stop();
                    }catch(Exception e ){}
                    try{
                        if(systemTray != null) systemTray.remove(trayIcon);
                    }catch(Exception e ){}
                }

            };
            Runtime.getRuntime().addShutdownHook(shutdown);
        }
    }
    
    public static class JSObj{
        public void quit(){
            PrinterServer.quit();
        }

        public void stop(){
            PrinterServer.stop();
        }

        public void start(){
            PrinterServer.start();
        }
    }
}
