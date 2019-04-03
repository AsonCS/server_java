package javaapplication1;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

public class Printer {
    
    private String printerName;
    private String zplCode;

    public Printer(String printerName, String zplCode) {
        this.printerName = printerName;
        this.zplCode = zplCode;
    }

    public String getPrinterName() {
        return printerName;
    }

    public Printer setPrinterName(String printerName) {
        this.printerName = printerName;
        return this;
    }

    public String getZplCode() {
        return zplCode;
    }

    public Printer setZplCode(String zplCode) {
        this.zplCode = zplCode;
        return this;
    }
    
    public String[] getPrinters(){
        PrintService[] printers = PrintServiceLookup.lookupPrintServices(null, null);
        String[] response = new String[printers.length];
        for(int i = 0; i < printers.length; i++){
            response[i] = printers[i].getName();
        }
        return response;
    }
    
    public void print() throws Exception, PrintException{
        
        if(printerName == null) throw new Exception("Sem nome de impressora.");
        if(zplCode == null) throw new Exception("Sem código zpl.");
        
        PrintService printer = null;
        for(PrintService ps : PrintServiceLookup.lookupPrintServices(null, null)){
            if (ps.getName().toLowerCase().indexOf(printerName.toLowerCase()) >= 0) { 
                printer = ps; 
                break; 
            } 
        }        
        
        if(printer == null) throw new Exception("Impressora não encontrada.");
        
        DocPrintJob job = printer.createPrintJob();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE; 
        Doc doc = new SimpleDoc(zplCode.getBytes(), flavor, null); 
        try {
            job.print(doc, null);
        } catch (PrintException ex) {
            throw ex;
        }
    }

    @Override
    public String toString() {
        return "Printer{" + "printerName=" + printerName + ", zplCode=" + zplCode + '}';
    }
    
