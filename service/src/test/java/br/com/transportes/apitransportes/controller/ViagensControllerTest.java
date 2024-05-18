package br.com.transportes.apitransportes.controller;

import br.com.transportes.apitransportes.helper.HelperParaRequests;
import br.com.transportes.apitransportes.helper.HelperParaResponses;
import br.com.transportes.apitransportes.service.ViagensService;
import br.com.transportes.server.model.Confirmacao;
import br.com.transportes.server.model.Destino;
import br.com.transportes.server.model.Encerramento;
import br.com.transportes.server.model.MaterialQuantidadeSetor;
import br.com.transportes.server.model.UpsertViagem;
import br.com.transportes.server.model.Viagem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@WebMvcTest(ViagensController.class)
@AutoConfigureMockMvc(addFilters = false)
class ViagensControllerTest {

    private static final String VIAGENS = "/viagens";
    private static final String VIAGENS_ID = "/viagens/{id}";
    private static final String VIAGENS_ID_DESTINOS = "/viagens/{id}/destinos";
    private static final String VIAGENS_ID_CONFIRMACAO = "/viagens/{id}/confirmacao";
    private static final String VIAGENS_ID_ENCERRAMEMTO = "/viagens/{id}/encerramento";
    private static final String VIAGENS_ID_EXPORTAR = "/viagens/{id}/exportar";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ViagensService viagensService;

    HelperParaRequests helperParaRequests;
    HelperParaResponses helperParaResponses;

    @BeforeEach
    void setUp() {
        helperParaRequests = new HelperParaRequests();
        helperParaResponses = new HelperParaResponses();
    }

