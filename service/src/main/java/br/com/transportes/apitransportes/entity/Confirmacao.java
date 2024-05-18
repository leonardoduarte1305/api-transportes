package br.com.transportes.apitransportes.entity;

import lombok.Getter;

@Getter
public enum Confirmacao {

    CONFIRMADO("CONFIRMADO"),

    NAO_CONFIRMADO("NAO_CONFIRMADO");

    private String value;

    Confirmacao(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
