package lojadebicicletas.v2.pkg0;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientObj implements Serializable{
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

    public void insertCategory(String category) {
        categorias.add(category);
    }

    @Override
    public String toString() {
        return "ClientObj{" + "ip=" + ip + ", categorias=" + categorias + '}';
    }
}  