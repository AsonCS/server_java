/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

/**
 *
 * @author adm
 */
public class TestServer{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Thread t = new Thread(new FileServer());
        t.start();
    }
    
    
    
}
