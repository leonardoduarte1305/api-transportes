package br.com.transportes.apitransportes.controller;

import br.com.transportes.apitransportes.service.MateriaisService;
import br.com.transportes.server.MateriaisApi;
import br.com.transportes.server.model.Material;
import br.com.transportes.server.model.UpsertMaterial;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping
@CrossOrigin("*")
@RequiredArgsConstructor
public class MaterialController implements MateriaisApi {
    private static final String MATERIAIS_ID = "/materiais/{id}";
    private final MateriaisService materialsService;
    private final UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

    @Override
    public ResponseEntity<Material> criarMaterial(UpsertMaterial upsertMaterial) {
        Material materialSalvo = materialsService.upsertMaterial(null, upsertMaterial);
        URI uri = uriBuilder.path(MATERIAIS_ID)
                .buildAndExpand(materialSalvo.getId()).toUri();

        return ResponseEntity.created(uri).body(materialSalvo);
    }

    @Override
    public ResponseEntity<Material> editarCadastroMaterial(Integer id, UpsertMaterial upsertMaterial) {
        Material materialEditado = materialsService.upsertMaterial(id, upsertMaterial);
        return ResponseEntity.ok(materialEditado);
    }

    @Override
    public ResponseEntity<Void> excluirMaterial(Integer id) {
        materialsService.excluirMaterialPorId(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<Material>> listaTodosMateriaisCadastrados() {
        List<Material> materials = materialsService.listarTodos();
        return ResponseEntity.ok(materials);
    }

    @Override
    public ResponseEntity<Material> trazMaterialPorId(Integer id) {
        Material encontrado = materialsService.trazerMaterialPorId(id);
        return ResponseEntity.ok(encontrado);
    }
}
