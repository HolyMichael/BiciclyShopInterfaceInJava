package lojadebicicletas.v2.pkg0;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class Server{
    
    
    
    public static void main(String[] argv) {
        System.setSecurityManager(new SecurityManager());
        try { //Iniciar a execução do registry no porto desejado
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            System.out.println("RMI registry ready.");
        }
        catch (Exception e) {
            System.out.println("Exception starting RMI registry:");
            e.printStackTrace();
        }
        try {
            RMIImpl implementaInterface = new RMIImpl("RMIImpl");
            System.out.println("Servidor está Online");
        }
        catch (Exception e) {
            System.out.println("Erro no servidor " + e);
        }
        while(true){
            System.out.println("Press 1 to exit.");
            int close = Ler.umInt();
            if(close==1)
                //perrrrrrrrrrrguntar
                ;
        }
    }
}