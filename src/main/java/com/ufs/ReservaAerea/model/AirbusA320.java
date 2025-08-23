package com.ufs.ReservaAerea.model;

import jakarta.persistence.Entity;

@Entity
public class AirbusA320 extends TipoAviao {
    
    public AirbusA320() {
        this.setModelo("Airbus A320");
    }

    @Override
    public int getTotalLinhas() {
        return 12;
    }

    @Override
    public int getTotalColunas() {
        return 6;
    }
}