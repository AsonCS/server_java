package printerserver;

import printerserver.server.Printer;
import printerserver.server.Server;
import printerserver.server.Handler;
import java.lang.reflect.Array;
import printerserver.server.Parameter.*;

public class PrinterServer {
    public static void main(String[] args){
        try {
            new CreateRoutes().getServer().start();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
}
