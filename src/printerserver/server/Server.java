package printerserver.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import printerserver.server.Parameter.*;

public class Server {
    
    public static final int PORT = 9101;
    public static final String DIRLABELS = "etiquetas";
    public static final String DIRTEMPLATES = "templates";
    public static final String DIRSTATICS = "statics";
    public static final String PATHLABELS = DIRLABELS + "/";
    
    private ArrayList<Route> routes;
    private boolean runStatus = false;
    private static Thread server;
    
    private ServerSocket servidor;

    public Server() throws IOException{
        servidor = new ServerSocket(PORT);
        this.routes = new ArrayList<>();
    }
    
    private String[] processRoute(String route){
        String[] response = new String[2];
        response[0] = "";
        response[1] = "";
        route = route.split("[?]")[0];
        if(route.split("/").length < 1){
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
            //new File("static").mkdirs();
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
    
}
