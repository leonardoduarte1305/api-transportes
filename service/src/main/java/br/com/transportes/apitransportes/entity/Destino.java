package br.com.transportes.apitransportes.entity;

import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "destino")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class Destino {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer sedeId;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MaterialQuantidadeSetor> materiaisQntdSetor;

	private String status;

	public void confirmar() {
		status = "CONFIRMADO";
	}

	public void desconfirmar() {
		status = "NAO_CONFIRMADO";
	}
}
