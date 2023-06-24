package br.com.transportes.apitransportes.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.transportes.apitransportes.service.VeiculosService;
import br.com.transportes.server.VeiculosApi;
import br.com.transportes.server.model.UpsertVeiculo;
import br.com.transportes.server.model.Veiculo;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@CrossOrigin("*")
@RequiredArgsConstructor
public class VeiculosController implements VeiculosApi {

	private final VeiculosService veiculosService;

	@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Veiculo> criarVeiculo(UpsertVeiculo upsertVeiculo) {
		Veiculo veiculoSalvo = veiculosService.upsertVeiculo(null, upsertVeiculo);
		return ResponseEntity.ok(veiculoSalvo);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Veiculo> editarVeiculo(Integer id, UpsertVeiculo upsertVeiculo) {
		Veiculo veiculoEditado = veiculosService.upsertVeiculo(id, upsertVeiculo);
		return ResponseEntity.ok(veiculoEditado);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Void> excluirVeiculo(Integer id) {
		veiculosService.excluirVeiculoPorId(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Override public ResponseEntity<List<Veiculo>> listaTodosVeiculos() {
		List<Veiculo> veiculos = veiculosService.listarTodos();
		return ResponseEntity.ok(veiculos);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Override public ResponseEntity<Veiculo> trazVeiculoPorId(Integer id) {
		Veiculo encontrado = veiculosService.trazerVeiculoPorId(id);
		return ResponseEntity.ok(encontrado);
	}
}
