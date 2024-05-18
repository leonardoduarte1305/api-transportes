package br.com.transportes.apitransportes.controller;

import br.com.transportes.apitransportes.helper.HelperParaRequests;
import br.com.transportes.apitransportes.helper.HelperParaResponses;
import br.com.transportes.apitransportes.service.UsuarioService;
import br.com.transportes.server.model.UpsertUsuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@WebMvcTest(UsuariosController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuariosControllerTest {

    private static final String USUARIOS = "/usuarios";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UsuarioService usuarioService;

    HelperParaRequests helperParaRequests;
    HelperParaResponses helperParaResponses;

    @BeforeEach
    void setUp() {
        helperParaRequests = new HelperParaRequests();
        helperParaResponses = new HelperParaResponses();
    }

    //	@Test
    void criarUsuario() throws Exception {

        UpsertUsuario usuarioRequest = helperParaRequests.criarUpsertUsuario(
                "username", "password", "nome", "sobrenome", "email", UpsertUsuario.RoleEnum.ADMIN);

        String conteudo = objectMapper.writeValueAsString(usuarioRequest);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(USUARIOS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(conteudo)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }
}
