package com.ufs.ReservaAerea.service;

import com.ufs.ReservaAerea.model.*;
import com.ufs.ReservaAerea.repository.ReservaRepository;
import com.ufs.ReservaAerea.repository.PoltronaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
//servico para gerenciar reservas
@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final PoltronaRepository poltronaRepository;
    private final LoginService loginService;

    public ReservaService(ReservaRepository reservaRepository, PoltronaRepository poltronaRepository, LoginService loginService) {
        this.reservaRepository = reservaRepository;
        this.poltronaRepository = poltronaRepository;
        this.loginService = LoginService.getInstance();
    }

    // Criar uma nova reserva
    @Transactional
    public Reserva criarReserva(Voo voo, List<Long> poltronaIds) {
        if (!loginService.estaLogado()) {
            throw new RuntimeException("Cliente não está logado");
        }
        
        Cliente cliente = loginService.getClienteLogado();
        
        // Verifica se as poltronas estão disponíveis
        for (Long poltronaId : poltronaIds) {
            Poltrona poltrona = poltronaRepository.findById(poltronaId)
                .orElseThrow(() -> new RuntimeException("Poltrona não encontrada: " + poltronaId));
            
            if (poltrona.isReservada()) {
                throw new RuntimeException("Poltrona já reservada: " + poltrona.getNumero());
            }
        }
        
        // Cria a reserva
        Reserva reserva = new Reserva();
        reserva.setDataReserva(LocalDate.now().toString()); //formato aaaa-mm-dd
        reserva.setStatus("CONFIRMADA");
        reserva.setCliente(cliente);
        reserva.setVoo(voo);
        
        // Calcula preço total (preco a definir)
        reserva.setPrecoTotal(poltronaIds.size() * 200.0); //sugestao - usar um atributo de preco no voo
        
        // Adiciona as poltronas e marca como reservadas
        for (Long poltronaId : poltronaIds) {
            Poltrona poltrona = poltronaRepository.findById(poltronaId).get();
            reserva.adicionarPoltrona(poltrona);
            poltrona.setReservada(true);
            poltronaRepository.save(poltrona);
        }
        
        return reservaRepository.save(reserva);
    }

    // Listar reservas do cliente logado
    public List<Reserva> listarReservasCliente() {
        if (!loginService.estaLogado()) {
            throw new RuntimeException("Cliente não está logado");
        }
        
        Cliente cliente = loginService.getClienteLogado();
        return reservaRepository.findByClienteIdOrderByIdDesc(cliente.getId());//reserva mais recente primeiro
    }

    // Cancelar reserva
    @Transactional
    public void cancelarReserva(Long reservaId) {
        if (!loginService.estaLogado()) {
            throw new RuntimeException("Cliente não está logado");
        }
        
        Reserva reserva = reservaRepository.findById(reservaId)
            .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));
        
        Cliente cliente = loginService.getClienteLogado();
        
        // Verifica se a reserva pertence ao cliente
        if (!reserva.getCliente().getId().equals(cliente.getId())) {
            throw new RuntimeException("Reserva não pertence ao cliente");
        }
        
        // Libera as poltronas
        for (Poltrona poltrona : reserva.getPoltronas()) {
            poltrona.setReservada(false);
            poltronaRepository.save(poltrona);
        }
        
        // Cancela a reserva
        reserva.setStatus("CANCELADA");
        reservaRepository.save(reserva);
    }

    // Buscar reserva por ID
    public Reserva buscarPorId(Long id) {
        return reservaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));
    }
}
