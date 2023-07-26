package br.com.transportes.apitransportes.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import br.com.transportes.apitransportes.service.eventos.UpsertSedeEventPublisher;
import br.com.transportes.server.EventosApi;
import br.com.transportes.server.model.UpsertSede;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class EventosController implements EventosApi {

	private final UpsertSedeEventPublisher publisher;

	@Override
	public ResponseEntity<Void> criarSedeUsandoEvento(UpsertSede upsertSede) {
		publisher.emiteEventoParaSalvarSede(upsertSede);
		return ResponseEntity.noContent().build();
	}

}
