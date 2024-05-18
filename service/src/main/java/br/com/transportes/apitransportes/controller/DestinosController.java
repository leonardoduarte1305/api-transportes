package br.com.transportes.apitransportes.controller;

import br.com.transportes.apitransportes.service.DestinosService;
import br.com.transportes.server.DestinosApi;
import br.com.transportes.server.model.Confirmacao;
import br.com.transportes.server.model.Destino;
import br.com.transportes.server.model.MaterialQuantidadeSetor;
import br.com.transportes.server.model.UpsertDestino;
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
public class DestinosController implements DestinosApi {
    private static final String DESTINOS_ID = "/destinos/{id}";
    private final DestinosService destinosService;
    private final UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

    @Override
    public ResponseEntity<Destino> criarDestino(UpsertDestino upsertDestino) {
        Destino destinoSalvo = destinosService.upsertDestino(null, upsertDestino);
        URI uri = uriBuilder.path(DESTINOS_ID)
                .buildAndExpand(destinoSalvo.getId()).toUri();

        return ResponseEntity.created(uri).body(destinoSalvo);
    }

    @Override
    public ResponseEntity<Destino> editarDestino(Integer id, UpsertDestino upsertDestino) {
        Destino destinoEditado = destinosService.upsertDestino(id, upsertDestino);
        return ResponseEntity.ok(destinoEditado);
    }

    @Override
    public ResponseEntity<Void> excluirDestino(Integer id) {
        destinosService.excluirDestinoPorId(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Destino> trazDestinoPorId(Integer id) {
        Destino encontrado = destinosService.trazerDestinoPorId(id);
        return ResponseEntity.ok(encontrado);
    }

    @Override
    public ResponseEntity<List<MaterialQuantidadeSetor>> trazMateriaisDoDestino(Integer id) {
        List<MaterialQuantidadeSetor> materiais = destinosService.trazMateriaisDoDestino(id);
        return ResponseEntity.ok(materiais);
    }

    @Override
    public ResponseEntity<Void> confirmaDestino(Integer id, Confirmacao body) {
        destinosService.confirmaDestino(id, body);
        return ResponseEntity.noContent().build();
    }
}
