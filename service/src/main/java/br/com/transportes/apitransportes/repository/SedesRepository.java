package br.com.transportes.apitransportes.repository;

import br.com.transportes.apitransportes.entity.Sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SedesRepository extends JpaRepository<Sede, Long> {
}
