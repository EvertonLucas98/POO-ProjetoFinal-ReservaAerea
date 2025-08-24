package com.ufs.ReservaAerea.service;

import com.ufs.ReservaAerea.model.*;
import com.ufs.ReservaAerea.repository.PoltronaRepository;
import com.ufs.ReservaAerea.repository.TipoAviaoRepository;
import com.ufs.ReservaAerea.repository.VooRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Serviço de inicialização de dados
@Service
public class InicializacaoService {

    private final VooRepository vooRepository; // Repositório de voos
    private final PoltronaRepository poltronaRepository; // Repositório de poltronas
    private final TipoAviaoRepository tipoAviaoRepository; // Repositório de tipos de avião

    public InicializacaoService(VooRepository vooRepository, PoltronaRepository poltronaRepository,
            TipoAviaoRepository tipoAviaoRepository) {
        this.vooRepository = vooRepository;
        this.poltronaRepository = poltronaRepository;
        this.tipoAviaoRepository = tipoAviaoRepository;
    }

    // Método para carregar dados iniciais
    public void carregarDadosIniciais() {
        // Verifica se já existem voos para evitar duplicação em reinícios
        if (vooRepository.count() == 0) {
            System.out.println("Carregando dados iniciais de voos...");

            // Cria e salva os tipos de avião
            TipoAviao a320 = new AirbusA320();
            tipoAviaoRepository.save(a320);

            TipoAviao b737 = new Boeing737();
            tipoAviaoRepository.save(b737);

            // Cria uma lista com os tipos de avião
            List<TipoAviao> tiposAviao = List.of(a320, b737);
            // Cria uma instância de Random para gerar dados aleatórios
            Random random = new Random();

            // Define os parâmetros para a geração de voos
            String[] origens = {
                    "Aeroporto Internacional de Aracaju (AJU)",
                    "Aeroporto de São Paulo Cumbica (GRU)",
                    "Aeroporto de Salvador (SSA)",
                    "Aeroporto do Recife (REC)",
                    "Aeroporto de Brasília (BSB)",
                    "Aeroporto do Rio de Janeiro Santos Dumont (SDU)"
            };
            String[] destinos = {
                    "Aeroporto de Salvador (SSA)",
                    "Aeroporto de São Paulo Cumbica (GRU)",
                    "Aeroporto Internacional de Aracaju (AJU)",
                    "Aeroporto de Brasília (BSB)",
                    "Aeroporto do Rio de Janeiro Santos Dumont (SDU)"
            };
            String[] datasHoras = {
                    "01/09/2025 09:00", "02/09/2025 13:00", "03/09/2025 17:00",
                    "04/09/2025 21:00", "05/09/2025 09:00", "06/09/2025 13:00",
                    "07/09/2025 17:00", "08/09/2025 21:00", "09/09/2025 09:00",
                    "10/09/2025 13:00", "11/09/2025 17:00", "12/09/2025 21:00",
                    "13/09/2025 09:00", "14/09/2025 13:00"
            };

            // Cria múltiplos voos
            for (int i = 0; i <= 20; i++) {
                // Seleciona aleatoriamente a origem, destino, data/hora e tipo de avião
                String origem = origens[random.nextInt(origens.length)];
                String destino = destinos[random.nextInt(destinos.length)];

                // Evita que a origem e o destino sejam a mesma cidade
                while (origem.equals(destino)) {
                    destino = destinos[random.nextInt(destinos.length)];
                }

                // Define a data e hora do voo
                String dataHora = datasHoras[random.nextInt(datasHoras.length)];
                // Define o tipo de avião do voo
                TipoAviao tipoAviao = tiposAviao.get(random.nextInt(tiposAviao.size()));

                // Cria e salva o voo
                Voo voo = new Voo();
                voo.setOrigem(origem);
                voo.setDestino(destino);
                voo.setDataHora(dataHora);
                voo.setTipoAviao(tipoAviao);
                vooRepository.save(voo);

                // Gera as poltronas para o voo recém-criado
                gerarPoltronasParaVoo(voo);
            }
        }
    }

    // Método para gerar poltronas para um voo específico
    private void gerarPoltronasParaVoo(Voo voo) {
        String[] colunas = { "A", "B", "C", "D", "E", "F" };
        List<Poltrona> poltronas = new ArrayList<>();

        TipoAviao tipoAviao = voo.getTipoAviao();

        // Usa os métodos das subclasses de TipoAviao para gerar as poltronas
        for (int linha = 1; linha <= tipoAviao.getTotalLinhas(); linha++) {
            for (int i = 0; i < tipoAviao.getTotalColunas(); i++) {
                Poltrona p = new Poltrona();
                p.setNumero(linha + colunas[i]);
                p.setReservada(false);
                p.setDisponivel(true);
                p.setVoo(voo);
                poltronas.add(p);
            }
        }
        poltronaRepository.saveAll(poltronas);
    }
}