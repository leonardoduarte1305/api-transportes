package br.com.transportes.apitransportes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor;

@Repository
public interface MaterialQuantidadeSetorRepository extends JpaRepository<MaterialQuantidadeSetor, Integer> {

	List<MaterialQuantidadeSetor> findAllByDestino_Id(Integer destinoId);

	void removeAllByDestino_Id(Integer id);
}
