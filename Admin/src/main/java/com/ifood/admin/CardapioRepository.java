package com.ifood.admin;

import org.springframework.data.repository.CrudRepository;

public interface CardapioRepository extends CrudRepository<Item_cardapio,Integer> {
    Iterable<Item_cardapio> findByRestaurante(Restaurante restaurante);
}

