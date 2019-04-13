package printerserver.server;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;
import printerserver.server.Parameter.*;

public class ServerContext implements Runnable{
    
    private Handler handler;
    private Parameter[] parameters;
    private String header;
    private String body;
    private Method method;
    private String url;
    private final String hostAddress;
    
    private final ArrayList<Route> routes;
    
    private final Socket cliente;

    public ServerContext(Socket cliente, ArrayList<Route> routes){
        this.cliente = cliente;
        this.routes = routes;
        hostAddress = cliente.getInetAddress().getHostAddress();
    }

    @Override
    public void run(){
        try{
            String request = getRequest(cliente.getInputStream());
            splitHeaderBody(request);
            identifyMethod();
            identifyUrl();
            byte[] httpResponse = selectRoute();
            cliente.getOutputStream().write(httpResponse);
            cliente.getOutputStream().flush();
        }catch(Exception e){
            Debug.error(e.getMessage());
        }finally{
            try{
                cliente.close();
            }catch (Exception e){}
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
                body += b + "\r\n";
            }
        }
    }
    
    private void identifyMethod(){
        method = Method.GET;
        if(header.substring(0, 10).contains(Parameter.POST)) 
            method = Method.POST;
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
            params = QueryTransform.getKeyValue(url.split("[?]")[1], params);
            /*/
            for(String b : url.split("[?]")[1].split("&")){
                if(b.split("=").length > 1) params.put(b.split("=")[0], b.split("=")[1]);
                else params.put(b.split("=")[0], "");
            }// */
        }
        return params;
    }
    
    private byte[] selectRoute() throws UnsupportedEncodingException{
        for(Route route : routes){
            //System.out.println(url.split("[?]")[0] + " - " + route.getRoute() + " - " + url.split("[?]")[0].matches(route.getRoute()) + " - " + route.getMethod().equals(method));
            if(url.split("[?]")[0].matches(route.getRoute()) && route.getMethod().equals(method)){
                Debug.mensage("Cliente conectado: " + hostAddress);
                route.setParams(processUrl(url, route.getKeys()));
                return route.getHandler()
                        .handler(new Request(header, body, method, route.getParams()), new Response())
                        .getBytes();
            }
        }
        String headerResponse = "HTTP/1.1 404 NOT FOUND\r\n"
                + "Server: Anderson : 1.0\r\n"
                + "Date: " + new Date().toString() + "\r\n\r\n";
        return headerResponse.getBytes(Parameter.getEncoding(Encoding.UTF_8));
    }
    
}
