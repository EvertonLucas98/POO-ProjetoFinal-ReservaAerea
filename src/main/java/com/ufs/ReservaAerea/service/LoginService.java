package com.ufs.ReservaAerea.service;

import org.springframework.stereotype.Service;

import com.ufs.ReservaAerea.model.Cliente;

@Service
public class LoginService {
    // Singleton(classe que só pode ser instanciada uma vez)
    private static final LoginService INSTANCE = new LoginService();
    private Cliente clienteLogado;

    // construtor privado para evitar instanciamento externo
    private LoginService() {
    }

    // instacia unica
    public static LoginService getInstance() {
        return INSTANCE;
    }

    // Login
    public void login(Cliente cliente) {
        this.clienteLogado = cliente;
    }

    // Logout
    public void logout() {
        this.clienteLogado = null;
    }

    // Checa se o cliente está logado
    public boolean estaLogado() {
        return clienteLogado != null;
    }

    // Retorna o cliente logado
    public Cliente getClienteLogado() {
        return clienteLogado;
    }
}
