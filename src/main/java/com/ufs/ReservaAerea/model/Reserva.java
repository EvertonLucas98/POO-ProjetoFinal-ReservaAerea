package com.ufs.ReservaAerea.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String dataReserva;
    private Double precoTotal;
    private String status; // CONFIRMADA, CANCELADA
    
    //relacionamentos com outras classes
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    
    @ManyToOne
    @JoinColumn(name = "voo_id")
    private Voo voo;
    
    @ManyToMany
    @JoinTable(
        name = "reserva_poltrona",
        joinColumns = @JoinColumn(name = "reserva_id"),
        inverseJoinColumns = @JoinColumn(name = "poltrona_id")
    )
    private List<Poltrona> poltronas = new ArrayList<>();
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getDataReserva() { return dataReserva; }
    public void setDataReserva(String dataReserva) { this.dataReserva = dataReserva; }
    
    public Double getPrecoTotal() { return precoTotal; }
    public void setPrecoTotal(Double precoTotal) { this.precoTotal = precoTotal; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    
    public Voo getVoo() { return voo; }
    public void setVoo(Voo voo) { this.voo = voo; }
    
    public List<Poltrona> getPoltronas() { return poltronas; }
    public void setPoltronas(List<Poltrona> poltronas) { this.poltronas = poltronas; }
    
    // Método para adicionar poltrona
    public void adicionarPoltrona(Poltrona poltrona) {
        this.poltronas.add(poltrona);
    }
}
