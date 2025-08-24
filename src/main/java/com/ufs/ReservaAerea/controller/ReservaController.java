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
            return "redirect:/login"; //redireciona para login se nao estiver logado
        }
        
        List<Reserva> reservas = reservaService.listarReservasCliente(); //lista de reservas do cliente logado
        model.addAttribute("reservas", reservas); //adiciona a lista de reservas ao modelo
        model.addAttribute("logado", true); //indica que o cliente está logado
        model.addAttribute("nomeCliente", loginService.getClienteLogado().getNome()); //adiciona o nome do cliente logado ao modelo
        return "minhasReservas";
    }

    // Processar seleção de poltronas e criar reserva
    @PostMapping("/criar")
    public String criarReserva(@RequestParam("vooId") Long vooId, @RequestParam("poltronasSelecionadas") List<Long> poltronasSelecionadas, Model model) { 
        if (!loginService.estaLogado()) {
            return "redirect:/login?vooId=" + vooId; 
        }
        try {
            Voo voo = vooService.buscarPorId(vooId); //busca o voo pelo id
            Reserva reserva = reservaService.criarReserva(voo, poltronasSelecionadas); //cria a reserva com o voo e as poltronas selecionadas
            
            return "redirect:/reservas/" + reserva.getId();
            
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            return "redirect:/poltronas/voo/" + vooId + "?erro=" + e.getMessage();
        }
    }

    // Detalhes da reserva
    @GetMapping("/{id}") //id da reserva
    public String detalhesReserva(@PathVariable("id") Long id, Model model) {
        if (!loginService.estaLogado()) {
            return "redirect:/login"; //redireciona para login se nao estiver logado
        }
        
        try {
            Reserva reserva = reservaService.buscarPorId(id); //busca a reserva pelo id
            Cliente clienteLogado = loginService.getClienteLogado(); //pega o cliente logado
            
            // Verifica se a reserva pertence ao cliente
            if (!reserva.getCliente().getId().equals(clienteLogado.getId())) {
                return "redirect:/reservas"; //redireciona para a lista de reservas se a reserva nao pertencer ao cliente
            }
            
            model.addAttribute("reserva", reserva); //adiciona a reserva ao modelo
            model.addAttribute("logado", true); //indica que o cliente está logado
            model.addAttribute("nomeCliente", clienteLogado.getNome()); //adiciona o nome do cliente logado ao modelo
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
            reservaService.cancelarReserva(id); //cancela a reserva pelo id
            return "redirect:/reservas?cancelamento=sucesso"; 
        } catch (Exception e) {
            return "redirect:/reservas?erro=" + e.getMessage();
        }
    }
}
