package printerserver;

import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import printerserver.server.Server;
import javax.swing.ImageIcon;
import printerserver.MainScreen.Status;
import printerserver.server.Debug;

public class PrinterServer {
    
    private static Server server;
    private static Thread shutdown;
    private static MainScreen panel;
    private static SystemTray systemTray;
    private static TrayIcon trayIcon;
    
    public static void main(String[] args){
        //Debug.debug = true;
        //Debug.log = true;
        //Debug.info = true;
        for(String a : args){
            if(a.toLowerCase().equals("debug")) Debug.debug = true;
            if(a.toLowerCase().equals("log")) Debug.log = true;
        }
        if(server == null){
            try {
                server = new CreateRoutes().getServer().init();
            } catch (IOException ex) {
                Debug.logFile(ex.getMessage());
                System.exit(0);
            }
        }
        addIcon();
        addShutdown();
        show();
        start();
        panel.setTxt_status(Status.STARTED);
    }
    
    public static void show(){
        if(panel == null){
            panel = new MainScreen();
        }
        if(!SystemTray.isSupported()) panel.setDefault();
        panel.setAddress(Server.getIp(), Server.PORT);
        panel.setVisible(true);
    }
    
    public static void start(){
        PrinterServer.server.start();
    }
    
    public static void stop(){
        PrinterServer.server.pause();
    }
    
    public static void quit(){
        server.stop();
        if(systemTray != null) systemTray.remove(trayIcon);
        panel.dispose();
    }
    
    public static void refresh(){
        show();
        start();
        panel.setTxt_status(Status.STARTED);
    }
    
    private static boolean addIcon(){
        if (SystemTray.isSupported()) {   
            systemTray = SystemTray.getSystemTray();
            trayIcon = new TrayIcon(new ImageIcon("src/image.png", "omt").getImage(), "Printer Server");
            trayIcon.setImageAutoSize(true);// Autosize icon base on space

            MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    show();
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
}
