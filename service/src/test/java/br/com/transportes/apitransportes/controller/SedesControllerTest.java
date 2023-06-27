package br.com.transportes.apitransportes.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.transportes.apitransportes.helper.HelperParaRequests;
import br.com.transportes.apitransportes.helper.HelperParaResponses;
import br.com.transportes.apitransportes.service.SedesService;
import br.com.transportes.server.model.Sede;
import br.com.transportes.server.model.Uf;
import br.com.transportes.server.model.UpsertSede;

@ExtendWith({ SpringExtension.class, RestDocumentationExtension.class })
@WebMvcTest(SedesController.class)
@AutoConfigureMockMvc(addFilters = false)
class SedesControllerTest {

	private static final String SEDES = "/sedes";
	private static final String SEDES_ID = "/sedes/{id}";
	private static final String SEDES_ID_INSCREVER = "/sedes/{id}/inscrever";
	private static final String SEDES_ID_DESINSCREVER = "/sedes/{id}/desinscrever";
	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	SedesService sedesService;

	HelperParaRequests helperParaRequests;
	HelperParaResponses helperParaResponses;

	@BeforeEach
	void setUp(
			WebApplicationContext webApplicationContext,
			RestDocumentationContextProvider restDocumentation) {

		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(documentationConfiguration(restDocumentation))
				.build();

		helperParaRequests = new HelperParaRequests();
		helperParaResponses = new HelperParaResponses();
	}

