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

import br.com.transportes.apitransportes.service.MotoristasService;
import br.com.transportes.server.MotoristasApi;
import br.com.transportes.server.model.AtributosMotorista;
import br.com.transportes.server.model.Motorista;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/motoristas")
@CrossOrigin("*")
@RequiredArgsConstructor
public class MotoristasController implements MotoristasApi {

	private final MotoristasService motoristasService;

	@PostMapping
	@Override public ResponseEntity<Motorista> criarMotorista(AtributosMotorista atributosMotorista) {
		Motorista motoristaSalvo = motoristasService.upsertMotorista("", atributosMotorista);
		return ResponseEntity.ok(motoristaSalvo);
	}

	@PutMapping("/{id}")
	@Override public ResponseEntity<Motorista> editarMotorista(String id, AtributosMotorista atributosMotorista) {
		Motorista motoristaEditado = motoristasService.upsertMotorista(id, atributosMotorista);
		return ResponseEntity.ok(motoristaEditado);
	}

	@DeleteMapping("/{id}")
	@Override public ResponseEntity<Void> excluirMotorista(String id) {
		motoristasService.excluirMotoristaPorId(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	@Override public ResponseEntity<List<Motorista>> listaTodosMotoristas() {
		List<Motorista> motoristas = motoristasService.listarTodos();
		return ResponseEntity.ok(motoristas);
	}

	@GetMapping("/{id}")
	@Override public ResponseEntity<Motorista> trazMotoristaPorId(String id) {
		Motorista encontrado = motoristasService.trazerMotoristaPorId(id);
		return ResponseEntity.ok(encontrado);
	}
}
