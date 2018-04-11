/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lojadebicicletas.v2.pkg0;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Miguel Brandão
 */
public class ebay {
    
    ServerSocket myServer = null;
    Socket sServidor= null;
    
    public ebay() throws IOException{
        myServer = new ServerSocket(2222);
        while(true){
            try{
                new svThread(myServer.accept());
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
    }
    public static void main (String args[]) throws IOException{
        System.out.println("Creating Server");
        new ebay();
    }
}
