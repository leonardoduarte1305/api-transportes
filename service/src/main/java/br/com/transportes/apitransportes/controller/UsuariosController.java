package br.com.transportes.apitransportes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.transportes.apitransportes.service.UsuarioService;
import br.com.transportes.server.UsuariosApi;
import br.com.transportes.server.model.UpsertUsuario;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@CrossOrigin("*")
@RequiredArgsConstructor
public class UsuariosController implements UsuariosApi {

	@Autowired
	private final UsuarioService usuarioService;

	@Override public ResponseEntity<Void> criarUsuario(UpsertUsuario upsertUsuario) {
		usuarioService.criarUsuarioCompleto(upsertUsuario);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Override public ResponseEntity<Void> excluirUsuario(String id) {
		usuarioService.excluiUsuario(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
