package br.com.transportes.apitransportes.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.transportes.apitransportes.entity.Motorista;
import br.com.transportes.apitransportes.entity.Veiculo;
import br.com.transportes.apitransportes.exception.EntidadeNaoEncontradaException;
import br.com.transportes.apitransportes.mapper.DestinosMapper;
import br.com.transportes.apitransportes.mapper.ViagensMapper;
import br.com.transportes.apitransportes.repository.MaterialQuantidadeSetorRepository;
import br.com.transportes.apitransportes.repository.ViagensRepository;
import br.com.transportes.server.model.Confirmacao;
import br.com.transportes.server.model.Destino;
import br.com.transportes.server.model.Encerramento;
import br.com.transportes.server.model.UpsertViagem;
import br.com.transportes.server.model.Viagem;
import jakarta.transaction.Transactional;
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

	private final MaterialQuantidadeSetorRepository materialQuantidadeSetorRepository;

	private final ViagensMapper viagensMapper;
	private final DestinosMapper destinosMapper;

	public Viagem upsertViagem(Integer id, UpsertViagem upsertViagem) {
		if (id == null) {
			return salvarNovaViagem(upsertViagem);
		} else {
			return editarViagemExistente(id, upsertViagem);
		}
	}

	@Transactional
	private Viagem salvarNovaViagem(UpsertViagem upsertViagem) {
		Motorista motorista = getMotorista(upsertViagem);
		Veiculo veiculo = getVeiculo(upsertViagem);
		List<br.com.transportes.apitransportes.entity.Destino> destinosEncontrados = getNovosDestinosParaAViagem(
				upsertViagem);

		br.com.transportes.apitransportes.entity.Viagem viagemParaSalvar = gerarViagemParaSalvar(upsertViagem,
				motorista, veiculo, destinosEncontrados);
		viagemParaSalvar.desconfirmar();

		br.com.transportes.apitransportes.entity.Viagem viagemSalva = viagensRepository.save(viagemParaSalvar);
		destinosEncontrados.forEach(destino -> destino.setViagem(viagemSalva));
		destinosService.salvarTodos(destinosEncontrados);

		return viagensMapper.toViagemDto(viagemSalva);
	}

	private Motorista getMotorista(UpsertViagem upsertViagem) {
		String motoristaId = String.valueOf(upsertViagem.getMotoristaId());
		Motorista motorista = motoristasService.encontrarMotoristaPorId(motoristaId);
		return motorista;
	}

	private Veiculo getVeiculo(UpsertViagem upsertViagem) {
		String veiculoId = String.valueOf(upsertViagem.getVeiculoId());
		Veiculo veiculo = veiculosService.encontrarVeiculoPorId(String.valueOf(veiculoId));
		return veiculo;
	}

	private br.com.transportes.apitransportes.entity.Viagem gerarViagemParaSalvar(UpsertViagem upsertViagem,
			Motorista motorista, Veiculo veiculo,
			List<br.com.transportes.apitransportes.entity.Destino> destinosEncontrados) {
		return viagensMapper.toViagemEntity(upsertViagem, motorista, veiculo, destinosEncontrados);
	}

	@Transactional
	private Viagem editarViagemExistente(Integer id, UpsertViagem upsertViagem) {
		br.com.transportes.apitransportes.entity.Viagem viagemParaEditar = encontrarViagemPorId(id);
		limparListaDeDestinos(id);

		List<br.com.transportes.apitransportes.entity.Destino> novosDestinos = getNovosDestinosParaAViagem(
				upsertViagem);
		viagemParaEditar.setDestinos(novosDestinos);
		novosDestinos.forEach(destino -> destino.setViagem(viagemParaEditar));

		return viagensMapper.toViagemDto(viagemParaEditar);
	}

	private List<br.com.transportes.apitransportes.entity.Destino> getNovosDestinosParaAViagem(
			UpsertViagem upsertViagem) {
		return destinosService.findAllByIdIsIn(upsertViagem.getDestinos());
	}

	private void limparListaDeDestinos(Integer id) {
		destinosService.excluirDestinosByViagemId(id);
	}

	public void confirmaViagem(Integer id, Confirmacao confirmacao) {
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
				viagensRepository.findAll();//.stream().filter(item -> !item.isExcluido())
		//.toList();
		log.error("encontradas = " + encontradas);
		return encontradas.stream().map(viagensMapper::toViagemDto).collect(Collectors.toList());
	}

	public List<Destino> listarDestinosDaViagem(Integer id) {
		List<br.com.transportes.apitransportes.entity.Destino> destinos = destinosService.trazerDestinosDaViagem(id);
		return destinos.stream().map(destinosMapper::toDestinoDto).collect(Collectors.toList());
	}

	public void encerraViagem(Integer id, Encerramento encerramento) {
		br.com.transportes.apitransportes.entity.Viagem encontrada = encontrarViagemPorId(id);

		if ("ENCERRAR".equals(encerramento.getEncerrado().toString())) {
			log.error("VIAGEM ID: {} ENCERRAR ==========================", id);
			encontrada.encerrar();
			viagensRepository.save(encontrada);
		}
	}

	private br.com.transportes.apitransportes.entity.Viagem encontrarViagemPorId(Integer id)
			throws NumberFormatException {
		Long idLong = Long.parseLong(String.valueOf(id));
		return viagensRepository.findById(idLong)
				.filter(item -> !item.isExcluido())
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format("Viagem com o id: %d não foi encontrada", idLong)));
	}
}
