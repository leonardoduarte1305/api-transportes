package br.com.transportes.apitransportes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.transportes.apitransportes.entity.Veiculo;

@Repository
public interface VeiculosRepository extends JpaRepository<Veiculo, Long> {
}
