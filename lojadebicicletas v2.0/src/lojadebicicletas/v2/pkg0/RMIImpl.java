package lojadebicicletas.v2.pkg0;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class RMIImpl extends UnicastRemoteObject implements RMIServerInterface{
    int count;
    private class ClientObj implements Serializable{
        String ip;
        ArrayList<String> categorias;
        
        ClientObj(String ip){
            this.ip = ip;
            categorias = new ArrayList<>();
        }
        
        public String getIp(){
            return ip;
        }
        
        public ArrayList getCategory(){
            return categorias;
        }

        private void insertCategory(String category) {
            categorias.add(category);
        }
    }
    ArrayList<ClientObj> clients = new ArrayList<>();
    public RMIImpl(String name) throws RemoteException {
        super();
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
        for(ClientObj c : clients){
            if(c.getIp().equals(IP))
                flag = false;
        }
        if(flag){
            clients.add(new ClientObj(IP));
            System.out.println("Registered " + IP);
            return true;
        }
        System.out.println(IP + " logged in");
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
    
    public ArrayList getClientsSellingCategory(String category){
        ArrayList<String> sellingClients = new ArrayList<>();
        for(ClientObj c: clients){
            if(c.getCategory().contains(category))
                sellingClients.add(c.getIp());
        }
        if (sellingClients.isEmpty())
            ; //add to list of clients waiting
        return sellingClients;
    }
    
    
    public int getCount(){
        return count;
    }
}

