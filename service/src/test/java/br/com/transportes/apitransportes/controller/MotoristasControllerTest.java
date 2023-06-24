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
import br.com.transportes.apitransportes.service.MotoristasService;
import br.com.transportes.server.model.AtributosMotorista;
import br.com.transportes.server.model.Motorista;

@ExtendWith({ SpringExtension.class })
@WebMvcTest(MotoristasController.class)
@AutoConfigureMockMvc(addFilters = false)
class MotoristasControllerTest {

	private static final String MOTORISTAS = "/motoristas";
	private static final String MOTORISTAS_ID = "/motoristas/{id}";

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	MotoristasService motoristasService;

	HelperParaRequests helperParaRequests;
	HelperParaResponses helperParaResponses;

	@BeforeEach
	void setUp() {
		helperParaRequests = new HelperParaRequests();
		helperParaResponses = new HelperParaResponses();
	}

	@Test
	void criarMotorista() throws Exception {

		AtributosMotorista motoristaRequest =
				helperParaRequests.criarAtributosMotorista(
						"Ayrton Senna", "123456789", "ayrton_senna@stm.com");

		Motorista motoristaResponse = helperParaResponses.criarMotoristaResponse(
				1, "Ayrton Senna", "123456789", "ayrton_senna@stm.com");

		BDDMockito.given(motoristasService.upsertMotorista(null, motoristaRequest)).willReturn(motoristaResponse);

		String conteudo = objectMapper.writeValueAsString(motoristaRequest);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(MOTORISTAS)
				.contentType(MediaType.APPLICATION_JSON)
				.content(conteudo)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(motoristaResponse.getId())))
				.andExpect(jsonPath("$.nome", is(motoristaResponse.getNome())))
				.andExpect(jsonPath("$.carteira", is(motoristaResponse.getCarteira())))
				.andExpect(jsonPath("$.email", is(motoristaResponse.getEmail())));
	}

	@Test
	void editarMotorista() throws Exception {

		AtributosMotorista motoristaRequest =
				helperParaRequests.criarAtributosMotorista(
						"Jacques Villeneuve", "987654321", "jacques_villeneuve@stm.com");

		Integer idMotorista = 1;
		Motorista motoristaResponse = helperParaResponses.criarMotoristaResponse(
				idMotorista, "Jacques Villeneuve", "987654321", "jacques_villeneuve@stm.com");

		BDDMockito.given(motoristasService.upsertMotorista(idMotorista, motoristaRequest))
				.willReturn(motoristaResponse);

		String conteudo = objectMapper.writeValueAsString(motoristaRequest);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put(MOTORISTAS_ID, idMotorista)
				.contentType(MediaType.APPLICATION_JSON)
				.content(conteudo)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(motoristaResponse.getId())))
				.andExpect(jsonPath("$.nome", is(motoristaResponse.getNome())))
				.andExpect(jsonPath("$.carteira", is(motoristaResponse.getCarteira())))
				.andExpect(jsonPath("$.email", is(motoristaResponse.getEmail())));
	}

	@Test
	void excluirMotorista() throws Exception {
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(MOTORISTAS_ID, 2);

		mockMvc.perform(request).andExpect(status().isNoContent());
	}

	@Test
	void listaTodosMotoristas() throws Exception {

		Motorista motoristaAResponse = helperParaResponses.criarMotoristaResponse(1,
				"Ayrton Senna", "123456789", "ayrton_senna@stm.com");

		Motorista motoristaBResponse = helperParaResponses.criarMotoristaResponse(
				2, "Jacques Villeneuve", "987654321", "jacques_villeneuve@stm.com");

		List<Motorista> listaDeMotoristas = Arrays.asList(motoristaAResponse, motoristaBResponse);
		BDDMockito.given(motoristasService.listarTodos()).willReturn(listaDeMotoristas);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(MOTORISTAS)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$.[0].id", is(listaDeMotoristas.get(0).getId())))
				.andExpect(jsonPath("$.[0].nome", is(listaDeMotoristas.get(0).getNome())))
				.andExpect(jsonPath("$.[0].carteira", is(listaDeMotoristas.get(0).getCarteira())))
				.andExpect(jsonPath("$.[0].email", is(listaDeMotoristas.get(0).getEmail())))

				.andExpect(jsonPath("$.[1].id", is(listaDeMotoristas.get(1).getId())))
				.andExpect(jsonPath("$.[1].nome", is(listaDeMotoristas.get(1).getNome())))
				.andExpect(jsonPath("$.[1].carteira", is(listaDeMotoristas.get(1).getCarteira())))
				.andExpect(jsonPath("$.[1].email", is(listaDeMotoristas.get(1).getEmail())));
	}

	@Test
	void trazMotoristaPorId() throws Exception {

		Integer idMotorista = 1;
		Motorista motoristaResponse = helperParaResponses.criarMotoristaResponse(
				idMotorista, "Ayrton Senna", "123456789", "ayrton_senna@stm.com");

		BDDMockito.given(motoristasService.trazerMotoristaPorId(idMotorista)).willReturn(motoristaResponse);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(MOTORISTAS_ID, idMotorista)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(motoristaResponse.getId())))
				.andExpect(jsonPath("$.nome", is(motoristaResponse.getNome())))
				.andExpect(jsonPath("$.carteira", is(motoristaResponse.getCarteira())))
				.andExpect(jsonPath("$.email", is(motoristaResponse.getEmail())));
	}
}
