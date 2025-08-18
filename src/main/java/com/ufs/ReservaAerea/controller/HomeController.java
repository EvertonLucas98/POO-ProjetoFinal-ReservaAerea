package com.ufs.ReservaAerea.controller;

import com.ufs.ReservaAerea.service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// Controlador para gerenciar a página inicial
@Controller
public class HomeController {
    private final LoginService loginService;

    public HomeController(LoginService loginService) {
        this.loginService = LoginService.getInstance(); // instância do serviço de login
    }

    // Exibir a página inicial
    @GetMapping("/")
    public String index(Model model) {
        boolean logado = loginService.estaLogado();
        model.addAttribute("logado", logado);

        if (logado) {
            model.addAttribute("nomeCliente", loginService.getClienteLogado().getNome());
        }
        return "index";
    }

}
