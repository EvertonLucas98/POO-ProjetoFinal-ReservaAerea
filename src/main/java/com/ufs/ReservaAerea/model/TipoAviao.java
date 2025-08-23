package com.ufs.ReservaAerea.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Armazena e gerencia subclasses em uma única tabela no banco de dados
public abstract class TipoAviao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String modelo;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    // Métodos abstratos para obter as dimensões do avião
    public abstract int getTotalLinhas();
    public abstract int getTotalColunas();

}
