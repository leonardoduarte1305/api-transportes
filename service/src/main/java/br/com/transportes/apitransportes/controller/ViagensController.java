package br.com.transportes.apitransportes.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

	private final ViagensService viagensService;

	@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Viagem> criarViagem(UpsertViagem upsertViagem) {
		Viagem viagemSalva = viagensService.upsertViagem("", upsertViagem);
		return ResponseEntity.ok(viagemSalva);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Viagem> editarViagem(String id, UpsertViagem upsertViagem) {
		Viagem viagemEditada = viagensService.upsertViagem(id, upsertViagem);
		return ResponseEntity.ok(viagemEditada);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Void> confirmaViagem(String id, Confirmacao confirmacao) {
		viagensService.confirmaViagem(id, confirmacao);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Override public ResponseEntity<List<Viagem>> listaTodasViagens() {
		List<Viagem> encontradas = viagensService.listarViagens();
		return ResponseEntity.ok(encontradas);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Override public ResponseEntity<List<Destino>> trazDestinosDaViagem(String id) {
		List<Destino> destinosDaViagem = viagensService.listarDestinosDaViagem(id);
		return ResponseEntity.ok(destinosDaViagem);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Void> encerraViagem(String id, Encerramento encerramento) {
		viagensService.encerraViagem(id, encerramento);
		return ResponseEntity.noContent().build();
	}
}
