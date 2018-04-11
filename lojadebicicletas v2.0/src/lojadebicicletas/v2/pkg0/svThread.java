/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lojadebicicletas.v2.pkg0;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miguel Brandão
 */
public class svThread extends Thread {
    Socket socket;
    public svThread(Socket s){
        super();
        socket = s;
        start();
    }
    
    public void run(){
        try{
        ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
        os.writeObject("memes");
        System.out.println((SocketAddress)is.readObject());
        System.out.println((InetAddress)is.readObject());
        } catch (IOException ex) {
            Logger.getLogger(svThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(svThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
