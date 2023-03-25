package br.com.transportes.apitransportes.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "viagem")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class Viagem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "motoristaId", nullable = false)
	private Motorista motorista;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "veiculoId", nullable = false)
	private Veiculo veiculo;

	@OneToMany(mappedBy = "id", fetch = FetchType.EAGER)
	private List<Destino> destinos = new ArrayList<>();

	@JoinColumn(nullable = false)
	private String datetimeSaida;

	private String datetimeVolta;

	@Enumerated(EnumType.STRING)
	@JoinColumn(nullable = false)
	private Confirmacao status;

	@JoinColumn(nullable = true)
	private boolean excluido;

	@JoinColumn(nullable = true)
	private boolean encerrado;

	public void confirmar() {
		status = Confirmacao.CONFIRMADO;
	}

	public void desconfirmar() {
		status = Confirmacao.NAO_CONFIRMADO;
	}

	public void excluirDoBancoLogicamente() {
		excluido = true;
	}

	public void encerrar() {
		encerrado = true;
	}

	public String isEncerrado() {
		return encerrado ? "ENCERRADO" : "NAO_ENCERRADO";
	}
}
