/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author anderson
 */
public class NewClass {
    public static void main() {

        int timeout=50;
        int port = 3000;

        try {
            String currentIP = "192.168.0.";
            String subnet = "192.168.0.";//getSubnet(currentIP);
            //System.out.println("subnet: " + subnet);

            for (int i=2;i<254;i++){

                String host = subnet + i;
                //System.out.println("Checking :" + host);
                InetAddress ia = InetAddress.getByName(host);
                if (ia.isReachable(timeout)){
                    //System.out.println(host + " is reachable");
                    try {
                        Socket connected = new Socket(host, port);
                        System.out.println(ia.getHostAddress());
                    }
                    catch (Exception s) {
                        System.out.println(s);
                    }
                }
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public static String getSubnet(String currentIP) {
        int firstSeparator = currentIP.lastIndexOf("/");
        int lastSeparator = currentIP.lastIndexOf(".");
        return currentIP.substring(firstSeparator+1, lastSeparator+1);
    }  
}
