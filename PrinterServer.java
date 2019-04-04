package printerserver;

import java.lang.reflect.Array;

public class PrinterServer {
    public static void main(String[] args){
        try {
            new Server()
                .addRoute("/test/{name}/{id}/vai", handler, Parameter.GET)
                .addRoute("/test/{name}", handler, Parameter.GET)
                .addRoute("/test", handler1, Parameter.GET)
                .addRoute("/printers", printersName, Parameter.GET)
                .addRoute("/printer/{name}", print, Parameter.GET)
                .start();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    private static Handler printersName = (request, response) -> {
        String a = "<html><table>";
        for(String b : Printer.getPrinters()){
            a += "<tr><td>" + b + "</td></tr>";
        }
        a += "</table></html>";
        //System.out.println(a);
        response.putBody(a);
        return response;
    };
    
    private static Handler print = (request, response) -> {
        Printer printer = new Printer(request.getParams().get("name"), PrinterServer.zpl);
        response.putBody("<pre>" + PrinterServer.zpl);
        try{
            printer.print();
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
        return response;
    };
    
    private static Handler handler = (request, response) -> {
        response.setBody("<h1 style=\"color: red\">Olá mundo!!</h1>");
        return response;
    };
    
    private static Handler handler1 = (request, response) -> {
        response.setBody("<pre>" + PrinterServer.zpl);
        return response;
    };
    
    static String zpl = "^XA\n" +
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
