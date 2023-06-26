package br.com.transportes.apitransportes.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import br.com.transportes.apitransportes.FullApiImplApplication;
import br.com.transportes.server.model.UpsertUsuario;

@SpringBootTest(classes = FullApiImplApplication.class)
class UsuarioServiceTest {

	private static final String TOKEN_PATH = "http://127.0.0.1/realms/master/protocol/openid-connect/token";
	private static final String USERS_PATH = "http://127.0.0.1/admin/realms/master/users/";
	private static final String CLIENT_PATH = "http://127.0.0.1/admin/realms/master/clients/";
	@Autowired
	UsuarioService usuarioService;
	RestTemplate restTemplate;

	@BeforeEach
	void setUp() {
		restTemplate = new RestTemplate();
	}

	@Test
	void criarUsuarioCompletoEDepoisExcluiOUsuario() {
		UpsertUsuario upsertUsuario = new UpsertUsuario()
				.nome("Linus")
				.sobrenome("Torvalds")
				.email("linus_torvalds@linux.com")
				.username("lord_linux")
				.password("senhaMuitoSegura")
				.role(UpsertUsuario.RoleEnum.ADMIN);

		String usuarioId = usuarioService.criarUsuarioCompleto(upsertUsuario);

		usuarioService.excluiUsuario(usuarioId);
	}

}
