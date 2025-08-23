package com.ufs.ReservaAerea.service;

import com.ufs.ReservaAerea.service.exceptions.CamposObrigatoriosException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClienteServiceTest {

    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        // Cria uma instância real do ClienteService para o teste
        // Passamos 'null' para as dependências (repository, arquivoService)
        // porque este teste não vai usá-las.
        clienteService = new ClienteService(null, null);
    }

    @Test
    void deveLancarExcecaoQuandoEmailForVazio() {
        // 1. Prepara os dados
        String emailVazio = "";
        String senhaValida = "senha123";

        // 2. Executa a ação e verifica o resultado
        // "Tente autenticar com email vazio e confirme que ele lança a exceção esperada."
        assertThrows(CamposObrigatoriosException.class, () -> {
            clienteService.autenticar(emailVazio, senhaValida);
        });
    }

    @Test
    void deveLancarExcecaoQuandoSenhaForVazia() {
        // 1. Prepara os dados
        String emailValido = "teste@email.com";
        String senhaVazia = "";

        // 2. Executa a ação e verifica o resultado
        // "Tente autenticar com senha vazia e confirme que ele lança a exceção esperada."
        assertThrows(CamposObrigatoriosException.class, () -> {
            clienteService.autenticar(emailValido, senhaVazia);
        });
    }
    
}