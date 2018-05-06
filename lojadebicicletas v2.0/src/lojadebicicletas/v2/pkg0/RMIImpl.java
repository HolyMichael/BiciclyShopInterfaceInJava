package lojadebicicletas.v2.pkg0;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIImpl extends UnicastRemoteObject implements RMIServerInterface{
    ArrayList<ClientObj> clients = new ArrayList<>();
    ArrayList<OfflineObj> offlineList = new ArrayList<>();
    ArrayList<waitingClientObj> waitingList = new ArrayList<>();
    ObjectInputStream oisClinetes, oisWaitingClienetes, oisOffClients;
    ObjectOutputStream oosClinetes, oosWaitingClienetes, oosOffClients;
    static ArrayList<RMIClientInterface> cpi = new ArrayList<>();
    
    public RMIImpl(String name) throws RemoteException{
        super();
        File temp = new File("../../ServerSavedFiles"); //in case of first execution errorxd
        if(!temp.exists()){
            temp.mkdir();
            System.out.println("Created Saved Files.");
        }
        try {
            oisClinetes = new ObjectInputStream(new FileInputStream("../../ServerSavedFiles/Clinetes.txt"));
            System.out.println("loading Clients...");
            clients = (ArrayList<ClientObj>)oisClinetes.readObject();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RMIImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            oisWaitingClienetes = new ObjectInputStream(new FileInputStream("../../ServerSavedFiles/WaitingClients.txt"));
            waitingList = (ArrayList<waitingClientObj>)oisWaitingClienetes.readObject();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RMIImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            oisOffClients = new ObjectInputStream(new FileInputStream("../../ServerSavedFiles/OfflineClients.txt"));
            offlineList = (ArrayList<OfflineObj>)oisOffClients.readObject();
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
        boolean flagRemoved = true;
        for(ClientObj c : clients){ //itera por todos os utilizadores encontrando o que invocou este método
            if(c.getIp().equals(IP+":"+port)){
                flagExists = false; //cliente já existe
                c.setIsOnline(true);
                c.setCli(CliInterface);
                System.out.println(IP + " logged in.");
                while(flagRemoved){
                    flagRemoved = false;
                    for(OfflineObj o : offlineList){
                        if(o.getIp().equals(c.getIp())){
                            try {
                                c.getCli().printOnClient("Alguém adicionou " + o.getCategoria());
                            } 
                            catch (RemoteException ex) {
                                Logger.getLogger(RMIImpl.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            offlineList.remove(o);
                            saveOfflineClients();
                            flagRemoved = true;
                            break;
                        }
                    }
                }
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
    
    public boolean removeClient(String IP,int port){
        for(ClientObj c : clients){ //itera por todos os utilizadores encontrando o que invocou este método 
            if(c.getIp().equals(IP+":"+port)){ 
                clients.remove(c); 
                saveClients(); 
                return true;
            } 
        } 
        return false; 
    }     
 
    public boolean removeCategory(String ip, int port, String category){ 
        for(ClientObj c : clients){ //itera por todos os utilizadores encontrando o que invocou este método 
            if(c.getIp().equals(ip+":"+port)){ 
                for(String cat : c.categorias) 
                    if(cat.equals(category)){ 
                        c.categorias.remove(cat); 
                        saveClients(); 
                        return true; 
                    } 
            } 
        }
        return false; 
    }
    
    public ArrayList<String> getAllCategories(){
        ArrayList<String> cat;
        ArrayList<String> Categories = new ArrayList<>();
        for(ClientObj c : clients){ 
            cat = c.getCategory();
            for(String s : cat)
                if(!(Categories.contains(s)))
                    Categories.add(s);
        }
        return Categories;
    }
    
    public boolean registerCategory(String ip, String category){
        //retorna true caso tenha adicionado com sucesso esta categoria ao cliente que a chamou, e falso em caso de este cliente já ter esta categoria
        for(ClientObj c : clients){
            if(ip.equals(c.getIp())){ //itera por todos os utilizadores encontrando o que invocou este método
                if(!c.getCategory().contains(category)){ //se esta categoria ainda não existir
                    c.insertCategory(category); 
                    System.out.println(ip + " " + category);
                    for(waitingClientObj d: waitingList){ //iterate over waitingList to see if any client wants this category
                        System.out.println("teste5");
                        if(d.getCategory().contains(category)){ //if yes
                            System.out.println("teste4");
                            for(ClientObj f: clients){ 
                                System.out.println("teste3");
                                System.out.println(d.getIp() + f.getIp());
                                if(d.getIp().equals(f.getIp())){ //find it on client list
                                    System.out.println("teste2");
                                    if(f.isOnline)
                                        try {
                                            f.getCli().printOnClient("Alguém adicionou " + category);
                                            d.adeusCategory(category);
                                        } catch (RemoteException ex) {
                                            System.out.println("Erro no logout do cliente");
                                            f.setIsOnline(false);
                                        }
                                    if(!f.isOnline){
                                        offlineList.add(new OfflineObj(f.getIp(),category));
                                        saveOfflineClients();
                                    }
                                }
                            }
                        }
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
    
    public ArrayList<String> getClientsSellingCategory(String category, int port, String IP){
        ArrayList<String> sellingClients = new ArrayList<>();
        Boolean FLAG = false;
        for(ClientObj c: clients){
            if(c.getCategory().contains(category)){
                sellingClients.add(c.getIp()+":"+port);
            }
        }
        if (sellingClients.size()==0){
            for(waitingClientObj d: waitingList){
                if(d.getIp().equals(IP+":"+port)){
                    FLAG=true;
                    if(!d.getCategory().contains(category)){
                        d.insertCategory(category);
                    }
                }
            }
            if(!FLAG){
                waitingList.add(new waitingClientObj(IP+":"+port,category));
            }
        }
        saveWaitingClients();
        if(sellingClients.isEmpty()){
                return null;
        }
        return sellingClients;
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
    
    private void saveOfflineClients() {
        try {
            ObjectOutputStream oosOffClient = new ObjectOutputStream(new FileOutputStream("../../ServerSavedFiles/OfflineClients.txt"));
            System.out.println("saving offline Clients...");
            oosOffClient.writeObject(offlineList);
            oosOffClient.flush();
            System.out.println("Saved");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RMIImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RMIImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
}