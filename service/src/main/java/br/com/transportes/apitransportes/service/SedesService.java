package br.com.transportes.apitransportes.service;

import org.springframework.stereotype.Service;

import br.com.transportes.apitransportes.mapper.SedesMapper;
import br.com.transportes.apitransportes.repository.SedesRepository;
import br.com.transportes.server.model.Sede;
import br.com.transportes.server.model.UpsertSede;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SedesService {

	private final SedesRepository sedesRepository;
	private final SedesMapper sedesMapper;

	public Sede salvarNovaSede(UpsertSede upsertSede) {
		br.com.transportes.apitransportes.model.entity.Sede sedeSalva = sedesRepository.save(
				sedesMapper.toSedeEntity(upsertSede));

		return sedesMapper.toSedeDto(sedeSalva);
	}

}
