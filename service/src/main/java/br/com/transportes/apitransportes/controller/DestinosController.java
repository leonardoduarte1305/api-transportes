package br.com.transportes.apitransportes.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping
@CrossOrigin("*")
@RequiredArgsConstructor
public class DestinosController implements DestinosApi {

	private final DestinosService destinosService;

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Override public ResponseEntity<Destino> criarDestino(UpsertDestino upsertDestino) {
		Destino destinoSalvo = destinosService.upsertDestino(null, upsertDestino);
		return ResponseEntity.ok(destinoSalvo);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Override public ResponseEntity<Destino> editarDestino(String id, UpsertDestino upsertDestino) {
		Destino destinoEditado = destinosService.upsertDestino(Integer.valueOf(id), upsertDestino);
		return ResponseEntity.ok(destinoEditado);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Void> excluirDestino(String id) {
		destinosService.excluirDestinoPorId(Integer.valueOf(id));
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Override public ResponseEntity<Destino> trazDestinoPorId(String id) {
		Destino encontrado = destinosService.trazerDestinoPorId(Integer.valueOf(id));
		return ResponseEntity.ok(encontrado);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Override public ResponseEntity<List<MaterialQuantidadeSetor>> trazMateriaisDoDestino(String id) {
		List<MaterialQuantidadeSetor> materiais = destinosService.trazMateriaisDoDestino(Integer.valueOf(id));
		return ResponseEntity.ok(materiais);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Void> confirmaDestino(String id, Confirmacao body) {
		destinosService.confirmaDestino(Integer.valueOf(id), body);
		return ResponseEntity.noContent().build();
	}
}
