package br.com.transportes.apitransportes.controller;

import java.net.URI;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.transportes.apitransportes.service.ViagensService;
import br.com.transportes.server.ViagensApi;
import br.com.transportes.server.model.Confirmacao;
import br.com.transportes.server.model.Destino;
import br.com.transportes.server.model.Encerramento;
import br.com.transportes.server.model.UpsertViagem;
import br.com.transportes.server.model.Viagem;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@CrossOrigin("*")
@RequiredArgsConstructor
public class ViagensController implements ViagensApi {

	private static final String VIAGENS_ID = "/viagens/{id}";
	private final ViagensService viagensService;
	private final UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

	@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Viagem> criarViagem(UpsertViagem upsertViagem) {
		Viagem viagemSalva = viagensService.upsertViagem(null, upsertViagem);
		URI uri = uriBuilder.path(VIAGENS_ID)
				.buildAndExpand(viagemSalva.getId()).toUri();

		return ResponseEntity.created(uri).body(viagemSalva);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Viagem> editarViagem(Integer id, UpsertViagem upsertViagem) {
		Viagem viagemEditada = viagensService.upsertViagem(id, upsertViagem);
		return ResponseEntity.ok(viagemEditada);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Void> confirmaViagem(Integer id, Confirmacao confirmacao) {
		viagensService.confirmaViagem(id, confirmacao);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Override public ResponseEntity<List<Viagem>> listaTodasViagens() {
		List<Viagem> encontradas = viagensService.listarViagens();
		return ResponseEntity.ok(encontradas);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Override public ResponseEntity<List<Destino>> trazDestinosDaViagem(Integer id) {
		List<Destino> destinosDaViagem = viagensService.listarDestinosDaViagem(id);
		return ResponseEntity.ok(destinosDaViagem);
	}

	@Override public ResponseEntity<Viagem> trazViagemPorId(Integer id) {
		Viagem encontrada = viagensService.trazerViagemPorId(id);
		return ResponseEntity.ok(encontrada);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Void> encerraViagem(Integer id, Encerramento encerramento) {
		viagensService.encerraViagem(id, encerramento);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@Override public ResponseEntity<Void> excluirViagem(Integer id) {
		viagensService.excluirViagem(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@Override public ResponseEntity<Object> relatorioDeViagem(Integer id) {
		InputStreamResource inputStreamResource = viagensService.relatorioDeViagem(id);

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=viagem.csv");
		headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf");

		return new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK);
	}
}
