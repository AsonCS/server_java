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
    
    private TreeMap<String, Object> processRoute(String route){
        TreeMap<String, Object> objects = new TreeMap<>();
        TreeMap<String, String> params = new TreeMap<>();
        String response = "";
        if(route.split("/").length < 1){
            objects.put("route", response);
            objects.put("params", params);
            return objects;
        }
        for(String b : route.split("/")){
            if(!b.contains("{") && !b.contains("}")){
                response = b.length() > 0 ? response + "/" + b : response + "";
            }else{
                response = b.length() > 0 ? response + "/([^/]+)" : response + "";
                params.put(b.substring(b.indexOf("{") + 1, b.indexOf("}")), "");
            }
        }
        //System.out.println(url.matches(this.route));
        objects.put("route", response);
        objects.put("params", params);
        return objects;
    }
    
    public Server addRoute(String route, Handler handler, Parameter ...parameters){
        TreeMap<String, Object> map = processRoute(route);
        route = (String) map.get("route");
        TreeMap<String, String> params = (TreeMap<String, String>) map.get("params");
        routes.add(new Route(route, handler, parameters, params));
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
