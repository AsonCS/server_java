package printerserver;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import printerserver.server.Server;

/**
 * Object to configure server.
 * 
 * This object turn easy configuration server.
 *
 * @author Anderson Costa
 * @version 1.0
 *
 * @see <a href="https://github.com/AsonCS/server_java" target="_blank">Repository on GitHub</a>
 */
public class Config {    
    
    /**
     * Directory for config file.
     */
    protected static final String PATHCONFIG = "src/config";

    /**
     * Complete path for config template.
     */
    protected static final String TEMPL = PATHCONFIG + "/config.templ";

    /**
     * Complete path for config file.
     */
    protected static final String CONFIG = PATHCONFIG + "/config.config";    
    
    private int port = Server.PORT;
    private String ips = "*";
    private List<String> printers = Arrays.asList();
    private String config = "", rep;

    /**
     * Empty constructor, with the pattern parameters.
     */
    public Config() {
        new File(PATHCONFIG).mkdirs();
        config = readFile(CONFIG);
        indentifyPort();
        indentifyIP();
        indentifyPrinters();
    }
    
    /**
     * Close config file.
     */
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
                Scanner arq = new Scanner(file);
                while(arq.hasNextLine()) a += arq.nextLine() + "\n";
                arq.close();
                return a;
            }
        }catch (Exception e){}
        return "";
    }

    /**
     * Gets the current port.
     *
     * @return Current port.
     */
    public int getPort() {
        return port;
    }

    /**
     * Alters the current port.
     *
     * @param port The port for replaced the current port.
     * @return {@link Config}.
     */
    public Config setPort(int port) {
        config = config.replace(String.valueOf(this.port), String.valueOf(port));
        this.port = port;
        return this;
    }

    /**
     * Gets the current ips.
     *
     * @return Current ips.
     */
    public String getIps() {
        return ips;
    }

    /**
     * Alters the current ips.
     *
     * @param ips The ips for replaced the current ips.
     * @return {@link Config}.
     */
    public Config setIps(String ips) {
        config = config.replace(this.ips, ips);
        this.ips = ips;
        return this;
    }

    /**
     * Gets the current printers.
     *
     * @return Current printers.
     */
    public List<String> getPrinters() {
        return printers;
    }

    /**
     * Alters the current printers.
     *
     * @param printers The printers for replaced the current printers.
     * @return {@link Config}.
     */
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
