package br.com.transportes.apitransportes.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.transportes.apitransportes.helper.HelperParaRequests;
import br.com.transportes.apitransportes.helper.HelperParaResponses;
import br.com.transportes.apitransportes.service.SetoresService;
import br.com.transportes.server.model.Setor;

@ExtendWith({ SpringExtension.class })
@WebMvcTest(SetoresController.class)
@AutoConfigureMockMvc(addFilters = false)
class SetoresControllerTest {

	private static final String SETORES = "/setores";

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	SetoresService setoresService;

	HelperParaRequests helperParaRequests;
	HelperParaResponses helperParaResponses;

	@BeforeEach
	void setUp() {
		helperParaRequests = new HelperParaRequests();
		helperParaResponses = new HelperParaResponses();
	}

	@Test
	void criarSetor() throws Exception {

		br.com.transportes.server.model.Setor setorRequest = helperParaRequests.criarSetor("Financeiro");

		br.com.transportes.server.model.Setor setorResponse = helperParaResponses.criarSetorResponse(1, "Financeiro");

		BDDMockito.given(setoresService.criaSetor(setorRequest)).willReturn(setorResponse);

		String conteudo = objectMapper.writeValueAsString(setorRequest);
		System.err.println(conteudo);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(SETORES)
				.contentType(MediaType.APPLICATION_JSON)
				.content(conteudo)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(setorResponse.getId())))
				.andExpect(jsonPath("$.nome", is(setorResponse.getNome())));
	}

	@Test
	void listaTodosOsSetores() throws Exception {

		br.com.transportes.server.model.Setor setorAResponse = helperParaResponses.criarSetorResponse(1, "Financeiro");

		br.com.transportes.server.model.Setor setorBResponse = helperParaResponses.criarSetorResponse(2, "Diretoria");

		List<Setor> listaDeSetores = Arrays.asList(setorAResponse, setorBResponse);
		BDDMockito.given(setoresService.listaTodosOsSetores()).willReturn(listaDeSetores);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(SETORES)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$.[0].id", is(listaDeSetores.get(0).getId())))
				.andExpect(jsonPath("$.[0].nome", is(listaDeSetores.get(0).getNome())))

				.andExpect(jsonPath("$.[1].id", is(listaDeSetores.get(1).getId())))
				.andExpect(jsonPath("$.[1].nome", is(listaDeSetores.get(1).getNome())));
	}
}
