package lojadebicicletas.v2.pkg0;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.rmi.*;
import java.util.ArrayList;

public class Server{
    
    public static void main(String[] argv) {
        Server h;
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
            RMIImpl implementaInterface = new RMIImpl("RMIImpl") {};
            System.out.println("Servidor está Online");
        }
        catch (Exception e) {
            System.out.println("Erro no servidor " + e);
        }
    }
}