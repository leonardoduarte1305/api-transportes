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

import br.com.transportes.apitransportes.service.MateriaisService;
import br.com.transportes.server.MateriaisApi;
import br.com.transportes.server.model.Material;
import br.com.transportes.server.model.UpsertMaterial;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@CrossOrigin("*")
@RequiredArgsConstructor
public class MaterialController implements MateriaisApi {

	private final MateriaisService materialsService;

	@Override public ResponseEntity<Material> criarMaterial(UpsertMaterial upsertMaterial) {
		Material materialSalvo = materialsService.upsertMaterial("", upsertMaterial);
		return ResponseEntity.ok(materialSalvo);
	}

	@Override public ResponseEntity<Material> editarCadastroMaterial(String id, UpsertMaterial upsertMaterial) {
		Material materialEditado = materialsService.upsertMaterial(id, upsertMaterial);
		return ResponseEntity.ok(materialEditado);
	}

	@Override public ResponseEntity<Void> excluirMaterial(String id) {
		materialsService.excluirMaterialPorId(id);
		return ResponseEntity.noContent().build();
	}

	@Override public ResponseEntity<List<Material>> listaTodosMateriaisCadastrados() {
		List<Material> materials = materialsService.listarTodos();
		return ResponseEntity.ok(materials);
	}

	@Override public ResponseEntity<Material> trazMaterialPorId(String id) {
		Material encontrado = materialsService.trazerMaterialPorId(id);
		return ResponseEntity.ok(encontrado);
	}
}