    /*/
        String zebra = "ZEBRAtyut";
        Printer printer = new Printer(zebra, zplQRcode);
        for(String s : printer.getPrinters()) System.out.println(s);
        System.exit(0);
        try {
            printer.print();
        } catch (PrintException ex) {
            Logger.getLogger(JavaApplication1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(JavaApplication1.class.getName()).log(Level.SEVERE, null, ex);
        }
    
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
    
    static String zplQRcode = "^XA\n" +
                                "^MCY\n" +
                                "^XZ\n" +
                                "^XA\n" +
                                "^FWN^CFD,24^PW694^LH0,0\n" +
                                "^CI0^PR2^MNY^MTT^MMT^MD0^PON^PMN^LRN\n" +
                                "^XZ\n" +
                                "^XA\n" +
                                "^DFR:TEMP_FMT.ZPL\n" +
                                "^LRN\n" +
                                "^XZ\n" +
                                "^XA\n" +
                                "^XFR:TEMP_FMT.ZPL\n" +
                                "^FO138,180^GFA,04628,04628,00052,\n" +
                                "00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,000000000000\n" +
                                "0000000000000000000000030000000000C00000000000000000018000000000000000000060,0F\n" +
                                "FFFFFFF800FFFFFFC0000000FF8000007FF80000001FFE000000001FF00000FFFF000000000000\n" +
                                "00003FFFC0,0FFFFFFFF800FFFFFFC0000001FF800001FFFF0000007FFF800000003FF00007FFFF\n" +
                                "C000000000000001FFFFF0,0FFFFFFFF800FFFFFFC0000003FF800007FFFF800001FFFFE0000000\n" +
                                "7FF00007FFFFF000000000000001FFFFFC,0FFFFFFFF800FFFFFFC0000003FF80000FFFFFC00003\n" +
                                "FFFFF00000007FF00007FFFFF800000000000001FFFFFE,0FFFFFFFF800FFFFFFC0000007FF8000\n" +
                                "1FFFFFE00007FFFFF8000000FFF00007FFFFFC00000000000001FFFFFF,0FFFFFFFF800FFFFFFC0\n" +
                                "000007FF80003FE01FF0000FF80FF8000000FFF00007E00FFC00000000000001F803FF,00000003\n" +
                                "F000FC00000000000FDF80007F8007F0000FF003FC000001FBF000060003FE0000000000000180\n" +
                                "00FF80,00000007F000FC00000000001FDF80007F0003F8001FC001FE000003FBF000000001FE00\n" +
                                "00000000000000007F80,00000007E000FC00000000001F9F8000FE0001F8003F8000FE000003F3\n" +
                                "F000000000FE0000000000000000003F80,0000000FE000FC00000000003F9F8000FE0001FC003F\n" +
                                "8000FF000007F3F0000000007F0000000000000000001FC0,0000000FE000FC00000000007F1F80\n" +
                                "01FC0000FC007F00007F00000FE3F0000000007F0000000000000000001FC0,0000001FC000FC00\n" +
                                "000000007E1F8001FC0000FC007F00007F00000FC3F0000000007F0000000000000000001FC0,00\n" +
                                "00001FC000FC0000000000FE1F8001FC0000FE007E00003F80001FC3F0000000007F0000000000\n" +
                                "000000001FC0,0000003F8000FC0000000001FC1F8001F80000FE00FE00003F80003F83F0000000\n" +
                                "007F0000000000000000001FC0,0000003F8000FC0000000001F81F8001F800007E00FE00007F80\n" +
                                "003F03F0000000007E0000000000000000001F80,0000007F0000FC0000000003F81F8001F80000\n" +
                                "7E00FC0001FF80007F03F000000000FE0000000000000000003F80,0000007F0000FC0000000007\n" +
                                "F01F8001F800007E00FC0003FFC000FE03F000000000FE0000000000000000003F80,000000FE00\n" +
                                "00FC0000000007E01F8001FC00007F00FC000FFFC000FC03F000000001FC000000000000000000\n" +
                                "7F,000000FE0000FC000000000FE01F8001FC00007F00FC001FFFC001FC03F000000003F8000000\n" +
                                "000000000000FE,000001FC0000FFFF0000000FC01F8001FC00007F01FC003FFFC001F803F00000\n" +
                                "0007F0000000000000000001FC,000001FC0000FFFFF000001F801F8001FE00007F01FC00FFDFC0\n" +
                                "03F003F00000003FE000000000000000000FF8,000003F80000FFFFFC00003F801F8000FE00007F\n" +
                                "01FC01FF9FC007F003F000003FFFC0000000000000000FFFF0,000003F80000FFFFFF00003F001F\n" +
                                "8000FF0001FF01FC03FE0FC007E003F000003FFF00000000000000000FFFC0,000007F00000FFFF\n" +
                                "FF80007E001F80007FC00FFF01FC0FFC0FC00FC003F000003FFFE0000000000000000FFFF8,0000\n" +
                                "07F00000FFFFFFC000FE001F80007FFFFFFF01FC1FF00FC01FC003F000003FFFF8000000000000\n" +
                                "000FFFFE,00000FE000000000FFE000FC001F80003FFFFFFE01FC3FE00FC01F8003F000003FFFFC\n" +
                                "000000000000000FFFFF,00000FE0000000003FE001F8001F80001FFFFFFE01FCFFC00FC03F0003\n" +
                                "F00000000FFE000000000000000003FF80,00001FC0000000000FF003F8001F800007FFFE7E01FD\n" +
                                "FF001FC07F0003F000000001FF0000000000000000007FC0,00001FC00000000007F003F0001F80\n" +
                                "0001FFF87E01FFFE001FC07E0003F0000000007F8000000000000000001FE0,00003F8000000000\n" +
                                "07F007E0001F8000001F007E01FFFC001FC0FC0003F0000000003F8000000000000000000FE0,00\n" +
                                "003F800000000003F80FE0001F80000000007E00FFF0001FC1FC0003F0000000001FC000000000\n" +
                                "0000000007F0,00007F000000000003F80FC0001F8000000000FE00FFE0001F81F80003F0000000\n" +
                                "001FC0000000000000000007F0,00007F000000000003F80FFFFFFFFF80000000FC00FFC0001F81\n" +
                                "FFFFFFFFF00000001FC0000000000000000007F0,00007F000000000003F80FFFFFFFFF80000000\n" +
                                "FC00FF00001F81FFFFFFFFF00000001FC0000000000000000007F0,0000FE000000000003F80FFF\n" +
                                "FFFFFF80000001FC00FE00003F81FFFFFFFFF00000001FC0000000000000000007F0,0000FE0000\n" +
                                "00000003F00FFFFFFFFF80000001F8007E00003F81FFFFFFFFF00000001FC00000000000000000\n" +
                                "07F0,0001FC000000000007F00FFFFFFFFF80000003F8007F00003F01FFFFFFFFF00000001FC000\n" +
                                "0000000000000007F0,0001FC000000000007F00000001F8000000007F0007F00007F00000003F0\n" +
                                "000000001F80000000000000000007E0,0003F800000000000FE00000001F800000000FF0003F80\n" +
                                "007E00000003F0000000003F8000000000000000000FE0,0003F800000000001FE00000001F8000\n" +
                                "00001FE0003F8000FE00000003F0000000007F8000000000000000001FE0,0007F000000000003F\n" +
                                "C00000001F800000007FC0003FC001FC00000003F000000000FF0000000000000000003FC0,0007\n" +
                                "F000000000007FC00000001F80000003FF80001FE003FC00000003F000000001FE000000000000\n" +
                                "0000007F80,000FE00000008003FF800000001F80003FFFFF00000FF80FF800000003F00008000F\n" +
                                "FE000000000000020003FF80,000FE0000000FFFFFF000000001F80003FFFFE00000FFFFFF00000\n" +
                                "0003F0000FFFFFFC00000000000003FFFFFF,001FC0000000FFFFFE000000001F80003FFFF80000\n" +
                                "07FFFFE000000003F0000FFFFFF000000000000003FFFFFC,001FC0000000FFFFF8000000001F80\n" +
                                "003FFFE0000003FFFFC000000003F0000FFFFFE000000000000003FFFFF8,003F80000000FFFFE0\n" +
                                "000000001F80003FFF80000000FFFF8000000003F0000FFFFF8000000000000003FFFFE0,003F80\n" +
                                "000000FFFF80000000001F80003FF0000000003FFE0000000003F0000FFFFC0000000000000003\n" +
                                "FFFF,00000000000007F0000000000000000000000000000003E000000000000000007F80000000\n" +
                                "00000000001FE0,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,\n" +
                                "^BY2,2.5^FO143,80^B3N,N,104,N^FV75490433^FS\n" +
                                "^FO476,80^BQN,2,4^FH^FDMA,75490433^FS\n" +
                                "^FS\n" +
                                "^FO16,206^GB12,11,11,B,0^FS\n" +
                                "^FO684,206^GB12,11,11,B,0^FS\n" +
                                "^PQ1,0,1,Y\n" +
                                "^XZ\n" +
                                "^XA\n" +
                                "^IDR:TEMP_FMT.ZPL\n" +
                                "^XZ";
    
    // */  
}
