package br.com.transportes.apitransportes.service;

import static br.com.transportes.apitransportes.helper.Constantes.DESTINO_ID;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import br.com.transportes.apitransportes.FullApiImplApplication;
import br.com.transportes.apitransportes.email.EmailService;
import br.com.transportes.apitransportes.entity.Destino;
import br.com.transportes.apitransportes.entity.Material;
import br.com.transportes.apitransportes.entity.Sede;
import br.com.transportes.apitransportes.entity.Setor;
import br.com.transportes.apitransportes.exception.EntidadeNaoEncontradaException;
import br.com.transportes.apitransportes.fixtures.SedeFixture;
import br.com.transportes.apitransportes.helper.HelperGeral;
import br.com.transportes.apitransportes.helper.HelperParaRequests;
import br.com.transportes.apitransportes.mapper.DestinosMapper;
import br.com.transportes.apitransportes.mapper.MaterialQuantidadeSetorMapper;
import br.com.transportes.apitransportes.repository.DestinosRepository;
import br.com.transportes.server.model.Confirmacao;
import br.com.transportes.server.model.MaterialQuantidadeSetor;
import br.com.transportes.server.model.UpsertDestino;

@SpringBootTest(classes = FullApiImplApplication.class)
class DestinosServiceTest {

	@Autowired
	DestinosService destinosService;
	@Autowired
	DestinosMapper destinosMapper;
	@Autowired
	MaterialQuantidadeSetorMapper materialQuantidadeSetorMapper;

	@MockBean
	DestinosRepository destinosRepository;
	@MockBean
	MateriaisService materiaisService;
	@MockBean
	MaterialQuantidadeSetorService materialQuantidadeSetorService;
	@MockBean
	SedesService sedesService;
	@MockBean
	SetoresService setoresService;
	@MockBean
	EmailService emailService;

	@Captor
	ArgumentCaptor<br.com.transportes.apitransportes.entity.Destino> capturaDestinoEntity;

	@Captor
	ArgumentCaptor<List<br.com.transportes.apitransportes.entity.Destino>> listaDestinoCapture;
	HelperGeral helper;
	HelperParaRequests helperParaRequests;

	@BeforeEach
	void setUp() {
		helper = new HelperGeral();
		helperParaRequests = new HelperParaRequests();
	}

	@Test
	void deveSalvarDestinoCorretamente() {

		Integer sedeId = 10;
		Sede sedeEncontrada = SedeFixture.builder().setId(1).build();
		sedeEncontrada.inscreverUsuario("usuario@gmail.com");
		Mockito.when(sedesService.encontrarSedePorId(sedeId)).thenReturn(sedeEncontrada);

		//==========================================================

		String setorId = "15";
		Setor setor = helper.criaSetorCompleto(Integer.parseInt(setorId));
		Mockito.when(setoresService.encontrarSetorPorId(setorId)).thenReturn(setor);

		Integer materialIdA = 10;
		MaterialQuantidadeSetor matQntdSetorA = helperParaRequests
				.criarMaterialQuantidadeSetorCompleto(materialIdA);
		Material materialA = Material.builder().id(materialIdA).nome("CPU DELL").descricao("Uma máquina").build();
		Mockito.when(materiaisService.encontrarMaterialPorId(materialIdA)).thenReturn(materialA);

		Integer materialIdB = 15;
		MaterialQuantidadeSetor matQntdSetorB = helperParaRequests
				.criarMaterialQuantidadeSetorCompleto(materialIdB);
		Material materialB = Material.builder().id(materialIdB).nome("Notebook Samsung").descricao("Uma nave").build();
		Mockito.when(materiaisService.encontrarMaterialPorId(materialIdB)).thenReturn(materialB);

		List<MaterialQuantidadeSetor> listaDeMatQntdSetor = Arrays.asList(matQntdSetorA, matQntdSetorB);
		//==========================================================

		br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor materialQntdA =
				materialQuantidadeSetorMapper.toMaterialQuantidadeSetorEntity(matQntdSetorA, materialA, setor);

		br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor materialQntdB =
				materialQuantidadeSetorMapper.toMaterialQuantidadeSetorEntity(matQntdSetorB, materialB, setor);
		Mockito.when(materialQuantidadeSetorService.save(any())).thenReturn(materialQntdA, materialQntdB);

		//==========================================================

		UpsertDestino upserDestino = helperParaRequests.criarUpsertDestino(sedeId, listaDeMatQntdSetor);
		destinosService.upsertDestino(null, upserDestino);

		Mockito.verify(materialQuantidadeSetorService, Mockito.times(2))
				.save(any(br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor.class));
		Mockito.verify(destinosRepository, Mockito.times(1))
				.save(any(br.com.transportes.apitransportes.entity.Destino.class));
		Mockito.verify(materialQuantidadeSetorService, Mockito.times(1))
				.salvarTodos(anyList());
		Mockito.verify(emailService, Mockito.times(1))
				.enviarConfirmacaoDeDestino(eq(sedeEncontrada.getNome()), eq(sedeEncontrada.getInscritos()));

	}

