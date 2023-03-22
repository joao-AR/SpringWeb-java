package com.ifood.admin;

import org.springframework.ui.Model;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RestauranteController {
    @Autowired
    RestauranteRepository restauranteRepository;

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
            return "atualizar-contato";
        }
        restauranteRepository.save(restaurante);
        return "redirect:/index";
    }

    @GetMapping("/remover/{id}")
    public String removerRestaurante(@PathVariable("id") int id){
        Restaurante restaurante = restauranteRepository.findById(id).orElseThrow(()->new IllegalArgumentException("o id é invalido"+id));
        restauranteRepository.delete(restaurante);
        return "redirect:/index";
    }

}
