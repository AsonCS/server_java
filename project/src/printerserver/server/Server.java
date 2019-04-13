package printerserver.server;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import printerserver.server.Parameter.*;

public class Server {
    
    public static final int PORT = 9101;
    protected static final String DIRLABELS = "etiquetas";
    protected static final String DIRTEMPLATES = "templates";
    protected static final String DIRSTATICS = "statics";
    protected static final String PATHLABELS = DIRLABELS + "/";
    protected static final String PATHCONFIG = "src/config/config.templ";
    
    private ArrayList<Route> routes;
    private boolean runStatus = false;
    private static Thread server;
    
    private ServerSocket servidor;

    public Server() throws IOException{
        servidor = new ServerSocket(PORT);
        this.routes = new ArrayList<>();
        addRoute("/config", Method.GET, configGet());
        addRoute("/config", Method.POST, configPost());
        addRoute("/config/{file}", Method.GET, configFilesGet());
        addRoute("/static/{name1}", Method.GET, staticGet());
        addRoute("/static/{name1}/{name2}", Method.GET, staticGet());
        addRoute("/static/{name1}/{name2}/{name3}", Method.GET, staticGet());
        addRoute("/static/{name1}/{name2}/{name3}/{name4}", Method.GET, staticGet());
        addRoute("/static/{name1}/{name2}/{name3}/{name4}/{name5}", Method.GET, staticGet());
    }
    
    private String[] processRoute(String route){
        String[] response = new String[2];
        response[0] = "";
        response[1] = "";
        route = route.split("[?]")[0];
        if(route.split("/").length <= 1){
            response[0] = "/";
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
    
    public Server addRoute(String route, Method method, Handler handler){
        String[] map = processRoute(route);
        routes.add(new Route(map[0], map[1].split("/"), handler, method));
        Collections.sort(routes);
        return this;
    }
    
    public Server removeRoute(int idx){
        if(idx > -1 && idx < routes.size()) routes.remove(idx);
        return this;
    }
    
    public Server init() throws IOException{
        if(servidor == null){
            servidor = new ServerSocket(PORT);
        }
        if(routes.size() > 0){
            new File(DIRLABELS).mkdirs();
            new File(DIRSTATICS).mkdirs();
            new File(DIRTEMPLATES).mkdirs();
            new Thread(){
                @Override
                public void run() {
                    Debug.mensage("Servidor ouvindo a porta " + PORT);
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
    
    public void start(){
        runStatus = true;
    }
    
    public void pause(){
        runStatus = false;
    }
    
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
    
    private Response readForm(Response response){        
        String a = "";
        for(String b : Printer.getPrinters()){
            a += "<tr><td>" + QueryTransform.decode(b) + "</td></tr>";
        }
        String[][] variables = {
            {"address","<h2>Address: " + getIp() + ":" + PORT + "/</h2>"},
            {"printers",a}
        };
        return response.readFile(PATHCONFIG, variables).setContentType(ContentType.TEXT_HTML);
    }
    
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
    
    private Handler configGet(){
        return (request, response) -> {
            response = readForm(response);
            return response;
        };
    }
    
    private Handler configFilesGet(){
        return (request, response) -> {
            String name = "src/config/" + request.getParams().get("file");
            String[] a = name.split("\\.");
            if(a.length > 0) response.setContentType(Parameter.indentifyContentType(a[a.length - 1]));
            response.readFile(name);
            return response;
        };
    }
    
    private Handler configPost(){
        return (request, response) -> {
            response = readForm(response);
            return response;
        };
    }
    
    private Handler staticGet(){
        return (request, response) -> {
            String name = DIRSTATICS;
            for(int i = 1; i <= request.getParams().keySet().size(); i++){
                name += "/" + request.getParams().get("name"+i);
            }
            String[] a = name.split("\\.");
            if(a.length > 0) response.setContentType(Parameter.indentifyContentType(a[a.length - 1]));
            response.readFile(name);
            return response;
        };
    }
    
}