	@Test
	void deveAtualizarDestinoCorretamente() {

		Optional<Destino> destinoEncontrado = Optional.of(helper.criarDestinoCompleto());
		Mockito.when(destinosRepository.findById(any(Long.class))).thenReturn(destinoEncontrado);

		Integer sedeId = 10;
		Sede sedeEncontrada = SedeFixture.builder().setId(1).build();
		sedeEncontrada.inscreverUsuario("usuario@gmail.com");
		Mockito.when(sedesService.encontrarSedePorId(sedeId)).thenReturn(sedeEncontrada);

		String setorId = "15";
		Setor setor = helper.criaSetorCompleto(Integer.parseInt(setorId));
		Mockito.when(setoresService.encontrarSetorPorId(setorId)).thenReturn(setor);

		Integer materialIdA = 10;
		MaterialQuantidadeSetor matQntdSetorA = helperParaRequests
				.criarMaterialQuantidadeSetorCompleto(materialIdA);
		Material materialA = Material.builder().id(materialIdA).nome("CPU DELL").descricao("Uma máquina").build();
		Mockito.when(materiaisService.encontrarMaterialPorId(materialIdA)).thenReturn(materialA);

		Integer materialIdB = 15;
		MaterialQuantidadeSetor matQntdSetorB = helperParaRequests
				.criarMaterialQuantidadeSetorCompleto(materialIdB);
		Material materialB = Material.builder().id(materialIdB).nome("Notebook Samsung").descricao("Uma nave").build();
		Mockito.when(materiaisService.encontrarMaterialPorId(materialIdB)).thenReturn(materialB);

		List<MaterialQuantidadeSetor> listaDeMatQntdSetor = Arrays.asList(matQntdSetorA, matQntdSetorB);
		//==========================================================

		br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor materialQntdA =
				materialQuantidadeSetorMapper.toMaterialQuantidadeSetorEntity(matQntdSetorA, materialA, setor);

		br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor materialQntdB =
				materialQuantidadeSetorMapper.toMaterialQuantidadeSetorEntity(matQntdSetorB, materialB, setor);
		Mockito.when(materialQuantidadeSetorService.save(any())).thenReturn(materialQntdA, materialQntdB);

		//==========================================================

		UpsertDestino upserDestino = helperParaRequests.criarUpsertDestino(sedeId, listaDeMatQntdSetor);
		destinosService.upsertDestino(DESTINO_ID, upserDestino);

		Mockito.verify(materialQuantidadeSetorService, Mockito.times(2))
				.save(any(br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor.class));

		Mockito.verify(materialQuantidadeSetorService, Mockito.times(1))
				.removerMateriaisDoDestino(DESTINO_ID);
	}

	//	@Test
	void deveTrazerOsDestinosDeUmaViagemCorretamente() {

		//		Destino destinoA = helper.criarDestinoCompleto(1);
		//		destinoA.setMateriaisQntdSetor(new ArrayList<>());
		//
		//		Destino destinoB = helper.criarDestinoCompleto(2);
		//		destinoB.setMateriaisQntdSetor(new ArrayList<>());
		//
		//		br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor matQntdSetorA =
		//				helper.criaMaterialQuantidadeSetorCompleto(1);
		//		matQntdSetorA.setDestino(destinoA);
		//
		//		br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor matQntdSetorB =
		//				helper.criaMaterialQuantidadeSetorCompleto(2);
		//		matQntdSetorB.setDestino(destinoA);
		//		destinoA.setMateriaisQntdSetor(Arrays.asList(matQntdSetorA, matQntdSetorB));
		//
		//		br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor matQntdSetorC =
		//				helper.criaMaterialQuantidadeSetorCompleto(3);
		//		matQntdSetorC.setDestino(destinoB);
		//		destinoB.setMateriaisQntdSetor(Arrays.asList(matQntdSetorC));
		//
		//		List<Destino> destinos = Arrays.asList(destinoA, destinoB);
		//		Mockito.when(destinosRepository.findAllByViagemId(VIAGEM_ID)).thenReturn(destinos);
		//
		//		List<br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor> listaDeMateriaisQntdSetor =
		//				Arrays.asList(matQntdSetorA, matQntdSetorB, matQntdSetorC);
		//		Mockito.when(materialQuantidadeSetorService.findAllByDestinoIdIsIn(anyList()))
		//				.thenReturn(listaDeMateriaisQntdSetor);
		//
		//		destinosService.trazerDestinosDaViagem(VIAGEM_ID);
	}

	@Test
	void deveExcluirDestinoCorretamente() {

		Optional<Destino> destinoEncontrado = Optional.of(helper.criarDestinoCompleto());
		Mockito.when(destinosRepository.findById(any(Long.class))).thenReturn(destinoEncontrado);

		destinosService.excluirDestinoPorId(DESTINO_ID);

		Mockito.verify(destinosRepository, Mockito.times(1)).save(capturaDestinoEntity.capture());
		br.com.transportes.apitransportes.entity.Destino capturado = capturaDestinoEntity.getValue();
		Assertions.assertTrue(capturado.isExcluido());
	}

