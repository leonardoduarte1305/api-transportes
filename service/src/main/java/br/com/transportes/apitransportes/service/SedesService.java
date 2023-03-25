package br.com.transportes.apitransportes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.transportes.apitransportes.exception.EntidadeNaoEncontradaException;
import br.com.transportes.apitransportes.mapper.SedesMapper;
import br.com.transportes.apitransportes.repository.SedesRepository;
import br.com.transportes.server.model.Sede;
import br.com.transportes.server.model.UpsertSede;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SedesService {

	private final SedesRepository sedesRepository;
	private final SedesMapper sedesMapper;

	@Transactional
	public Sede upsertSede(Integer id, UpsertSede upsertSede) {

		if (id == null) {
			br.com.transportes.apitransportes.entity.Sede sedeSalva = sedesRepository.save(
					sedesMapper.toSedeEntity(upsertSede));

			return sedesMapper.toSedeDto(sedeSalva);
		} else {
			encontrarSedePorId(id);

			br.com.transportes.apitransportes.entity.Sede sedeMapeada = sedesMapper.toSedeEntity(upsertSede);
			sedeMapeada.setId(Long.valueOf(id));

			br.com.transportes.apitransportes.entity.Sede sedeEditada = sedesRepository.save(sedeMapeada);
			return sedesMapper.toSedeDto(sedeEditada);
		}
	}

	public List<Sede> listarTodas() {
		List<br.com.transportes.apitransportes.entity.Sede> encontradas = sedesRepository.findAll();
		return encontradas.stream().map(sedesMapper::toSedeDto).toList();
	}

	public void excluirSedePorId(Integer id) {
		encontrarSedePorId(id);
		sedesRepository.deleteById(Long.valueOf(id));
	}

	public Sede trazerSedePorId(Integer id) {
		return sedesMapper.toSedeDto(encontrarSedePorId(id));
	}

	public br.com.transportes.apitransportes.entity.Sede encontrarSedePorId(Integer id)
			throws NumberFormatException {
		Long idLong = Long.parseLong(String.valueOf(id));
		return sedesRepository.findById(idLong)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format("Sede com o id: %d não foi encontrada", idLong)));
	}

}
