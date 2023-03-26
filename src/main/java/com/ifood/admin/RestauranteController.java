package com.ifood.admin;

import org.springframework.ui.Model;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Controller
public class RestauranteController {
    @Autowired
    RestauranteRepository restauranteRepository;
    @Autowired
    CardapioRepository cardapioRepository;
    @GetMapping("/novo-restaurante")
    public String mostrarFromNovoRestaurante(Restaurante restaurante){
        return "novo-restaurante";
    }

    @PostMapping("/adicionar-restaurante")
    public String adicionarRestaurante(@Valid Restaurante restaurante, BindingResult result){
        if(result.hasErrors()){
            return "/novo-restaurante";
        }

        restauranteRepository.save(restaurante);
        return "redirect:/index";
    }

    @GetMapping(value={"/index","/"})
    public String mostrarRestaurantes(Model model){
        model.addAttribute("restaurantes",restauranteRepository.findAll());
        return "index";
    }

    @GetMapping("/iditar/{id}")
    public String mostrarFormAtualizar(@PathVariable("id") int id, Model model){
        Restaurante restaurante = restauranteRepository.findById(id).orElseThrow((() -> new IllegalArgumentException("o id do resturante é invalido" +id)));
        model.addAttribute("restaurante",restaurante);
        return "atualizar-restaurante";
    }


    @PostMapping("/atualizar/{id}")
    public String atualizarRestaurante(@PathVariable("id") int id, @Valid Restaurante restaurante, BindingResult result, Model model){
        if (result.hasErrors()) {
            restaurante.setId(id);
            return "atualizar-restaurante";
        }
        restauranteRepository.save(restaurante);
        return "redirect:/index";
    }

    @GetMapping("/remover/{id}")
    public String removerRestaurante(@PathVariable("id") int id){
        Restaurante restaurante = restauranteRepository.findById(id).orElseThrow(()->new IllegalArgumentException("o id é invalido"+id));
        Iterable<Item_cardapio> itens_cardapio = cardapioRepository.findByRestaurante(restaurante);
        for(Item_cardapio item : itens_cardapio){
            cardapioRepository.delete(item);
        }
        restauranteRepository.delete(restaurante);
        return "redirect:/index";
    }

    @GetMapping("/detalhes/{id}")
    public String detalhesRestaurante(@PathVariable("id") int id,Model model){
        Restaurante restaurante = restauranteRepository.findById(id).orElseThrow((() -> new IllegalArgumentException("o id do resturante é invalido" +id)));
        Iterable<Item_cardapio> itens_cardapio = cardapioRepository.findByRestaurante(restaurante);
        model.addAttribute("itens_cardapio",itens_cardapio);
        model.addAttribute("restaurante",restaurante);

        return "detalhes-restaurante";
    }

    @PostMapping("/detalhes/{id}")
    public String adicionarCardapio(@PathVariable("id") int id, Item_cardapio item_cardapio){
        Restaurante restaurante = restauranteRepository.findById(id).orElseThrow((() -> new IllegalArgumentException("o id do resturante é invalido" +id)));
        item_cardapio.setRestaurante(restaurante);
        cardapioRepository.save(item_cardapio);
        return "redirect:/detalhes/{id}";
    }

    @GetMapping("/editar-cardapio/{id}")
    public String mostrarFormAtualizarCardapio(@PathVariable("id") int id, Model model){
        Item_cardapio item_cardapio = cardapioRepository.findById(id).orElseThrow(()->new IllegalArgumentException("o id é invalido"+id));
        model.addAttribute("restaurante",item_cardapio.getRestaurante());
        model.addAttribute("item_cardapio",item_cardapio);
        return "atualizar-cardapio";
    }

    @PostMapping("/atualizar-cardapio/{id}")
    public String atualizarCardapio(@PathVariable("id") int id, @Valid Item_cardapio  item_cardapio, BindingResult result, Model model){
        if (result.hasErrors()) {
            item_cardapio.setId(id);
            return "atualizar-restaurante";
        }
        Item_cardapio item_cardapio1 =  cardapioRepository.findById(id).orElseThrow(()->new IllegalArgumentException("o id é invalido"+id));
        item_cardapio1.setNome(item_cardapio.getNome());
        item_cardapio1.setDescricao(item_cardapio.getDescricao());
        item_cardapio1.setPreco(item_cardapio.getPreco());

        cardapioRepository.save(item_cardapio1);
        return "redirect:/index";
    }



    @GetMapping("/removerCardapio/{id}")
    public String removerItemCardapio(@PathVariable("id") int id){
        Item_cardapio item_cardapio = cardapioRepository.findById(id).orElseThrow(()->new IllegalArgumentException("o id é invalido"+id));
        cardapioRepository.delete(item_cardapio);
        return "redirect:/index";
    }

}
