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
    ArrayList<ClientObj> waitingList = new ArrayList<>();
    ObjectInputStream oisClinetes, oisWaitingClienetes;
    ObjectOutputStream oosClinetes, oosWaitingClienetes;
    
    public RMIImpl(String name) throws RemoteException {
        super();
        File temp = new File("../../ServerSavedFiles"); //in case of first execution errorxd
        if(!temp.exists()){
            temp.mkdir();
            System.out.println("Created Saved Files.");
        }
        try {
            oisClinetes = new ObjectInputStream(new FileInputStream("../../ServerSavedFiles/Clinetes.txt"));
            System.out.println("loading");
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        try {
            oisWaitingClienetes = new ObjectInputStream(new FileInputStream("../../ServerSavedFiles/WaitingClienetes.txt"));
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
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
    
    public java.util.Date getDate() {
        System.out.println(" Método remoto -- RMIImpl.getDate()");
        count++;
        return new java.util.Date();
    }
    
    public boolean registerClient(String IP){
        boolean flag = true;
        System.out.println(clients.size());
        /*try{ //Buscar top clientes
            ObjectInputStream oisprod = new ObjectInputStream(new FileInputStream("../../SavedFiles/Clientes.txt"));
            clients = (ArrayList<ClientObj>) oisprod.readObject();
        }
        catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
        catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }*/
        for(ClientObj c : clients){
            if(c.getIp().equals(IP))
                flag = false;
        }
        if(flag){
            clients.add(new ClientObj(IP));
            saveClients();
            System.out.println("Registered " + IP + ".");
            /*try{ // Adicionar ao ficheiro cliente
                if(!clients.isEmpty()){
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("../../SavedFiles/Clientes.txt"));
                    oos.writeObject(clients);
                    oos.flush();
                }
            }
            catch (IOException e){
                System.out.println(e.getMessage());
            }*/
            return true;
        }
        System.out.println(IP + " logged in.");
        return false;
    }
    
    public boolean registerCategory(String ip, String category){
        for(ClientObj c : clients){
            if(ip.equals(c.getIp()))
                if(!c.getCategory().contains(category)){
                    c.insertCategory(category); 
                    System.out.println(ip + " " + category);
                    return true;
                }else
                    return false;
        }
        return false;
    }
    
    public ArrayList getClientsSellingCategory(String category, String IP){
        ArrayList<String> sellingClients = new ArrayList<>();
        for(ClientObj c: clients){
            if(c.getCategory().contains(category))
                sellingClients.add(c.getIp());
        }
        if (sellingClients.isEmpty()){
            for(ClientObj d: waitingList){
                if(d.getIp().equals(IP))
                    if(!d.getCategory().contains(category)){
                        waitingList.add(new ClientObj(IP));
                        saveWaitingClients();
                    }
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
            oosWaitingClienetes = new ObjectOutputStream(new FileOutputStream("../../ServerSavedFiles/WaitingClienetes.txt"));
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

