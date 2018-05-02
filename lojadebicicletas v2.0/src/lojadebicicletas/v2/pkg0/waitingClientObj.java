package lojadebicicletas.v2.pkg0;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class waitingClientObj implements Serializable{
    String ip;
    ArrayList<String> categorias;
    
    waitingClientObj(String ip){
        this.ip = ip;
        categorias = new ArrayList<>();
    }

    waitingClientObj(String ip, String categoria){
        this.ip = ip;
        categorias = new ArrayList<>();
        categorias.add(categoria);
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

    @Override
    public String toString() {
        return "waitingClientObj{" + "ip=" + ip + ", categorias=" + categorias + '}';
    }
}