    @Test
    void criarViagem() throws Exception {

        UpsertViagem viagemRequest = helperParaRequests.criarUpsertViagem(1, 3, List.of(1, 2, 3, 4),
                LocalDateTime.now().toString(), null, 5);

        Viagem viagemResponse = helperParaResponses.criarViagem(2, 1, 3, List.of(1, 2, 3, 4),
                LocalDateTime.now().toString(), null, 5);

        BDDMockito.given(viagensService.upsertViagem(null, viagemRequest)).willReturn(viagemResponse);

        String conteudo = objectMapper.writeValueAsString(viagemRequest);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(VIAGENS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(conteudo)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(viagemResponse.getId())))
                .andExpect(jsonPath("$.motoristaId", is(viagemResponse.getMotoristaId())))
                .andExpect(jsonPath("$.veiculoId", is(viagemResponse.getVeiculoId())))
                .andExpect(jsonPath("$.destinos", hasSize(4)))
                .andExpect(jsonPath("$.datetimeSaida", is(viagemResponse.getDatetimeSaida())))
                .andExpect(jsonPath("$.datetimeVolta", is(viagemResponse.getDatetimeVolta())))
                .andExpect(jsonPath("$.sede", is(viagemResponse.getSede())))
                .andExpect(jsonPath("$.encerrado", is(viagemResponse.getEncerrado())));
    }

    @Test
    void editarViagem() throws Exception {

        UpsertViagem viagemRequest = helperParaRequests.criarUpsertViagem(4, 2, List.of(7),
                LocalDateTime.now().toString(), LocalDateTime.now().plusHours(10).toString(), 4);

        Integer viagemId = 10;
        Viagem viagemResponse = helperParaResponses.criarViagem(viagemId, 4, 2, List.of(7),
                LocalDateTime.now().toString(), LocalDateTime.now().plusHours(10).toString(), 4);

        BDDMockito.given(viagensService.upsertViagem(viagemId, viagemRequest)).willReturn(viagemResponse);

        String conteudo = objectMapper.writeValueAsString(viagemRequest);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(VIAGENS_ID, viagemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(conteudo)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(viagemResponse.getId())))
                .andExpect(jsonPath("$.motoristaId", is(viagemResponse.getMotoristaId())))
                .andExpect(jsonPath("$.veiculoId", is(viagemResponse.getVeiculoId())))
                .andExpect(jsonPath("$.destinos", hasSize(1)))
                .andExpect(jsonPath("$.datetimeSaida", is(viagemResponse.getDatetimeSaida())))
                .andExpect(jsonPath("$.datetimeVolta", is(viagemResponse.getDatetimeVolta())))
                .andExpect(jsonPath("$.sede", is(viagemResponse.getSede())))
                .andExpect(jsonPath("$.encerrado", is(viagemResponse.getEncerrado())));
    }

    @Test
    void confirmaViagem() throws Exception {

        Confirmacao confirmacao = new Confirmacao().confirmacao(Confirmacao.ConfirmacaoEnum.CONFIRMADO);
        String conteudo = objectMapper.writeValueAsString(confirmacao);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(VIAGENS_ID_CONFIRMACAO, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(conteudo)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    void listaTodasViagens() throws Exception {

        Viagem viagemResponseA = helperParaResponses.criarViagem(1, 4, 2, List.of(7),
                LocalDateTime.now().toString(), LocalDateTime.now().plusHours(6).toString(), 4);

        Viagem viagemResponseB = helperParaResponses.criarViagem(2, 7, 3, List.of(2, 3, 5),
                LocalDateTime.now().toString(), LocalDateTime.now().plusHours(9).toString(), 4);

        List<Viagem> listaDeViagens = List.of(viagemResponseA, viagemResponseB);
        BDDMockito.given(viagensService.listarViagens()).willReturn(listaDeViagens);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(VIAGENS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(listaDeViagens.get(0).getId())))
                .andExpect(jsonPath("$.[0].motoristaId", is(listaDeViagens.get(0).getMotoristaId())))
                .andExpect(jsonPath("$.[0].veiculoId", is(listaDeViagens.get(0).getVeiculoId())))
                .andExpect(jsonPath("$.[0].destinos", hasSize(1)))
                .andExpect(jsonPath("$.[0].datetimeSaida", is(listaDeViagens.get(0).getDatetimeSaida())))
                .andExpect(jsonPath("$.[0].datetimeVolta", is(listaDeViagens.get(0).getDatetimeVolta())))
                .andExpect(jsonPath("$.[0].sede", is(listaDeViagens.get(0).getSede())))
                .andExpect(jsonPath("$.[0].encerrado", is(listaDeViagens.get(0).getEncerrado())))

                .andExpect(jsonPath("$.[1].id", is(listaDeViagens.get(1).getId())))
                .andExpect(jsonPath("$.[1].motoristaId", is(listaDeViagens.get(1).getMotoristaId())))
                .andExpect(jsonPath("$.[1].veiculoId", is(listaDeViagens.get(1).getVeiculoId())))
                .andExpect(jsonPath("$.[1].destinos", hasSize(3)))
                .andExpect(jsonPath("$.[1].datetimeSaida", is(listaDeViagens.get(1).getDatetimeSaida())))
                .andExpect(jsonPath("$.[1].datetimeVolta", is(listaDeViagens.get(1).getDatetimeVolta())))
                .andExpect(jsonPath("$.[1].sede", is(listaDeViagens.get(1).getSede())))
                .andExpect(jsonPath("$.[1].encerrado", is(listaDeViagens.get(1).getEncerrado())));
    }

    @Test
    void trazDestinosDaViagem() throws Exception {

        MaterialQuantidadeSetor materialQntSetorA = helperParaRequests.criarMaterialQuantidadeSetor(
                1, 5, 2);
        MaterialQuantidadeSetor materialQntSetorB = helperParaRequests.criarMaterialQuantidadeSetor(
                3, 2, 2);
        Confirmacao confirmacaoA = helperParaResponses.criarConfirmacao(Confirmacao.ConfirmacaoEnum.CONFIRMADO);
        Destino destinoResponseA = helperParaResponses.criarDestino(
                35, 1, confirmacaoA, List.of(materialQntSetorA, materialQntSetorB));

        MaterialQuantidadeSetor materialQntSetorC = helperParaRequests.criarMaterialQuantidadeSetor(
                3, 2, 2);
        Confirmacao confirmacaoB = helperParaResponses.criarConfirmacao(Confirmacao.ConfirmacaoEnum.NAO_CONFIRMADO);
        Destino destinoResponseB = helperParaResponses.criarDestino(39, 1, confirmacaoB, List.of(materialQntSetorC));

        List<Destino> destinosResponse = List.of(destinoResponseA, destinoResponseB);
        Integer viagemId = 22;
        BDDMockito.given(viagensService.listarDestinosDaViagem(viagemId)).willReturn(destinosResponse);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(VIAGENS_ID_DESTINOS, viagemId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(destinosResponse.get(0).getId())))
                .andExpect(jsonPath("$.[0].sedeId", is(destinosResponse.get(0).getSedeId())))
                .andExpect(jsonPath("$.[0].status.confirmacao",
                        is(destinosResponse.get(0).getStatus().getConfirmacao().getValue())))
                .andExpect(jsonPath("$.[0].materiaisQntdSetor", hasSize(2)))

                .andExpect(jsonPath("$.[1].id", is(destinosResponse.get(1).getId())))
                .andExpect(jsonPath("$.[1].sedeId", is(destinosResponse.get(1).getSedeId())))
                .andExpect(jsonPath("$.[1].status.confirmacao",
                        is(destinosResponse.get(1).getStatus().getConfirmacao().getValue())))
                .andExpect(jsonPath("$.[1].materiaisQntdSetor", hasSize(1)));
    }

    @Test
    void trazViagemPorId() throws Exception {

        Integer viagemId = 7;
        Viagem viagemResponse = helperParaResponses.criarViagem(viagemId, 21, 15, List.of(2, 5, 8),
                LocalDateTime.now().toString(), LocalDateTime.now().plusHours(24).toString(), 1);

        BDDMockito.given(viagensService.trazerViagemPorId(viagemId)).willReturn(viagemResponse);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(VIAGENS_ID, viagemId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(viagemResponse.getId())))
                .andExpect(jsonPath("$.motoristaId", is(viagemResponse.getMotoristaId())))
                .andExpect(jsonPath("$.veiculoId", is(viagemResponse.getVeiculoId())))
                .andExpect(jsonPath("$.destinos", hasSize(3)))
                .andExpect(jsonPath("$.datetimeSaida", is(viagemResponse.getDatetimeSaida())))
                .andExpect(jsonPath("$.datetimeVolta", is(viagemResponse.getDatetimeVolta())))
                .andExpect(jsonPath("$.sede", is(viagemResponse.getSede())))
                .andExpect(jsonPath("$.encerrado", is(viagemResponse.getEncerrado())));
    }

    @Test
    void encerraViagem() throws Exception {

        Encerramento encerramento = new Encerramento().encerrado(Encerramento.EncerradoEnum.ENCERRAR);
        String conteudo = objectMapper.writeValueAsString(encerramento);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(VIAGENS_ID_ENCERRAMEMTO, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(conteudo)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    void excluirViagem() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(VIAGENS_ID, 2);

        mockMvc.perform(request).andExpect(status().isNoContent());
    }

    @Test
    void relatorioDeViagem() throws Exception {

		/*

			O InputStreamResource pode ser lido uma vez apenas, porém estamos tentando lê-lo duas vezes
		 Uma para criar o InputStreamResource e outra na assertiva

		 	Para contornar este problema criamos uma cópia do fluxo antes de criar o InputStreamResource
		 Utilizei o ByteArrayOutputStream para ler o conteúdo do fluxo de entrada e depois copiar usando
		 o IOUtils.copy

		 */

        ByteArrayOutputStream outputStreram = new ByteArrayOutputStream();
        InputStream inputStream = InputStream.nullInputStream();
        IOUtils.copy(inputStream, outputStreram);

        byte[] bytes = outputStreram.toByteArray();
        InputStreamResource inputStreamResource = new InputStreamResource(new ByteArrayInputStream(bytes));

        Integer viagemId = 17;
        BDDMockito.given(viagensService.relatorioDeViagem(viagemId)).willReturn(inputStreamResource);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(VIAGENS_ID_EXPORTAR, viagemId)
                .accept(MediaType.APPLICATION_PDF);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=viagem.csv"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/pdf"))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(content().bytes(bytes));
    }
}
