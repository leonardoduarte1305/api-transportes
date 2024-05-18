package br.com.transportes.apitransportes.repository;

import br.com.transportes.apitransportes.entity.Motorista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotoristasRepository extends JpaRepository<Motorista, Long> {
}
