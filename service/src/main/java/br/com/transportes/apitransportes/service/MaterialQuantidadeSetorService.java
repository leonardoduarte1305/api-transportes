package br.com.transportes.apitransportes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor;
import br.com.transportes.apitransportes.repository.MaterialQuantidadeSetorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialQuantidadeSetorService {

	private final MaterialQuantidadeSetorRepository materialQuantidadeSetorRepository;

	public MaterialQuantidadeSetor save(MaterialQuantidadeSetor entidade) {
		return materialQuantidadeSetorRepository.save(entidade);
	}

	public List<MaterialQuantidadeSetor> findAllByDestino_Id(Integer destinoId) {
		return materialQuantidadeSetorRepository.findAllByDestino_Id(destinoId);
	}

	public void removerMateriaisDoDestino(Integer id) {
		materialQuantidadeSetorRepository.removeAllByDestino_Id(id);
	}
}
