package br.com.transportes.apitransportes.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
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

	//@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Sede> criarSede(UpsertSede upsertSede) {
		Sede sedeSalva = sedesService.upsertSede(null, upsertSede);
		return ResponseEntity.ok(sedeSalva);
	}

	//@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Sede> editarSede(Integer id, UpsertSede upsertSede) {
		Sede sedeEditada = sedesService.upsertSede(id, upsertSede);
		return ResponseEntity.ok(sedeEditada);
	}

	//@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Void> excluirSede(Integer id) {
		sedesService.excluirSedePorId(id);
		return ResponseEntity.noContent().build();
	}

	//@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Override public ResponseEntity<List<Sede>> listaTodasAsSedes() {
		List<Sede> sedes = sedesService.listarTodas();
		return ResponseEntity.ok(sedes);
	}

	//@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Override public ResponseEntity<Sede> trazSedePorId(Integer id) {
		Sede encontrada = sedesService.trazerSedePorId(id);
		return ResponseEntity.ok(encontrada);
	}

	//@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Override public ResponseEntity<Void> inscrever(Integer id, List<String> emails) {
		sedesService.inscreverUsuarios(id, emails);
		return ResponseEntity.noContent().build();
	}

	//@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Override public ResponseEntity<Void> desinscrever(Integer id, List<String> emails) {
		sedesService.desinscreverUsuarios(id, emails);
		return ResponseEntity.noContent().build();
	}
}
