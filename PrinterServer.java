package printerserver;

public class PrinterServer {
    public static void main(String[] args){
        try {
            new Server()
                .addRoute("/test/{name}/{id}/vai", handler, Parameter.GET, Parameter.TEXT_ZPL)
                .addRoute("/test/{name}", handler, Parameter.GET, Parameter.TEXT_ZPL)
                .addRoute("/test", handler1, Parameter.GET, Parameter.TEXT_ZPL)
                .start();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    private static Handler handler = (request, response) -> {
        response.setBody("<h1 style=\"color: red\">Olá mundo!!</h1>");
        //System.out.println("getParams: " + request.getParams());
        return response;
    };
    
    private static Handler handler1 = (request, response) -> {
        response.setBody(PrinterServer.zpl);
        //System.out.println("getParams: " + request.getParams());
        return response;
    };
    
    static String zpl = "<pre>^XA\n" +
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
