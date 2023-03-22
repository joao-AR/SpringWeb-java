package com.ifood.admin;

import jakarta.persistence.*;

@Entity
public class Cardapio {
    @Id
    private int id;
    private String nomeCardapio;
    private String descricao;
    private int preco;
    private int id_restaurante;

    @ManyToOne
    private Restaurante restaurante;
    public int getId_restaurante() {
        return id_restaurante;
    }

    public void setId_restaurante(int id_restaurante) {
        this.id_restaurante = id_restaurante;
    }

    public int getPreco() {
        return preco;
    }

    public void setPreco(int preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNome() {
        return nomeCardapio;
    }

    public void setNome(String nome) {
        this.nomeCardapio = nome;
    }
}
