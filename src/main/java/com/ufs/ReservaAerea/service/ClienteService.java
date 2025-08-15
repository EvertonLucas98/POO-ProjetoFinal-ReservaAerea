package com.ufs.ReservaAerea.service;

import com.ufs.ReservaAerea.model.Cliente;
import com.ufs.ReservaAerea.repository.ClienteRepository;
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
        return repository.save(cliente);
    }

    // Excluir um cliente
    public void excluir(Long id) {
        repository.deleteById(id);
    }
    
}
