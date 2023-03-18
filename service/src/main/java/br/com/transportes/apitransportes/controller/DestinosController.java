package br.com.transportes.apitransportes.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.transportes.apitransportes.service.DestinosService;
import br.com.transportes.server.DestinosApi;
import br.com.transportes.server.model.Confirmacao;
import br.com.transportes.server.model.Destino;
import br.com.transportes.server.model.MaterialQuantidadeSetor;
import br.com.transportes.server.model.UpsertDestino;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/destinos")
@CrossOrigin("*")
@RequiredArgsConstructor
public class DestinosController implements DestinosApi {

	private final DestinosService destinosService;

	@PostMapping
	@Override public ResponseEntity<Destino> criarDestino(UpsertDestino upsertDestino) {
		Destino destinoSalvo = destinosService.upsertDestino("", upsertDestino);
		return ResponseEntity.ok(destinoSalvo);
	}

	@PutMapping("/{id}")
	@Override public ResponseEntity<Destino> editarDestino(String id, UpsertDestino upsertDestino) {
		Destino destinoEditado = destinosService.upsertDestino(id, upsertDestino);
		return ResponseEntity.ok(destinoEditado);
	}

	@DeleteMapping("/{id}")
	@Override public ResponseEntity<Void> excluirDestino(String id) {
		destinosService.excluirDestinoPorId(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}")
	@Override public ResponseEntity<Destino> trazDestinoPorId(String id) {
		Destino encontrado = destinosService.trazerDestinoPorId(id);
		return ResponseEntity.ok(encontrado);
	}

	@GetMapping("/{id}/materiais")
	@Override public ResponseEntity<List<MaterialQuantidadeSetor>> trazMateriaisDoDestino(String id) {
		List<MaterialQuantidadeSetor> materiais = destinosService.trazMateriaisDoDestino(id);
		return ResponseEntity.ok(materiais);
	}

	@PostMapping("/{id}/confirmacao")
	@Override public ResponseEntity<Void> confirmaDestino(String id, Confirmacao body) {
		destinosService.confirmaDestino(id, body);
		return ResponseEntity.noContent().build();
	}
}
