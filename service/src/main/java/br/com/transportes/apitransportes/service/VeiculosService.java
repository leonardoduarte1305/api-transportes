package br.com.transportes.apitransportes.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import br.com.transportes.apitransportes.exception.EntidadeNaoEncontradaException;
import br.com.transportes.apitransportes.mapper.VeiculosMapper;
import br.com.transportes.apitransportes.repository.VeiculosRepository;
import br.com.transportes.server.model.UpsertVeiculo;
import br.com.transportes.server.model.Veiculo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class VeiculosService {

	private final VeiculosRepository veiculosRepository;
	private final VeiculosMapper veiculosMapper;

	public Veiculo upsertVeiculo(String id, UpsertVeiculo upsertVeiculo) {

		if (id.isBlank() || id.isEmpty()) {
			br.com.transportes.apitransportes.entity.Veiculo veiculoSalvo = veiculosRepository.save(
					veiculosMapper.toVeiculoEntity(upsertVeiculo));

			return veiculosMapper.toVeiculoDto(veiculoSalvo);
		} else {
			br.com.transportes.apitransportes.entity.Veiculo encontrado = encontrarVeiculoPorId(id);
			BeanUtils.copyProperties(upsertVeiculo, encontrado);

			br.com.transportes.apitransportes.entity.Veiculo veiculoEditada = veiculosRepository.save(encontrado);
			return veiculosMapper.toVeiculoDto(veiculoEditada);
		}
	}

	public List<br.com.transportes.server.model.Veiculo> listarTodos() {
		List<br.com.transportes.apitransportes.entity.Veiculo> encontrados = veiculosRepository.findAll();
		return encontrados.stream().map(veiculosMapper::toVeiculoDto).toList();
	}

	public void excluirVeiculoPorId(String id) {
		encontrarVeiculoPorId(id);
		veiculosRepository.deleteById(Long.valueOf(id));
	}

	public br.com.transportes.server.model.Veiculo trazerVeiculoPorId(String id) {
		return veiculosMapper.toVeiculoDto(encontrarVeiculoPorId(id));
	}

	br.com.transportes.apitransportes.entity.Veiculo encontrarVeiculoPorId(String id) throws NumberFormatException {
		Long idLong = Long.parseLong(id);
		return veiculosRepository.findById(idLong)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format("Veiculo com o id: %d não foi encontrado", idLong)));
	}
}
