package br.com.transportes.apitransportes.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.transportes.apitransportes.entity.keycloak.Access;
import br.com.transportes.apitransportes.entity.keycloak.CredenciaisDeUsuarioKeycloak;
import br.com.transportes.apitransportes.entity.keycloak.RepresentacaoDeClientDoKeycloak;
import br.com.transportes.apitransportes.entity.keycloak.RepresentacaoDeUsuarioDoKeycloak;
import br.com.transportes.apitransportes.entity.keycloak.Role;
import br.com.transportes.server.model.UpsertUsuario;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsuarioService {

	private static final String CLIENTS_NAME = "api-transportes-client";
	private static final String ADMIN_USERNAME = "admin";
	private static final String ADMIN_PASSWORD = "admin";
	private static final String ERRO_DE_PARSE = "Ops, deu erro na hora de parsear o objeto!!!";
	private final String tokenUrl = "http://127.0.0.1:8081/realms/master/protocol/openid-connect/token";
	private final String usuariosUrl = "http://127.0.0.1:8081/admin/realms/master/users/";
	private final String clientsUrl = "http://127.0.0.1:8081/admin/realms/master/clients/";
	private RestTemplate restTemplate;
	private HttpHeaders headers;

	@PostConstruct
	private void setUp() {
		restTemplate = new RestTemplate();
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	}

	public String criarUsuarioCompleto(UpsertUsuario upsertUsuario) {
		String token = getBearerTokenComoString();
		headers.setBearerAuth(token);

		criarUsuario(upsertUsuario);
		RepresentacaoDeUsuarioDoKeycloak usuarioKeycloak = getUsuarioByUsername(upsertUsuario.getUsername());
		setarSenha(upsertUsuario.getPassword(), usuarioKeycloak.getId());

		RepresentacaoDeClientDoKeycloak client = getApiTransportesClientInKeycloak();
		Role role = getRoleParaSalvarNoUsuario(upsertUsuario.getRole(), client.getId());
		setarRoleNoUsuario(usuarioKeycloak.getId(), client.getId(), role);

		return usuarioKeycloak.getId();
	}

	@Cacheable("access_token") // FIXME Não funciona neste método (imagino que por causa do retorno)
	public String getBearerTokenComoString() {
		log.info("Gerando access_token.");
		String response = "";

		try {
			response = fazLoginComoAdminEDevolveAResposta();
		} catch (IOException e) {
			log.error("Ops, deu erro na hora de gerar o token!!!", e);
		}

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;

		try {
			jsonNode = objectMapper.readTree(response);
		} catch (JsonProcessingException e) {
			log.error("Ops, deu erro na hora de parsear o token!!!", e);
		}

		return jsonNode.get("access_token").asText();
	}

	private String fazLoginComoAdminEDevolveAResposta() throws IOException {
		String cookie = "JSESSIONID=FAFD9F4F86E5CC139DA9C63047AC86CC";
		String data = "grant_type=password&client_id=admin-cli&username=" +
				ADMIN_USERNAME + "&password=" + ADMIN_PASSWORD;
		String contentType = "application/x-www-form-urlencoded";

		URL obj = new URL(tokenUrl);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", contentType);
		con.setRequestProperty("Cookie", cookie);
		con.setDoOutput(true);

		con.getOutputStream().write(data.getBytes(StandardCharsets.UTF_8));

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuilder response = new StringBuilder();
		String line;

		while ((line = in.readLine()) != null) {
			response.append(line);
		}

		in.close();

		return response.toString();
	}

	private void criarUsuario(UpsertUsuario upsertUsuario) {
		String requestBody = "";
		requestBody = parseObjectParaString(getRepresentacaoDeUsuarioKeycloak(upsertUsuario));

		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

		ResponseEntity<String> responseEntity =
				restTemplate.exchange(usuariosUrl, HttpMethod.POST, requestEntity, String.class);

		if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
			log.info("Usuario criado com sucesso.");
		} else {
			log.info("Erro ao criar usuário, StatusCode: {}", responseEntity.getStatusCode());
		}
	}

	private RepresentacaoDeUsuarioDoKeycloak getUsuarioByUsername(String username) {

		HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
		ResponseEntity<String> response =
				restTemplate.exchange(usuariosUrl, HttpMethod.GET, requestEntity, String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;

		try {
			jsonNode = objectMapper.readTree(response.getBody());
		} catch (JsonProcessingException e) {
			log.error(erroNoParsearOObjeto(), e);
		}

		for (JsonNode node : jsonNode) {
			RepresentacaoDeUsuarioDoKeycloak user = objectMapper.convertValue(
					node,
					RepresentacaoDeUsuarioDoKeycloak.class);

			if (user.getUsername().equalsIgnoreCase(username)) {
				return user;
			}
		}

		return new RepresentacaoDeUsuarioDoKeycloak();
	}

	private void setarSenha(String password, String id) {
		String content = parseObjectParaString(CredenciaisDeUsuarioKeycloak.builder()
				.temporary(false)
				.type("password")
				.value(password)
				.build());
		HttpEntity<String> requestEntity = new HttpEntity<>(content, headers);

		ResponseEntity<String> responseEntity = restTemplate.exchange(
				usuariosUrl +
						id + "/reset-password",
				HttpMethod.PUT, requestEntity,
				String.class);

		if (responseEntity.getStatusCode() == HttpStatus.NO_CONTENT) {
			log.info("Senha adicionada com sucesso.");
		} else {
			log.error("Erro ao adicionar a senha, StatusCode: {}", responseEntity.getStatusCode());
		}
	}

	private String parseObjectParaString(Object objetoParaConverter) {
		try {
			return new ObjectMapper().writeValueAsString(objetoParaConverter);
		} catch (JsonProcessingException e) {
			log.error("Erro na hora de converter o objeto para String.", e);
		}
		return "";
	}

	private Role getRoleParaSalvarNoUsuario(UpsertUsuario.RoleEnum role, String clientId) {
		HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
		String url = clientsUrl + clientId + "/roles";
		ResponseEntity<String> response =
				restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;

		try {
			jsonNode = objectMapper.readTree(response.getBody());
		} catch (JsonProcessingException e) {
			log.error(erroNoParsearOObjeto(), e);
		}

		for (JsonNode node : jsonNode) {
			Role keycloakRole = objectMapper.convertValue(
					node,
					Role.class);

			if (keycloakRole.getName().equalsIgnoreCase(role.toString())) {
				return keycloakRole;
			}
		}

		return new Role();
	}

	private void setarRoleNoUsuario(String usuarioId, String clientId, Role role) {
		String requestBody = "";
		requestBody = parseObjectParaString(Arrays.asList(role));

		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

		String url = usuariosUrl + usuarioId + "/role-mappings/clients/" + clientId;
		ResponseEntity<String> responseEntity =
				restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

		if (responseEntity.getStatusCode() == HttpStatus.NO_CONTENT) {
			log.info("Role associada com sucesso.");
		} else {
			log.error("Erro ao associar a role, StatusCode: {}", responseEntity.getStatusCode());
		}
	}

	private RepresentacaoDeUsuarioDoKeycloak getRepresentacaoDeUsuarioKeycloak(UpsertUsuario upsertUsuario) {
		return RepresentacaoDeUsuarioDoKeycloak.builder()
				.createdTimestamp(Instant.now().getEpochSecond())
				.username(upsertUsuario.getUsername())
				.enabled(true)
				.totp(false)
				.emailVerified(true)
				.firstName(upsertUsuario.getNome())
				.lastName(upsertUsuario.getSobrenome())
				.email(upsertUsuario.getEmail())
				.disableableCredentialTypes(new ArrayList<>())
				.requiredActions(new ArrayList<>())
				.notBefore(0)
				.access(Access.builder()
						.manageGroupMembership(true)
						.view(true)
						.mapRoles(true)
						.impersonate(true)
						.manage(true)
						.build())
				.realmRoles(new ArrayList<>())
				.build();
	}

	private RepresentacaoDeClientDoKeycloak getApiTransportesClientInKeycloak() {

		HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
		ResponseEntity<String> response =
				restTemplate.exchange(clientsUrl, HttpMethod.GET, requestEntity, String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;

		try {
			jsonNode = objectMapper.readTree(response.getBody());
		} catch (JsonProcessingException e) {
			log.error(erroNoParsearOObjeto());
		}

		for (JsonNode node : jsonNode) {
			RepresentacaoDeClientDoKeycloak client = objectMapper.convertValue(
					node,
					RepresentacaoDeClientDoKeycloak.class);

			if (client.getClientId().equalsIgnoreCase(CLIENTS_NAME)) {
				return client;
			}
		}

		return new RepresentacaoDeClientDoKeycloak();
	}

	private String erroNoParsearOObjeto() {
		return ERRO_DE_PARSE;
	}

	public void excluiUsuario(String usuarioId) {
		String token = getBearerTokenComoString();
		headers.setBearerAuth(token);

		HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

		ResponseEntity<String> responseEntity =
				restTemplate.exchange(usuariosUrl + usuarioId, HttpMethod.DELETE, requestEntity, String.class);

		if (responseEntity.getStatusCode() == HttpStatus.NO_CONTENT) {
			log.info("Usuario excluido com sucesso.");
		} else {
			log.info("Erro ao excluir usuário, StatusCode: {}", responseEntity.getStatusCode());
		}
	}
}
