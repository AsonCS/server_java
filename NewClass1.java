/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 *
 * @author anderson
 */
public class NewClass1 implements Runnable{
    
    private Socket cliente;

    public NewClass1(Socket cliente) {
        System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
        this.cliente = cliente;
    }
    
    

    @Override
    public void run() {
        try {
            // Instancia o ServerSocket ouvindo a porta 12345
            //ServerSocket servidor = new ServerSocket(8080);
            //System.out.println("Servidor ouvindo a porta 8080");
            //while(true) {
                // o método accept() bloqueia a execução até que
                // o servidor receba um pedido de conexão
                InputStream input = cliente.getInputStream();
                String request = "";
                do{
                    request += (char) input.read();
                }while(input.available()>0);
                System.out.println(request);
                PrintWriter out = new PrintWriter(cliente.getOutputStream());
                out.println("HTTP/1.1 200 OK");
                out.println("Server: Anderson : 1.0");
                out.println("Date: " + new Date().toString());
                out.println("content-length: 0");
                out.println();
                System.out.println(request);
                out.flush();
                input.close();
                out.close();
                //cliente.close();
            //}  
        }   
        catch(Exception e) {
           System.out.println("Erro: " + e.getMessage());
        }
        finally {} 
    }
    
    
    
}
