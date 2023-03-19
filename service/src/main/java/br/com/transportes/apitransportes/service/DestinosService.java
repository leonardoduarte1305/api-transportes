package br.com.transportes.apitransportes.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.transportes.apitransportes.entity.Material;
import br.com.transportes.apitransportes.entity.Sede;
import br.com.transportes.apitransportes.entity.Setor;
import br.com.transportes.apitransportes.exception.EntidadeNaoEncontradaException;
import br.com.transportes.apitransportes.mapper.DestinosMapper;
import br.com.transportes.apitransportes.mapper.MaterialQuantidadeSetorMapper;
import br.com.transportes.apitransportes.repository.DestinosRepository;
import br.com.transportes.server.model.Confirmacao;
import br.com.transportes.server.model.Destino;
import br.com.transportes.server.model.MaterialQuantidadeSetor;
import br.com.transportes.server.model.UpsertDestino;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DestinosService {

	private final DestinosRepository destinosRepository;
	private final MateriaisService materiaisService;
	private final MaterialQuantidadeSetorService materialQuantidadeSetorService;
	private final SedesService sedesService;
	private final SetoresService setoresService;

	private final DestinosMapper destinosMapper;
	private final MaterialQuantidadeSetorMapper materialQuantidadeSetorMapper;

	public Destino upsertDestino(String id, UpsertDestino upsertDestino) {
		br.com.transportes.apitransportes.entity.Sede sede = sedesService.encontrarSedePorId(
				String.valueOf(upsertDestino.getSedeId()));

		if (id.isBlank() || id.isEmpty()) {
			List<br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor> materialQuantidadeSetor =
					salvarTodosOsMateriaisQuantidadeSetor(upsertDestino);

			br.com.transportes.apitransportes.entity.Destino salvarDestino =
					getDestinoEntity(sede, materialQuantidadeSetor);
			salvarDestino.desconfirmar();

			br.com.transportes.apitransportes.entity.Destino destinoSalvo = destinosRepository.save(salvarDestino);
			return destinosMapper.toDestinoDto(destinoSalvo);
		} else {
			br.com.transportes.apitransportes.entity.Destino encontrado = encontrarDestinoPorId(id);

			List<br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor> materialQuantidadeSetor =
					salvarTodosOsMateriaisQuantidadeSetor(upsertDestino);

			br.com.transportes.apitransportes.entity.Destino salvarDestino =
					getDestinoEntity(sede, materialQuantidadeSetor);
			salvarDestino.setStatus(encontrado.getStatus());
			salvarDestino.setId(encontrado.getId());

			br.com.transportes.apitransportes.entity.Destino destinoSalvo = destinosRepository.save(salvarDestino);
			return destinosMapper.toDestinoDto(destinoSalvo);
		}
	}

	private br.com.transportes.apitransportes.entity.Destino getDestinoEntity(Sede sede,
			List<br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor> materialQuantidadeSetor) {
		return destinosMapper.toDestinoEntity(sede, materialQuantidadeSetor);
	}

	private List<br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor> salvarTodosOsMateriaisQuantidadeSetor(
			UpsertDestino upsertDestino) {
		return upsertDestino.getMateriaisQntdSetor().stream().map(item -> {
			Setor setor = setoresService.encontrarSetorPorId(String.valueOf(item.getSetorDestino()));
			Material material = materiaisService.encontrarMaterialPorId(
					String.valueOf(item.getMaterialId()));
			br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor paraSalvar =
					materialQuantidadeSetorMapper.toMaterialQuantidadeSetorEntity(item, material, setor);
			return materialQuantidadeSetorService.save(paraSalvar);
		}).toList();
	}

	public void excluirDestinoPorId(String id) {
		encontrarDestinoPorId(id);
		destinosRepository.deleteById(Long.valueOf(id));
	}

	public Destino trazerDestinoPorId(String id) {
		return destinosMapper.toDestinoDto(encontrarDestinoPorId(id));
	}

	public void confirmaDestino(String id, Confirmacao confirmacao) {
		br.com.transportes.apitransportes.entity.Destino encontrado = encontrarDestinoPorId(id);

		if ("CONFIRMADO".equals(confirmacao.getConfirmacao().toString())) {
			encontrado.confirmar();
		} else {
			encontrado.desconfirmar();
		}
		destinosRepository.save(encontrado);
	}

	public List<MaterialQuantidadeSetor> trazMateriaisDoDestino(String id) {
		br.com.transportes.apitransportes.entity.Destino destino = encontrarDestinoPorId(id);


		return destino.getMateriaisQntdSetor().stream().map(materialQuantidadeSetorMapper::toMaterialQuantidadeSetorDto)
				.collect(Collectors.toList());
	}

	public br.com.transportes.apitransportes.entity.Destino encontrarDestinoPorId(String id)
			throws NumberFormatException {
		Long idLong = Long.parseLong(id);
		br.com.transportes.apitransportes.entity.Destino encontrado = destinosRepository.findById(idLong)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format("Destino com o id: %d não foi encontrado", idLong)));
		log.error("encontrado = " + encontrado);
		return encontrado;
	}

	public List<br.com.transportes.apitransportes.entity.Destino> findAllByIdIsIn(List<Integer> destinos) {
		return destinosRepository.findByIdIsIn(destinos);
	}
}
