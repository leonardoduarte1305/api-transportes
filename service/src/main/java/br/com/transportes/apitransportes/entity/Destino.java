package br.com.transportes.apitransportes.entity;

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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "destino")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Destino {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sedeId", nullable = false)
	private Sede sede;

	@OneToMany(mappedBy = "id", fetch = FetchType.EAGER)
	private List<MaterialQuantidadeSetor> materiaisQntdSetor;

	@Enumerated(EnumType.STRING)
	private Confirmacao status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "viagem_id")
	private Viagem viagem;

	@JoinColumn(nullable = true)
	private boolean excluido;

	public void confirmar() {
		status = Confirmacao.CONFIRMADO;
	}

	public void desconfirmar() {
		status = Confirmacao.NAO_CONFIRMADO;
	}

	public void excluirDoBancoLogicamente() {
		excluido = true;
	}
}
