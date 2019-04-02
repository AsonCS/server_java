package javaapplication1;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    
    private ArrayList<Route> routes;

    public Server() {
        this.routes = new ArrayList<>();
    }
    
    private String[] processRoute(String route){
        String[] response = new String[2];
        if(route.split("/").length < 1){
        	response[0] = "";
            response[1] = "";
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
    
    public Server addRoute(String route, Handler handler, Parameter ...parameters){
        String[] map = processRoute(route);
        routes.add(new Route(map[0], map[1], handler, parameters));
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
            while (true) {
                try{
                    Socket cliente = servidor.accept();
                    cliente.setSoTimeout(10000);                
                    Thread t = new Thread(new ServerContext(cliente, routes));
                    t.start();
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
    }
    
}
    
    public Server removeRoute(int idx){
        if(idx > -1 && idx < routes.size()) routes.remove(idx);
        return this;
    }
    
    public void start() throws Exception{
        if(routes.size() > 0){
            ServerSocket servidor = new ServerSocket(8080);
            System.out.println("Servidor ouvindo a porta 8080");
            while (true) {
                try{
                    Socket cliente = servidor.accept();
                    cliente.setSoTimeout(10000);                
                    Thread t = new Thread(new ServerContext(cliente, routes));
                    t.start();
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
    }
    
}
