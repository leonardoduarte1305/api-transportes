package br.com.transportes.apitransportes.helper;

import java.math.BigDecimal;
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

public class HelperGeralParaTestes {

	protected Motorista criaMotorista(Integer id, String nome, String carteira, String email) {
		return Motorista.builder()
				.id(id)
				.nome(nome)
				.carteira(carteira)
				.email(email)
				.build();
	}

	protected Veiculo criaVeiculo(Integer id, Integer ano, String marca, String modelo, BigDecimal renavan,
			String tamanho, String placa) {
		return Veiculo.builder()
				.id(id)
				.ano(ano)
				.marca(marca)
				.modelo(modelo)
				.renavan(renavan)
				.tamanho(tamanho)
				.placa(placa)
				.build();
	}

	protected Sede criaSede(Long id, String rua, Integer numero, String cep, String cidade, Uf uf, String nome,
			String observacao,
			List<String> inscritos) {
		return Sede.builder()
				.id(id)
				.rua(rua)
				.numero(numero)
				.cep(cep)
				.cidade(cidade)
				.uf(uf)
				.nome(nome)
				.observacao(observacao)
				.inscritos(inscritos)
				.build();
	}

	protected Material criaMaterial(Integer id, String nome, String descricao) {
		return Material.builder()
				.id(id)
				.nome(nome)
				.descricao(descricao)
				.build();
	}

	protected Setor criaSetor(Integer id, String nome) {
		return Setor.builder()
				.id(id)
				.nome(nome)
				.build();
	}

	protected MaterialQuantidadeSetor criaMaterialQuantidadeSetor(Integer id, Material material, Integer quantidade
			, Setor setor, Destino destino) {
		return MaterialQuantidadeSetor.builder()
				.id(id)
				.material(material)
				.quantidade(quantidade)
				.setorDestino(setor)
				.destino(destino)
				.build();
	}

	protected Destino criaDestino(Integer id, Sede sede, List<MaterialQuantidadeSetor> materialQuantidadeSetor,
			Confirmacao status, Viagem viagem, boolean excluido) {
		return Destino.builder()
				.id(id)
				.sede(sede)
				.materiaisQntdSetor(materialQuantidadeSetor)
				.status(status)
				.viagem(viagem)
				.excluido(excluido)
				.build();
	}

	protected Viagem criaViagem(Integer id, Motorista motorista, Veiculo veiculo, List<Destino> destinos,
			String dateTimeSaida, String dateTimeVolta, Integer sede, Confirmacao status, boolean excluido,
			boolean encerrado) {
		return Viagem.builder()
				.id(id)
				.motorista(motorista)
				.veiculo(veiculo)
				.destinos(destinos)
				.datetimeSaida(dateTimeSaida)
				.datetimeVolta(dateTimeVolta)
				.sede(sede)
				.status(status)
				.excluido(excluido)
				.encerrado(encerrado)
				.build();

	}
}
