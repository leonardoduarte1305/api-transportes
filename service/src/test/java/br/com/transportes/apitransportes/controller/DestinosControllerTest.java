package br.com.transportes.apitransportes.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import br.com.transportes.apitransportes.service.DestinosService;
import br.com.transportes.server.model.Confirmacao;
import br.com.transportes.server.model.Destino;
import br.com.transportes.server.model.MaterialQuantidadeSetor;
import br.com.transportes.server.model.UpsertDestino;

@ExtendWith({ SpringExtension.class })
@WebMvcTest(DestinosController.class)
@AutoConfigureMockMvc(addFilters = false)
class DestinosControllerTest {

	private static final String DESTINOS = "/destinos";
	private static final String DESTINOS_ID = "/destinos/{id}";
	private static final String DESTINOS_ID_MATERIAIS = "/destinos/{id}/materiais";
	private static final String DESTINOS_ID_CONFIRMACAO = "/destinos/{id}/confirmacao";

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	DestinosService destinosService;

	HelperParaRequests helperParaRequests;
	HelperParaResponses helperParaResponses;

	@BeforeEach
	void setUp() {
		helperParaRequests = new HelperParaRequests();
		helperParaResponses = new HelperParaResponses();
	}

	@Test
	void criarDestino() throws Exception {

		MaterialQuantidadeSetor materialQntSetor =
				helperParaRequests.criarMaterialQuantidadeSetor(3, 10, 4);

		Integer sedeId = 1;
		UpsertDestino destinoRequest = helperParaRequests.
				criarUpsertDestino(sedeId, List.of(materialQntSetor));

		Confirmacao confirmacao = helperParaResponses.criarConfirmacao(Confirmacao.ConfirmacaoEnum.CONFIRMADO);
		Destino destinoResponse =
				helperParaResponses.criarDestino(2, sedeId, confirmacao, List.of(materialQntSetor));

		BDDMockito.given(destinosService.upsertDestino(null, destinoRequest)).willReturn(destinoResponse);

		String conteudo = objectMapper.writeValueAsString(destinoRequest);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(DESTINOS)
				.contentType(MediaType.APPLICATION_JSON)
				.content(conteudo)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(destinoResponse.getId())))
				.andExpect(jsonPath("$.sedeId", is(destinoResponse.getSedeId())))
				.andExpect(jsonPath("$.status.confirmacao",
						is(destinoResponse.getStatus().getConfirmacao().getValue())))
				.andExpect(jsonPath("$.materiaisQntdSetor[0].materialId",
						is(destinoResponse.getMateriaisQntdSetor().get(0).getMaterialId())))
				.andExpect(jsonPath("$.materiaisQntdSetor[0].quantidade",
						is(destinoResponse.getMateriaisQntdSetor().get(0).getQuantidade())))
				.andExpect(jsonPath("$.materiaisQntdSetor[0].setorDestino",
						is(destinoResponse.getMateriaisQntdSetor().get(0).getSetorDestino())));
	}

	@Test
	void editarDestino() throws Exception {

		MaterialQuantidadeSetor materialQntSetorA =
				helperParaRequests.criarMaterialQuantidadeSetor(1, 5, 2);

		MaterialQuantidadeSetor materialQntSetorB =
				helperParaRequests.criarMaterialQuantidadeSetor(3, 2, 2);

		Integer sedeId = 1;
		UpsertDestino destinoRequest = helperParaRequests.
				criarUpsertDestino(sedeId, List.of(materialQntSetorA, materialQntSetorB));

		Integer destinoId = 2;
		Confirmacao confirmacao = helperParaResponses.criarConfirmacao(Confirmacao.ConfirmacaoEnum.CONFIRMADO);
		Destino destinoResponse =
				helperParaResponses.criarDestino(destinoId, sedeId, confirmacao,
						List.of(materialQntSetorA, materialQntSetorB));

		BDDMockito.given(destinosService.upsertDestino(destinoId, destinoRequest)).willReturn(destinoResponse);

		String conteudo = objectMapper.writeValueAsString(destinoRequest);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put(DESTINOS_ID, destinoId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(conteudo)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(destinoResponse.getId())))
				.andExpect(jsonPath("$.sedeId", is(destinoResponse.getSedeId())))
				.andExpect(jsonPath("$.status.confirmacao",
						is(destinoResponse.getStatus().getConfirmacao().getValue())))
				.andExpect(jsonPath("$.materiaisQntdSetor[0].materialId",
						is(destinoResponse.getMateriaisQntdSetor().get(0).getMaterialId())))
				.andExpect(jsonPath("$.materiaisQntdSetor[0].quantidade",
						is(destinoResponse.getMateriaisQntdSetor().get(0).getQuantidade())))
				.andExpect(jsonPath("$.materiaisQntdSetor[0].setorDestino",
						is(destinoResponse.getMateriaisQntdSetor().get(0).getSetorDestino())))

				.andExpect(jsonPath("$.materiaisQntdSetor[1].materialId",
						is(destinoResponse.getMateriaisQntdSetor().get(1).getMaterialId())))
				.andExpect(jsonPath("$.materiaisQntdSetor[1].quantidade",
						is(destinoResponse.getMateriaisQntdSetor().get(1).getQuantidade())))
				.andExpect(jsonPath("$.materiaisQntdSetor[1].setorDestino",
						is(destinoResponse.getMateriaisQntdSetor().get(1).getSetorDestino())));
	}

	@Test
	void excluirDestino() throws Exception {
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(DESTINOS_ID, 2);

		mockMvc.perform(request).andExpect(status().isNoContent());
	}

	@Test
	void trazDestinoPorId() throws Exception {

		MaterialQuantidadeSetor materialQntSetor =
				helperParaRequests.criarMaterialQuantidadeSetor(1, 5, 2);

		Integer destinoId = 2;
		Confirmacao confirmacao = helperParaResponses.criarConfirmacao(Confirmacao.ConfirmacaoEnum.CONFIRMADO);
		Destino destinoResponse =
				helperParaResponses.criarDestino(destinoId, 1, confirmacao, List.of(materialQntSetor));

		BDDMockito.given(destinosService.trazerDestinoPorId(destinoId)).willReturn(destinoResponse);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(DESTINOS_ID, destinoId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(destinoResponse.getId())))
				.andExpect(jsonPath("$.sedeId", is(destinoResponse.getSedeId())))
				.andExpect(jsonPath("$.status.confirmacao",
						is(destinoResponse.getStatus().getConfirmacao().getValue())))
				.andExpect(jsonPath("$.materiaisQntdSetor[0].materialId",
						is(destinoResponse.getMateriaisQntdSetor().get(0).getMaterialId())))
				.andExpect(jsonPath("$.materiaisQntdSetor[0].quantidade",
						is(destinoResponse.getMateriaisQntdSetor().get(0).getQuantidade())))
				.andExpect(jsonPath("$.materiaisQntdSetor[0].setorDestino",
						is(destinoResponse.getMateriaisQntdSetor().get(0).getSetorDestino())));
	}

	@Test
	void trazMateriaisDoDestino() throws Exception {

		MaterialQuantidadeSetor materialQntSetorA =
				helperParaRequests.criarMaterialQuantidadeSetor(1, 5, 2);

		MaterialQuantidadeSetor materialQntSetorB =
				helperParaRequests.criarMaterialQuantidadeSetor(2, 10, 3);

		Integer destinoId = 2;
		List<MaterialQuantidadeSetor> materiaisResponse = List.of(materialQntSetorA, materialQntSetorB);
		BDDMockito.given(destinosService.trazMateriaisDoDestino(destinoId)).willReturn(materiaisResponse);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(DESTINOS_ID_MATERIAIS, destinoId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$.[0].materialId",
						is(materiaisResponse.get(0).getMaterialId())))
				.andExpect(jsonPath("$.[0].quantidade",
						is(materiaisResponse.get(0).getQuantidade())))
				.andExpect(jsonPath("$.[0].setorDestino",
						is(materiaisResponse.get(0).getSetorDestino())))

				.andExpect(jsonPath("$.[1].materialId",
						is(materiaisResponse.get(1).getMaterialId())))
				.andExpect(jsonPath("$.[1].quantidade",
						is(materiaisResponse.get(1).getQuantidade())))
				.andExpect(jsonPath("$.[1].setorDestino",
						is(materiaisResponse.get(1).getSetorDestino())));
	}

	@Test
	void confirmaDestino() throws Exception {

		Confirmacao confirmacao = helperParaResponses.criarConfirmacao(Confirmacao.ConfirmacaoEnum.CONFIRMADO);
		String conteudo = objectMapper.writeValueAsString(confirmacao);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(DESTINOS_ID_CONFIRMACAO, 10)
				.content(conteudo)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isNoContent());
	}
}
