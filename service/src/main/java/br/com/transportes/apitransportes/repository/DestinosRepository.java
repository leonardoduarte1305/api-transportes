package br.com.transportes.apitransportes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.transportes.apitransportes.entity.Destino;

@Repository
public interface DestinosRepository extends JpaRepository<Destino, Long> {
	List<Destino> findByIdIsIn(List<Integer> destinos);
}
