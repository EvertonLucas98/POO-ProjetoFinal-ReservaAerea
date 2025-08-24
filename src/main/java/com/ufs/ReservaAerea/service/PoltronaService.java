package com.ufs.ReservaAerea.service;

import com.ufs.ReservaAerea.model.Poltrona;
import com.ufs.ReservaAerea.repository.PoltronaRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// Serviço para gerenciar poltronas
@Service
public class PoltronaService {

    private final PoltronaRepository repository;

    public PoltronaService(PoltronaRepository repository) {
        this.repository = repository;
    }

    // Lista todas as poltronas de um voo
    public List<Poltrona> listarPorVoo(Long vooId) {
        return repository.findByVoo_Id(vooId);
    }
    // Busca poltronas por uma lista de IDs
    public List<Poltrona> buscarPoltronasPorIds(List<Long> poltronaIds) {
    return repository.findAllById(poltronaIds);
}

    // Reserva uma poltrona
    @Transactional
    public Poltrona reservar(Long poltronaId) {
        Optional<Poltrona> resultado = repository.findById(poltronaId);

        // Verifica se a poltrona foi encontrada
        if (resultado.isEmpty()) {
            throw new RuntimeException("Poltrona não encontrada");
        }

        // Verifica se a poltrona já está reservada
        Poltrona poltrona = resultado.get();
        if (poltrona.isReservada()) {
            throw new RuntimeException("Poltrona já reservada!");
        }

        // Reserva a poltrona
        poltrona.setReservada(true);
        // Atualiza a poltrona no repositório
        return repository.save(poltrona);
    }

}
