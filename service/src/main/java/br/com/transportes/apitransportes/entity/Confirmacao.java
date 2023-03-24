package br.com.transportes.apitransportes.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum Confirmacao {

	CONFIRMADO("CONFIRMADO"),

	NAO_CONFIRMADO("NAO_CONFIRMADO");

	private String value;

	Confirmacao(String value) {
		this.value = value;
	}
}