package printerserver.server;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import printerserver.server.Parameter.*;

public class Server {
    
    private ArrayList<Route> routes;

    public Server() {
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
    
    public void start() throws Exception{
        if(routes.size() > 0){
            ServerSocket servidor = new ServerSocket(8080);
            System.out.println("Servidor ouvindo a porta 8080");
            new File("etiquetas").mkdirs();
            new File("static").mkdirs();
            new File("template").mkdirs();
            while (true) {
                try{
                    Socket cliente = servidor.accept();
                    cliente.setSoTimeout(1000);
                    Thread t = new Thread(new ServerContext(cliente, routes));
                    t.start();
                } catch (Exception ex) {
                    //System.err.println(ex.getMessage());
                }
            }
        }
    }
    
}
