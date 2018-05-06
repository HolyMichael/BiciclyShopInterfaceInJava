package lojadebicicletas.v2.pkg0;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
public class ThreadServer extends Thread{
    static String ip;
    static int port;
    static ServerSocket serversock;
    static Socket sock;
    static Thread main;
        
    public ThreadServer(String ip, int port, Thread main){ 
        setDaemon(true);
        this.port = port;
        this.ip = ip;
        this.main = main;
    }

    public ArrayList<Produto> getProdutos(String ip, int port){
        ArrayList<Produto> aux;
        try{
            ObjectInputStream oisprod = new ObjectInputStream(new FileInputStream("../../SavedFiles/produtos.txt"));
            aux = (ArrayList<Produto>) oisprod.readObject();
            if(!aux.isEmpty())
                for(int memeajuda = 0; memeajuda < aux.size(); memeajuda ++){
                    if(aux.get(memeajuda).getIP().equals(ip)==false || aux.get(memeajuda).getPORT() != port){
                        aux.remove(memeajuda);
                    }
                }
            oisprod.close();
            return aux;
        } catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        } catch(IOException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    @Override
    public synchronized void run(){
        try {
            serversock = new ServerSocket(port);
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        while(true){
            try {
                String WR, guccigang;
                sock = serversock.accept();
                //main.wait();
                ObjectInputStream is = new ObjectInputStream(sock.getInputStream());
                ObjectOutputStream os = new ObjectOutputStream(sock.getOutputStream());
                guccigang = (String) is.readObject();
                System.out.println("Pretende aceitar o contacto de " + guccigang + "? [Y/N]");
                while(true){
                    WR = Ler.umaString();
                    if(WR.equals("Y") || WR.equals("N")){
                        break;
                    }
                }
                if(WR.equals("N")){
                    os.writeObject("fim");
                    System.out.println("Chamada desligada.");
                    continue;
                }
                else{
                   os.writeObject("heyyyy okayyyyy"); 
                }
                System.out.println("Contacto de " + guccigang + ". !exit para sair, !envia para enviar os seus produtos.");// Recebe ip de quem o contacta
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
                    System.out.println("Mensagem enviada!");
                    if (WR.equals("!exit"))
                        break;
                    if(WR.equals("!envia")){
                        os.writeObject(getProdutos(ip,port));
                    }
                }
                System.out.println("Chamada Terminada.");
                is.close();
                os.close();
            }
            catch (IOException | ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
            //main.notify();
        } 
    }
}