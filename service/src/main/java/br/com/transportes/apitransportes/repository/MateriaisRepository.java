package br.com.transportes.apitransportes.repository;

import br.com.transportes.apitransportes.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MateriaisRepository extends JpaRepository<Material, Long> {

    Material findById(Integer id);
}
