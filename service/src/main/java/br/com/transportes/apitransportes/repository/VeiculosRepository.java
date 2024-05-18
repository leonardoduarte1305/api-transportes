package br.com.transportes.apitransportes.repository;

import br.com.transportes.apitransportes.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeiculosRepository extends JpaRepository<Veiculo, Long> {
}
