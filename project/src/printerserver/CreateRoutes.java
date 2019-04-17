package printerserver;

import java.io.IOException;
import jdk.nashorn.api.scripting.AbstractJSObject;
import jdk.nashorn.api.scripting.JSObject;
import printerserver.server.Config;
import printerserver.server.Debug;
import printerserver.server.Handler;
import printerserver.server.Parameter;
import printerserver.server.Parameter.*;
import printerserver.server.Server;
import printerserver.server.Printer;
import printerserver.server.QueryTransform;
import printerserver.server.Request;
import printerserver.server.Response;
import static printerserver.server.Server.PORT;
import static printerserver.server.Server.getIp;

public class CreateRoutes {
    
    protected static final String PATHCONFIG = "src/config/";
    
    public Server getServer() throws IOException{ 
        /*Config config = new Config();
        //System.out.println(config.toString());
        config.setPort(9101);     
        config.setIps("*");
        config.setPrinters(Arrays.asList("123","456","789","TST","eee"));
        //System.out.println(config.toString());
        config.close();
        PORT = config.getPort();// */
        return new Server()
                .addRoute("/config", Method.GET, configGet())
                .addRoute("/config", Method.POST, configPost())
                .addRoute("/config/{file}", Method.GET, configFilesGet())
                .addRoute("/printer/{name}", Method.POST, printPost())
                .addRoute("/printers", Method.GET, printersGet())
                .addRoute("/printers", Method.POST, printersPost())
                .addRoute("/test", Method.GET, zplCode())
                .addRoute("/{name}", Method.GET, handler())
                .addRoute("/", Method.GET, handler())
        ;
    }
    
    private Response readForm(Response response){        
        String a = "", b = "", c = "", d = "";
        for(String z : Printer.getPrinters()){
            a += "<tr><td>" + QueryTransform.decode(z) + "</td></tr>";
        }
        Config config = new Config();
        for(String z : config.getPrinters()){
            b += "<tr><td>" + z + "</td></tr>";
        }
        for(int i = 1; i <= config.getPrinters().size(); i++){
            c += "<tr>"
                    + "<td><label>Printer: "
                        + "<input type=\"text\" name=\"printer" + i + "\" value=\"" + config.getPrinters().get(i-1) + "\" />"
                    + "</label></td>"
                    + "<td><input type=\"button\" value=\"add\" /></td><td><input type=\"button\" value=\"rmv\" /></td>"
                + "</tr>";
        }
        String[][] variables = {
            {"address","<h2>Address: " + getIp() + ":" + PORT + "/</h2>"},
            {"printers",a},
            {"scapes",b},
            {"body",c}
        };
        return response.readTemplate(PATHCONFIG + "config.templ", variables).setContentType(ContentType.TEXT_HTML);
    }
    
    private Handler configGet(){
        return (request, response) -> {
            response = readForm(response);
            return response;
        };
    }
    
    private Handler configFilesGet(){
        return (request, response) -> {
            String name = PATHCONFIG + request.getParams().get("file");
            response.readTemplate(name);
            return response;
        };
    }
    
    private Handler configPost(){
        return (request, response) -> {
            request.processKeyValue();
            Debug.info(request.getParams());
            response = readForm(response);
            return response;
        };
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
