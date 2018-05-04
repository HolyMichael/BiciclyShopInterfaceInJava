package lojadebicicletas.v2.pkg0;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ClientObj implements Serializable{
    String ip;
    ArrayList<String> categorias;
    RMIClientInterface cli;
    boolean isOnline;
    
    ClientObj(String ip, RMIClientInterface cli){
        this.ip = ip;
        categorias = new ArrayList<>();
        this.cli=cli;
        isOnline=true;
    }
    
    public String getIp(){
        return ip;
    }

    public ArrayList getCategory(){
        return categorias;
    }

    public void insertCategory(String category) {
        categorias.add(category);
    }

    public boolean isIsOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    @Override
    public String toString() {
        return "ClientObj{" + "ip=" + ip + ", categorias=" + categorias + '}';
    }
}