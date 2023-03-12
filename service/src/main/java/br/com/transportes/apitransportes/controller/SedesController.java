package br.com.transportes.apitransportes.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.transportes.apitransportes.service.SedesService;
import br.com.transportes.server.SedesApi;
import br.com.transportes.server.model.Sede;
import br.com.transportes.server.model.UpsertSede;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/sedes")
@CrossOrigin("*")
@RequiredArgsConstructor
public class SedesController implements SedesApi {

	private final SedesService sedesService;

	@PostMapping
	@Override public ResponseEntity<Sede> criarSede(UpsertSede upsertSede) {
		Sede sedeSalva = sedesService.salvarNovaSede(upsertSede);
		return ResponseEntity.ok(sedeSalva);
	}

	@Override public ResponseEntity<Sede> editarSede(String id, UpsertSede upsertSede) {
		return null;
	}

	@Override public ResponseEntity<Void> excluirSede(String id) {
		return null;
	}

	@Override public ResponseEntity<List<Sede>> listaTodasAsSedes() {
		return null;
	}

	@Override public ResponseEntity<Sede> trazSedePorId(String id) {
		return null;
	}
}
