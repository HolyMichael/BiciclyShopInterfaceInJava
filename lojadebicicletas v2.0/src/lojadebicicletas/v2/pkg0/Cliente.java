package lojadebicicletas.v2.pkg0;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {
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
        if (serverName.equals( "") ) {
            System.out.println("usage: java RMIClient < host running RMI server>");
            System.exit(0);
        }
        try {
            //bind server object to object in client
            RMIInterface myServerObject = (RMIInterface) Naming.lookup("//"+serverName+"/RMIImpl");
            //invoke method on server object
            //Date d = myServerObject.getDate();
            System.out.println("RMI connection successful");
            ClientLoop();
        }
        catch(Exception e) {
            System.out.println("Exception occured: " + e);
            System.exit(0);
        }
    }

    private static void ClientLoop() {
        ArrayList<Produto> produtos = new ArrayList<>();
        ArrayList<String> categorias = new ArrayList<>();
        File temp = new File("../../SavedFiles");
        if(!temp.exists()){
            temp.mkdir();
            System.out.println("created Saved Files");
        }
        try{
            ObjectInputStream oisprod = new ObjectInputStream(new FileInputStream("SavedFiles/produtos.txt"));
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
            ObjectInputStream oiscatg = new ObjectInputStream(new FileInputStream("SavesFiled/categorias.txt"));
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
        int i;
        do{
            System.out.println("1- registar produto, 4-exit");
            i = Ler.umInt();
            int counter = 1;
            switch(i){
                case 1:
                    System.out.println("    " + "Insira o nome");
                    String nome = Ler.umaString();
                    System.out.println("    " + "insira a categoria");
                    if(categorias != null)
                        for(counter=1;counter<categorias.size()+1;counter++)
                            System.out.println("    " + counter + "- " +categorias.get(counter-1));
                    System.out.println("    " + (counter) + "- Nova Categoria");
                    int choice = Ler.umInt();
                    if(choice == categorias.size()+1){
                        System.out.println("    " + "    " + "Nome da nova categoria");
                        categorias.add(Ler.umaString());
                    }
                    System.out.println("    " + "stock inicial?");
                    int stock = Ler.umInt();
                    produtos.add(new Produto(nome, categorias.get(choice-1),  stock));
                    System.out.println("Produto adicionado com sucesso");
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
            }
        } while (i!=4);
        
        //produtos.add(new Produto("x91topbiek", APIExtension.categoria.Bicicletas.name(), 10)); //must test if not repeated
        //System.out.println(produtos.get(0).nome);
        try{
            if(!produtos.isEmpty()){
                ObjectOutputStream oosprod = new ObjectOutputStream(new FileOutputStream("../../SavedFiles/produtos.txt"));
                oosprod.writeObject(produtos);
                System.out.println("produtos not null saving");
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        try{
            if(!categorias.isEmpty()){
                ObjectOutputStream ooscatg = new ObjectOutputStream(new FileOutputStream("../../SavedFiles/categorias.txt"));
                ooscatg.writeObject(categorias);
                System.out.println("categorias not null saving");
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}

