package com.ufs.ReservaAerea.service;

import com.ufs.ReservaAerea.model.Voo;
import com.ufs.ReservaAerea.model.Poltrona;
import com.ufs.ReservaAerea.repository.PoltronaRepository;
import com.ufs.ReservaAerea.repository.VooRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

// Serviço para gerenciar voos
@Service
public class VooService {

    @Autowired
    private VooRepository vooRepository;
    @Autowired
    private PoltronaRepository poltronaRepository;

    public VooService(VooRepository vooRepository, PoltronaRepository poltronaRepository) {
        this.vooRepository = vooRepository;
        this.poltronaRepository = poltronaRepository;
    }

    // Lista todos os voos
    public List<Voo> listarTodos() {
        return vooRepository.findAll();
    }

    // Busca um voo pelo ID
    public Voo buscarPorId(Long id) {
        Optional<Voo> vooEncontrado = vooRepository.findById(id);

        // Verifica se o voo foi encontrado
        if (vooEncontrado.isPresent()) {
            return vooEncontrado.get();
        } else {
            throw new RuntimeException("Voo não encontrado!");
        }
    }

    // Salva um novo voo
    public Voo salvarVoo(Voo voo) {
        Voo novoVoo = vooRepository.save(voo);

        // Gerar poltronas (exemplo: 30 fileiras, assentos A-F)
        for (int fila = 1; fila <= 12; fila++) {
            for (char letra = 'A'; letra <= 'F'; letra++) {
                // Criar poltrona
                Poltrona poltrona = new Poltrona();
                // Define os atributos da poltrona
                poltrona.setNumero(fila + String.valueOf(letra));
                poltrona.setReservada(false);
                poltrona.setVoo(novoVoo);
                // Salva a poltrona no repositório
                poltronaRepository.save(poltrona);
            }
        }

        return novoVoo;
    }

}
