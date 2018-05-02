package lojadebicicletas.v2.pkg0;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import static java.rmi.server.RemoteServer.getClientHost;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.*;

public class Cliente{
    /*String nome;
    String categoria;
    String IP;
    
    Cliente(String n,String c, String ip){
        this.nome = n;
        this.categoria = c;
        this.IP = ip;
    }*/
    static String ip = "";
    static int port;
    static InetAddress host; // IP do Cliente
    static RMIServerInterface serverObject;
    static ArrayList<String> list = new ArrayList<>();
    static String Nome = "catalogo";
    static String serverIP = "192.168.43.246";
    static int serverPort = 2043;
    //static ServerSocket serversocketa = null;
    //static Socket Cliente = new Socket (), Server = new Socket ();
    
    
    public static void main(String[] argv) {
        String serverName = "";
        System.setSecurityManager(new SecurityManager());
        if (argv.length != 1) {
            try {
                serverName = java.net.InetAddress.getLocalHost().getHostName();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        else {
            serverName = argv[0];
        }
        if (serverName.equals("")){
            System.out.println("usage: java RMIClient < host running RMI server>");
            System.exit(0);
        }
        System.out.println("Server IP:");
        serverIP = Ler.umaString();
        System.out.println("Server Port:");
        serverPort = Ler.umInt();
        try {
            //bind server object to object in client
            serverObject = (RMIServerInterface) Naming.lookup("//"+serverName+"/RMIImpl");
            //invoke method on server object
            //Date d = serverObject.getDate();
            System.out.println("RMI connection successful");
            host = InetAddress.getLocalHost();
            byte ipaux [] = host.getAddress();
            for(int n= 0;n<ipaux.length;n++){
                if (n>0) 
                    ip+=".";
                ip+=(ipaux[n] & 0xff);
            }
            System.out.println("Seu Port:");
            port = Ler.umInt();
            System.out.println("Machine IP identified.");  
            ClientLoop();
        }
        catch(Exception e) {
            System.out.println("Exception occured: " + e);
            System.exit(0);
        }
    }
    
    public void printOnClient (String s) throws java.rmi.RemoteException{
        System.out.println ("Message from server: " + s);
    }
    
    private static void ClientLoop() throws InterruptedException{
        ArrayList<Produto> produtos = new ArrayList<>();
        ArrayList<String> categorias = new ArrayList<>();
        File temp = new File("../../SavedFiles");
        if(!temp.exists()){
            temp.mkdir();
            System.out.println("Created Saved Files.");
        }
        try{
            ObjectInputStream oisprod = new ObjectInputStream(new FileInputStream("../../SavedFiles/produtos.txt"));
            produtos = (ArrayList<Produto>) oisprod.readObject();
        } catch(FileNotFoundException e){
            System.out.println(e.getMessage());
            System.out.println("File will be created when you create your first product");
        } catch(IOException e){
            System.out.println(e.getMessage());
        } catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{
            ObjectInputStream oiscatg = new ObjectInputStream(new FileInputStream("../../SavedFiles/categorias.txt"));
            categorias = (ArrayList<String>) oiscatg.readObject();
        } catch(FileNotFoundException e){
            System.out.println(e.getMessage());
            System.out.println("File will be created when you create your first product");
        } catch(IOException e){
            System.out.println(e.getMessage());
            System.exit(0);
        } catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        System.out.println("All loaded");
        int i = 0;
        try{
            if(serverObject.registerClient(ip,port))
                System.out.println("Registered with " + ip + ":" + port);
            else
                System.out.println("Logged in with " + ip + ":" + port);
        }
        catch(RemoteException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("xd");
        }
        ThreadServer comunic = new ThreadServer(ip,port);
        comunic.start();
        synchronized (comunic){
            do{
                System.out.println("1 - Registar produto, 2 - Comprar produto, 3 - Seus Produtos à venda, 4 - Comunicar com outro Cliente, 5 - exit");
                i = Ler.umInt();
                int counter = 1;
                switch(i){
                    case 1:
                        System.out.println("    " + "Insira o nome:");
                        String nome = Ler.umaString();
                        System.out.println("    " + "insira a categoria:");

                        if(categorias != null) //Mostrar Categorias
                            for(counter=1;counter<categorias.size()+1;counter++)
                                System.out.println("    " + counter + "- " +categorias.get(counter-1));
                        System.out.println("    " + (counter) + "- Nova Categoria");

                        int choice = Ler.umInt();
                        if(choice == categorias.size()+1){ //Nova Categoria
                            System.out.println("    " + "    " + "Nome da nova categoria");
                            categorias.add(Ler.umaString());
                        }
                        System.out.println("    " + "stock inicial?");
                        int stock = Ler.umInt();
                        produtos.add(new Produto(nome, categorias.get(choice-1), stock, ip)); //Adição do Produto
                        System.out.println("Produto adicionado com sucesso");
                        try {
                            if(serverObject.registerCategory(ip , categorias.get(choice-1)))
                                System.out.println("Nice");
                            else
                                System.out.println("Não nice");
                        } 
                        catch (RemoteException ex) {
                            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case 2:
                        System.out.println("Escreva a sua categoria:");
                        String op = Ler.umaString();
                        try {
                            ArrayList <String> cat = serverObject.getClientsSellingCategory(op,ip);
                            if(cat == null)
                                System.out.println("Categoria não encontrada foi adicionada à lista de memes");
                            else
                                for(int n = 0; n<cat.size();n++)
                                    System.out.println(cat.get(n));
                        } catch (RemoteException ex) {
                            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case 3:
                        int j;
                        System.out.println("Produtos que tem à venda:");
                        for(j=0;j<produtos.size();j++)
                            System.out.println((j+1) + " - " + produtos.get(j));
                        System.out.println("Categorias que disponibiliza:");
                        for(j=0;j<categorias.size();j++)
                            System.out.println((j+1) + " - " + categorias.get(j));
                        break;
                    case 4:
                        String Lere;
                        int auxport;
                        Socket Cliente = null;
                        System.out.println("Introduza um port:");
                        auxport = Ler.umInt();
                        try { 
                            Cliente = new Socket ("127.0.0.1", auxport);
                        }
                        catch (IOException e){
                            System.out.println("O Cliente para o qual ligou neste momento não se encontra disponível. Por favor, tente mais tarde.");
                        }
                        try {
                            ObjectOutputStream oss = new ObjectOutputStream (Cliente.getOutputStream());
                            ObjectInputStream iss = new ObjectInputStream(Cliente.getInputStream());
                            oss.writeObject(ip + ":" + port); //Envia ip para o outro cliente
                            System.out.println("Em contacto com " + (String) iss.readObject() + ". !exit para sair."); // Recebe ip de quem contacta
                            while(true){
                                System.out.println("Mensagem para o outro Cliente:");
                                Lere = Ler.umaString();
                                oss.writeObject(Lere);
                                if (Lere.equals("!exit"))
                                    break;
                                Lere = (String) iss.readObject();
                                if(Lere.equals("!exit")){
                                    System.out.println("Linha Fechada do outro lado.");
                                    break;
                                }
                                System.out.println("Mensagem do outro Cliente:");
                                System.out.println(Lere);
                            }
                            System.out.println("Chamada Terminada.");
                        } catch (IOException ex) {
                            System.out.println(ex.getMessage());
                        } catch (ClassNotFoundException ex) {
                            System.out.println(ex.getMessage());
                        }
                    break;
                }
            }while (i!=5);
        }
        //produtos.add(new Produto("x91topbiek", APIExtension.categoria.Bicicletas.name(), 10)); //must test if not repeated
        //System.out.println(produtos.get(0).nome);
        try{
            if(!produtos.isEmpty()){
                ObjectOutputStream oosprod = new ObjectOutputStream(new FileOutputStream("../../SavedFiles/produtos.txt"));
                oosprod.writeObject(produtos);
                oosprod.flush();
                System.out.println("produtos not null saving");
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        try{
            if(!categorias.isEmpty()){
                ObjectOutputStream ooscatg = new ObjectOutputStream(new FileOutputStream("../../SavedFiles/categorias.txt"));
                ooscatg.writeObject(categorias);
                ooscatg.flush();
                System.out.println("categorias not null saving");
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}

