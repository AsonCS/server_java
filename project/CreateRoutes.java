package printerserver;

import printerserver.server.Handler;
import printerserver.server.Parameter.*;
import printerserver.server.Server;
import printerserver.server.Printer;

public class CreateRoutes {
    
    public Server getServer(){
        return new Server()
                .addRoute("/printers", Method.GET, printers)
                .addRoute("/test/{name}/{id}/vai", Method.GET, handler)
                .addRoute("/test/{name}", Method.GET, handler)
                .addRoute("/test", Method.GET, handler1)
                .addRoute("/printer/{name}", Method.GET, print)
        ;
    }
    
    private Handler printers = (request, response) -> {
        String a = "<html><table>";
        for(String b : Printer.getPrinters()){
            a += "<tr><td>" + b + "</td></tr>";
        }
        a += "</table></html>";
        //System.out.println(a);
        response.putBody(a);
        return response.setContentType(ContentType.TEXT_HTML);
    };
    
    private Handler print = (request, response) -> {
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
    
    private Handler handler = (request, response) -> {
        response.setBody("<h1 style=\"color: red\">Olá mundo!!</h1>");
        return response.setContentType(ContentType.TEXT_HTML);
    };
    
    private Handler handler1 = (request, response) -> {
        response.setBody(zpl).setContentType(ContentType.TEXT_ZPL);
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
