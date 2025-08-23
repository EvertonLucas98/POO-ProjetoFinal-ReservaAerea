package com.ufs.ReservaAerea.controller;

import com.ufs.ReservaAerea.service.PoltronaService;
import com.ufs.ReservaAerea.model.Voo;
import com.ufs.ReservaAerea.service.LoginService;
import com.ufs.ReservaAerea.service.VooService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
        return "redirect:/login?vooId=" + vooId;
    }
    //verifica se selecionou poltrona
    if (poltronasSelecionadas == null || poltronasSelecionadas.isEmpty()) {
        return "redirect:/poltronas/voo/" + vooId + "?erro=Nenhuma poltrona selecionada";
    }

    //busca informacoes
    Voo voo = vooService.buscarPorId(vooId);

    model.addAttribute("vooId", vooId);
    model.addAttribute("voo", voo);
    model.addAttribute("poltronasSelecionadas", poltronasSelecionadas);
    model.addAttribute("logado", loginService.estaLogado());
    model.addAttribute("nomeCliente", loginService.getClienteLogado().getNome());
    
    return "confirmarReserva";
}

}
