package lojadebicicletas.v2.pkg0;

import java.io.Serializable;

public class Produto implements Serializable{
    String nome;
    String categoria;
    int stock;
    
    Produto(String nome, String categoria, int stock){
        this.nome=nome;
        this.categoria = categoria;
        this.stock = stock;
    }
}
