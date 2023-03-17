package br.com.transportes.apitransportes.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import br.com.transportes.apitransportes.exception.EntidadeNaoEncontradaException;
import br.com.transportes.apitransportes.mapper.DestinosMapper;
import br.com.transportes.apitransportes.repository.DestinosRepository;
import br.com.transportes.server.model.Destino;
import br.com.transportes.server.model.Material;
import br.com.transportes.server.model.UpsertDestino;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DestinosService {

	private final DestinosRepository destinosRepository;

	private final DestinosMapper destinosMapper;

	public Destino upsertDestino(String id, UpsertDestino upsertDestino) {
		if (id.isBlank() || id.isEmpty()) {
			br.com.transportes.apitransportes.entity.Destino salvarDestino = destinosMapper.toDestinoEntity(
					upsertDestino);
			salvarDestino.setStatus("NAO_CONFIRMADO");

			br.com.transportes.apitransportes.entity.Destino destinoSalvo = destinosRepository.save(
					salvarDestino);

			return destinosMapper.toDestinoDto(destinoSalvo);
		} else {
			br.com.transportes.apitransportes.entity.Destino encontrado = encontrarDestinoPorId(id);
			BeanUtils.copyProperties(upsertDestino, encontrado);

			br.com.transportes.apitransportes.entity.Destino materialEditado = destinosRepository.save(encontrado);
			return destinosMapper.toDestinoDto(materialEditado);
		}
	}

	public void excluirDestinoPorId(String id) {
		encontrarDestinoPorId(id);
		destinosRepository.deleteById(Long.valueOf(id));
	}

	public Destino trazerDestinoPorId(String id) {
		return destinosMapper.toDestinoDto(encontrarDestinoPorId(id));
	}

	public void confirmaDestino(String id, String confirmacao) {
		br.com.transportes.apitransportes.entity.Destino encontrado = encontrarDestinoPorId(id);
		//TODO Implementar confirmaDestino
	}

	public List<Material> trazMateriaisDoDestino(String id) {
		//TODO Implementar trazMateriaisDoDestino
		return null;
	}

	private br.com.transportes.apitransportes.entity.Destino encontrarDestinoPorId(String id)
			throws NumberFormatException {
		Long idLong = Long.parseLong(id);
		return destinosRepository.findById(idLong)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format("Destino com o id: %d não foi encontrado", idLong)));
	}
}
