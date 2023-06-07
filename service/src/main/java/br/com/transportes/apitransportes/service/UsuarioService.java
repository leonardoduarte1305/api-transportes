package br.com.transportes.apitransportes.service;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import br.com.transportes.server.model.UpsertUsuario;
import br.com.transportes.server.model.Usuario;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UsuarioService {

	private final UsersResource usersResource;

	public Usuario upsertUsuario(Integer id, UpsertUsuario upsertUsuario) {
		String username = upsertUsuario.getUsername();
		String password = upsertUsuario.getPassword();
		String email = upsertUsuario.getEmail();

		UserRepresentation userRepresentation = new UserRepresentation();
		userRepresentation.setEnabled(true);
		userRepresentation.setUsername(username);
		userRepresentation.setEmail(email);

		final var response = usersResource.create(userRepresentation);
		String userId = CreatedResponseUtil.getCreatedId(response);

		CredentialRepresentation credenciaisDeSenha = criarCredenciaisDeSenha(password);


//		UserRepresentation user = keycloakService.createUser(
//				userRepresentation,
//				password,
//				mapRolesToRepresentation(roles)
//		);
		return null;
	}

	private static CredentialRepresentation criarCredenciaisDeSenha(String password) {
		CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
		credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
		credentialRepresentation.setValue(password);
		credentialRepresentation.setTemporary(false);
		return credentialRepresentation;
	}
}
