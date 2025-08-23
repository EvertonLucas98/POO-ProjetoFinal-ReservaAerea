// FILE: src/main/java/com/ufs/ReservaAerea/controller/ReservaController.java
package com.ufs.ReservaAerea.controller;

import com.ufs.ReservaAerea.model.*;
import com.ufs.ReservaAerea.service.ReservaService;
import com.ufs.ReservaAerea.service.VooService;
import com.ufs.ReservaAerea.service.PoltronaService;
import com.ufs.ReservaAerea.service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final VooService vooService;
    private final LoginService loginService;

    public ReservaController(ReservaService reservaService, VooService vooService, PoltronaService poltronaService, LoginService loginService) {
        this.reservaService = reservaService;
        this.vooService = vooService;
        this.loginService = LoginService.getInstance();
    }

    // Página de minhas reservas
    @GetMapping
    public String minhasReservas(Model model) {
        if (!loginService.estaLogado()) {
            return "redirect:/login";
        }
        
        List<Reserva> reservas = reservaService.listarReservasCliente();
        model.addAttribute("reservas", reservas);
        model.addAttribute("logado", true);
        model.addAttribute("nomeCliente", loginService.getClienteLogado().getNome());
        return "minhasReservas";
    }

    // Processar seleção de poltronas e criar reserva
    @PostMapping("/criar")
    public String criarReserva(@RequestParam("vooId") Long vooId, @RequestParam("poltronasSelecionadas") List<Long> poltronasSelecionadas, Model model) {
        if (!loginService.estaLogado()) {
            return "redirect:/login?vooId=" + vooId;
        }
        try {
            Voo voo = vooService.buscarPorId(vooId);
            Reserva reserva = reservaService.criarReserva(voo, poltronasSelecionadas);
            
            return "redirect:/reservas/" + reserva.getId();
            
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            return "redirect:/poltronas/voo/" + vooId + "?erro=" + e.getMessage();
        }
    }

    // Detalhes da reserva
    @GetMapping("/{id}")
    public String detalhesReserva(@PathVariable("id") Long id, Model model) {
        if (!loginService.estaLogado()) {
            return "redirect:/login";
        }
        
        try {
            Reserva reserva = reservaService.buscarPorId(id);
            Cliente clienteLogado = loginService.getClienteLogado();
            
            // Verifica se a reserva pertence ao cliente
            if (!reserva.getCliente().getId().equals(clienteLogado.getId())) {
                return "redirect:/reservas";
            }
            
            model.addAttribute("reserva", reserva);
            model.addAttribute("logado", true);
            model.addAttribute("nomeCliente", clienteLogado.getNome());
            return "detalhesReserva";
            
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            return "redirect:/reservas";
        }
    }

    // Cancelar reserva
    @PostMapping("/{id}/cancelar")
    public String cancelarReserva(@PathVariable("id") Long id) {
        try {
            reservaService.cancelarReserva(id);
            return "redirect:/reservas?cancelamento=sucesso";
        } catch (Exception e) {
            return "redirect:/reservas?erro=" + e.getMessage();
        }
    }
}
