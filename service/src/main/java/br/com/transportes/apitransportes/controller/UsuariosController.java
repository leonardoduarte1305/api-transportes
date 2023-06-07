package br.com.transportes.apitransportes.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.transportes.apitransportes.service.UsuarioService;
import br.com.transportes.server.UsuariosApi;
import br.com.transportes.server.model.UpsertUsuario;
import br.com.transportes.server.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@CrossOrigin("*")
@RequiredArgsConstructor
public class UsuariosController implements UsuariosApi {

	private final UsuarioService usuariosService;

	@Override public ResponseEntity<Usuario> criarUsuario(UpsertUsuario upsertUsuario) {
		Usuario usuarioSalvo = usuariosService.upsertUsuario(null, upsertUsuario);
		return ResponseEntity.ok(usuarioSalvo);
	}

	// https://documenter.getpostman.com/view/7294517/SzmfZHnd#0569158b-e412-48e4-a849-8473ce31eb1d

	// https://keycloak.discourse.group/t/keycloak-admin-client-in-spring-boot/2547

	// https://bitbucket.dev.kiwigrid.com/projects/TECI/repos/user-service-api/browse

	// https://bitbucket.dev.kiwigrid.com/projects/TECI/repos/user-service/browse/pom.xml

	// https://bitbucket.dev.kiwigrid.com/projects/TECI/repos/ecockpit-tenant-manager/browse

	// https://keycloak.discourse.group/t/keycloak-admin-client-in-spring-boot/2547

	// https://www.programcreek.com/java-api-examples/?api=org.keycloak.admin.client.Keycloak

	//	@PostMapping("/user")
	//	public ResponseEntity<String> createUser(@RequestParam String username, @RequestParam String password) {
	//		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
	//			return ResponseEntity.badRequest().body("Empty username or password");
	//		}
	//		CredentialRepresentation credentials = new CredentialRepresentation();
	//		credentials.setType(CredentialRepresentation.PASSWORD);
	//		credentials.setValue(password);
	//		credentials.setTemporary(false);
	//		UserRepresentation userRepresentation = new UserRepresentation();
	//		userRepresentation.setUsername(username);
	//		userRepresentation.setCredentials(Arrays.asList(credentials));
	//		userRepresentation.setEnabled(true);
	//		Map<String, List<String>> attributes = new HashMap<>();
	//		attributes.put("description", Arrays.asList("A test user"));
	//		userRepresentation.setAttributes(attributes);
	//		Keycloak keycloak = getKeycloakInstance();
	//		Response result = keycloak.realm(keycloakRealm).users().create(userRepresentation);
	//		return new ResponseEntity<>(HttpStatus.valueOf(result.getStatus()));
	//	}
}
