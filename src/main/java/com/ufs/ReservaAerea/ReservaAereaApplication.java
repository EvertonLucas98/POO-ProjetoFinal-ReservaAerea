package com.ufs.ReservaAerea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;

import com.ufs.ReservaAerea.service.ClienteService;
import com.ufs.ReservaAerea.service.InicializacaoService;
import com.ufs.ReservaAerea.service.ReservaService;

@SpringBootApplication
public class ReservaAereaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservaAereaApplication.class, args);
    }

    // Carregando dados iniciais
    @Bean
    CommandLineRunner loadData(ClienteService clienteService, InicializacaoService inicializacaoService, ReservaService reservaService) {
        return args -> {
            // Carrega clientes do arquivo (não mudou)
            clienteService.carregarClientesDoArquivo();
            // Chama o novo serviço para carregar voos e poltronas
            inicializacaoService.carregarDadosIniciais();
            // Carrega reservas do arquivo
            reservaService.carregarReservasDoArquivo();
        };
    }

}
