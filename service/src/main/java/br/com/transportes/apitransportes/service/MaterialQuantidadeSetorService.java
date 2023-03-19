package br.com.transportes.apitransportes.service;

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
}
