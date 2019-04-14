package printerserver;

import java.io.IOException;
import jdk.nashorn.api.scripting.AbstractJSObject;
import jdk.nashorn.api.scripting.JSObject;
import printerserver.server.Handler;
import printerserver.server.Parameter;
import printerserver.server.Parameter.*;
import printerserver.server.Server;
import printerserver.server.Printer;
import printerserver.server.QueryTransform;
import printerserver.server.Request;
import printerserver.server.Response;

public class CreateRoutes {
    
    public Server getServer() throws IOException{
        return new Server()
                .addRoute("/printer/{name}", Method.POST, printPost())
                .addRoute("/printers", Method.GET, printersGet())
                .addRoute("/printers", Method.POST, printersPost())
                .addRoute("/test", Method.GET, zplCode())
                .addRoute("/{name}", Method.GET, handler())
                .addRoute("/", Method.GET, handler())
        ;
    }
    
    private Handler printPost(){
        return (request, response) -> {
            if(request.getContentType() != ContentType.TEXT_ZPL){
                return response.setStatus(Status.NOT_FOUND);
            }
            Printer printer = new Printer(request.getParams().get("name"), request.getBody());
            try{
                printer.print();
            }catch(Exception e){
                System.err.println(e.getMessage());
                return response.setStatus(Status.NOT_FOUND);
            }
            return response.setBody(request.getBody());
        };
    }
    
    private Handler printersPost(){
        return (request, response) -> {
            String a = "{\"printers\": [";
            for(String b : Printer.getPrinters()){
                a += "\"" + b + "\",";
            }
            a = a.substring(0, a.length() - 1) + "]}";
            return response.setBody(a).setContentType(ContentType.APPLICATION_JSON);
        };
    }
    
    private Handler printersGet(){
        return (request, response) -> {
            String a = "";
            for(String b : Printer.getPrinters()){
                a += "<tr><td>" + QueryTransform.decode(b) + "</td><td>" + b + "</td></tr>";
            }
            String[][] b = {{"printers",a}};
            return response.readTemplate("templates/printers.html", b);
        };
    }
    
    private Handler handler(){
        return (request, response) -> {
            String a = Server.getIp() + ":" + Server.PORT + "/", c = request.getParams().get("name");
            System.out.println();
            c = c != null ? c : "";
            String[][] b = {
                {"address","<h2>Address: " + a + "</h2>"},
                {"name",c}
            };
            return response.readTemplate("templates/hello.html", b);
        };
    }
    
    private Handler zplCode(){
        return (request, response) -> {
            return response.readTemplate("templates/zpl_code.html");
        };
    }
    
}