	@Test
	void criarSede() throws Exception {
		UpsertSede sedeRequest = helperParaRequests.criarUpsertSede("Sede Campeche",
				"88063-100", "Florianópolis", "Uma sede na praia", Uf.SC, "Rua do Gramal", 1234);

		Sede sedeResponse = helperParaResponses.criarSedeResponse(1, "Sede Campeche",
				"88063-100", "Florianópolis", "Uma sede na praia", Uf.SC, "Rua do Gramal", 1234);

		BDDMockito.given(sedesService.upsertSede(null, sedeRequest)).willReturn(sedeResponse);

		String conteudo = objectMapper.writeValueAsString(sedeRequest);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(SEDES)
				.contentType(MediaType.APPLICATION_JSON)
				.content(conteudo)
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer <jwt-token>")
				.header("x-apikey", "<x-apikey>");

		mockMvc.perform(request)
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(sedeResponse.getId())))
				.andExpect(jsonPath("$.cep", is(sedeResponse.getCep())))
				.andExpect(jsonPath("$.cidade", is(sedeResponse.getCidade())))
				.andExpect(jsonPath("$.observacao", is(sedeResponse.getObservacao())))
				.andExpect(jsonPath("$.uf", is(sedeResponse.getUf().toString())))
				.andExpect(jsonPath("$.rua", is(sedeResponse.getRua())))
				.andExpect(jsonPath("$.numero", is(sedeResponse.getNumero())))
				.andDo(document("save-sede"));
	}

	@Test
	void editarSede() throws Exception {
		UpsertSede sedeRequest = helperParaRequests.criarUpsertSede("Sede Campeche",
				"88063-100", "Florianópolis", "Uma sede na praia", Uf.SC, "Rua do Gramal", 1234);

		Integer idSede = 2;
		Sede sedeResponse = helperParaResponses.criarSedeResponse(idSede, "Sede UFSC",
				"88040-600", "Florianópolis", "Uma na esquerda", Uf.SC, "Rua Capitão Romualdo de Barros",
				500);

		BDDMockito.given(sedesService.upsertSede(idSede, sedeRequest)).willReturn(sedeResponse);

		String conteudo = objectMapper.writeValueAsString(sedeRequest);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put(SEDES_ID, idSede)
				.contentType(MediaType.APPLICATION_JSON)
				.content(conteudo)
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer <jwt-token>")
				.header("x-apikey", "<x-apikey>");

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(sedeResponse.getId())))
				.andExpect(jsonPath("$.cep", is(sedeResponse.getCep())))
				.andExpect(jsonPath("$.cidade", is(sedeResponse.getCidade())))
				.andExpect(jsonPath("$.observacao", is(sedeResponse.getObservacao())))
				.andExpect(jsonPath("$.uf", is(sedeResponse.getUf().toString())))
				.andExpect(jsonPath("$.rua", is(sedeResponse.getRua())))
				.andExpect(jsonPath("$.numero", is(sedeResponse.getNumero())))
				.andDo(document("update-sede"));
	}

	@Test
	void deleteSede() throws Exception {
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.delete(SEDES_ID, 2)
				.header("Authorization", "Bearer <jwt-token>")
				.header("x-apikey", "<x-apikey>");

		mockMvc.perform(request)
				.andExpect(status()
						.isNoContent())
				.andDo(document("delete-sede"));
	}

	@Test
	void listarTodasAsSedes() throws Exception {

		Sede sedeAResponse = helperParaResponses.criarSedeResponse(1, "Sede Campeche",
				"88063-100", "Florianópolis", "Uma sede na praia", Uf.SC, "Rua do Gramal", 1234);

		Sede sedeBResponse = helperParaResponses.criarSedeResponse(2, "Sede UFSC",
				"88040-600", "Florianópolis", "Uma na esquerda", Uf.SC, "Rua Capitão Romualdo de Barros",
				500);

		List<Sede> listaDeSedes = Arrays.asList(sedeAResponse, sedeBResponse);
		BDDMockito.given(sedesService.listarTodas())
				.willReturn(listaDeSedes);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(SEDES)
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer <jwt-token>")
				.header("x-apikey", "<x-apikey>");

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$.[0].id", is(listaDeSedes.get(0).getId())))
				.andExpect(jsonPath("$.[0].cep", is(listaDeSedes.get(0).getCep())))
				.andExpect(jsonPath("$.[0].cidade", is(listaDeSedes.get(0).getCidade())))
				.andExpect(jsonPath("$.[0].observacao", is(listaDeSedes.get(0).getObservacao())))
				.andExpect(jsonPath("$.[0].uf", is(listaDeSedes.get(0).getUf().toString())))
				.andExpect(jsonPath("$.[0].rua", is(listaDeSedes.get(0).getRua())))
				.andExpect(jsonPath("$.[0].numero", is(listaDeSedes.get(0).getNumero())))

				.andExpect(jsonPath("$.[1].id", is(listaDeSedes.get(1).getId())))
				.andExpect(jsonPath("$.[1].cep", is(listaDeSedes.get(1).getCep())))
				.andExpect(jsonPath("$.[1].cidade", is(listaDeSedes.get(1).getCidade())))
				.andExpect(jsonPath("$.[1].observacao", is(listaDeSedes.get(1).getObservacao())))
				.andExpect(jsonPath("$.[1].uf", is(listaDeSedes.get(1).getUf().toString())))
				.andExpect(jsonPath("$.[1].rua", is(listaDeSedes.get(1).getRua())))
				.andExpect(jsonPath("$.[1].numero", is(listaDeSedes.get(1).getNumero())))
				.andDo(document("getAll-sede"));
	}

	@Test
	void trazSedePorId() throws Exception {

		Integer idSede = 2;
		Sede sedeResponse = helperParaResponses.criarSedeResponse(idSede, "Sede UFSC",
				"88040-600", "Florianópolis", "Uma na esquerda", Uf.SC, "Rua Capitão Romualdo de Barros",
				500);

		BDDMockito.given(sedesService.trazerSedePorId(idSede)).willReturn(sedeResponse);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(SEDES_ID, idSede)
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer <jwt-token>")
				.header("x-apikey", "<x-apikey>");

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(sedeResponse.getId())))
				.andExpect(jsonPath("$.cep", is(sedeResponse.getCep())))
				.andExpect(jsonPath("$.cidade", is(sedeResponse.getCidade())))
				.andExpect(jsonPath("$.observacao", is(sedeResponse.getObservacao())))
				.andExpect(jsonPath("$.uf", is(sedeResponse.getUf().toString())))
				.andExpect(jsonPath("$.rua", is(sedeResponse.getRua())))
				.andExpect(jsonPath("$.numero", is(sedeResponse.getNumero())))
				.andDo(document("getById-sede"));
	}

	@Test
	void inscrever() throws Exception {

		String email01 = "email01@gmail.com";
		String email02 = "email02@gmail.com";
		String email03 = "email03@gmail.com";

		List<String> emails = Arrays.asList(email01, email02, email03);

		String conteudo = objectMapper.writeValueAsString(emails);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(SEDES_ID_INSCREVER, 2)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(conteudo)
				.header("Authorization", "Bearer <jwt-token>")
				.header("x-apikey", "<x-apikey>");

		mockMvc.perform(request)
				.andExpect(status()
						.isNoContent())
				.andDo(document("inscrever-sede"));
	}

	@Test
	void desinscrever() throws Exception {

		String email01 = "email01@gmail.com";
		String email02 = "email02@gmail.com";
		String email03 = "email03@gmail.com";

		List<String> emails = Arrays.asList(email01, email02, email03);

		String conteudo = objectMapper.writeValueAsString(emails);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(SEDES_ID_DESINSCREVER, 2)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(conteudo)
				.header("Authorization", "Bearer <jwt-token>")
				.header("x-apikey", "<x-apikey>");

		mockMvc.perform(request)
				.andExpect(status()
						.isNoContent())
				.andDo(document("desinscrever-sede"));
	}

}
