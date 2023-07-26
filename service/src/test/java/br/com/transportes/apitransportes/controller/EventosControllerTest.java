package br.com.transportes.apitransportes.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import br.com.transportes.apitransportes.service.eventos.UpsertSedeEventPublisher;
import br.com.transportes.server.model.Uf;
import br.com.transportes.server.model.UpsertSede;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EventosController.class)
@AutoConfigureMockMvc(addFilters = false)
class EventosControllerTest {

	private static final String EVENTOS_SEDES = "/eventos/sedes";

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;
	@MockBean
	UpsertSedeEventPublisher publisher;

	HelperParaRequests helperParaRequests;
	HelperParaResponses helperParaResponses;

	@BeforeEach
	void setUp() {
		helperParaRequests = new HelperParaRequests();
		helperParaResponses = new HelperParaResponses();
	}

	@Test
	void criarSedeUsandoEvento() throws Exception {

		UpsertSede sedeRequest = helperParaRequests.criarUpsertSede("Sede Campeche",
				"88063-100", "Florianópolis", "Uma sede na praia", Uf.SC, "Rua do Gramal", 1234);

		String conteudo = objectMapper.writeValueAsString(sedeRequest);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(EVENTOS_SEDES)
				.contentType(MediaType.APPLICATION_JSON)
				.content(conteudo)
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer <jwt-token>")
				.header("x-apikey", "<x-apikey>");

		mockMvc.perform(request)
				.andExpect(status().isNoContent());
		verify(publisher, atLeastOnce()).emiteEventoParaSalvarSede(any(UpsertSede.class));
	}
}