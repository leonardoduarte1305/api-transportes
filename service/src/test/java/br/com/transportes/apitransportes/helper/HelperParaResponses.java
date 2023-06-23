package br.com.transportes.apitransportes.helper;

import java.math.BigDecimal;
import java.util.List;

import br.com.transportes.server.model.Confirmacao;
import br.com.transportes.server.model.Destino;
import br.com.transportes.server.model.Material;
import br.com.transportes.server.model.MaterialQuantidadeSetor;
import br.com.transportes.server.model.Motorista;
import br.com.transportes.server.model.Sede;
import br.com.transportes.server.model.Setor;
import br.com.transportes.server.model.Uf;
import br.com.transportes.server.model.Veiculo;

public class HelperParaResponses {

	public Sede criarSedeResponse(
			Integer id,
			String nome,
			String cep,
			String cidade,
			String observacao,
			Uf uf,
			String rua,
			Integer numero) {
		return new Sede()
				.id(id)
				.uf(uf)
				.rua(rua)
				.cep(cep)
				.nome(nome)
				.numero(numero)
				.cidade(cidade)
				.observacao(observacao);
	}

	public Material criarMaterialResponse(
			Integer id,
			String nome,
			String descricao) {
		return new Material()
				.id(id)
				.nome(nome)
				.descricao(descricao);
	}

	public Motorista criarMotoristaResponse(
			Integer id,
			String nome,
			String carteira,
			String email) {
		return new Motorista()
				.id(id)
				.nome(nome)
				.carteira(carteira)
				.email(email);
	}

	public Setor criarSetorResponse(
			Integer id,
			String nome) {
		return new Setor()
				.id(id)
				.nome(nome);
	}

	public Veiculo criarVeiculoResponse(
			Integer id,
			String modelo,
			String marca,
			String placa,
			Integer ano,
			BigDecimal renavan,
			String tamanho) {
		return new Veiculo()
				.id(id)
				.modelo(modelo)
				.marca(marca)
				.placa(placa)
				.ano(ano)
				.renavan(renavan)
				.tamanho(tamanho);
	}

	public Destino criarDestino(
			Integer id,
			Integer sedeId,
			Confirmacao confirmacao,
			List<MaterialQuantidadeSetor> materialQntSetorLista) {
		return new Destino()
				.id(id)
				.status(confirmacao)
				.sedeId(sedeId)
				.materiaisQntdSetor(materialQntSetorLista);
	}

	public Confirmacao criarConfirmacao(
			Confirmacao.ConfirmacaoEnum confirmacao) {
		return new Confirmacao()
				.confirmacao(confirmacao);
	}
}
