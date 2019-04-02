/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Date;

/**
 *
 * @author adm
 */
public class FileServer implements Runnable{
    

    @Override
    public  void run() {
        while(true){
            FileWriter file = null;

            try{
                File f = new File("C:/Users/adm/Documents/test.txt");
                if(!f.exists()) f.createNewFile();
                file = new FileWriter(f, true);
                String s = "test: " + new Date() + "\r\n";
                file.write(s);
                file.close();
                //System.out.println("foi...");
                Thread.sleep(60000);
            }catch(Exception e){
                System.err.println(e.getMessage());
                break;
            }
        }
    }
    
}
