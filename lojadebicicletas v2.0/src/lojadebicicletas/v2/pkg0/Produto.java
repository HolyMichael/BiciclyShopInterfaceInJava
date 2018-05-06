package lojadebicicletas.v2.pkg0;

import java.io.Serializable;

public class Produto implements Serializable{
    String nome;
    String categoria;
    int stock;
    String IP; //Identificação do Cliente que está a vender o certo produto
    int PORT; //Nada por agora
    
    Produto(String nome, String categoria, int stock, String IP){
        this.nome=nome;
        this.categoria = categoria;
        this.stock = stock;
        this.IP = IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public void setPORT(int PORT) {
        this.PORT = PORT;
    }

    public String getIP() {
        return IP;
    }

    public int getPORT() {
        return PORT;
    }

    @Override
    public String toString() {
        return "Produto{" + "nome=" + nome + ", categoria=" + categoria + ", stock=" + stock + ", IP=" + IP + '}';
    }    
}
