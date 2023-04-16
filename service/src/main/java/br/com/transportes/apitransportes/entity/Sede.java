package br.com.transportes.apitransportes.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@Table(name = "sede")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
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
	private Uf uf;

	@JoinColumn(nullable = false)
	private String nome;

	@JoinColumn(nullable = false)
	private String observacao;

	@ElementCollection(fetch = FetchType.EAGER)
	@Column(name = "inscritos_emails")
	private List<String> inscritos = new ArrayList<>();

	@Override public String toString() {
		return "Nome: " + nome + ", Observações" + observacao +
				"\nEndereco: Rua " + rua + ", " + numero + " - CEP'" + cep + ", " + cidade + "/" + uf;
	}

	public void inscreverUsuario(String email) {
		this.inscritos.add(email);
	}

	public void removerInscrito(String email) {
		this.inscritos.remove(email);
	}
}
