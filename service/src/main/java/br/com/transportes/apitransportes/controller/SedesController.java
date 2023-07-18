package br.com.transportes.apitransportes.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

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

	private static final String SEDES_ID = "/sedes/{id}";
	private final SedesService sedesService;
	private final UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

	@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Sede> criarSede(UpsertSede upsertSede) {
		Sede sedeSalva = sedesService.upsertSede(null, upsertSede);
		URI uri = uriBuilder.path(SEDES_ID)
				.buildAndExpand(sedeSalva.getId()).toUri();

		return ResponseEntity.created(uri).body(sedeSalva);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Sede> editarSede(Integer id, UpsertSede upsertSede) {
		Sede sedeEditada = sedesService.upsertSede(id, upsertSede);
		return ResponseEntity.ok(sedeEditada);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Void> excluirSede(Integer id) {
		sedesService.excluirSedePorId(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Override public ResponseEntity<List<Sede>> listaTodasAsSedes() {
		List<Sede> sedes = sedesService.listarTodas();
		return ResponseEntity.ok(sedes);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Override public ResponseEntity<Sede> trazSedePorId(Integer id) {
		Sede encontrada = sedesService.trazerSedePorId(id);
		return ResponseEntity.ok(encontrada);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Override public ResponseEntity<Void> inscrever(Integer id, List<String> emails) {
		sedesService.inscreverUsuarios(id, emails);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Override public ResponseEntity<Void> desinscrever(Integer id, List<String> emails) {
		sedesService.desinscreverUsuarios(id, emails);
		return ResponseEntity.noContent().build();
	}
}
