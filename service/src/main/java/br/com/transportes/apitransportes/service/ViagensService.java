package br.com.transportes.apitransportes.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.transportes.apitransportes.entity.Motorista;
import br.com.transportes.apitransportes.entity.Veiculo;
import br.com.transportes.apitransportes.exception.EntidadeNaoEncontradaException;
import br.com.transportes.apitransportes.mapper.DestinosMapper;
import br.com.transportes.apitransportes.mapper.ViagensMapper;
import br.com.transportes.apitransportes.repository.ViagensRepository;
import br.com.transportes.server.model.Confirmacao;
import br.com.transportes.server.model.Destino;
import br.com.transportes.server.model.UpsertViagem;
import br.com.transportes.server.model.Viagem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViagensService {

	private final ViagensRepository viagensRepository;
	private final DestinosService destinosService;
	private final MotoristasService motoristasService;
	private final VeiculosService veiculosService;

	private final ViagensMapper viagensMapper;
	private final DestinosMapper destinosMapper;

	public Viagem upsertViagem(String viagemId, UpsertViagem upsertViagem) {
		if (viagemId.isBlank() || viagemId.isEmpty()) {
			Motorista motorista = motoristasService.encontrarMotoristaPorId(String.valueOf(upsertViagem.getMotoristaId()));
			Veiculo veiculo = veiculosService.encontrarVeiculoPorId(String.valueOf(upsertViagem.getVeiculoId()));
			List<br.com.transportes.apitransportes.entity.Destino> destinosEncontrados = destinosService.findAllByIdIsIn(upsertViagem.getDestinos());

			br.com.transportes.apitransportes.entity.Viagem salvarViagem = viagensMapper.toViagemEntity(upsertViagem, motorista, veiculo, destinosEncontrados);
			salvarViagem.desconfirmar();

			br.com.transportes.apitransportes.entity.Viagem viagemSalva = viagensRepository.save(salvarViagem);

			return viagensMapper.toViagemDto(viagemSalva);
		} else {
			br.com.transportes.apitransportes.entity.Viagem encontrada = encontrarViagemPorId(viagemId);
			BeanUtils.copyProperties(upsertViagem, encontrada);

			br.com.transportes.apitransportes.entity.Viagem viagemEditada = viagensRepository.save(encontrada);
			return viagensMapper.toViagemDto(viagemEditada);
		}
	}

	public void confirmaViagem(String id, Confirmacao confirmacao) {
		br.com.transportes.apitransportes.entity.Viagem encontrada = encontrarViagemPorId(id);

		if ("CONFIRMADO".equals(confirmacao.getConfirmacao().toString())) {
			encontrada.confirmar();
		} else {
			encontrada.desconfirmar();
		}
		viagensRepository.save(encontrada);
	}

	public List<Viagem> listarViagens() {
		List<br.com.transportes.apitransportes.entity.Viagem> encontradas =
				viagensRepository.findAll(Sort.by(Sort.Direction.ASC)).stream().filter(item -> !item.isExcluido()).toList();
		//		return encontradas.stream().map(viagensMapper::toViagemDto).collect(Collectors.toList());
		return null;
	}

	public List<Destino> listarDestinosDaViagem(String id) {
		br.com.transportes.apitransportes.entity.Viagem encontrada = encontrarViagemPorId(id);
		return encontrada.getDestinos().stream().map(destinosMapper::toDestinoDto).collect(Collectors.toList());
	}

	private br.com.transportes.apitransportes.entity.Viagem encontrarViagemPorId(String id)
			throws NumberFormatException {
		Long idLong = Long.parseLong(id);
		return viagensRepository.findById(idLong)
				.filter(item -> !item.isExcluido())
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format("Viagem com o id: %d não foi encontrada", idLong)));
	}
}
