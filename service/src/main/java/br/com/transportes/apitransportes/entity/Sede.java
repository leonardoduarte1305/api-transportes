package br.com.transportes.apitransportes.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sede")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class Sede {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(nullable = false)
	private String rua;

	@JoinColumn(nullable = false)
	private Integer numero;

	@JoinColumn(nullable = false)
	private String cep;

	@JoinColumn(nullable = false)
	private String cidade;

	@Enumerated(EnumType.STRING)
	@JoinColumn(nullable = false)
	private Uf uf;

	@JoinColumn(nullable = false)
	private String nome;

	@JoinColumn(nullable = false)
	private String observacao;
}
