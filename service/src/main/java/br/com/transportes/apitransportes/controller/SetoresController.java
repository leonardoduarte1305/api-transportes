package br.com.transportes.apitransportes.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.transportes.apitransportes.service.SetoresService;
import br.com.transportes.server.SetoresApi;
import br.com.transportes.server.model.Setor;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@CrossOrigin("*")
@RequiredArgsConstructor
public class SetoresController implements SetoresApi {

	private final SetoresService setorService;

	@Override public ResponseEntity<Setor> criarSetor(Setor setor) {
		Setor setorSalvo = setorService.criaSetor(setor);
		return ResponseEntity.ok(setorSalvo);
	}

	@Override public ResponseEntity<List<Setor>> listaTodosOsSetores() {
		List<Setor> setores = setorService.listaTodosOsSetores();
		return ResponseEntity.ok(setores);
	}
}
