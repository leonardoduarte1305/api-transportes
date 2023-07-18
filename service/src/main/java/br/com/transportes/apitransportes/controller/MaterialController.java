package br.com.transportes.apitransportes.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

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

	private static final String MATERIAIS_ID = "/materiais/{id}";
	private final MateriaisService materialsService;
	private final UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Override public ResponseEntity<Material> criarMaterial(UpsertMaterial upsertMaterial) {
		Material materialSalvo = materialsService.upsertMaterial(null, upsertMaterial);
		URI uri = uriBuilder.path(MATERIAIS_ID)
				.buildAndExpand(materialSalvo.getId()).toUri();

		return ResponseEntity.created(uri).body(materialSalvo);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Material> editarCadastroMaterial(Integer id, UpsertMaterial upsertMaterial) {
		Material materialEditado = materialsService.upsertMaterial(id, upsertMaterial);
		return ResponseEntity.ok(materialEditado);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Override public ResponseEntity<Void> excluirMaterial(Integer id) {
		materialsService.excluirMaterialPorId(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Override public ResponseEntity<List<Material>> listaTodosMateriaisCadastrados() {
		List<Material> materials = materialsService.listarTodos();
		return ResponseEntity.ok(materials);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Override public ResponseEntity<Material> trazMaterialPorId(Integer id) {
		Material encontrado = materialsService.trazerMaterialPorId(id);
		return ResponseEntity.ok(encontrado);
	}
}
