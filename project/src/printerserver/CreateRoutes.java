<<<<<<< HEAD
package printerserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import printerserver.server.Debug;
import printerserver.server.Handler;
import printerserver.server.Parameter.*;
import printerserver.server.Server;
import printerserver.server.Printer;
import printerserver.server.QueryTransform;
import printerserver.server.Response;

public class CreateRoutes {
    
    protected static final String PATHCONFIG = "src/config/";
    
    public Server getServer() throws IOException{ 
        return new Server()
                .addRoute("/config", Method.GET, configGet())
                .addRoute("/config", Method.POST, configPost())
                .addRoute("/allPrinters", Method.POST, allPrinters())
                .addRoute("/setPrinters", Method.POST, setPrinters())
                .addRoute("/config/{file}", Method.GET, configFilesGet())
                .addRoute("/printer/{name}", Method.POST, printPost())
                .addRoute("/printers", Method.GET, printersGet())
                .addRoute("/printers", Method.POST, printersPost())
                .addRoute("/test", Method.GET, zplCode())
        ;
    }
    
    private Response readForm(Response response){
        Config config = new Config();
        List<String> listConfig = config.getPrinters();
        config.close();
        List<String> listPrinTots = Printer.getPrinters();
        String body = "", name = "", selected = "checked";
        for(int i = 0; i < listPrinTots.size(); i++){
            name = listPrinTots.get(i);
            if(name.toLowerCase().equals("tst")){
                continue;
            }
            selected = "";
            if(listConfig.contains(name)){
                selected = "checked";
            }
            body += "<tr><td>"
                    + "<label for=\"printercheck" + i + "\">"
                        + "<input type=\"checkbox\" id=\"printercheck" + i + "\" name=\"printercheck" + i + "\" " + selected + "/>: " + name.replace("\\", "\\\\") + ""
                    + "</label>"
                + "</td></tr>";
            body += "<input type=\"text\" name=\"printername" + i + "\" value=\"" + name.replace("\\", "\\\\") + "\" hidden/>";
        }
        body += "<tr><td><label>Senha: <input type=\"text\" name=\"senha\" value=\"1234\" /></label></td></tr>";
        String[][] variables = {
            {"body",body}
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
            response.readFile(name);
            return response;
        };
    }
    
    private Handler configPost(){
        return (request, response) -> {
            request.processKeyValue();
            if(!request.getParams().get("senha").trim().equals("1234")){
                return response.setStatus(Status.NOT_FOUND);
            }
            List<String> list = new ArrayList<>();
            for(String value : request.getParams().keySet()){
                if(value.contains("printercheck")){
                    String a = value.replace("printercheck", "printername");
                    list.add(QueryTransform.decode(request.getParams().get(a)));
                }
            }
            new Config().setPrinters(list).close();
            response = readForm(response);
            return response;
        };
    }
    
    private Handler printPost(){
        return (request, response) -> {
            Printer printer = new Printer(QueryTransform.decode(request.getParams().get("name")), request.getBody());
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
            try{
                Config config = new Config();
                List<String> prints = Printer.getPrinters(config.getPrinters());
                config.close();
                JSONArray array = new JSONArray();
                for(int i = 0; i < prints.size(); i++){
                    array.put(new JSONObject()
                            .put(prints.get(i), QueryTransform.encode(prints.get(i)))
                    );
                }
                return response.setBody(new JSONObject().put("printers", array).toString()).setContentType(ContentType.APPLICATION_JSON);
            } catch (Exception e){
                Debug.error(e);
                return response.setStatus(Status.NOT_FOUND);
            }
        };
    }
    
    private Handler printersGet(){
        return (request, response) -> {
            Config config = new Config();
            List<String> list = config.getPrinters();
            config.close();   
            String a = "";
            for(String b : Printer.getPrinters(list)){
                a += "<tr><td>" + b.replace("\\", "\\\\") + "</td><td>" + QueryTransform.encode(b) + "</td></tr>";
            }
            String[][] b = {{"printers",a}};
            return response.readTemplate("templates/printers.html", b);
        };
    }
    
    private Handler allPrinters(){
        return (request, response) -> {
            try{
                Config config = new Config();
                List<String> list = config.getPrinters(), prints = Printer.getPrinters();
                config.close();
                JSONArray array = new JSONArray();
                for(int i = 0; i < prints.size(); i++){
                    if(prints.get(i).equalsIgnoreCase("tst")) continue;
                    array.put(new JSONObject()
                            .put("name", prints.get(i))
                            .put("status", list.contains(prints.get(i)))
                    );
                }
                return response.setBody(new JSONObject().put("printers", array).toString()).setContentType(ContentType.APPLICATION_JSON);
            } catch (Exception e){
                Debug.error(e);
                return response.setStatus(Status.NOT_FOUND);
            }
        };
    }
    
