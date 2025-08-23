package com.ufs.ReservaAerea.model;

import jakarta.persistence.Entity;

@Entity
public class Boeing737 extends TipoAviao {

    public Boeing737() {
        this.setModelo("Boeing 737");
    }

    @Override
    public int getTotalLinhas() {
        return 15;
    }

    @Override
    public int getTotalColunas() {
        return 6;
    }
}