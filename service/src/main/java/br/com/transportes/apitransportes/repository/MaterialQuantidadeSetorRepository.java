package br.com.transportes.apitransportes.repository;

import br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialQuantidadeSetorRepository extends JpaRepository<MaterialQuantidadeSetor, Integer> {

    List<MaterialQuantidadeSetor> findAllByDestinoIdIs(Integer destinoId);

    List<MaterialQuantidadeSetor> findAllByDestinoIdIsIn(List<Integer> destinosId);
}
