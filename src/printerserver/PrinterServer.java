package printerserver;

import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import printerserver.server.Server;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import printerserver.server.Debug;

public class PrinterServer {
    
    private static Server server;
    private static Thread shutdown;
    private static NewJFrame panel;
    public static String ip = "";
    private static final SystemTray systemTray = SystemTray.getSystemTray();
    private static final TrayIcon trayIcon = new TrayIcon(new ImageIcon("D:/Anderson/twx.png", "omt").getImage(), "TWX");
    
    public static void main(String[] args){
        //Debug.debug = true;Debug.log = true;
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
        try {
            ip = getIp();
        } catch (Exception ex) {
            ip = "127.0.0.1";
        }
        addIcon();
        addShutdown();
        show();
    }
    
    public static void show(){
        if(panel == null){
            panel = new NewJFrame();
        }
        panel.setTxt_ip(ip);
        panel.setTxt_port(String.valueOf(Server.PORT));
        panel.setVisible(true);
    }
    
    public static void start(){
        try {
            PrinterServer.server.start();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    public static void stop(){
        PrinterServer.server.pause();
    }
    
    public static void quit(){
        server.stop();
        systemTray.remove(trayIcon);
        panel.dispose();
    }
    
    private static boolean addIcon(){
        if (SystemTray.isSupported()) {            
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
                        systemTray.remove(trayIcon);
                    }catch(Exception e ){}
                }

            };
            Runtime.getRuntime().addShutdownHook(shutdown);
        }
    }
    
    public static String getIp() throws Exception{
        long ips = 0;
        String ipAddress = InetAddress.getLocalHost().getHostAddress().trim();
        Enumeration<NetworkInterface> net = NetworkInterface.getNetworkInterfaces();
        while (net.hasMoreElements()) {
            NetworkInterface element = net.nextElement();
            Enumeration<InetAddress> addresses = element.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress ip = addresses.nextElement();
                if (ip.isSiteLocalAddress()) {
                    if((Long.valueOf(ip.getHostAddress().replace(".", "").trim())) < ips ||
                            (ips == 0)){
                        ips = Long.valueOf(ip.getHostAddress().replace(".", "").trim());
                        ipAddress = ip.getHostAddress();
                    }
                }           
            }
        }
        return ipAddress;
    }
}
