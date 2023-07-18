package br.com.transportes.apitransportes.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

	private static final String USUARIOS_ID = "/admin/realms/master/users/{id}";
	private final UsuarioService usuarioService;

	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public ResponseEntity<Void> criarUsuario(UpsertUsuario upsertUsuario) {
		usuarioService.criarUsuarioCompleto(upsertUsuario);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public ResponseEntity<Void> excluirUsuario(String id) {
		usuarioService.excluiUsuario(id);
		return ResponseEntity.noContent().build();
	}
}
