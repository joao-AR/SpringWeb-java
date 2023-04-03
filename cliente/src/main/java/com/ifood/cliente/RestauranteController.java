package com.ifood.cliente;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;
import java.util.ArrayList;

@Controller
public class RestauranteController {
    @Autowired
    RestauranteRepository restauranteRepository;
    @Autowired
    CardapioRepository cardapioRepository;
    private static final String SESSION_CARRINHO = "sessionCarrinho";


    @GetMapping("/carrinho/{id}")
    public String addCarrinho(@PathVariable("id") int id,HttpServletRequest request){
        Item_cardapio item_cardapio = cardapioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("O id é inválido: " + id));
        List<Item_cardapio> carrinho = (List<Item_cardapio>) request.getSession().getAttribute(SESSION_CARRINHO);
        boolean novo_item = true; // variavel usada para verificar se o item que vai ser adicionado no carrinho já está ou não no carrinho

        if (CollectionUtils.isEmpty(carrinho)) { // caso o carrinho esteja vazio
            carrinho = new ArrayList<>(); // criar uma uma lista de para o carrinho
            item_cardapio.setQuantidade(1); // setar a quanitdade de elementos do item para 1(um)
            carrinho.add(item_cardapio);// adicionar item no carrinho
        }else {
            for (int i = 0; i < carrinho.size(); i++) {
                if (item_cardapio.getId() == carrinho.get(i).getId()) { // se o item que vai ser adicionado no carrinho já está no carrinho
                    novo_item = false; // não vai ser um novo item
                    int quantidade = carrinho.get(i).getQuantidade(); //  pegar a quantidade de itens que está na session
                    quantidade += 1; // add 1 a quantidade desse item
                    carrinho.get(i).setQuantidade(quantidade);// seta a nova quantidade
                }
            }

            if(novo_item){ // se for um novo item adicona no carrinho com a quantidade 1
                item_cardapio.setQuantidade(1);
                carrinho.add(item_cardapio);
            }
        }

        request.getSession().setAttribute(SESSION_CARRINHO, carrinho);

        return "redirect:/carrinho";
    }
    @GetMapping("/remover-carrinho/{id}")
    public String removerCarrinho(@PathVariable("id") int id,HttpServletRequest request){
        Item_cardapio item_cardapio = cardapioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("O id é inválido: " + id));
        List<Item_cardapio> carrinho = (List<Item_cardapio>) request.getSession().getAttribute(SESSION_CARRINHO);
        int posicao = 0;
        for (int i = 0; i < carrinho.size(); i++) {
            if (item_cardapio.getId() == carrinho.get(i).getId()) { // se o item que vai ser adicionado no carrinho já está no carrinho
                posicao = i;
            }
        }
        carrinho.remove(posicao);
        request.getSession().setAttribute(SESSION_CARRINHO, carrinho);
        return "redirect:/carrinho";
    }

    @GetMapping("/sub-carrinho/{id}")
    public String subCarrinho(@PathVariable("id") int id,HttpServletRequest request){
        Item_cardapio item_cardapio = cardapioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("O id é inválido: " + id));
        List<Item_cardapio> carrinho = (List<Item_cardapio>) request.getSession().getAttribute(SESSION_CARRINHO);

        for (int i = 0; i < carrinho.size(); i++) {
            if (item_cardapio.getId() == carrinho.get(i).getId()) { // se o item que vai ser adicionado no carrinho já está no carrinho
                int quantidade = carrinho.get(i).getQuantidade(); //  pegar a quantidade de itens que está na session
                quantidade -= 1; // sub 1 a quantidade desse item
                if(quantidade <= 0){
                    removerCarrinho(id,request);
                }else{
                    carrinho.get(i).setQuantidade(quantidade);// seta a nova quantidade
                }
            }
        }

        request.getSession().setAttribute(SESSION_CARRINHO, carrinho);
        return "redirect:/carrinho";
    }

    @GetMapping("/carrinho")
    public String mostrarCarrinho(Model model, HttpServletRequest request){
        List<Item_cardapio> carrinho = (List<Item_cardapio>) request.getSession().getAttribute(SESSION_CARRINHO);

        for (int i = 0; i < carrinho.size(); i++) {
            int id = carrinho.get(i).getId();
            try {
                Item_cardapio item_cardapio = cardapioRepository.findById(id).orElseThrow();
            }catch (Exception e){
                carrinho.remove(i);
                request.getSession().setAttribute(SESSION_CARRINHO, carrinho);
                return "carrinho";
            }
        }

        if(CollectionUtils.isEmpty(carrinho)){
            model.addAttribute("sessionCarrinho", new ArrayList<>());
        }else{
            model.addAttribute("sessionCarrinho", carrinho);
        }

        return "carrinho";
    }

    @GetMapping(value={"/index","/"})
    public String mostrarRestaurantes(Model model){
        model.addAttribute("restaurantes",restauranteRepository.findAll());
        return "index";
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

}
