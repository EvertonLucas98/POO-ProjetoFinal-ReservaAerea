package com.ufs.ReservaAerea.controller;

import com.ufs.ReservaAerea.model.Cliente;
import com.ufs.ReservaAerea.service.ClienteService;
import com.ufs.ReservaAerea.service.exceptions.CamposObrigatoriosException;
import com.ufs.ReservaAerea.service.exceptions.CredenciaisInvalidasException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Controlador para gerenciar clientes
@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    // Listar todos os clientes
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", service.listarTodos());
        return "clientes";
    }

    // Exibir o formulário para criar um novo cliente
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "formCliente";
    }

    // Salvar um novo cliente
    @PostMapping
    public String salvar(@ModelAttribute Cliente cliente, Model model) {
        try {
            service.salvar(cliente);
            return "redirect:/clientes";
        } catch (CamposObrigatoriosException | CredenciaisInvalidasException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("cliente", cliente);
            return "formCliente";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("cliente", cliente);
            return "formCliente";
        } catch (Exception e) {
            model.addAttribute("erro", "Ocorreu um erro inesperado. Tente novamente.");
            model.addAttribute("cliente", cliente);
            return "formCliente";
        }
    }
    

    // Excluir um cliente
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        service.excluir(id);
        return "redirect:/clientes";
    }
    
}
