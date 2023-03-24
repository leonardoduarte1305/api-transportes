package br.com.transportes.apitransportes.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
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

	@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Material> criarMaterial(UpsertMaterial upsertMaterial) {
		Material materialSalvo = materialsService.upsertMaterial("", upsertMaterial);
		return ResponseEntity.ok(materialSalvo);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Material> editarCadastroMaterial(String id, UpsertMaterial upsertMaterial) {
		Material materialEditado = materialsService.upsertMaterial(id, upsertMaterial);
		return ResponseEntity.ok(materialEditado);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Void> excluirMaterial(String id) {
		materialsService.excluirMaterialPorId(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Override public ResponseEntity<List<Material>> listaTodosMateriaisCadastrados() {
		List<Material> materials = materialsService.listarTodos();
		return ResponseEntity.ok(materials);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Override public ResponseEntity<Material> trazMaterialPorId(String id) {
		Material encontrado = materialsService.trazerMaterialPorId(id);
		return ResponseEntity.ok(encontrado);
	}
}