    private Handler setPrinters(){
        return (request, response) -> {
            try{
                JSONArray array = new JSONObject(request.getBody()).getJSONArray("printers");
                ArrayList<String> list = new ArrayList<>();
                array.forEach(e -> list.add(e.toString()));
                new Config().setPrinters(list).close();
            }catch (Exception e){
                Debug.error(e);
                return response.setStatus(Status.NOT_FOUND);
            }
            return response;
        };
    }
    
    private Handler zplCode(){
        return (request, response) -> {
            return response.readFile("templates/zpl_code.html");
        };
    }
    
}
=======
package printerserver;

import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import jdk.nashorn.api.scripting.AbstractJSObject;
import jdk.nashorn.api.scripting.JSObject;
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
        ;
    }
    
    private Response readForm(Response response){
        Config config = new Config();
        List<String> listConfig = config.getPrinters();
        config.close();        
        List<String> listPrinTots = Printer.getPrinters();
        List<String> listPrin = Printer.getPrinters(listConfig);
        int size = listPrinTots.size() > listConfig.size() ? listPrinTots.size() : listConfig.size();
        size = listPrin.size() > size ? listPrin.size() : size;
        String printers = "", body = "", a = "", b = "", c = "";
        for(int i = 0; i < size; i++){
            a = i < listPrinTots.size() ? listPrinTots.get(i) : "";
            b = i < listPrin.size() ? listPrin.get(i) : "";
            c = i < listConfig.size() ? listConfig.get(i) : "";
            printers += "<tr>";
            printers += "<td><pre>" + a.replace("\\", "\\\\") + "</pre></td>";
            printers += "<td><pre>" + b.replace("\\", "\\\\") + "</pre></td>";
            printers += "<td><pre>" + c.replace("\\", "\\\\") + "</pre></td>";            
            printers += "</tr>";
        }
        for(int i = 0; i < 18; i++){
            a = i < listConfig.size() ? listConfig.get(i) : "";
            boolean e = (i == 3 || i == 6 || i == 9 || i == 12 || i == 15 || i == 18);
            if(e) body += "</tr>\n<tr>";
            else if(i == 0) body += "<tr>";
            else if(i == 21) body += "</tr>";
            body += "<td><input type=\"text\" name=\"printer" + i + "\" value=\"" + a.replace("\\", "\\\\") + "\" size=\"35\" /></td>";
        }
        String[][] variables = {
            {"address","<h2>Address: " + getIp() + ":" + PORT + "/</h2>"},
            {"printers",printers},
            {"body",body}
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
            response.readFile(name);
            return response;
        };
    }
    
    private Handler configPost(){
        return (request, response) -> {
            request.processKeyValue();
            List<String> list = new ArrayList<>();
            for(String value : request.getParams().values()){
                if(value.trim().length() > 0) list.add(QueryTransform.decode(value));
            }
            if(list.size() > 0){
                new Config().setPrinters(list).close();                
            }
            response = readForm(response);
            return response;
        };
    }
    
    private Handler printPost(){
        return (request, response) -> {
            Printer printer = new Printer(QueryTransform.decode(request.getParams().get("name")), request.getBody());
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
            Config config = new Config();
            List<String> list = config.getPrinters();
            config.close(); 
            String a = "{\"printers\": [";
            for(String b : Printer.getPrinters(list)){
                a += "\"" + QueryTransform.encode(b) + "\",";
            }
            a = a.substring(0, a.length() - 1) + "]}";
            return response.setBody(a).setContentType(ContentType.APPLICATION_JSON);
        };
    }
    
    private Handler printersGet(){
        return (request, response) -> {
            Config config = new Config();
            List<String> list = config.getPrinters();
            config.close();   
            String a = "";
            for(String b : Printer.getPrinters(list)){
                a += "<tr><td>" + b.replace("\\", "\\\\") + "</td><td>" + QueryTransform.encode(b) + "</td></tr>";
            }
            String[][] b = {{"printers",a}};
            return response.readTemplate("templates/printers.html", b);
        };
    }
    
    private Handler zplCode(){
        return (request, response) -> {
            return response.readFile("templates/zpl_code.html");
        };
    }
    
}
>>>>>>> c349b21af459fa604849a938a05082d9af388b38
