package com.ufs.ReservaAerea.controller;

import com.ufs.ReservaAerea.model.Cliente;
import com.ufs.ReservaAerea.service.ClienteService;
import com.ufs.ReservaAerea.service.LoginService;
import com.ufs.ReservaAerea.service.exceptions.CamposObrigatoriosException;
import com.ufs.ReservaAerea.service.exceptions.CredenciaisInvalidasException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Controlador para gerenciar login
@Controller
public class LoginController {

    private final ClienteService clienteService;
    private final LoginService loginService;

    public LoginController(ClienteService clienteService) {
        this.clienteService = clienteService;
        this.loginService = LoginService.getInstance(); //instacia o serviço de login
    }

    @GetMapping("/login")
    public String mostrarLogin(@RequestParam(name = "vooId", required = false) Long vooId, Model model) {
    model.addAttribute("vooId", vooId);  
    return "login";
}

    @PostMapping("/login")
    public String autenticar(@RequestParam("email") String email, @RequestParam("senha") String senha, @RequestParam(name = "vooId", required = false) Long vooId, Model model) {
        try {
                Cliente cliente = clienteService.autenticar(email, senha);
                loginService.login(cliente); //salva o cliente logado na instancia

                if (vooId != null) {
                    return "redirect:/poltronas/voo/" + vooId;
                } else {
                    return "redirect:/";
                }
            } catch (CamposObrigatoriosException | CredenciaisInvalidasException e) {
                model.addAttribute("erro", e.getMessage());
                model.addAttribute("vooId", vooId);
                return "login";
            } catch (IllegalArgumentException e) {
                model.addAttribute("erro", e.getMessage());
                model.addAttribute("vooId", vooId);
                return "login";
            } catch (Exception e) {
                model.addAttribute("erro", "Ocorreu um erro inesperado. Tente novamente.");
                model.addAttribute("vooId", vooId);
                return "login";
            }
        
    }
    @GetMapping("/logout")
    public String logout() {
        loginService.logout(); //limpa o cliente logado na instancia
        return "redirect:/";
    }
}
