package com.ifood.cliente;

import jakarta.persistence.*;
import java.util.List;
import java.io.Serializable;
@Entity
public class Restaurante  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;

    @OneToMany
    @JoinColumn(name="id_restaurante")
    private List<Item_cardapio> cardapios;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }



}
