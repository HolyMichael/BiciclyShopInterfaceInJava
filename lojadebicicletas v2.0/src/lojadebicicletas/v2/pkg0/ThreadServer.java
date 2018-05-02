package lojadebicicletas.v2.pkg0;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
public class ThreadServer extends Thread{
    String ip;
    int port;
    ServerSocket serversock;
    Socket sock;
    //String estado;
    public ThreadServer(String ip, int port){ 
        setDaemon(true);
        this.port = port;
        this.ip = ip;
    }

    //public String getEstado() {
    //    return estado;
    //}

    @Override
    public void run(){
        try {
            serversock = new ServerSocket(port);
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        while(true){
            try {
                String WR;
                sock = serversock.accept();
                ObjectInputStream is = new ObjectInputStream(sock.getInputStream());
                ObjectOutputStream os = new ObjectOutputStream(sock.getOutputStream());
                //estado = "ativo";
                System.out.println("Contacto de " + (String) is.readObject() + ". !exit para sair.");// Recebe ip de quem o contacta
                os.writeObject(ip + ":" + port); //Envia ip para o outro cliente
                while(true){
                    WR = (String) is.readObject();
                    if(WR.equals("!exit")){
                        System.out.println("Linha Fechada do outro lado.");
                        break;
                    }
                    System.out.println("Mensagem do outro Cliente:");
                    System.out.println(WR);
                    System.out.println("Mensagem para o outro Cliente:");
                    WR = Ler.umaString();
                    os.writeObject(WR);
                    if (WR.equals("!exit"))
                        break;
                }
                System.out.println("Chamada Terminada.");
                //estado = "desativo";
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } catch (ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        }
    } 
}