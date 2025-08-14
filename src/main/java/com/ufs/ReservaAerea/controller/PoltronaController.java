package com.ufs.ReservaAerea.controller;

import com.ufs.ReservaAerea.service.PoltronaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Controlador para gerenciar poltronas
@Controller
@RequestMapping("/poltronas")
public class PoltronaController {

    private final PoltronaService service;

    public PoltronaController(PoltronaService service) {
        this.service = service;
    }

    // Listar poltronas por voo
    @GetMapping("/voo/{vooId}")
    public String listarPorVoo(@PathVariable Long vooId, Model model) {
        model.addAttribute("vooId", vooId);
        model.addAttribute("poltronas", service.listarPorVoo(vooId));
        return "poltronas";
    }

    // Reservar uma poltrona
    @PostMapping("/reservar/{id}")
    public String reservar(@PathVariable Long id, @RequestParam Long vooId) {
        service.reservar(id);
        return "redirect:/poltronas/voo/" + vooId;
    }

}
