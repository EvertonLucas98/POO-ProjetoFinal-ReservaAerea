package com.ufs.ReservaAerea.controller;

import com.ufs.ReservaAerea.service.PoltronaService;
import com.ufs.ReservaAerea.model.Poltrona;
import com.ufs.ReservaAerea.model.Voo;
import com.ufs.ReservaAerea.service.LoginService;
import com.ufs.ReservaAerea.service.VooService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

// Controlador para gerenciar poltronas
@Controller
@RequestMapping("/poltronas")
public class PoltronaController {

    private final PoltronaService service;
    private final LoginService loginService;
    private final VooService vooService;
    
    public PoltronaController(PoltronaService service, VooService vooService) {
        this.service = service;
        this.vooService = vooService;
        this.loginService = LoginService.getInstance(); // instância do serviço de login
    }

    // Listar poltronas por voo
    @GetMapping("/voo/{vooId}")
    public String listarPorVoo(@PathVariable("vooId") Long vooId, Model model) {
        model.addAttribute("vooId", vooId);
        model.addAttribute("poltronas", service.listarPorVoo(vooId));
        model.addAttribute("logado", loginService.estaLogado());
        model.addAttribute("loginManager", loginService);

        if (loginService.estaLogado()) {
        model.addAttribute("nomeCliente", loginService.getClienteLogado().getNome());
    }else {return "redirect:/login?vooId=" + vooId;}

        return "poltronas";
    }

    
    @PostMapping("/selecionar")
    public String selecionarPoltronas(@RequestParam("vooId") Long vooId, @RequestParam(value = "poltronasSelecionadas", required = false) List<Long> poltronasSelecionadas, Model model) {
    if (!loginService.estaLogado()) {
        return "redirect:/login?vooId=" + vooId; //redireciona para login se nao estiver logado
    }
    //verifica se selecionou poltrona
    if (poltronasSelecionadas == null || poltronasSelecionadas.isEmpty()) {
        return "redirect:/poltronas/voo/" + vooId + "?erro=Nenhuma poltrona selecionada"; //redireciona para a pagina de poltronas com mensagem de erro
    }

    //busca informacoes
    Voo voo = vooService.buscarPorId(vooId); //busca o voo pelo id
    double precoTotal = poltronasSelecionadas.size() * voo.getTipoAviao().getPrecoBase(); //calcula o preco total
    List<Poltrona> poltronas = service.buscarPoltronasPorIds(poltronasSelecionadas); //busca as poltronas selecionadas
    List<String> numerosPoltronas = poltronas.stream().map(Poltrona::getNumero).collect(Collectors.toList()); //extrai os numeros das poltronas

    model.addAttribute("vooId", vooId); //adiciona o id do voo ao modelo
    model.addAttribute("voo", voo); //adiciona o voo ao modelo
    model.addAttribute("nomeVoo", voo.getOrigem()+" -> " +voo.getDestino() ); //adiciona o nome do voo ao modelo
    model.addAttribute("poltronasSelecionadas", poltronasSelecionadas); //adiciona as poltronas selecionadas ao modelo
    model.addAttribute("numerosPoltronas", numerosPoltronas); //adiciona os numeros das poltronas ao modelo
    model.addAttribute("precoTotal", precoTotal);
    model.addAttribute("logado", loginService.estaLogado()); //indica que o cliente está logado
    model.addAttribute("nomeCliente", loginService.getClienteLogado().getNome()); //adiciona o nome do cliente logado ao modelo
    
    return "confirmarReserva";
}

}
