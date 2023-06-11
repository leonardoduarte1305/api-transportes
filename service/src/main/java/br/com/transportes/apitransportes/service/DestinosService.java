package br.com.transportes.apitransportes.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.transportes.apitransportes.email.EmailService;
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
import jakarta.transaction.Transactional;
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

	private final EmailService emailService;

	public Destino upsertDestino(Integer id, UpsertDestino upsertDestino) {
		if (id == null) {
			return salvarNovoDestino(upsertDestino);
		} else {
			return editarDestinoExistente(id, upsertDestino);
		}
	}

	@Transactional
	private Destino salvarNovoDestino(UpsertDestino upsertDestino) {
		br.com.transportes.apitransportes.entity.Sede sede = getSedePorId(upsertDestino);

		List<br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor> materiaisSalvos =
				salvarTodosOsMateriaisQuantidadeSetor(upsertDestino);

		br.com.transportes.apitransportes.entity.Destino destinoParaSalvar =
				getDestinoEntity(sede, materiaisSalvos);
		destinoParaSalvar.desconfirmar();

		br.com.transportes.apitransportes.entity.Destino destinoSalvo = destinosRepository.save(destinoParaSalvar);
		materiaisSalvos.forEach(item -> item.setDestino(destinoSalvo));
		materialQuantidadeSetorService.salvarTodos(materiaisSalvos);

		if (!sede.getInscritos().isEmpty()) {
			emailService.enviarConfirmacaoDeDestino(sede.getNome(), sede.getInscritos());
		}
		return destinosMapper.toDestinoDto(destinoSalvo);
	}

	@Transactional
	private Destino editarDestinoExistente(Integer id, UpsertDestino upsertDestino) {
		br.com.transportes.apitransportes.entity.Destino destinoParaEditar = encontrarDestinoPorId(id);
		limparListaDeMateriaisDoDestino(id);

		List<br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor> novosMateriaisSalvos =
				salvarTodosOsMateriaisQuantidadeSetor(upsertDestino);

		novosMateriaisSalvos.forEach(item -> item.setDestino(destinoParaEditar));
		destinoParaEditar.setMateriaisQntdSetor(novosMateriaisSalvos);

		return destinosMapper.toDestinoDto(destinoParaEditar);
	}

	private Sede getSedePorId(UpsertDestino upsertDestino) {
		return sedesService.encontrarSedePorId(upsertDestino.getSedeId());
	}

	private void limparListaDeMateriaisDoDestino(Integer id) {
		materialQuantidadeSetorService.removerMateriaisDoDestino(id);
	}

	private br.com.transportes.apitransportes.entity.Destino getDestinoEntity(Sede sede,
			List<br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor> materialQuantidadeSetor) {
		return destinosMapper.toDestinoEntity(sede, materialQuantidadeSetor);
	}

	private List<br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor> salvarTodosOsMateriaisQuantidadeSetor(
			UpsertDestino upsertDestino) {
		return upsertDestino.getMateriaisQntdSetor().stream().map(item -> {
			Setor setor = setoresService.encontrarSetorPorId(String.valueOf(item.getSetorDestino()));
			Material material = materiaisService.encontrarMaterialPorId(item.getMaterialId());

			br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor paraSalvar =
					materialQuantidadeSetorMapper.toMaterialQuantidadeSetorEntity(
							item, material, setor);
			return materialQuantidadeSetorService.save(paraSalvar);
		}).toList();
	}

	public void excluirDestinoPorId(Integer id) {
		br.com.transportes.apitransportes.entity.Destino encontrado = encontrarDestinoPorId(id);
		encontrado.excluirDoBancoLogicamente();
		destinosRepository.save(encontrado);
	}

	public Destino trazerDestinoPorId(Integer id) {
		List<br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor> materialQuantidadeSetors =
				materialQuantidadeSetorService.findAllByDestinoId(id);

		br.com.transportes.apitransportes.entity.Destino destino = encontrarDestinoPorId(id);
		destino.setMateriaisQntdSetor(new ArrayList<>());
		destino.setMateriaisQntdSetor(materialQuantidadeSetors);

		return destinosMapper.toDestinoDto(destino);
	}

	public void confirmaDestino(Integer id, Confirmacao confirmacao) {
		br.com.transportes.apitransportes.entity.Destino encontrado = encontrarDestinoPorId(id);

		if ("CONFIRMADO".equals(confirmacao.getConfirmacao().toString())) {
			encontrado.confirmar();
			destinosRepository.save(encontrado);
			// TODO fazer o envio do email de confirmação
		} else {
			encontrado.desconfirmar();
			destinosRepository.save(encontrado);
		}
	}

	public List<MaterialQuantidadeSetor> trazMateriaisDoDestino(Integer id) {
		br.com.transportes.apitransportes.entity.Destino destino = encontrarDestinoPorId(id);
		return materialQuantidadeSetorService.findAllByDestinoId(Integer.valueOf(id))
				.stream()
				.map(materialQuantidadeSetorMapper::toMaterialQuantidadeSetorDto)
				.collect(Collectors.toList());
	}

	public br.com.transportes.apitransportes.entity.Destino encontrarDestinoPorId(Integer id)
			throws NumberFormatException {
		Long idLong = Long.parseLong(String.valueOf(id));
		br.com.transportes.apitransportes.entity.Destino encontrado = destinosRepository.findById(idLong)
				.filter(item -> !item.isExcluido())
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format("Destino com o id: %d não foi encontrado", idLong)));

		return encontrado;
	}

	public List<br.com.transportes.apitransportes.entity.Destino> findAllByIdIsIn(List<Integer> destinos) {
		return destinosRepository.findByIdIsIn(destinos).stream().filter(item -> !item.isExcluido()).toList();
	}

	public List<br.com.transportes.apitransportes.entity.Destino> trazerDestinosDaViagem(Integer id) {
		List<br.com.transportes.apitransportes.entity.Destino> destinos = destinosRepository.findAllByViagemId(id);

		List<Integer> destinosIds = new ArrayList<>();
		for (br.com.transportes.apitransportes.entity.Destino destino : destinos) {
			destinosIds.add(destino.getId());
		}

		List<br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor> materiais =
				materialQuantidadeSetorService.findAllByDestinoIdIsIn(destinosIds);

		for (br.com.transportes.apitransportes.entity.Destino destino : destinos) {
			for (br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor material : materiais) {
				if (Objects.equals(material.getDestino().getId(), destino.getId())) {
					destino.adicionarMaterial(material);
				}
			}
		}

		return destinos;
	}

	public void salvarTodos(List<br.com.transportes.apitransportes.entity.Destino> destinos) {
		destinosRepository.saveAll(destinos);
	}
}
