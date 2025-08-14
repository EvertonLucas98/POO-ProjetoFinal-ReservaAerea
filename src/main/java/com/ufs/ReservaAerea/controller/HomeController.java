package com.ufs.ReservaAerea.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Controlador para gerenciar a página inicial
@Controller
public class HomeController {

    // Exibir a página inicial
    @GetMapping("/")
    public String index() {
        return "index";
    }

}
