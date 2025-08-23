package com.ufs.ReservaAerea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;

import com.ufs.ReservaAerea.model.Voo;
import com.ufs.ReservaAerea.model.Poltrona;
import com.ufs.ReservaAerea.repository.VooRepository;
import com.ufs.ReservaAerea.repository.PoltronaRepository;
import com.ufs.ReservaAerea.service.ClienteService;

@SpringBootApplication
public class ReservaAereaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservaAereaApplication.class, args);
    }

    // Carregando dados iniciais
    @Bean
    CommandLineRunner loadData(VooRepository vooRepository, PoltronaRepository poltronaRepository,
            ClienteService clienteService) {
        return args -> {
            clienteService.carregarClientesDoArquivo();
            // Verificando se já existem dados cadastrados
            if (vooRepository.count() == 0) {
                // Criando o primeiro voo
                Voo v1 = new Voo();
                v1.setOrigem("AJU");
                v1.setDestino("GRU");
                v1.setDataHora("20/08/2025 09:30");
                vooRepository.save(v1);

                // Criando o segundo voo
                Voo v2 = new Voo();
                v2.setOrigem("GRU");
                v2.setDestino("SSA");
                v2.setDataHora("21/08/2025 15:10");
                vooRepository.save(v2);

                // Criando poltronas para os voos
                gerarPoltronasParaVoo(v1, poltronaRepository);
                gerarPoltronasParaVoo(v2, poltronaRepository);
            }
        };
    }

    // Método para gerar poltronas para um voo
    private void gerarPoltronasParaVoo(Voo voo, PoltronaRepository poltronaRepository) {
        // Gerando um array de colunas
        String[] colunas = { "A", "B", "C", "D", "E", "F" };
        // Gerando poltronas de A1 a F12
        for (int linha = 1; linha <= 12; linha++) {
            for (int coluna = 0; coluna < colunas.length; coluna++) {
                // Criando poltrona
                Poltrona p = new Poltrona();
                // Definindo número da poltrona
                p.setNumero(linha + colunas[coluna]);
                // Definindo status de reserva
                p.setReservada(false);
                p.setDisponivel(true);
                // Definindo voo da poltrona
                p.setVoo(voo);
                // Salvando poltrona
                poltronaRepository.save(p);
            }
        }
    }
}
