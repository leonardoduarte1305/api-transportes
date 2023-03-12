package br.com.transportes.apitransportes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.transportes.apitransportes.model.entity.Sede;

@Repository
public interface SedesRepository extends JpaRepository<Sede, Long> {
}
