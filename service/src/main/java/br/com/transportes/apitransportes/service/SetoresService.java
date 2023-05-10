package br.com.transportes.apitransportes.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.transportes.apitransportes.exception.EntidadeNaoEncontradaException;
import br.com.transportes.apitransportes.mapper.SetoresMapper;
import br.com.transportes.apitransportes.repository.SetoresRepository;
import br.com.transportes.server.model.Setor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SetoresService {

	private final SetoresRepository setoresRepository;

	private final SetoresMapper setoresMapper;

	public br.com.transportes.apitransportes.entity.Setor encontrarSetorPorId(String id)
			throws NumberFormatException {
		Long idLong = Long.parseLong(id);
		return setoresRepository.findById(idLong)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format("Setor com o id: %d não foi encontrado", idLong)));
	}

	public Setor criaSetor(Setor setor) {
		return setoresMapper.toSetorDto(setoresRepository.save(setoresMapper.toSetorEntity(setor)));
	}

	public List<Setor> listaTodosOsSetores() {
		List<br.com.transportes.apitransportes.entity.Setor> setores = setoresRepository.findAll();
		return setores.stream().map(setoresMapper::toSetorDto).collect(Collectors.toList());
	}
}
