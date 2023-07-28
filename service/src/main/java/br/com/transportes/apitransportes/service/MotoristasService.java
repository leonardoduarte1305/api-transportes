package br.com.transportes.apitransportes.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.com.transportes.apitransportes.exception.EntidadeNaoEncontradaException;
import br.com.transportes.apitransportes.mapper.MotoristasMapper;
import br.com.transportes.apitransportes.repository.MotoristasRepository;
import br.com.transportes.server.model.AtributosMotorista;
import br.com.transportes.server.model.Motorista;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MotoristasService {

	private final MotoristasRepository motoristasRepository;
	private final MotoristasMapper motoristasMapper;

	public Motorista upsertMotorista(Integer id, AtributosMotorista atributosMotorista) {
		if (id == null) {
			br.com.transportes.apitransportes.entity.Motorista veiculoSalvo = motoristasRepository.save(
					motoristasMapper.toMotoristaEntity(atributosMotorista));

			return motoristasMapper.toMotoristaDto(veiculoSalvo);
		} else {
			br.com.transportes.apitransportes.entity.Motorista encontrado = encontrarMotoristaPorId(id);
			BeanUtils.copyProperties(atributosMotorista, encontrado);

			br.com.transportes.apitransportes.entity.Motorista veiculoEditado = motoristasRepository.save(encontrado);
			return motoristasMapper.toMotoristaDto(veiculoEditado);
		}
	}

	@Cacheable(value = "motoristas")
	public List<Motorista> listarTodos() {
		try {
			Thread.sleep(7000L);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		log.info("Buscando motoristas.");
		List<br.com.transportes.apitransportes.entity.Motorista> encontrados = motoristasRepository.findAll();
		return encontrados.stream().map(motoristasMapper::toMotoristaDto).toList();
	}

	public void excluirMotoristaPorId(Integer id) {
		encontrarMotoristaPorId(id);
		motoristasRepository.deleteById(Long.valueOf(id));
	}

	public Motorista trazerMotoristaPorId(Integer id) {
		return motoristasMapper.toMotoristaDto(encontrarMotoristaPorId(id));
	}

	public br.com.transportes.apitransportes.entity.Motorista encontrarMotoristaPorId(Integer id)
			throws NumberFormatException {
		Long idLong = Long.parseLong(String.valueOf(id));
		return motoristasRepository.findById(idLong)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format("Motorista com o id: %d não foi encontrado", idLong)));
	}
}
