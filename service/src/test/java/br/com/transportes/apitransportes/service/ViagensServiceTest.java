package br.com.transportes.apitransportes.service;

import br.com.transportes.apitransportes.FullApiImplApplication;
import br.com.transportes.apitransportes.email.EmailService;
import br.com.transportes.apitransportes.entity.Confirmacao;
import br.com.transportes.apitransportes.entity.Destino;
import br.com.transportes.apitransportes.entity.Motorista;
import br.com.transportes.apitransportes.entity.Veiculo;
import br.com.transportes.apitransportes.entity.Viagem;
import br.com.transportes.apitransportes.exception.EntidadeNaoEncontradaException;
import br.com.transportes.apitransportes.helper.HelperGeral;
import br.com.transportes.apitransportes.helper.HelperParaRequests;
import br.com.transportes.apitransportes.mapper.DestinosMapper;
import br.com.transportes.apitransportes.mapper.ViagensMapper;
import br.com.transportes.apitransportes.pdf.CriadorDeRelatorioDeViagem;
import br.com.transportes.apitransportes.repository.ViagensRepository;
import br.com.transportes.server.model.Encerramento;
import br.com.transportes.server.model.UpsertViagem;
import com.itextpdf.text.DocumentException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static br.com.transportes.apitransportes.helper.Constantes.CONFIRMADO;
import static br.com.transportes.apitransportes.helper.Constantes.MOTORISTA_ID;
import static br.com.transportes.apitransportes.helper.Constantes.NAO_CONFIRMADO;
import static br.com.transportes.apitransportes.helper.Constantes.SEDE_ID;
import static br.com.transportes.apitransportes.helper.Constantes.VEICULO_ID;
import static br.com.transportes.apitransportes.helper.Constantes.VIAGEM_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;

@SpringBootTest(classes = FullApiImplApplication.class)
class ViagensServiceTest {

    @Autowired
    ViagensService viagensService;
    @Autowired
    ViagensMapper viagensMapper;
    @Autowired
    DestinosMapper destinosMapper;

    @MockBean
    ViagensRepository viagensRepository;
    @MockBean
    DestinosService destinosService;
    @MockBean
    MotoristasService motoristasService;
    @MockBean
    VeiculosService veiculosService;
    @MockBean
    EmailService emailService;
    @Mock
    CriadorDeRelatorioDeViagem criadorDeRelatorioDeViagem;

    HelperGeral helper;

    @BeforeEach
    void setUp() {
        helper = new HelperGeral();
    }

    @Test
    void deveEncontrarUmaViagemPorId() {
        String datetimeSaida = LocalDateTime.now().toString();
        String datetimeVolta = LocalDateTime.now().plusHours(8).toString();

        Destino destino = helper.criarDestinoCompleto();
        Viagem viagem = helper.criarViagemCompleta(destino, datetimeSaida, datetimeVolta, Confirmacao.NAO_CONFIRMADO);

        Mockito.when(viagensRepository.findById(VIAGEM_ID)).thenReturn(Optional.ofNullable(viagem));
        Mockito.when(destinosService.trazerDestinosDaViagem(VIAGEM_ID)).thenReturn(Arrays.asList(destino));

        br.com.transportes.server.model.Viagem encontrada = viagensService.trazerViagemPorId(VIAGEM_ID);

        Assertions.assertEquals(VIAGEM_ID, encontrada.getId());
        Assertions.assertEquals(VEICULO_ID, encontrada.getVeiculoId());
        Assertions.assertEquals(MOTORISTA_ID, encontrada.getMotoristaId());
        Assertions.assertEquals(destino.getId(), encontrada.getDestinos().get(0));
        Assertions.assertEquals(datetimeSaida, encontrada.getDatetimeSaida());
        Assertions.assertEquals(datetimeVolta, encontrada.getDatetimeVolta());
        Assertions.assertEquals(SEDE_ID, encontrada.getSede());
        Assertions.assertEquals(NAO_CONFIRMADO, encontrada.getStatus().getConfirmacao().getValue());
        Assertions.assertTrue("NAO_ENCERRADO".equals(encontrada.getEncerrado()));
    }

