package br.com.transportes.apitransportes.service;

import org.springframework.stereotype.Service;

import br.com.transportes.apitransportes.exception.EntidadeNaoEncontradaException;
import br.com.transportes.apitransportes.repository.SetoresRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SetoresService {

	private final SetoresRepository setoresRepository;

	public br.com.transportes.apitransportes.entity.Setor encontrarSetorPorId(String id)
			throws NumberFormatException {
		Long idLong = Long.parseLong(id);
		return setoresRepository.findById(idLong)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format("Setor com o id: %d não foi encontrado", idLong)));
	}
}
