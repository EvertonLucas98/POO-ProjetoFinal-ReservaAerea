package com.ufs.ReservaAerea.service;

import com.ufs.ReservaAerea.model.*;
import com.ufs.ReservaAerea.repository.ReservaRepository;
import com.ufs.ReservaAerea.repository.PoltronaRepository;
import com.ufs.ReservaAerea.repository.ClienteRepository;
import com.ufs.ReservaAerea.repository.VooRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

//servico para gerenciar reservas
@Service
public class ReservaService {

    private static final String ARQUIVO_RESERVAS = "reservas.txt"; // Nome do arquivo para salvar reservas
    private final ReservaRepository reservaRepository;
    private final PoltronaRepository poltronaRepository;
    private final ClienteRepository clienteRepository;
    private final VooRepository vooRepository;
    private final LoginService loginService;
    private final ArquivoService arquivoService;

    public ReservaService(ReservaRepository reservaRepository, PoltronaRepository poltronaRepository, LoginService loginService, ArquivoService arquivoService, ClienteRepository clienteRepository, VooRepository vooRepository) {
        this.reservaRepository = reservaRepository;
        this.poltronaRepository = poltronaRepository;
        this.arquivoService = arquivoService;
        this.clienteRepository = clienteRepository;
        this.vooRepository = vooRepository;
        this.loginService = LoginService.getInstance(); // Singleton
    }
    //Salva uma reserva no arquivo
     private void salvarReservaNoArquivo(Reserva reserva) {
        String dadosReserva = reserva.getId() + "|" + //
                reserva.getDataReserva() + "|" +
                reserva.getStatus() + "|" +
                reserva.getPrecoTotal() + "|" +
                reserva.getCliente().getId() + "|" +
                reserva.getVoo().getId() + "|" +
                reserva.getPoltronas().stream().map(p -> p.getId().toString()).collect(Collectors.joining(",")); // para cada poltrona, transfora o id em string, coleta, e separa por virgula 
        arquivoService.salvarDados(ARQUIVO_RESERVAS, dadosReserva, true);
    }
     // Lê todas as reservas do arquivo
    private List<String> lerTodasReservasDoArquivo() {
        return arquivoService.lerTodosDados(ARQUIVO_RESERVAS);
    }
    // Carrega reservas do arquivo para o banco de dados
    @Transactional
    public void carregarReservasDoArquivo() {
        List<String> reservasArquivo = lerTodasReservasDoArquivo();
        
        for (String linha : reservasArquivo) { //para cada linha do arquivo
            String[] partes = linha.split("\\|"); //separa em partes, usando | como separador
            if (partes.length >= 7) {               //verifica se tem todas as partes
                Long id = Long.parseLong(partes[0]);   //pega o id
                
                if (!reservaRepository.existsById(id)) { //se a reserva ainda nao existe no banco
                    try {
                        Reserva reserva = new Reserva(); //cria nova reserva
                        reserva.setId(id);               //seta o id
                        reserva.setDataReserva(partes[1]);    //seta a data
                        reserva.setStatus(partes[2]);        //seta o status
                        reserva.setPrecoTotal(Double.parseDouble(partes[3]));   //seta o preco total
                        //Reconstrucao dos relacionamentos
                        // Reconstruir relacionamento com Cliente
                        Long clienteId = Long.parseLong(partes[4]);  //pega o id do cliente
                        Optional<Cliente> clienteOpt = clienteRepository.findById(clienteId); //busca o cliente no banco
                        if (clienteOpt.isPresent()) {  
                            reserva.setCliente(clienteOpt.get());  //se econtrou, seta o cliente na reserva
                        } else {
                            throw new RuntimeException("Cliente não encontrado: " + clienteId); 
                        }
                        
                        // Reconstruir relacionamento com Voo
                        Long vooId = Long.parseLong(partes[5]);   //pega o id do voo
                        Optional<Voo> vooOpt = vooRepository.findById(vooId);  //busca o voo no banco
                        if (vooOpt.isPresent()) {
                            reserva.setVoo(vooOpt.get());  //se encontrou, seta o voo na reserva
                        } else {
                            throw new RuntimeException("Voo não encontrado: " + vooId);
                        }
                        
                        // Reconstruir relacionamento com Poltronas
                        String[] poltronaIds = partes[6].split(",");  // pega os ids das poltronas, separados por vírgula
                        for (String poltronaIdStr : poltronaIds) { //para cada id de poltrona
                            if (!poltronaIdStr.isEmpty()) {    //se nao estiver vazio
                                Long poltronaId = Long.parseLong(poltronaIdStr);  
                                Optional<Poltrona> poltronaOpt = poltronaRepository.findById(poltronaId);  //busca a poltrona no banco
                                if (poltronaOpt.isPresent()) {  
                                    Poltrona poltrona = poltronaOpt.get();  //se encontrou, seta a poltrona
                                    reserva.adicionarPoltrona(poltrona); //adiciona a poltrona na reserva
                                   if ("CONFIRMADA".equals(partes[2])) { 
                                    poltrona.setReservada(true); //seta a poltrona como reservada se a reserva estiver confirmada
                                } else {
                                    poltrona.setReservada(false); // Libera a poltrona se a reserva foi cancelada
                                }
                                    poltronaRepository.save(poltrona); //atualiza a poltrona no banco
                                } else {
                                    throw new RuntimeException("Poltrona não encontrada: " + poltronaId);
                                }
                            }
                        }
                        //Fim da reconstrucao

                        reservaRepository.save(reserva); //salva a reserva no banco
                        
                    } catch (Exception e) {
                        System.err.println("Erro ao carregar reserva do arquivo: " + e.getMessage());
                    }
                }
            }
        }
    }
    // Limpa e recria o arquivo com os dados atuais do banco
    public void recriarArquivoReservas() {
        // Limpa o arquivo
        arquivoService.limparArquivo(ARQUIVO_RESERVAS); 

        // Recria com dados atuais do banco
        List<Reserva> reservas = reservaRepository.findAll(); //pega todas as reservas do banco
        for (Reserva reserva : reservas) {
            salvarReservaNoArquivo(reserva); //para cada reserva, salva no arquivo
        }
    }

