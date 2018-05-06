package lojadebicicletas.v2.pkg0;

import java.io.Serializable;

public class OfflineObj implements Serializable{
    String ip;
    String categoria;

    public OfflineObj(String ip, String categoria){
        this.ip=ip;
        this.categoria=categoria;
    }
    
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
