package lojadebicicletas.v2.pkg0;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LojadebicicletasV20 { //aka processo cliente
    
    public static void main(String[] args) throws ClassNotFoundException{
        ArrayList<Produto> produtos = new ArrayList<>();
        ArrayList<String> categorias = new ArrayList<>();
        try{
            ObjectInputStream oisprod = new ObjectInputStream(new FileInputStream("produtos.txt"));
            produtos = (ArrayList<Produto>) oisprod.readObject();
        } catch(FileNotFoundException e){
            System.out.println("file not found");
            System.out.println(e.getMessage());
        } catch(IOException e){
            System.out.println("something went wrong with produtos please contact gui it's his fault"); //TODO
            System.out.println(e.getMessage());
            System.exit(0);
        }
        try{
            ObjectInputStream oiscatg = new ObjectInputStream(new FileInputStream("categorias.txt"));
            categorias = (ArrayList<String>) oiscatg.readObject();
        } catch(FileNotFoundException e){
            System.out.println("file not found");
            System.out.println(e.getMessage());
        } catch(IOException e){
            System.out.println("something went wrong with categorias please contact gui it's his fault"); //TODO
            System.out.println(e.getMessage());
            System.exit(0);
        }
        
        System.out.println("All loaded");
        try {
            Socket socket = new Socket("127.0.0.1", 2222);
            ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
            System.out.println((String)is.readObject());
            os.writeObject(socket.getRemoteSocketAddress());
            os.writeObject(socket.getInetAddress());
            while(true){
                System.out.println("1- registar produto, 2-exit");
                int i = Ler.umInt();
                int counter = 1;
                if (i == 1){
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
                    System.out.println(categorias.get(0));
                    System.out.println("    " + "stock inicial?");
                    int stock = Ler.umInt();
                    produtos.add(new Produto(nome, categorias.get(choice-1),  stock));
                    System.out.println("Produto adicionado com sucesso");
                }
                if (i == 2){
                    break;
                }
                if (i == 3){
                }
                if (i == 4){
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(LojadebicicletasV20.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //produtos.add(new Produto("x91topbiek", APIExtension.categoria.Bicicletas.name(), 10)); //must test if not repeated
        //System.out.println(produtos.get(0).nome);
        try{
            if(!produtos.isEmpty()){
                ObjectOutputStream oosprod = new ObjectOutputStream(new FileOutputStream("produtos.txt"));
                oosprod.writeObject(produtos);
                System.out.println("produtos not null saving");
            }
        } catch (IOException e){
            System.out.println("something went wrong saving contact gui again");
            System.out.println(e.getMessage());
        }
        try{
            if(!categorias.isEmpty()){
                ObjectOutputStream ooscatg = new ObjectOutputStream(new FileOutputStream("categorias.txt"));
                ooscatg.writeObject(categorias);
                System.out.println("categorias not null saving");
            }
        } catch (IOException e){
            System.out.println("something went wrong saving contact gui again");
            System.out.println(e.getMessage());
        }
    }
}
