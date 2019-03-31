package javaapplication1;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

public class ServerContext implements Runnable{
    
    private static final String GET = "GET";
    private static final String POST = "POST";
    
    private InputStream in;
    private OutputStream out;
    
    private Handler handler;
    private Parameter[] parameters;
    private String header;
    private String body;
    private String method;
    private String url;
    private String hostAddress;
    
    private ArrayList<Route> routes;
    
    private Socket cliente;

    public ServerContext(Socket cliente, ArrayList<Route> routes){
        this.cliente = cliente;
        this.routes = routes;
        hostAddress = cliente.getInetAddress().getHostAddress();
    }
    
    private String getRequest(InputStream reader) throws Exception{
        String request = "";
        do{
            request += (char) reader.read();
        }while(reader.available()>0);
        return request;
    }
    
    private void splitHeaderBody(String request){
        boolean a = true;
        header = "";
        body = "";
        for(String b : request.split("\r\n")){
            if(a){
                header += b + "\n";
                if(b.length() == 0) a = !a;
            }else{
                body += b;
            }
        }
    }
    
    private void identifyMethod(){
        method = GET;
        if(header.substring(0, 10).contains(POST)) 
            method = POST;
    }
    
    private void identifyUrl(){
        url = "";
        int a = header.toLowerCase().indexOf("/");
        if(a < 0) return;
        int b = header.toLowerCase().indexOf(" ", a);
        if(b < 0) return;
        url = header.substring(a, b);
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
    
    private TreeMap<String, String> processUrl(String url, TreeMap<String, String> params){
        if(params == null) params = new TreeMap<>();
        if(url.split("[?]").length > 1){
            for(String b : url.split("[?]")[1].split("&")){
                if(b.split("=").length > 1) params.put(b.split("=")[0], b.split("=")[1]);
                else params.put(b.split("=")[0], "");
            }
        }
        return params;
    }

    @Override
    public void run(){
        try{
            System.out.println("-------------------------------------------------");
            System.out.println("Cliente conectado: " + hostAddress); 
            in = cliente.getInputStream();
            out = cliente.getOutputStream();
            String request = getRequest(in);
            splitHeaderBody(request);
            identifyMethod();
            identifyUrl();
            byte[] httpResponse = new byte[0];
            if(routes.size() > 0){
                routes.get(0).setParams(processUrl(url, routes.get(0).getParams()));
                System.out.println(header);
                System.out.println(routes.get(0).getParams());
                
                httpResponse = routes.get(0).getHandler()
                        .handler(new Request(body, method, routes.get(0).getParams()), new Response("test2"))
                        .getBytes();
            }
            if(httpResponse.length < 1) httpResponse = "HTTP/1.1 505 OK\r\n\r\n".getBytes("UTF-8");
            out.write(httpResponse);
            out.flush();
            in.close();
            out.close();
            cliente.close();
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
    
}
