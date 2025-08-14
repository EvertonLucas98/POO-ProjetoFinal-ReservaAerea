package com.ufs.ReservaAerea.model;

import jakarta.persistence.*;

@Entity
public class Poltrona {

    // Identificador único da poltrona
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numero;
    private boolean reservada;
    private boolean disponivel;
    // Relacionamento com a entidade Voo
    @ManyToOne
    @JoinColumn(name = "voo_id")
    private Voo voo;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public boolean isReservada() {
        return reservada;
    }

    public void setReservada(boolean reservada) {
        this.reservada = reservada;
    }

    public Voo getVoo() {
        return voo;
    }

    public void setVoo(Voo voo) {
        this.voo = voo;
    }

    // Verifica se a poltrona está disponível
    public boolean isDisponivel() {
        return disponivel;
    }

    // Define o estado de disponibilidade da poltrona
    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
}
