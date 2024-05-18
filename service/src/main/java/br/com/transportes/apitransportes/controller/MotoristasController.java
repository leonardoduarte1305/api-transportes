package br.com.transportes.apitransportes.controller;

import br.com.transportes.apitransportes.service.MotoristasService;
import br.com.transportes.server.MotoristasApi;
import br.com.transportes.server.model.AtributosMotorista;
import br.com.transportes.server.model.Motorista;
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
public class MotoristasController implements MotoristasApi {
    private static final String MOTORISTAS_ID = "/motoristas/{id}";
    private final MotoristasService motoristasService;
    private final UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

    @Override
    public ResponseEntity<Motorista> criarMotorista(AtributosMotorista atributosMotorista) {
        Motorista motoristaSalvo = motoristasService.upsertMotorista(null, atributosMotorista);
        URI uri = uriBuilder.path(MOTORISTAS_ID)
                .buildAndExpand(motoristaSalvo.getId()).toUri();

        return ResponseEntity.created(uri).body(motoristaSalvo);
    }

    @Override
    public ResponseEntity<Motorista> editarMotorista(Integer id, AtributosMotorista atributosMotorista) {
        Motorista motoristaEditado = motoristasService.upsertMotorista(id, atributosMotorista);
        return ResponseEntity.ok(motoristaEditado);
    }

    @Override
    public ResponseEntity<Void> excluirMotorista(Integer id) {
        motoristasService.excluirMotoristaPorId(id);
        return ResponseEntity.noContent().build();
    }


    @Override
    public ResponseEntity<List<Motorista>> listaTodosMotoristas() {
        List<Motorista> motoristas = motoristasService.listarTodos();
        return ResponseEntity.ok(motoristas);
    }

    @Override
    public ResponseEntity<Motorista> trazMotoristaPorId(Integer id) {
        Motorista encontrado = motoristasService.trazerMotoristaPorId(id);
        return ResponseEntity.ok(encontrado);
    }
}
