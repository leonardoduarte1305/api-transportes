package br.com.transportes.apitransportes.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import br.com.transportes.apitransportes.email.EmailService;
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
		} else {
			encontrada.desconfirmar();
		}
		br.com.transportes.apitransportes.entity.Viagem confirmada = viagensRepository.save(encontrada);
		emailService.enviarConfirmacaoDeViagem(encontrada);
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
		Long idLong = Long.parseLong(String.valueOf(id));
		return viagensRepository.findById(idLong)
				.filter(item -> !item.isExcluido())
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format("Viagem com o id: %d não foi encontrada", idLong)));
	}

	@Transactional
	public void excluirViagem(Integer id) {
		br.com.transportes.apitransportes.entity.Viagem viagem = encontrarViagemPorId(id);
		viagem.excluirDoBancoLogicamente();
	}

	public InputStreamResource relatorioDeViagem(Integer id) {
		List<br.com.transportes.apitransportes.entity.Viagem> viagens = viagensRepository.findAll();

		// replace this with your header (if required)
		String[] headers = { "idItinerario", "Motorista", "Veiculo", "Data de saída", "Data de chegada", "Status",
				"Encerrada?" };

		// replace this with your data retrieving logic
		List<List<String>> csvBody = new ArrayList<>();

		viagens.forEach(encontrada -> csvBody.add(Arrays.asList(
				encontrada.getId().toString(),
				encontrada.getMotorista().getNome(),
				encontrada.getVeiculo().getPlaca(),
				encontrada.getDatetimeSaida(),
				encontrada.getDatetimeVolta(),
				encontrada.getStatus().toString(),
				encontrada.isEncerrado()
		)));

		ByteArrayInputStream byteArrayOutputStream;

		// closing resources by using a try with resources
		// https://www.baeldung.com/java-try-with-resources
		try (
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				// defining the CSV printer
				CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), CSVFormat.DEFAULT.withHeader(headers));
		) {
			// populating the CSV content
			for (List<String> record : csvBody)
				csvPrinter.printRecord(record);

			// writing the underlying stream
			csvPrinter.flush();

			byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}

		return new InputStreamResource(byteArrayOutputStream);
	}
}
