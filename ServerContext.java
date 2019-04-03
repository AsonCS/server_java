package printerserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
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

    @Override
    public void run(){
        try{
            in = cliente.getInputStream();
            out = cliente.getOutputStream();
            String request = getRequest(in);
            splitHeaderBody(request);
            identifyMethod();
            identifyUrl();
            byte[] httpResponse = selectRoute();
            out.write(httpResponse);
            in.close();
            out.flush();
            out.close();
            cliente.close();
        }catch(Exception e){
            System.err.println("e" + e.getMessage());
        }
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
    
    private TreeMap<String, String> processUrl(String url, String[] keys){
        TreeMap<String, String> params = new TreeMap<>();
        String[] a = url.split("[?]"), c = a[0].split("/");
        if(c.length == keys.length){
            for(int i = 1; i < c.length; i++){
                if(!keys[i].equals("?")){
                    params.put(keys[i], c[i]);
                }
            }
        }
        if(url.split("[?]").length > 1){
            for(String b : url.split("[?]")[1].split("&")){
                if(b.split("=").length > 1) params.put(b.split("=")[0], b.split("=")[1]);
                else params.put(b.split("=")[0], "");
            }
        }
        return params;
    }
    
    private byte[] selectRoute() throws UnsupportedEncodingException{        
        for(Route route : routes){
            if(url.split("[?]")[0].matches(route.getRoute()) && false){
                System.out.println("Cliente conectado: " + hostAddress);
                route.setParams(processUrl(url, route.getKeys()));
                System.out.println(route.getParams());
                return route.getHandler()
                        .handler(new Request(body, method, route.getParams()), new Response("test2"))
                        .getBytes();
            }
        }
        return "HTTP/1.1 404 OK\r\n\r\n".getBytes("UTF-8");
    }
    
}
