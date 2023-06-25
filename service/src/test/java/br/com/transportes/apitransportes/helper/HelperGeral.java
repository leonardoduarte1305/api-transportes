package br.com.transportes.apitransportes.helper;

import static br.com.transportes.apitransportes.helper.Constantes.MOTORISTA_ID;
import static br.com.transportes.apitransportes.helper.Constantes.SEDE_ID;
import static br.com.transportes.apitransportes.helper.Constantes.VEICULO_ID;
import static br.com.transportes.apitransportes.helper.Constantes.VIAGEM_ID;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.transportes.apitransportes.entity.Confirmacao;
import br.com.transportes.apitransportes.entity.Destino;
import br.com.transportes.apitransportes.entity.Material;
import br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor;
import br.com.transportes.apitransportes.entity.Motorista;
import br.com.transportes.apitransportes.entity.Sede;
import br.com.transportes.apitransportes.entity.Setor;
import br.com.transportes.apitransportes.entity.Uf;
import br.com.transportes.apitransportes.entity.Veiculo;
import br.com.transportes.apitransportes.entity.Viagem;

public class HelperGeral {

	public br.com.transportes.server.model.Confirmacao criarConfirmacao(String confirmacao) {
		if ("CONFIRMADO".equals(confirmacao)) {
			return new br.com.transportes.server.model.Confirmacao().confirmacao(
					br.com.transportes.server.model.Confirmacao.ConfirmacaoEnum.CONFIRMADO);
		}

		return new br.com.transportes.server.model.Confirmacao().confirmacao(
				br.com.transportes.server.model.Confirmacao.ConfirmacaoEnum.NAO_CONFIRMADO);
	}

	public Viagem criarViagemCompleta(Destino destino) {
		String datetimeSaida = LocalDateTime.now().toString();
		String datetimeVolta = LocalDateTime.now().plusHours(8).toString();
		return criarViagemCompleta(destino, datetimeSaida, datetimeVolta, Confirmacao.NAO_CONFIRMADO);
	}

	public Viagem criarViagemCompleta(
			Destino destino,
			String datetimeSaida,
			String datetimeVolta,
			Confirmacao confirmacao) {
		Motorista motorista = criaMotoristaCompleto();
		Veiculo veiculo = criaVeiculoCompleto();
		Viagem viagem = Viagem.builder()
				.id(VIAGEM_ID)
				.motorista(motorista)
				.veiculo(veiculo)
				.destinos(Arrays.asList(destino))
				.datetimeSaida(datetimeSaida)
				.datetimeVolta(datetimeVolta)
				.sede(SEDE_ID)
				.status(confirmacao)
				.excluido(false)
				.encerrado(false)
				.build();

		destino.setViagem(viagem);
		return viagem;
	}

	public Motorista criaMotoristaCompleto() {
		Motorista motorista = Motorista.builder()
				.id(MOTORISTA_ID)
				.nome("José da Silva")
				.carteira("1234567897")
				.email("jose_da_silva@gmail.com")
				.build();
		return motorista;
	}

	public Veiculo criaVeiculoCompleto() {
		Veiculo veiculo = Veiculo.builder()
				.id(VEICULO_ID)
				.ano(2019)
				.marca("Renaut")
				.modelo("Sandero")
				.renavan(new BigDecimal("123"))
				.tamanho("Médio")
				.placa("ABCC-1234")
				.build();
		return veiculo;
	}

	public Destino criarDestinoCompleto() {
		return criarDestinoCompleto(1);
	}

	public Destino criarDestinoCompleto(Integer destinoId) {
		Sede sede = Sede.builder()
				.id(1L)
				.rua("Rua A")
				.numero(123)
				.cep("88111-232")
				.cidade("Cidade A")
				.uf(Uf.BA)
				.nome("Sede Nome A")
				.observacao("sem obsevação")
				.inscritos(new ArrayList<>())
				.build();

		Material materialA = Material.builder()
				.id(50)
				.nome("Monitor 24 polegadas")
				.descricao("Um monitor legal")
				.build();

		Setor setorDestinoA = Setor.builder()
				.id(36)
				.nome("Financeiro")
				.build();

		MaterialQuantidadeSetor materialQuantidadeSetorA = MaterialQuantidadeSetor.builder()
				.id(2)
				.material(materialA)
				.quantidade(2)
				.setorDestino(setorDestinoA)
				.destino(null)
				.build();

		Material materialB = Material.builder()
				.id(31)
				.nome("CPU DELL")
				.descricao("Uma máquina")
				.build();

		Setor setorDestinoB = Setor.builder()
				.id(36)
				.nome("RH")
				.build();

		MaterialQuantidadeSetor materialQuantidadeSetorB = MaterialQuantidadeSetor.builder()
				.id(7)
				.material(materialB)
				.quantidade(5)
				.setorDestino(setorDestinoB)
				.destino(null)
				.build();

		List<MaterialQuantidadeSetor> listaDeMateriaisQntdSetor =
				Arrays.asList(materialQuantidadeSetorA, materialQuantidadeSetorB);

		Destino destino = Destino.builder()
				.id(destinoId)
				.sede(sede)
				.materiaisQntdSetor(listaDeMateriaisQntdSetor)
				.status(Confirmacao.CONFIRMADO)
				.viagem(null)
				.excluido(false)
				.build();

		materialQuantidadeSetorA.setDestino(destino);
		materialQuantidadeSetorB.setDestino(destino);

		return destino;
	}
}
