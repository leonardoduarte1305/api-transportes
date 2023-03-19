package br.com.transportes.apitransportes.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
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

	public Motorista upsertMotorista(String id, AtributosMotorista atributosMotorista) {
		if (id.isBlank() || id.isEmpty()) {
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

	public List<Motorista> listarTodos() {
		List<br.com.transportes.apitransportes.entity.Motorista> encontrados = motoristasRepository.findAll();
		return encontrados.stream().map(motoristasMapper::toMotoristaDto).toList();
	}

	public void excluirMotoristaPorId(String id) {
		encontrarMotoristaPorId(id);
		motoristasRepository.deleteById(Long.valueOf(id));
	}

	public Motorista trazerMotoristaPorId(String id) {
		return motoristasMapper.toMotoristaDto(encontrarMotoristaPorId(id));
	}

	public br.com.transportes.apitransportes.entity.Motorista encontrarMotoristaPorId(String id)
			throws NumberFormatException {
		Long idLong = Long.parseLong(id);
		return motoristasRepository.findById(idLong)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format("Motorista com o id: %d não foi encontrado", idLong)));
	}
}