    @Test
    void deveLancarEntidadeNaoEncontradaExceptionQuandoViagemNaoEncontrada() {
        Mockito.when(viagensRepository.findById(VIAGEM_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntidadeNaoEncontradaException.class,
                () -> viagensService.trazerViagemPorId(VIAGEM_ID));
    }

    @Test
    void deveExcluirViagemComSucesso() {
        boolean excluido = false;
        Viagem viagem = Viagem.builder().excluido(excluido).build();

        Mockito.when(viagensRepository.findById(VIAGEM_ID)).thenReturn(Optional.of(viagem));
        viagensService.excluirViagem(VIAGEM_ID);

        Assertions.assertTrue(viagem.isExcluido());
    }

    @Test
    void deveEncerarUmaViagem() {
        Encerramento encerramento = new Encerramento().encerrado(Encerramento.EncerradoEnum.ENCERRAR);
        Viagem viagem = Viagem.builder().encerrado(false).build();

        Mockito.when(viagensRepository.findById(VIAGEM_ID)).thenReturn(Optional.of(viagem));
        viagensService.encerraViagem(VIAGEM_ID, encerramento);

        Assertions.assertTrue("ENCERRADO".equals(viagem.isEncerrado()));
        Mockito.verify(viagensRepository, Mockito.times(1)).save(any(Viagem.class));
    }

    @Test
    void emiteRelatorioDeViagemComSucesso() throws DocumentException, IOException {

        Destino destino = helper.criarDestinoCompleto();
        Viagem viagem = helper.criarViagemCompleta(destino);

        byte[] relatorioBytes = "Relatório de Viagem".getBytes();

        Mockito.when(viagensRepository.findById(VIAGEM_ID)).thenReturn(Optional.of(viagem));
        Mockito.when(destinosService.trazerDestinosDaViagem(VIAGEM_ID)).thenReturn(List.of(destino));
        Mockito.when(criadorDeRelatorioDeViagem.criaRelatorioDeViagem(viagem)).thenReturn(relatorioBytes);

        Assertions.assertNotNull(viagensService.relatorioDeViagem(VIAGEM_ID));
    }

    @Test
    void deveTrazerUmaViagemQuandoUmaDeDuasEstaExcluida() {
        Destino destino = helper.criarDestinoCompleto();
        Viagem viagemNaoExluida = helper.criarViagemCompleta(destino);
        Viagem viagemExcluida = Viagem.builder().excluido(true).build();

        Mockito.when(viagensRepository.findAll()).thenReturn(Arrays.asList(viagemExcluida, viagemNaoExluida));
        Mockito.when(destinosService.trazerDestinosDaViagem(VIAGEM_ID)).thenReturn(Arrays.asList(destino));

        List<br.com.transportes.server.model.Viagem> viagens = viagensService.listarViagens();

        Assertions.assertEquals(1, viagens.size());
        Assertions.assertEquals(viagens.get(0).getId(), viagemNaoExluida.getId());
        Assertions.assertEquals(viagens.get(0).getSede(), viagemNaoExluida.getSede());
        Assertions.assertEquals(viagens.get(0).getMotoristaId(), viagemNaoExluida.getMotorista().getId());
    }

    @Test
    void deveTrazerListaVaziaQuandoTodasEstaoExcluidas() {
        Viagem viagemNaoExluida = Viagem.builder().excluido(true).build();
        Viagem viagemExcluida = Viagem.builder().excluido(true).build();

        Mockito.when(viagensRepository.findAll()).thenReturn(Arrays.asList(viagemExcluida, viagemNaoExluida));
        Mockito.when(destinosService.trazerDestinosDaViagem(VIAGEM_ID)).thenReturn(new ArrayList<>());

        List<br.com.transportes.server.model.Viagem> viagens = viagensService.listarViagens();

        Assertions.assertEquals(0, viagens.size());
    }

    @Test
    void deveListar1DestinosQuandoProcurarPeloIdDaViagem() {
        List<Destino> destinos = Arrays.asList(helper.criarDestinoCompleto());
        Mockito.when(destinosService.trazerDestinosDaViagem(VIAGEM_ID)).thenReturn(destinos);

        List<br.com.transportes.server.model.Destino> destinosEncontrados =
                viagensService.listarDestinosDaViagem(VIAGEM_ID);

        Assertions.assertEquals(1, destinosEncontrados.size());
    }

    @Test
    void deveConfirmarUmaViagemCorretamente() {
        Destino destino = helper.criarDestinoCompleto();
        Viagem viagem = helper.criarViagemCompleta(destino);

        Assertions.assertEquals(Confirmacao.NAO_CONFIRMADO, viagem.getStatus());

        Mockito.when(viagensRepository.findById(VIAGEM_ID)).thenReturn(Optional.ofNullable(viagem));

        br.com.transportes.server.model.Confirmacao confirmacao = helper.criarConfirmacao(CONFIRMADO);
        viagensService.confirmaViagem(VIAGEM_ID, confirmacao);

        Assertions.assertEquals(Confirmacao.CONFIRMADO, viagem.getStatus());
        Mockito.verify(viagensRepository, Mockito.times(1)).save(any(Viagem.class));
        Mockito.verify(emailService, Mockito.times(1)).enviarConfirmacaoDeViagem(any(Viagem.class));
    }

    @Test
    void deveDesconfirmarUmaViagemCorretamente() {
        Destino destino = helper.criarDestinoCompleto();
        Viagem viagem = Viagem.builder()
                .status(Confirmacao.CONFIRMADO)
                .destinos(Arrays.asList(destino))
                .build();

        Assertions.assertEquals(Confirmacao.CONFIRMADO, viagem.getStatus());

        Mockito.when(viagensRepository.findById(VIAGEM_ID)).thenReturn(Optional.ofNullable(viagem));

        br.com.transportes.server.model.Confirmacao confirmacao = helper.criarConfirmacao(NAO_CONFIRMADO);
        viagensService.confirmaViagem(VIAGEM_ID, confirmacao);

        Assertions.assertEquals(Confirmacao.NAO_CONFIRMADO, viagem.getStatus());
        Mockito.verify(viagensRepository, Mockito.times(1)).save(any(Viagem.class));
    }

    @Test
    void criarViagemFuncionaCorretamente() {

        List<Integer> destinos = List.of(1, 2);
        UpsertViagem upsertViagem = new HelperParaRequests()
                .criarUpsertViagem(MOTORISTA_ID, VEICULO_ID, destinos,
                        LocalDateTime.now().toString(), null, SEDE_ID);

        Motorista motorista = helper.criaMotoristaCompleto();
        Mockito.when(motoristasService.encontrarMotoristaPorId(MOTORISTA_ID)).thenReturn(motorista);

        Veiculo veiculo = helper.criaVeiculoCompleto();
        Mockito.when(veiculosService.encontrarVeiculoPorId(VEICULO_ID)).thenReturn(veiculo);

        List<Destino> destinosEncontrados = Arrays.asList(helper.criarDestinoCompleto(1),
                helper.criarDestinoCompleto(2));
        Mockito.when(destinosService.findAllByIdIsIn(destinos)).thenReturn(destinosEncontrados);

        br.com.transportes.server.model.Viagem viagem = viagensService.upsertViagem(null, upsertViagem);

        Mockito.verify(viagensRepository, Mockito.times(1)).save(any(Viagem.class));
        Mockito.verify(destinosService, Mockito.times(1)).salvarTodos(anyList());
    }

    @Test
    void atualizarViagemFuncionaCorretamente() {

        List<Destino> destinosEncontrados = Arrays.asList(helper.criarDestinoCompleto(3));
        Mockito.when(destinosService.trazerDestinosDaViagem(VIAGEM_ID)).thenReturn(destinosEncontrados);

        Destino destino = Destino.builder().viagem(null).build();
        Viagem viagemEncontrada = helper.criarViagemCompleta(destino);
        Mockito.when(viagensRepository.findById(VIAGEM_ID)).thenReturn(Optional.ofNullable(viagemEncontrada));

        Motorista motorista = helper.criaMotoristaCompleto();
        Mockito.when(motoristasService.encontrarMotoristaPorId(MOTORISTA_ID)).thenReturn(motorista);

        Veiculo veiculo = helper.criaVeiculoCompleto();
        Mockito.when(veiculosService.encontrarVeiculoPorId(VEICULO_ID)).thenReturn(veiculo);

        List<Integer> destinos = List.of(3);
        List<Destino> novosDestinos = Arrays.asList(helper.criarDestinoCompleto(1), helper.criarDestinoCompleto(2));
        Mockito.when(destinosService.findAllByIdIsIn(destinos)).thenReturn(novosDestinos);

        UpsertViagem upsertViagem = new HelperParaRequests()
                .criarUpsertViagem(MOTORISTA_ID, VEICULO_ID, destinos,
                        LocalDateTime.now().toString(), null, SEDE_ID);

        br.com.transportes.server.model.Viagem viagem = viagensService.upsertViagem(VIAGEM_ID, upsertViagem);

        Mockito.verify(destinosService, Mockito.times(2)).salvarTodos(anyList());
    }
}
