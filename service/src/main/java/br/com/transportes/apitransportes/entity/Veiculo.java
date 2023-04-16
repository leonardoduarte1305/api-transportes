package br.com.transportes.apitransportes.entity;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "veiculo")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Veiculo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@JoinColumn(nullable = false)
	private String modelo;

	@JoinColumn(nullable = false)
	private String marca;

	@JoinColumn(nullable = false)
	private String placa;

	@JoinColumn(nullable = false)
	private Integer ano;

	@JoinColumn(nullable = false)
	private BigDecimal renavan;

	@JoinColumn(nullable = false)
	private String tamanho;

	@Override public String toString() {
		return "Modelo: " + modelo + " - " + marca + ", Placa: " + placa + " - " + ano + ", Tamanho: " + tamanho;
	}
}
