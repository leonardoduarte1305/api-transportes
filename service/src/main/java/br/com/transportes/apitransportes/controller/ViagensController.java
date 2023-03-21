package br.com.transportes.apitransportes.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.transportes.apitransportes.service.ViagensService;
import br.com.transportes.server.ViagensApi;
import br.com.transportes.server.model.Confirmacao;
import br.com.transportes.server.model.Destino;
import br.com.transportes.server.model.UpsertViagem;
import br.com.transportes.server.model.Viagem;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@CrossOrigin("*")
@RequiredArgsConstructor
public class ViagensController implements ViagensApi {

	private final ViagensService viagensService;

	@Override public ResponseEntity<Viagem> criarViagem(UpsertViagem upsertViagem) {
		Viagem viagemSalva = viagensService.upsertViagem("", upsertViagem);
		return ResponseEntity.ok(viagemSalva);
	}

	@Override public ResponseEntity<Viagem> editarViagem(String id, UpsertViagem upsertViagem) {
		Viagem viagemEditada = viagensService.upsertViagem(id, upsertViagem);
		return ResponseEntity.ok(viagemEditada);
	}

	@Override public ResponseEntity<Void> confirmaViagem(String id, Confirmacao confirmacao) {
		viagensService.confirmaViagem(id, confirmacao);
		return ResponseEntity.noContent().build();
	}

	@Override public ResponseEntity<List<Viagem>> listaTodasViagens() {
		List<Viagem> encontradas = viagensService.listarViagens();
		return ResponseEntity.ok(encontradas);
	}

	@Override public ResponseEntity<List<Destino>> trazDestinosDaViagem(String id) {
		List<Destino> destinosDaViagem = viagensService.listarDestinosDaViagem(id);
		return ResponseEntity.ok(destinosDaViagem);
	}
}
