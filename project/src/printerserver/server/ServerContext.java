package printerserver.server;

import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeMap;
import printerserver.server.Parameter.*;

/**
 * Object to find route and execute the method which will treat the request from client side.
 * 
 * This object find the route which user sent for server and perform the treatment method of this route.
 *
 * @author Anderson Costa
 * @version 2019/01
 *
 * @see <a href="https://github.com/AsonCS/server_java" target="_blank">Repository on GitHub</a>
 */
public class ServerContext implements Runnable{
    
    private Handler handler;
    private String header;
    private String body;
    private Method method;
    private String url;
    private final String hostAddress;
    
    private final ArrayList<Route> routes;
    
    private final Socket cliente;

    /**
     * Constructor with client connection and the list of routes.
     *
     * @param cliente Client connection with the stream of that.
     * @param routes List of routes for find out which match that client route.
     */
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
            Debug.error(e);
        }finally{
            try{
                cliente.close();
            }catch (Exception e){
                Debug.error(e);           
            }
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
        }
        return params;
    }
    
    private byte[] selectRoute() throws Exception{
        for(Route route : routes){
            //Debug.info(("/route" + url.split("[?]")[0]) + " - " + route.getRoute() + " - " + url.split("[?]")[0].matches(route.getRoute()) + " - " + route.getMethod().equals(method));
            if(("/route" + url.split("[?]")[0]).matches(route.getRoute()) && route.getMethod().equals(method)){
                Debug.message("Cliente conectado: " + hostAddress);
                route.setParams(processUrl(url, route.getKeys()));
                return route.getHandler()
                        .handler(new Request(header, body, method, route.getParams()), new Response())
                        .getBytes();
            }
        }
        return new Response().readFile(url.split("[?]")[0].substring(1, url.split("[?]")[0].length())).getBytes();
    }
    
}
