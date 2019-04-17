package printerserver.server;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import javafx.print.Collation;

public class Config {    
    
    protected static final String PATHCONFIG = "src/config";
    protected static final String TEMPL = PATHCONFIG + "/config.templ";
    protected static final String CONFIG = PATHCONFIG + "/config.config";    
    
    private int port = Server.PORT;
    private String ips = "*";
    private List<String> printers = Arrays.asList("");
    private String config = "", rep;

    public Config() {
        new File(PATHCONFIG).mkdirs();
        config = readFile(CONFIG);
        indentifyPort();
        indentifyIP();
        indentifyPrinters();
    }
    
    public void close(){
        try{
            File file = new File(CONFIG);
            if(!file.exists()) file.createNewFile();
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write(config);
            fileWriter.close();
        }catch (Exception e){}
    }
    
    private void indentifyPort(){
        int a = config.indexOf("PORT=");
        if(a == -1) return;
        a += 5;
        try{
            port = Integer.valueOf(config.substring(a, config.indexOf("\n", a)).trim());
        }catch (Exception e){
            port = 9101;
        }
    }
    
    private void indentifyIP(){
        int a = config.indexOf("FILTERIP=");
        if(a == -1) return;
        a += 9;
        try{
            ips = config.substring(a, config.indexOf("\n", a)).trim();
        }catch (Exception e){
            ips = "*";
        }
    }
    
    private void indentifyPrinters(){
        int a = config.indexOf("SCAPESNAMES\n[");
        if(a == -1) return;
        a += 14;
        try{
            String b = config.substring(a, config.indexOf("]", a));
            printers = Arrays.asList(b.split("\n"));
            printers.forEach(e -> e.trim());
        }catch (Exception e){}
    }
    
    private String readFile(String fileName){
        String a = "";
        try{
            File file = new File(fileName);
            if(file.exists()){
                FileReader fileReader = new FileReader(file);
                char[] buffer = new char[1024];
                while(fileReader.read(buffer) != -1) a += String.copyValueOf(buffer);
                fileReader.close();
                return a;
            }
        }catch (Exception e){}
        return "";
    }

    public int getPort() {
        return port;
    }

    public Config setPort(int port) {
        config = config.replace(String.valueOf(this.port), String.valueOf(port));
        this.port = port;
        return this;
    }

    public String getIps() {
        return ips;
    }

    public Config setIps(String ips) {
        config = config.replace(this.ips, ips);
        this.ips = ips;
        return this;
    }

    public List<String> getPrinters() {
        return printers;
    }

    public Config setPrinters(List<String> printers) {
        rep = "SCAPESNAMES\n[\n";
        printers.forEach(e -> rep += e + "\n");
        rep += "]";
        int a = config.indexOf("SCAPESNAMES\n[");
        if(a == -1){
            config += rep;
        }
        try{
            config = config.replace(config.substring(a, config.indexOf("]", a) + 1), rep);
        }catch (Exception e){}
        this.printers = printers;
        return this;
    }    

    @Override
    public String toString() {
        return "Config{" + "port=" + port + ", ips=" + ips + ", printers=" + printers + ", config=" + config + '}';
    }    
    
}
