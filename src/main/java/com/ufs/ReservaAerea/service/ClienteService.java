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

    private static final String ARQUIVO_CLIENTES = "clientes.txt";
    private final ClienteRepository repository;
    private final ArquivoService arquivoService;

    public ClienteService(ClienteRepository repository, ArquivoService arquivoService) {
        this.repository = repository;
        this.arquivoService = arquivoService;
    }

    // Salva um cliente no arquivo
    private void salvarClienteNoArquivo(Cliente cliente) {
        String dadosCliente = cliente.getId() + "|" + //salva separado por |
                cliente.getNome() + "|" +
                cliente.getEmail() + "|" +
                cliente.getSenha();
        arquivoService.salvarDados(ARQUIVO_CLIENTES, dadosCliente, true); 
    }

    // Lê todos os clientes do arquivo
    private List<String> lerTodosClientesDoArquivo() {
        return arquivoService.lerTodosDados(ARQUIVO_CLIENTES);
    }

    // Carrega clientes do arquivo para o banco de dados
    public void carregarClientesDoArquivo() {
        List<String> clientesArquivo = lerTodosClientesDoArquivo(); //lista de strings, cada string é uma linha do arquivo
        for (String linha : clientesArquivo) {           //para cada linha do arquivo
            String[] partes = linha.split("\\|");  //separa usando |
            if (partes.length >= 4) {
                // Verifica se o cliente já existe no banco
                Long id = Long.parseLong(partes[0]);   
                if (!repository.existsById(id)) {  //se nao existe 
                    Cliente cliente = new Cliente(); //cria novo cliente
                    cliente.setId(id);
                    cliente.setNome(partes[1]);
                    cliente.setEmail(partes[2]);
                    cliente.setSenha(partes[3]);

                    repository.save(cliente); //salva no banco
                }
            }
        }
    }

    // Limpa e recria o arquivo com os dados atuais do banco
    public void recriarArquivoClientes() {
        // Limpa o arquivo
        arquivoService.limparArquivo(ARQUIVO_CLIENTES);

        // Recria com dados atuais do banco
        List<Cliente> clientes = repository.findAll(); //busca todos os clientes do banco
        for (Cliente cliente : clientes) {
            salvarClienteNoArquivo(cliente); //para cada cliente, salva no arquivo
        }
    }

    // Listar todos os clientes
    public List<Cliente> listarTodos() {
        return repository.findAll();  
    }

    // Buscar cliente por ID
    public Cliente buscarPorId(Long id) {
        // Busca o cliente pelo ID
        Optional<Cliente> clienteEncontrado = repository.findById(id); 

        // Verifica se o cliente foi encontrado, caso contrário exibe uma mensagem de erro
        if (clienteEncontrado.isPresent()) {
            return clienteEncontrado.get();
        } else {
            throw new RuntimeException("Cliente não encontrado!");
        }
    }

    // Salvar um novo cliente
    public Cliente salvar(Cliente cliente) {
        // Verifica se já existe um cliente com o mesmo e-mail
        if (repository.existsByEmail(cliente.getEmail())) {
            throw new CredenciaisInvalidasException("Já existe um cliente cadastrado com este e-mail!");
        }

        // Verifica se todos os campos obrigatórios estão preenchidos
        if (cliente.getEmail() == null || cliente.getEmail().isBlank() || cliente.getSenha() == null
                || cliente.getSenha().isBlank()) {
            throw new CamposObrigatoriosException("Preencha todos os campos obrigatórios.");
        }

        // Verifica se o formato do email é válido
        if (!cliente.getEmail().matches("^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,}$")) {
            throw new IllegalArgumentException("Formato de email inválido.");
        }

        // Salva o cliente no banco de dados
        Cliente clienteSalvo = repository.save(cliente);
        // Salva o cliente no arquivo
        salvarClienteNoArquivo(clienteSalvo);
        return clienteSalvo;
    }

    // Autenticar Email e senha
    public Cliente autenticar(String email, String senha) {
        // Verifica se os campos estão preenchidos
        if (email == null || email.isBlank() || senha == null || senha.isBlank()) {
            throw new CamposObrigatoriosException("Preencha todos os campos.");
        }

        // Verifica se o formato do email é válido
        if (!email.matches("^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,}$")) {
            throw new IllegalArgumentException("Formato de email inválido.");
        }

        // Busca o cliente pelo email
        Cliente cliente = repository.findByEmail(email);
        // Verifica se o cliente foi encontrado e se a senha está correta
        if (cliente == null || !cliente.getSenha().equals(senha)) {
            throw new CredenciaisInvalidasException("Email ou senha inválidos.");
        }
        return cliente;
    }

}
