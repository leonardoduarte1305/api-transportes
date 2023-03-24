package br.com.transportes.apitransportes.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.transportes.apitransportes.service.SedesService;
import br.com.transportes.server.SedesApi;
import br.com.transportes.server.model.Sede;
import br.com.transportes.server.model.UpsertSede;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@CrossOrigin("*")
@RequiredArgsConstructor
public class SedesController implements SedesApi {

	private final SedesService sedesService;

	@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Sede> criarSede(UpsertSede upsertSede) {
		Sede sedeSalva = sedesService.upsertSede("", upsertSede);
		return ResponseEntity.ok(sedeSalva);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Sede> editarSede(String id, UpsertSede upsertSede) {
		Sede sedeEditada = sedesService.upsertSede(id, upsertSede);
		return ResponseEntity.ok(sedeEditada);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Void> excluirSede(String id) {
		sedesService.excluirSedePorId(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Override public ResponseEntity<List<Sede>> listaTodasAsSedes() {
		List<Sede> sedes = sedesService.listarTodas();
		return ResponseEntity.ok(sedes);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Override public ResponseEntity<Sede> trazSedePorId(String id) {
		Sede encontrada = sedesService.trazerSedePorId(id);
		return ResponseEntity.ok(encontrada);
	}
}
