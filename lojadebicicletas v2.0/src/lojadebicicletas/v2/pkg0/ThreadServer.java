package lojadebicicletas.v2.pkg0;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadServer extends Thread{
    int port;
    ServerSocket serversock;
    Socket sock;
    public ThreadServer(int port){ 
        setDaemon(true);
        this.port=port;
    }
    public void run(){
        while(true){
            try {
                serversock = new ServerSocket(port);
            }
            catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            try {
                String WR;
                sock = serversock.accept();
                ObjectInputStream is = new ObjectInputStream(sock.getInputStream());
                ObjectOutputStream os = new ObjectOutputStream(sock.getOutputStream());
                System.out.println("Contacto de " + (String) is.readObject() + ". !exit para sair.");// Recebe ip de quem o contacta
                os.writeObject("ip teste"); //Envia ip para o outro cliente
                while(true){
                    System.out.println("Mensagem do outro Cliente:");
                    WR = (String) is.readObject();
                    if(WR.equals("!exit")){
                        System.out.println("Linha Fechada do outro lado.");
                        break;
                    }
                    System.out.println(WR);
                    System.out.println("Mensagem para o outro Cliente:");
                    WR = Ler.umaString();
                    os.writeObject(WR);
                    if (WR.equals("!exit"))
                        break;
                }
                System.out.println("Chamada Terminada");
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } catch (ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        }
    } 
}