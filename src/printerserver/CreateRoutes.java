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

public class CreateRoutes {
    
    public Server getServer() throws IOException{
        return new Server()
                .addRoute("/printers", Method.GET, printersGet)
                .addRoute("/printers", Method.POST, printersPost)
                .addRoute("/test/{name}/{id}/vai", Method.GET, handler)
                .addRoute("/test/{name}", Method.GET, handler)
                .addRoute("/test", Method.GET, handler1)
                .addRoute("/printer/{name}", Method.GET, printGet)
                .addRoute("/printer/{name}", Method.POST, printPost)
        ;
    }
    
    private Handler printPost = (request, response) -> {
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
        return response.setBody(request.getBody()).setContentType(ContentType.TEXT_ZPL);
    };
    
    private Handler printGet = (request, response) -> {
        Printer printer = new Printer(request.getParams().get("name"), zpl);
        try{
            printer.print();
            response.putBody(zpl).setContentType(ContentType.TEXT_ZPL);
        }catch(Exception e){
            response.setStatus(Status.NOT_FOUND);
            //System.err.println(e.getMessage());
        }
        return response;
    };
    
    private Handler printersPost = (request, response) -> {
        String a = "{\"printers\": [";
        for(String b : Printer.getPrinters()){
            a += "\"" + b + "\",";
        }
        a = a.substring(0, a.length() - 1) + "]}";
        return response.setBody(a).setContentType(ContentType.APPLICATION_JSON);
    };
    
    private Handler printersGet = (request, response) -> {
        String a = "<html><table>";
        a += "<thead><tr><th>Printer name</th><th>Param name</th></tr></thead>";
        a += "<tbody>";
        for(String b : Printer.getPrinters()){
            a += "<tr><td>" + QueryTransform.decode(b) + "</td><td>" + b + "</td></tr>";
        }
        a += "<tbody></table></html>";
        //System.out.println(a);
        response.putBody(a);
        return response.setContentType(ContentType.TEXT_HTML);
    };
    
    private Handler handler = (request, response) -> {
        response.setBody("<h1 style=\"color: red\">Olá mundo!!</h1>");
        return response.setContentType(ContentType.TEXT_HTML);
    };
    
    private Handler handler1 = (request, response) -> {
        response.setBody(zpl.replace("\n", "<br>")).setContentType(ContentType.TEXT_HTML);
        return response;
    };
    
    public static String zpl = "^XA\n" +
                        "^MCY\n" +
                        "^XZ\n" +
                        "^XA\n" +
                        "^FWN^CFD,32^PW798^LH0,0\n" +
                        "^CI0^PR2^MNY^MTT^MMT^MD0^PON^PMN^LRN\n" +
                        "^XZ\n" +
                        "^XA\n" +
                        "^DFR:TEMP_FMT.ZPL\n" +
                        "^LRN\n" +
                        "^XZ\n" +
                        "^XA\n" +
                        "^XFR:TEMP_FMT.ZPL\n" +
                        "^FO4,11^GB792,450,7,B,0^FS\n" +
                        "^BY3^FO200,149^BCN,182,N,N,N^FD>:21156^FS\n" +
                        "^ADN,28,22^FO50,58^FDMINERAL CREAM 100ML^FS\n" +
                        "^ADN,52,30^FO170,360^FD21156^FS\n" +
                        "^ADN,18,10^FO334,425^FDAUTOLOG WMS^FS\n" +
                        "^PQ,0,1,Y\n" +
                        "^XZ";
    
}
