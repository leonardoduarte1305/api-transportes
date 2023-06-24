package br.com.transportes.apitransportes.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
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
import br.com.transportes.apitransportes.service.VeiculosService;
import br.com.transportes.server.model.UpsertVeiculo;
import br.com.transportes.server.model.Veiculo;

@ExtendWith({ SpringExtension.class })
@WebMvcTest(VeiculosController.class)
@AutoConfigureMockMvc(addFilters = false)
class VeiculosControllerTest {

	private static final String VEICULOS = "/veiculos";
	private static final String VEICULOS_ID = "/veiculos/{id}";

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	VeiculosService veiculosService;

	HelperParaRequests helperParaRequests;
	HelperParaResponses helperParaResponses;

	@BeforeEach
	void setUp() {
		helperParaRequests = new HelperParaRequests();
		helperParaResponses = new HelperParaResponses();
	}

	@Test
	void criarVeiculo() throws Exception {

		UpsertVeiculo veiculoRequest = helperParaRequests.criarUpsertVeiculo(
				"modelo", "marca", "QJA-5023", 2023, BigDecimal.valueOf(1234567890), "tamanho");

		Veiculo veiculoResponse = helperParaResponses.criarVeiculoResponse(
				1, "modelo", "marca", "QJA-5023", 2023, BigDecimal.valueOf(1234567890), "tamanho");

		BDDMockito.given(veiculosService.upsertVeiculo(null, veiculoRequest)).willReturn(veiculoResponse);

		String conteudo = objectMapper.writeValueAsString(veiculoRequest);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(VEICULOS)
				.contentType(MediaType.APPLICATION_JSON)
				.content(conteudo)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(veiculoResponse.getId())))
				.andExpect(jsonPath("$.ano", is(veiculoResponse.getAno())))
				.andExpect(jsonPath("$.marca", is(veiculoResponse.getMarca())))
				.andExpect(jsonPath("$.modelo", is(veiculoResponse.getModelo())))
				.andExpect(jsonPath("$.placa", is(veiculoResponse.getPlaca())))
				// .andExpect(jsonPath("$.renavan", is(veiculoResponse.getRenavan())))
				.andExpect(jsonPath("$.tamanho", is(veiculoResponse.getTamanho())));
	}

	@Test
	void editarVeiculo() throws Exception {

		UpsertVeiculo veiculoRequest = helperParaRequests.criarUpsertVeiculo(
				"HB20", "Hyundai", "QHQ-6364", 2012, BigDecimal.valueOf(1987654321), "Pequeno");

		Integer idVeiculo = 2;
		Veiculo veiculoResponse = helperParaResponses.criarVeiculoResponse(
				idVeiculo, "T40", "JAC", "QJA-5023", 2018, BigDecimal.valueOf(1234567890), "Médio");

		BDDMockito.given(veiculosService.upsertVeiculo(idVeiculo, veiculoRequest)).willReturn(veiculoResponse);

		String conteudo = objectMapper.writeValueAsString(veiculoRequest);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put(VEICULOS_ID, idVeiculo)
				.contentType(MediaType.APPLICATION_JSON)
				.content(conteudo)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(veiculoResponse.getId())))
				.andExpect(jsonPath("$.ano", is(veiculoResponse.getAno())))
				.andExpect(jsonPath("$.marca", is(veiculoResponse.getMarca())))
				.andExpect(jsonPath("$.modelo", is(veiculoResponse.getModelo())))
				.andExpect(jsonPath("$.placa", is(veiculoResponse.getPlaca())))
				// .andExpect(jsonPath("$.renavan", is(veiculoResponse.getRenavan())))
				.andExpect(jsonPath("$.tamanho", is(veiculoResponse.getTamanho())));
	}

	@Test
	void excluirVeiculo() throws Exception {
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(VEICULOS_ID, 2);

		mockMvc.perform(request).andExpect(status().isNoContent());
	}

	@Test
	void listaTodosVeiculos() throws Exception {

		Veiculo veiculoAResponse = helperParaResponses.criarVeiculoResponse(
				1, "HB20", "Hyundai", "QHQ-6364", 2012, BigDecimal.valueOf(1987654321), "Pequeno");

		Veiculo veiculoBResponse = helperParaResponses.criarVeiculoResponse(
				2, "T40", "JAC", "QJA-5023", 2018, BigDecimal.valueOf(1234567890), "Médio");

		List<Veiculo> listaDeVeiculos = Arrays.asList(veiculoAResponse, veiculoBResponse);
		BDDMockito.given(veiculosService.listarTodos()).willReturn(listaDeVeiculos);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(VEICULOS)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$.[0].id", is(listaDeVeiculos.get(0).getId())))
				.andExpect(jsonPath("$.[0].ano", is(listaDeVeiculos.get(0).getAno())))
				.andExpect(jsonPath("$.[0].marca", is(listaDeVeiculos.get(0).getMarca())))
				.andExpect(jsonPath("$.[0].modelo", is(listaDeVeiculos.get(0).getModelo())))
				.andExpect(jsonPath("$.[0].placa", is(listaDeVeiculos.get(0).getPlaca())))
				// .andExpect(jsonPath("$.[0].renavan", is(listaDeVeiculos.get(0).getRenavan())))
				.andExpect(jsonPath("$.[0].tamanho", is(listaDeVeiculos.get(0).getTamanho())))

				.andExpect(jsonPath("$.[1].ano", is(listaDeVeiculos.get(1).getAno())))
				.andExpect(jsonPath("$.[1].marca", is(listaDeVeiculos.get(1).getMarca())))
				.andExpect(jsonPath("$.[1].modelo", is(listaDeVeiculos.get(1).getModelo())))
				.andExpect(jsonPath("$.[1].placa", is(listaDeVeiculos.get(1).getPlaca())))
				// .andExpect(jsonPath("$.[1].renavan", is(listaDeVeiculos.get(1).getRenavan())))
				.andExpect(jsonPath("$.[1].tamanho", is(listaDeVeiculos.get(1).getTamanho())));
	}

	@Test
	void trazVeiculoPorId() throws Exception {

		Integer idVeiculo = 2;
		Veiculo veiculoResponse = helperParaResponses.criarVeiculoResponse(
				idVeiculo, "T40", "JAC", "QJA-5023", 2018, BigDecimal.valueOf(1234567890), "Médio");

		BDDMockito.given(veiculosService.trazerVeiculoPorId(idVeiculo)).willReturn(veiculoResponse);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(VEICULOS_ID, idVeiculo)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(veiculoResponse.getId())))
				.andExpect(jsonPath("$.ano", is(veiculoResponse.getAno())))
				.andExpect(jsonPath("$.marca", is(veiculoResponse.getMarca())))
				.andExpect(jsonPath("$.modelo", is(veiculoResponse.getModelo())))
				.andExpect(jsonPath("$.placa", is(veiculoResponse.getPlaca())))
				// .andExpect(jsonPath("$.renavan", is(veiculoResponse.getRenavan())))
				.andExpect(jsonPath("$.tamanho", is(veiculoResponse.getTamanho())));
	}
}
