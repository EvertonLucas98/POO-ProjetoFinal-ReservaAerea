package com.ufs.ReservaAerea.controller;

import com.ufs.ReservaAerea.service.PoltronaService;
import com.ufs.ReservaAerea.service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Controlador para gerenciar poltronas
@Controller
@RequestMapping("/poltronas")
public class PoltronaController {

    private final PoltronaService service;
    private final LoginService loginService;
    
    public PoltronaController(PoltronaService service) {
        this.service = service;
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

    // Reservar uma poltrona
    @PostMapping("/reservar/{id}")
    public String reservar(@PathVariable("id") Long id, @RequestParam("vooId") Long vooId) {
        service.reservar(id);
        return "redirect:/poltronas/voo/" + vooId;
    }

}
