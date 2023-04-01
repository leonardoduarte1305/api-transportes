package br.com.transportes.apitransportes.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "material_quantidade_setor")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class MaterialQuantidadeSetor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "materialId", nullable = false)
	private Material material;

	@JoinColumn(nullable = false)
	private Integer quantidade;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "setorId", nullable = false)
	private Setor setorDestino;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "destinoId")
	private Destino destino;

	@Override public String toString() {
		return "Material: " + material.toString() + ", Quantidade: " + quantidade + ", Setor de destino: " + setorDestino + "\n";
	}
}
