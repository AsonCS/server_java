package printerserver.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import printerserver.server.Parameter.*;

/**
 * Object for create and configure the server with routes.
 * 
 * This class select which port will be used for server, and load the routes for
 * treat requests of client side.
 *
 * @author Anderson Costa
 * @version 1.0
 *
 * @see <a href="https://github.com/AsonCS" target="_blank">My repository on GitHub</a>
 */
public class Server {
    
    /**
     * Port pattern of server.
     */
    public static int PORT = 9101;
    
    private ArrayList<Route> routes;
    private boolean runStatus = false;
    private static Thread server;
    
    private ServerSocket servidor;

    /**
     * Constructor for server with port number.
     * 
     * @param port Number for server in.
     * @throws IOException Exception of socket.
     * @throws Exception Exception for <em><i>Invalid port number.</i></em>
     */
    public Server(int port) throws IOException, Exception{
        if(port < 1 || port > 9999) throw new Exception("Invalid port number.");
        PORT = port;
        servidor = new ServerSocket(PORT);
        this.routes = new ArrayList<>();
    }

    /**
     * Constructor for server without port number, which will be pattern port.
     * 
     * @throws IOException Exception of socket.
     */
    public Server() throws IOException{      
        servidor = new ServerSocket(PORT);
        this.routes = new ArrayList<>();
    }
    
    private String[] processRoute(String route){
        String[] response = new String[2];
        response[0] = "/route";
        response[1] = "";
        route = route.split("[?]")[0];
        if(route.split("/").length <= 1){
            response[0] += "/";
            return response;
        }
        for(String b : route.split("/")){
            if(!b.contains("{") && !b.contains("}")){
                response[0] = b.length() > 0 ? response[0] + "/" + b : response[0];
                response[1] = b.length() > 0 ? response[1] + "/?" : response[1];
            }else{
                response[0] = b.length() > 0 ? response[0] + "/([^/]+)" : response[0];
                response[1] = b.length() > 0 ? response[1] + "/" + b.substring(b.indexOf("{") + 1, b.indexOf("}"))  : response[1];
            }
        }
        return response;
    }
    
    /**
     * Method for add routes for treat request of client side.
     *
     * @param route Route string which identifies <i>url</i> requested. 
     * @param method Method <i>HTTP</i> of request route.
     * @param handler Method for treat this request route.
     * @return This object server.
     */
    public Server addRoute(String route, Method method, Handler handler){
        String[] map = processRoute(route);
        routes.add(new Route(map[0], map[1].split("/"), handler, method));
        Collections.sort(routes);
        return this;
    }
    
    /**
     * Method for remove routes.
     *
     * @param idx Index of route which will be removed.
     * @return This object server.
     */
    public Server removeRoute(int idx){
        if(idx > -1 && idx < routes.size()) routes.remove(idx);
        return this;
    }
    
    /**
     * Init server for listen request of client side.
     *
     * @return This object server.
     * @throws IOException Exception of socket.
     */
    public Server init() throws IOException{
        if(servidor == null){
            servidor = new ServerSocket(PORT);
        }
        if(routes.size() > 0){
            new Thread(){
                @Override
                public void run() {
                    Debug.message("Servidor ouvindo a porta " + PORT);
                    while (true) {
                        try{
                            if(servidor == null){
                                break;
                            }
                            if(servidor.isClosed()){
                                break;
                            }
                            Socket cliente = servidor.accept();
                            if(!runStatus){
                                cliente.close();
                                continue;
                            }
                            cliente.setSoTimeout(1000);
                            Thread t = new Thread(new ServerContext(cliente, routes));
                            t.start();
                        } catch (Exception ex) {
                            Debug.error(ex.getMessage());
                        }
                    }
                }                    
            }.start();
        }
        return this;
    }
    
    /**
     * Start server, if this is paused.
     */
    public void start(){
        runStatus = true;
    }
    
    /**
     * Pause server, if this is running.
     */
    public void pause(){
        runStatus = false;
    }
    
    /**
     * Stop server and release memory of socket.
     *
     * @return This object server.
     */
    public Server stop(){
        runStatus = false;
        if(servidor != null){
            while(!servidor.isClosed()){
                try {
                    servidor.close();
                } catch (IOException ex) {}
            }
        }
        servidor = null;
        return this;
    }
    
    /**
     * Refresh server with give port. 
     *
     * @param port Port number for server at.
     * @return This object server.
     * @throws Exception Exception for <em><i>Invalid port number.</i></em>
     * @throws IOException IOException Exception of socket.
     */
    public Server refresh(int port) throws Exception, IOException{
        stop();
        if(port < 1 || port > 9999) throw new Exception("Invalid port number.");
        PORT = port;
        return init();        
    }
    
    /**
     * Get <i>IP</i> which server running at.
     *
     * @return <i>IP</i> address of server.
     */
    public static String getIp(){
        long ips = 0;
        String ipAddress = "127.0.0.1";
        try{
            ipAddress = InetAddress.getLocalHost().getHostAddress().trim();
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
        }catch (Exception e){}
        return ipAddress;
    }
    
}
