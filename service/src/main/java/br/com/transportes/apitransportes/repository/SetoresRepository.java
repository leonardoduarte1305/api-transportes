package br.com.transportes.apitransportes.repository;

import br.com.transportes.apitransportes.entity.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SetoresRepository extends JpaRepository<Setor, Long> {
}
