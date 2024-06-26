package br.com.transportes.apitransportes.service;

import br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor;
import br.com.transportes.apitransportes.repository.MaterialQuantidadeSetorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialQuantidadeSetorService {

    private final MaterialQuantidadeSetorRepository materialQuantidadeSetorRepository;

    public MaterialQuantidadeSetor save(MaterialQuantidadeSetor entidade) {
        return materialQuantidadeSetorRepository.save(entidade);
    }

    public List<MaterialQuantidadeSetor> findAllByDestinoId(Integer destinoId) {
        return materialQuantidadeSetorRepository.findAllByDestinoIdIs(destinoId);
    }

    public List<MaterialQuantidadeSetor> findAllByDestinoIdIsIn(List<Integer> destinosId) {
        return materialQuantidadeSetorRepository.findAllByDestinoIdIsIn(destinosId);
    }

    public void removerMateriaisDoDestino(Integer id) {
        List<MaterialQuantidadeSetor> listaDeMateriais = materialQuantidadeSetorRepository.findAllByDestinoIdIs(id);
        for (MaterialQuantidadeSetor material : listaDeMateriais) {
            material.setDestino(null);
        }
        materialQuantidadeSetorRepository.saveAll(listaDeMateriais);
    }

    public void salvarTodos(List<MaterialQuantidadeSetor> materiaisSalvos) {
        materialQuantidadeSetorRepository.saveAll(materiaisSalvos);
    }
}
