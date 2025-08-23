package com.ufs.ReservaAerea.service;

import com.ufs.ReservaAerea.model.*;
import com.ufs.ReservaAerea.repository.PoltronaRepository;
import com.ufs.ReservaAerea.repository.TipoAviaoRepository;
import com.ufs.ReservaAerea.repository.VooRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// Serviço de inicialização de dados
@Service
public class InicializacaoService {

    private final VooRepository vooRepository; // Repositório de voos
    private final PoltronaRepository poltronaRepository; // Repositório de poltronas
    private final TipoAviaoRepository tipoAviaoRepository; // Repositório de tipos de avião

    public InicializacaoService(VooRepository vooRepository, PoltronaRepository poltronaRepository, TipoAviaoRepository tipoAviaoRepository) {
        this.vooRepository = vooRepository;
        this.poltronaRepository = poltronaRepository;
        this.tipoAviaoRepository = tipoAviaoRepository;
    }

    // Método para carregar dados iniciais
    public void carregarDadosIniciais() {
        // Verifica se já existem voos cadastrados
        if (vooRepository.count() == 0) {
            System.out.println("Carregando dados iniciais de voos, tipos de avião e poltronas...");

            // Cria e salva as instâncias concretas
            TipoAviao a320 = new AirbusA320();
            tipoAviaoRepository.save(a320);

            TipoAviao b737 = new Boeing737();
            tipoAviaoRepository.save(b737);

            // Cria o primeiro voo com o tipo de avião A320
            Voo v1 = new Voo();
            v1.setOrigem("AJU");
            v1.setDestino("GRU");
            v1.setDataHora("20/08/2025 09:30");
            v1.setTipoAviao(a320);
            vooRepository.save(v1);

            // Cria o segundo voo com o tipo de avião B737
            Voo v2 = new Voo();
            v2.setOrigem("GRU");
            v2.setDestino("SSA");
            v2.setDataHora("21/08/2025 15:10");
            v2.setTipoAviao(b737);
            vooRepository.save(v2);

            // Gera as poltronas para os voos criados
            gerarPoltronasParaVoo(v1);
            gerarPoltronasParaVoo(v2);
        }
    }

    // Método para gerar poltronas para um voo específico
    private void gerarPoltronasParaVoo(Voo voo) {
        String[] colunas = {"A", "B", "C", "D", "E", "F"};
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