package br.com.transportes.apitransportes.controller;

import br.com.transportes.apitransportes.service.SetoresService;
import br.com.transportes.server.SetoresApi;
import br.com.transportes.server.model.Setor;
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
public class SetoresController implements SetoresApi {

    private static final String SETORES_ID = "/setores/{id}";
    private final SetoresService setorService;
    private final UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

    @Override
    public ResponseEntity<Setor> criarSetor(Setor setor) {
        Setor setorSalvo = setorService.criaSetor(setor);
        URI uri = uriBuilder.path(SETORES_ID)
                .buildAndExpand(setorSalvo.getId()).toUri();

        return ResponseEntity.created(uri).body(setorSalvo);
    }

    @Override
    public ResponseEntity<List<Setor>> listaTodosOsSetores() {
        List<Setor> setores = setorService.listaTodosOsSetores();
        return ResponseEntity.ok(setores);
    }
}
