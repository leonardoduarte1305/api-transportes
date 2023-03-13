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

import br.com.transportes.apitransportes.service.VeiculosService;
import br.com.transportes.server.VeiculosApi;
import br.com.transportes.server.model.UpsertVeiculo;
import br.com.transportes.server.model.Veiculo;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/veiculos")
@CrossOrigin("*")
@RequiredArgsConstructor
public class VeiculosController implements VeiculosApi {

	private final VeiculosService veiculosService;

	@PostMapping
	@Override public ResponseEntity<Veiculo> criarVeiculo(UpsertVeiculo upsertVeiculo) {
		Veiculo veiculoSalvo = veiculosService.upsertVeiculo("", upsertVeiculo);
		return ResponseEntity.ok(veiculoSalvo);
	}

	@PutMapping("/{id}")
	@Override public ResponseEntity<Veiculo> editarVeiculo(String id, UpsertVeiculo upsertVeiculo) {
		Veiculo veiculoEditado = veiculosService.upsertVeiculo(id, upsertVeiculo);
		return ResponseEntity.ok(veiculoEditado);
	}

	@DeleteMapping("/{id}")
	@Override public ResponseEntity<Void> excluirVeiculo(String id) {
		veiculosService.excluirVeiculoPorId(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	@Override public ResponseEntity<List<Veiculo>> listaTodosVeiculos() {
		List<Veiculo> veiculos = veiculosService.listarTodos();
		return ResponseEntity.ok(veiculos);
	}

	@GetMapping("/{id}")
	@Override public ResponseEntity<Veiculo> trazVeiculoPorId(String id) {
		Veiculo encontrado = veiculosService.trazerVeiculoPorId(id);
		return ResponseEntity.ok(encontrado);
	}
}
