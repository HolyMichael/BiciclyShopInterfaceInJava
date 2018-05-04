package lojadebicicletas.v2.pkg0;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIImpl extends UnicastRemoteObject implements RMIServerInterface{
    int count;
    ArrayList<ClientObj> clients = new ArrayList<>();
    ArrayList<waitingClientObj> waitingList = new ArrayList<>();
    ObjectInputStream oisClinetes, oisWaitingClienetes;
    ObjectOutputStream oosClinetes, oosWaitingClienetes;
    private static RMIClientInterface client;
    static ArrayList<RMIClientInterface> cpi = new ArrayList<>();
    
    public RMIImpl(String name) throws RemoteException{
        super();
        File temp = new File("../../ServerSavedFiles"); //in case of first execution errorxd
        if(!temp.exists()){
            temp.mkdir();
            System.out.println("Created Saved Files.");
        }
        File wow = new File("../../ServerSavedFiles/Clinetes.txt");
        File ples = new File("../../ServerSavedFiles/WaitingClients.txt");
        try {
            oisClinetes = new ObjectInputStream(new FileInputStream("../../ServerSavedFiles/Clinetes.txt"));
            oisWaitingClienetes = new ObjectInputStream(new FileInputStream("../../ServerSavedFiles/WaitingClients.txt"));
            System.out.println("loading Clients...");
            clients = (ArrayList<ClientObj>)oisClinetes.readObject();
            waitingList = (ArrayList<waitingClientObj>)oisWaitingClienetes.readObject();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RMIImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Naming.rebind(name, this);
        }
        catch (Exception e) {
            if (e instanceof RemoteException)
            throw (RemoteException)e;
            else
            throw new RemoteException(e.getMessage());
        }
    }
    
    public boolean registerClient(String IP, int port, RMIClientInterface CliInterface){ 
    //retorna true caso registe o cliente e false caso lhe dê log-in
        boolean flagExists = true;
        
        for(ClientObj c : clients){ //itera por todos os utilizadores encontrando o que invocou este método
            if(c.getIp().equals(IP+":"+port)){
                flagExists = false; //cliente já existe
                c.setIsOnline(true);
                System.out.println(IP + " logged in.");
                return false;
            }
        }
        
        if(flagExists){
            clients.add(new ClientObj(IP+":"+port, CliInterface));
            saveClients();
            System.out.println("Registered " + IP + ":"+ port +".");
            return true;
        }
        return false;
    }
    
    public boolean logoutClient(String IP,int port){
        for(ClientObj c : clients){ //itera por todos os utilizadores encontrando o que invocou este método
            if(c.getIp().equals(IP+":"+port)){
                c.setIsOnline(false);
            }
        }
        return true;
    }
    
    public boolean registerCategory(String ip, String category){
        //retorna true caso tenha adicionado com sucesso esta categoria ao cliente que a chamou, e falso em caso de este cliente já ter esta categoria
        for(ClientObj c : clients){
            if(ip.equals(c.getIp())){ //itera por todos os utilizadores encontrando o que invocou este método
                if(!c.getCategory().contains(category)){ //se esta categoria ainda não existir
                    c.insertCategory(category); 
                    System.out.println(ip + " " + category);
                    for(waitingClientObj d: waitingList)
                        if(d.getCategory().equals(category)){
                            //for
                            //printOnClient("Bro alguem adicionou o teu mame");
                        }   
                    saveClients();
                    return true;
                }
                else
                    return false;
            }
        }
        return false;
    }
    
    public ArrayList getClientsSellingCategory(String category, String IP){
        ArrayList<String> sellingClients = new ArrayList<>();
        Boolean FLAG = false;
        for(ClientObj c: clients){
            if(c.getCategory().contains(category))
                sellingClients.add(c.getIp());
        }
        if (sellingClients.size()==0){
            for(waitingClientObj d: waitingList){
                FLAG=true;
                if(d.getIp().equals(IP)){
                    System.out.println("clinete encontradddoooo reeeeee");
                    if(!d.getCategory().contains(category)){
                        waitingList.add(new waitingClientObj(IP));
                        System.out.println("chameite pikachu");
                        saveWaitingClients();
                    }
                }
            }
            if(!FLAG){
                waitingList.add(new waitingClientObj(IP,category));
                saveWaitingClients();
            }
        }
        if(sellingClients.isEmpty())
                return null;
        return sellingClients;
    }
    
    
    public int getCount(){
        return count;
    }
    
    private void saveClients() {
        try {
            oosClinetes = new ObjectOutputStream(new FileOutputStream("../../ServerSavedFiles/Clinetes.txt"));
            System.out.println("loading");
            System.out.println("saving Clients...");
            oosClinetes.writeObject(clients);
            oosClinetes.flush();
            System.out.println("Saved");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RMIImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RMIImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void saveWaitingClients() {
        try {
            oosWaitingClienetes = new ObjectOutputStream(new FileOutputStream("../../ServerSavedFiles/WaitingClients.txt"));
            System.out.println("saving waiting Clients...");
            oosWaitingClienetes.writeObject(waitingList);
            oosWaitingClienetes.flush();
            System.out.println("Saved");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RMIImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RMIImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}

