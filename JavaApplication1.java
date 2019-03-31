package javaapplication1;

import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaApplication1 {
    public static void main(String[] args){
        try {
            new Server()
                .addRoute("/test/{name}/{id}/vai", handler, Parameter.GET, Parameter.TEXT_ZPL)
                .addRoute("/test/{name}", handler, Parameter.GET, Parameter.TEXT_ZPL)
                .addRoute("/test", handler, Parameter.GET, Parameter.TEXT_ZPL)
                .start();
        } catch (Exception ex) {
            Logger.getLogger(JavaApplication1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static Handler handler = (request, response) -> {
        //System.out.println();
        //System.out.println("getParams: " + request.getParams());
        //System.out.println("getBody: " + request.getBody());
        //System.out.println("getMethod: " + request.getMethod());
        //System.out.println("response: " + response.getBody());
        System.out.println("-------------------------------------------------");
        return response;
    };
}