	@Test
	void deveTrazerDestinoPorIdCorretamente() {

		Destino destino = helper.criarDestinoCompleto(DESTINO_ID);
		Mockito.when(destinosRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(destino));

		br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor matQntdSetorA =
				helper.criaMaterialQuantidadeSetorCompleto(1);
		matQntdSetorA.setDestino(destino);

		br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor matQntdSetorB =
				helper.criaMaterialQuantidadeSetorCompleto(2);
		matQntdSetorB.setDestino(destino);
		destino.setMateriaisQntdSetor(Arrays.asList(matQntdSetorA, matQntdSetorB));

		List<br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor> listaDeMateriaisQntdSetor =
				Arrays.asList(matQntdSetorA, matQntdSetorB);
		Mockito.when(materialQuantidadeSetorService.findAllByDestinoId(any()))
				.thenReturn(listaDeMateriaisQntdSetor);

		br.com.transportes.server.model.Destino encontrado = destinosService.trazerDestinoPorId(DESTINO_ID);

		Assertions.assertEquals(DESTINO_ID, encontrado.getId());
		Assertions.assertEquals(2, encontrado.getMateriaisQntdSetor().size());
	}

	@Test
	void deveLancarEntidadeNaoEncontradaException() {
		assertThrows(EntidadeNaoEncontradaException.class,
				() -> destinosService.encontrarDestinoPorId(DESTINO_ID));
	}

	@Test
	void deveTrazerOsMateriaisDeUmDestinoCorretamente() {
		Destino destino = helper.criarDestinoCompleto(DESTINO_ID);
		Mockito.when(destinosRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(destino));

		br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor matQntdSetorA =
				helper.criaMaterialQuantidadeSetorCompleto(1);
		matQntdSetorA.setDestino(destino);

		br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor matQntdSetorB =
				helper.criaMaterialQuantidadeSetorCompleto(2);
		matQntdSetorB.setDestino(destino);

		destino.setMateriaisQntdSetor(Arrays.asList(matQntdSetorA, matQntdSetorB));

		List<br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor> listaDeMateriaisQntdSetor =
				Arrays.asList(matQntdSetorA, matQntdSetorB);
		Mockito.when(materialQuantidadeSetorService.findAllByDestinoId(destino.getId()))
				.thenReturn(listaDeMateriaisQntdSetor);

		List<MaterialQuantidadeSetor> encontrados = destinosService.trazMateriaisDoDestino(DESTINO_ID);

		Assertions.assertEquals(2, encontrados.size());
	}

	@Test
	void deveTrazerListaCom2De3DestinosSe1FoiExcluido() {

		Integer idA = 1;
		Integer idB = 2;
		Integer idC = 3;
		List<Integer> listaDeIds = Arrays.asList(idA, idB, idC);

		List<Destino> listaDeDestinos = Arrays.asList(
				Destino.builder().excluido(true).build(),
				Destino.builder().excluido(false).build(),
				Destino.builder().excluido(false).build()
		);
		Mockito.when(destinosRepository.findByIdIsIn(listaDeIds)).thenReturn(listaDeDestinos);

		List<Destino> encontrados = destinosService.findAllByIdIsIn(listaDeIds);

		Assertions.assertEquals(2, encontrados.size());
	}

	@Test
	void confirmaDestinoComSucesso() {

		Destino destino = helper.criarDestinoCompleto(DESTINO_ID);
		destino.desconfirmar();
		Mockito.when(destinosRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(destino));

		destinosService.confirmaDestino(DESTINO_ID,
				new Confirmacao().confirmacao(Confirmacao.ConfirmacaoEnum.CONFIRMADO));

		Mockito.verify(destinosRepository, Mockito.times(1)).save(capturaDestinoEntity.capture());
		Destino capturado = capturaDestinoEntity.getValue();
		Assertions.assertEquals(br.com.transportes.apitransportes.entity.Confirmacao.CONFIRMADO,
				capturado.getStatus());
	}

	@Test
	void desconfirmaDestinoComSucesso() {

		Destino destino = helper.criarDestinoCompleto(DESTINO_ID);
		Mockito.when(destinosRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(destino));

		destinosService.confirmaDestino(DESTINO_ID,
				new Confirmacao().confirmacao(Confirmacao.ConfirmacaoEnum.NAO_CONFIRMADO));

		Mockito.verify(destinosRepository, Mockito.times(1)).save(capturaDestinoEntity.capture());
		Destino capturado = capturaDestinoEntity.getValue();
		Assertions.assertEquals(br.com.transportes.apitransportes.entity.Confirmacao.NAO_CONFIRMADO,
				capturado.getStatus());
	}

	@Test
	void deveSalvarTodosOsDestinosComSucesso() {
		List<Destino> listaDeDestinos = Arrays.asList(
				helper.criarDestinoCompleto(1),
				helper.criarDestinoCompleto(2)
		);
		destinosService.salvarTodos(listaDeDestinos);

		Mockito.verify(destinosRepository, Mockito.times(1)).saveAll(listaDestinoCapture.capture());
		List<Destino> capturado = listaDestinoCapture.getValue();
		Assertions.assertEquals(2, capturado.size());
	}
}
