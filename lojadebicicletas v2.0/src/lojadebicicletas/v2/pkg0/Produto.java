/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