    // Criar uma nova reserva
    @Transactional
    public Reserva criarReserva(Voo voo, List<Long> poltronaIds) { //recebe o voo e a lista de ids das poltronas a serem reservadas
         // Verifica se o cliente está logado
        if (!loginService.estaLogado()) { 
            throw new RuntimeException("Cliente não está logado");
        }
        
        Cliente cliente = loginService.getClienteLogado(); //pega o cliente logado do servico de login
        
        // Verifica se as poltronas estão disponíveis
        for (Long poltronaId : poltronaIds) {   //para cada id de poltrona
            Poltrona poltrona = poltronaRepository.findById(poltronaId)  //busca a poltrona no banco
                .orElseThrow(() -> new RuntimeException("Poltrona não encontrada: " + poltronaId)); //se nao encontrar, lança exceção   
            
            if (poltrona.isReservada()) { //se a poltrona ja estiver reservada, lança exceção
                throw new RuntimeException("Poltrona já reservada: " + poltrona.getNumero());
            }
        }
        
        // Cria a reserva
        Reserva reserva = new Reserva(); 
        reserva.setDataReserva(LocalDate.now().toString()); //formato aaaa-mm-dd
        reserva.setStatus("CONFIRMADA"); 
        reserva.setCliente(cliente);
        reserva.setVoo(voo);
        
        // Calcula preço total
        reserva.setPrecoTotal(poltronaIds.size() * voo.getTipoAviao().getPrecoBase()); //preco base conforme o tipo do aviao
        
        // Adiciona as poltronas e marca como reservadas
        for (Long poltronaId : poltronaIds) {
            Poltrona poltrona = poltronaRepository.findById(poltronaId).get();
            reserva.adicionarPoltrona(poltrona); //adiciona a poltrona na reserva
            poltrona.setReservada(true); //marca a poltrona como reservada
            poltronaRepository.save(poltrona); //atualiza a poltrona no banco
        }
        Reserva reservaSalva = reservaRepository.save(reserva); //salva a reserva no banco
        salvarReservaNoArquivo(reservaSalva); //salva a reserva no arquivo
        return reservaSalva;
    }

    // Listar reservas do cliente logado
    public List<Reserva> listarReservasCliente() {
        if (!loginService.estaLogado()) {
            throw new RuntimeException("Cliente não está logado");
        }
        
        Cliente cliente = loginService.getClienteLogado(); 
        return reservaRepository.findByClienteIdOrderByIdDesc(cliente.getId());//reserva mais recente primeiro
    }

    // Cancelar reserva
    @Transactional
    public void cancelarReserva(Long reservaId) {
        if (!loginService.estaLogado()) {
            throw new RuntimeException("Cliente não está logado");
        }
        
        Reserva reserva = reservaRepository.findById(reservaId) //procura a reserva no banco
            .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));
        
        Cliente cliente = loginService.getClienteLogado(); //pega o cliente logado
        
        // Verifica se a reserva pertence ao cliente
        if (!reserva.getCliente().getId().equals(cliente.getId())) {
            throw new RuntimeException("Reserva não pertence ao cliente");
        }
        
        // Libera as poltronas
        for (Poltrona poltrona : reserva.getPoltronas()) {
            poltrona.setReservada(false); //para cada poltrona da reserva, seta como nao reservada
            poltronaRepository.save(poltrona); //atualiza a poltrona no banco
        }
        
        // Cancela a reserva
        reserva.setStatus("CANCELADA");
        reservaRepository.save(reserva); //atualiza a reserva no banco
        recriarArquivoReservas(); // Atualiza o arquivo de reservas
    }

    // Buscar reserva por ID
    public Reserva buscarPorId(Long id) {
        return reservaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));
    }
    
}
