package printerserver.server;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

/**
 * Object to manage operating system printers.
 * 
 * This object seeks printers at operating system, 
 * prints and create files for that prints.
 *
 * @author Anderson Costa
 * @version 2019/01
 *
 * @see <a href="https://github.com/AsonCS/server_java" target="_blank">Repository on GitHub</a>
 */
public class Printer {
    
    private String printerName;
    private String content;

    /**
     * Directory for labels.
     */
    protected static final String DIRLABELS = "etiquetas";

    /**
     * Path for directory labels.
     */
    protected static final String PATHLABELS = DIRLABELS + "/";

    /**
     * Constructor with printer name and content for print.
     *
     * @param printerName Printer name for print.
     * @param content Text content for print.
     */
    public Printer(String printerName, String content) {
        this.printerName = printerName;
        this.content = content;
    }

    /**
     * Gets the printer current name.
     *
     * @return Printer current name.
     */
    public String getPrinterName() {
        return printerName;
    }

    /**
     * Alters the printer current name.
     *
     * @param printerName The printer name for replaced the current name.
     * @return {@link Printer}.
     */
    public Printer setPrinterName(String printerName) {
        this.printerName = printerName;
        return this;
    }

    /**
     * Gets the current content.
     *
     * @return Current content.
     */
    public String getCode() {
        return content;
    }

    /**
     * Alters the current content.
     *
     * @param content The content for replaced the current content.
     * @return {@link Printer}.
     */
    public Printer setCode(String content) {
        this.content = content;
        return this;
    }
    
    /**
     * Seeks printers at operating system, and also adds the <i>TST</i> printer for test.
     *
     * @return Printers list.
     */
    public static List<String> getPrinters(){
        ArrayList<String> list = new ArrayList<>(Arrays.asList("TST"));
        for(PrintService ps : PrintServiceLookup.lookupPrintServices(null, null)){
            list.add(ps.getName());
        }
        return list;
    }
    
    /**
     * Seeks printers at operating system with given filter,
     * and also adds the <i>TST</i> printer for test.
     *
     * @param filters Filter to be applied.
     * @return Printers list.
     */
    public static List<String> getPrinters(List<String> filters){
        ArrayList<String> list = new ArrayList<>(getPrinters());
        return list.stream().filter(e -> filters.contains(e) || e.toLowerCase().equals("tst")).collect(Collectors.toList());
     }
    
    /**
     * Method to print the current content at the current printer.
     *
     * @throws Exception {@link Exception} for <em><i>Without printer name</i></em>, <em><i>Printer not found</i></em> or <em><i>Without content</i></em>.
     * @throws PrintException {@link PrintException}.
     */
    public void print() throws Exception, PrintException{
        
        if(printerName == null) throw new Exception("Sem nome de impressora.");
        if(content == null) throw new Exception("Sem código.");
        
        if(printerName.toLowerCase().equals("tst")){
            createFile(printerName.toUpperCase());
            return;
        }
        
        PrintService printer = null;
        for(PrintService ps : PrintServiceLookup.lookupPrintServices(null, null)){
            if (ps.getName().toLowerCase().contains(printerName.toLowerCase())) { 
                printer = ps; 
                break; 
            } 
        }        
        
        if(printer == null) throw new Exception("Impressora não encontrada.");
        
        DocPrintJob job = printer.createPrintJob();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE; 
        Doc doc = new SimpleDoc(content.getBytes(), flavor, null);
        createFile(printerName.toUpperCase());
        job.print(doc, null);
    }
    
    private void createFile(String name){
        try{
            name += "_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            new File(DIRLABELS).mkdirs();
            File file = new File(PATHLABELS + name + ".txt");
            if(!file.exists()) file.createNewFile();
            FileWriter writer = new FileWriter(file, true);
            writer.write(content);
            writer.close();
        }catch (Exception e){}
    }

    @Override
    public String toString() {
        return "Printer{" + "printerName=" + printerName + ", zplCode=" + content + '}';
    }
}
