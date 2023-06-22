package br.com.transportes.apitransportes.controller;

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
import br.com.transportes.apitransportes.service.MateriaisService;
import br.com.transportes.server.model.Material;

@ExtendWith({ SpringExtension.class })
@WebMvcTest(MaterialController.class)
@AutoConfigureMockMvc(addFilters = false)
class MaterialControllerTest {

	private static final String MATERIAIS = "/materiais";
	private static final String MATERIAIS_ID = "/materiais/{id}";

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	MateriaisService materiaisService;

	HelperParaRequests helperParaRequests;
	HelperParaResponses helperParaResponses;

	@BeforeEach
	void setUp() {
		helperParaRequests = new HelperParaRequests();
		helperParaResponses = new HelperParaResponses();
	}

	@Test
	void criarMaterial() throws Exception {

		br.com.transportes.server.model.UpsertMaterial materialRequest = helperParaRequests.criarUpsertMaterial(
				"Monitor de 24 DELL", "Lindo monitor de 24 polegadas");

		br.com.transportes.server.model.Material materialResponse = helperParaResponses.criarMaterialResponse(
				2, "Monitor de 24 DELL", "Lindo monitor de 24 polegadas");

		BDDMockito.given(materiaisService.upsertMaterial(null, materialRequest)).willReturn(materialResponse);

		String conteudo = objectMapper.writeValueAsString(materialRequest);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(MATERIAIS)
				.contentType(MediaType.APPLICATION_JSON)
				.content(conteudo)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(materialResponse.getId())))
				.andExpect(jsonPath("$.nome", is(materialResponse.getNome())))
				.andExpect(jsonPath("$.descricao", is(materialResponse.getDescricao())));
	}

	@Test
	void editarCadastroMaterial() throws Exception {

		br.com.transportes.server.model.UpsertMaterial materialRequest = helperParaRequests.criarUpsertMaterial(
				"Monitor de 24 DELL", "Lindo monitor de 24 polegadas");

		Integer idMaterial = 2;
		br.com.transportes.server.model.Material materialResponse = helperParaResponses.criarMaterialResponse(
				idMaterial, "Cadeira de balanço", "Linda cadeira de balanço de vime");

		BDDMockito.given(materiaisService.upsertMaterial(idMaterial, materialRequest)).willReturn(materialResponse);

		String conteudo = objectMapper.writeValueAsString(materialRequest);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put(MATERIAIS_ID, idMaterial)
				.contentType(MediaType.APPLICATION_JSON)
				.content(conteudo)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(materialResponse.getId())))
				.andExpect(jsonPath("$.nome", is(materialResponse.getNome())))
				.andExpect(jsonPath("$.descricao", is(materialResponse.getDescricao())));
	}

	@Test
	void excluirMaterial() throws Exception {

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(MATERIAIS_ID, 2);

		mockMvc.perform(request).andExpect(status().isNoContent());
	}

	@Test
	void listaTodosMateriaisCadastrados() throws Exception {

		br.com.transportes.server.model.Material materialAResponse = helperParaResponses.criarMaterialResponse(
				1, "Monitor de 24 DELL", "Lindo monitor de 24 polegadas");

		br.com.transportes.server.model.Material materialBResponse = helperParaResponses.criarMaterialResponse(
				2, "Cadeira de balanço", "Linda cadeira de balanço de vime");

		List<Material> listaMateriais = Arrays.asList(materialAResponse, materialBResponse);
		BDDMockito.given(materiaisService.listarTodos()).willReturn(listaMateriais);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(MATERIAIS)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[0].id", is(listaMateriais.get(0).getId())))
				.andExpect(jsonPath("$.[0].nome", is(listaMateriais.get(0).getNome())))
				.andExpect(jsonPath("$.[0].descricao", is(listaMateriais.get(0).getDescricao())))

				.andExpect(jsonPath("$.[1].id", is(listaMateriais.get(1).getId())))
				.andExpect(jsonPath("$.[1].nome", is(listaMateriais.get(1).getNome())))
				.andExpect(jsonPath("$.[1].descricao", is(listaMateriais.get(1).getDescricao())));
	}

	@Test
	void trazMaterialPorId() throws Exception {

		Integer idMaterial = 1;
		br.com.transportes.server.model.Material materialResponse = helperParaResponses.criarMaterialResponse(
				idMaterial, "Cadeira de balanço", "Linda cadeira de balanço de vime");

		BDDMockito.given(materiaisService.trazerMaterialPorId(idMaterial)).willReturn(materialResponse);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(MATERIAIS_ID, idMaterial)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(materialResponse.getId())))
				.andExpect(jsonPath("$.nome", is(materialResponse.getNome())))
				.andExpect(jsonPath("$.descricao", is(materialResponse.getDescricao())));
	}
}
