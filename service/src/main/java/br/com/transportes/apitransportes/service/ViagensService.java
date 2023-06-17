package br.com.transportes.apitransportes.service;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;

import br.com.transportes.apitransportes.email.EmailService;
import br.com.transportes.apitransportes.entity.Motorista;
import br.com.transportes.apitransportes.entity.Veiculo;
import br.com.transportes.apitransportes.exception.EntidadeNaoEncontradaException;
import br.com.transportes.apitransportes.mapper.DestinosMapper;
import br.com.transportes.apitransportes.mapper.ViagensMapper;
import br.com.transportes.apitransportes.pdf.CriadorDeRelatorioDeViagem;
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
	private final EmailService emailService;

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
		return motoristasService.encontrarMotoristaPorId(upsertViagem.getMotoristaId());
	}

	private Veiculo getVeiculo(UpsertViagem upsertViagem) {
		return veiculosService.encontrarVeiculoPorId(upsertViagem.getVeiculoId());
	}

	private br.com.transportes.apitransportes.entity.Viagem gerarViagemParaSalvar(UpsertViagem upsertViagem,
			Motorista motorista, Veiculo veiculo,
			List<br.com.transportes.apitransportes.entity.Destino> destinosEncontrados) {
		return viagensMapper.toViagemEntity(upsertViagem, motorista, veiculo, destinosEncontrados);
	}

	@Transactional
	private Viagem editarViagemExistente(Integer id, UpsertViagem upsertViagem) {
		limparListaDeDestinos(id);
		br.com.transportes.apitransportes.entity.Viagem viagemParaSalvar = encontrarViagemPorId(id);

		Motorista motorista = getMotorista(upsertViagem);
		Veiculo veiculo = getVeiculo(upsertViagem);
		List<br.com.transportes.apitransportes.entity.Destino> novosDestinos = getNovosDestinosParaAViagem(
				upsertViagem);

		viagemParaSalvar.setMotorista(motorista);
		viagemParaSalvar.setVeiculo(veiculo);
		viagemParaSalvar.setDestinos(novosDestinos);

		novosDestinos.forEach(destino -> destino.setViagem(viagemParaSalvar));
		destinosService.salvarTodos(novosDestinos);

		return viagensMapper.toViagemDto(viagemParaSalvar);
	}

	private List<br.com.transportes.apitransportes.entity.Destino> getNovosDestinosParaAViagem(
			UpsertViagem upsertViagem) {
		return destinosService.findAllByIdIsIn(upsertViagem.getDestinos());
	}

	@Transactional
	private void limparListaDeDestinos(Integer id) {
		List<br.com.transportes.apitransportes.entity.Destino> destinos = destinosService.trazerDestinosDaViagem(id);
		destinos.forEach(destino -> destino.setViagem(null));
		destinosService.salvarTodos(destinos);
	}

	public void confirmaViagem(Integer id, Confirmacao confirmacao) {
		br.com.transportes.apitransportes.entity.Viagem encontrada = encontrarViagemPorId(id);

		if ("CONFIRMADO".equals(confirmacao.getConfirmacao().toString())) {
			encontrada.confirmar();
			encontrada.getDestinos().forEach(br.com.transportes.apitransportes.entity.Destino::confirmar);
			viagensRepository.save(encontrada);
			emailService.enviarConfirmacaoDeViagem(encontrada);
		} else {
			encontrada.desconfirmar();
			viagensRepository.save(encontrada);
		}
	}

	public List<Viagem> listarViagens() {
		List<br.com.transportes.apitransportes.entity.Viagem> encontradas =
				viagensRepository.findAll().stream().filter(item -> !item.isExcluido()).toList();
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
		return viagensRepository.findById(id)
				.filter(item -> !item.isExcluido())
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format("Viagem com o id: %d não foi encontrada", id)));
	}

	@Transactional
	public void excluirViagem(Integer id) {
		br.com.transportes.apitransportes.entity.Viagem viagem = encontrarViagemPorId(id);
		viagem.excluirDoBancoLogicamente();
	}

	public InputStreamResource relatorioDeViagem(Integer id) {
		final br.com.transportes.apitransportes.entity.Viagem viagem = encontrarViagemPorId(id);
		final var destinos = destinosService.trazerDestinosDaViagem(viagem.getId());
		viagem.setDestinos(destinos);

		ByteArrayInputStream byteArrayOutputStream;

		CriadorDeRelatorioDeViagem criadorDeRelatorio = new CriadorDeRelatorioDeViagem();
		try {
			final var out = criadorDeRelatorio.criaRelatorioDeViagem(viagem);
			byteArrayOutputStream = new ByteArrayInputStream(out);
		} catch (FileNotFoundException | DocumentException e) {
			throw new RuntimeException(e);
		}

		return new InputStreamResource(byteArrayOutputStream);
	}

	public Viagem trazerViagemPorId(Integer id) {
		return viagensMapper.toViagemDto(encontrarViagemPorId(id));
	}
}
