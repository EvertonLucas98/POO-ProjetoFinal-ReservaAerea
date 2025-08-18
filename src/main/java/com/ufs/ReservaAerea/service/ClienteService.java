package com.ufs.ReservaAerea.service;

import com.ufs.ReservaAerea.model.Cliente;
import com.ufs.ReservaAerea.repository.ClienteRepository;
import com.ufs.ReservaAerea.service.exceptions.CamposObrigatoriosException;
import com.ufs.ReservaAerea.service.exceptions.CredenciaisInvalidasException;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    // Listar todos os clientes
    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    // Buscar cliente por ID
    public Cliente buscarPorId(Long id) {
        Optional<Cliente> clienteEncontrado = repository.findById(id);

        // Verifica se o cliente foi encontrado
        if (clienteEncontrado.isPresent()) {
            return clienteEncontrado.get();
        } else {
            throw new RuntimeException("Cliente não encontrado!");
        }
    }

    // Salvar um novo cliente
    public Cliente salvar(Cliente cliente) {
        if (repository.existsByEmail(cliente.getEmail())) {
            throw new CredenciaisInvalidasException("Já existe um cliente cadastrado com este e-mail!");
        }
        if (cliente.getEmail() == null || cliente.getEmail().isBlank() || cliente.getSenha() == null || cliente.getSenha().isBlank()) {
            throw new CamposObrigatoriosException("Preencha todos os campos obrigatórios.");
        }
        if (!cliente.getEmail().matches("^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,}$")) {
            throw new IllegalArgumentException("Formato de email inválido.");
        }
        return repository.save(cliente);
    }

    // Autenticar Email e senha
    public Cliente autenticar(String email, String senha) {

    if (email == null || email.isBlank() || senha == null || senha.isBlank()) {
            throw new CamposObrigatoriosException("Preencha todos os campos.");
        }

    if (!email.matches("^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,}$")) {
        throw new IllegalArgumentException("Formato de email inválido.");
    }
    
    Cliente cliente = repository.findByEmail(email);
        if (cliente == null || !cliente.getSenha().equals(senha)) {
            throw new CredenciaisInvalidasException("Email ou senha inválidos.");
        }
    return cliente;
}
    
    // Excluir um cliente
    public void excluir(Long id) {
        repository.deleteById(id);
    }
    
}
