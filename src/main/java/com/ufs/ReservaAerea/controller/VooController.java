package com.ufs.ReservaAerea.controller;

import com.ufs.ReservaAerea.model.Voo;
import com.ufs.ReservaAerea.service.VooService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Controlador para gerenciar voos
@Controller
@RequestMapping("/voos")
public class VooController {

    private final VooService service;

    public VooController(VooService service) {
        this.service = service;
    }

    // Listar todos os voos
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("voos", service.listarTodos());
        return "voos";
    }

    // Exibir o formulário para criar um novo voo
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("voo", new Voo());
        return "formVoo";
    }

    // Salvar um novo voo
    @PostMapping
    public String salvar(@ModelAttribute Voo voo) {
        service.salvarVoo(voo);
        return "redirect:/voos";
    }

    // Excluir um voo
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        service.excluir(id);
        return "redirect:/voos";
    }

}
