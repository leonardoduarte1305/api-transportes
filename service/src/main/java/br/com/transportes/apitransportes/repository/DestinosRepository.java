package br.com.transportes.apitransportes.repository;

import br.com.transportes.apitransportes.entity.Destino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinosRepository extends JpaRepository<Destino, Long> {
    List<Destino> findByIdIsIn(List<Integer> destinos);

    List<Destino> findAllByViagemId(Integer id);

    void removeAllByViagem_Id(Integer id);
}
