package br.com.transportes.apitransportes.helper;

import br.com.transportes.server.model.AtributosMotorista;
import br.com.transportes.server.model.Setor;
import br.com.transportes.server.model.Uf;
import br.com.transportes.server.model.UpsertMaterial;
import br.com.transportes.server.model.UpsertSede;

public class HelperParaRequests {

	public UpsertSede criarUpsertSede(
			String nome,
			String cep,
			String cidade,
			String observacao,
			Uf uf,
			String rua,
			Integer numero) {
		br.com.transportes.server.model.UpsertSede sede = new UpsertSede();
		sede.setUf(uf);
		sede.setRua(rua);
		sede.setCep(cep);
		sede.setNome(nome);
		sede.setNumero(numero);
		sede.setCidade(cidade);
		sede.setObservacao(observacao);
		return sede;
	}

	public UpsertMaterial criarUpsertMaterial(
			String nome,
			String descricao) {
		br.com.transportes.server.model.UpsertMaterial material = new UpsertMaterial();
		material.setNome(nome);
		material.setDescricao(descricao);
		return material;
	}

	public AtributosMotorista criarAtributosMotorista(
			String nome,
			String carteira,
			String email) {
		AtributosMotorista atributosMotorista = new AtributosMotorista();
		atributosMotorista.setNome(nome);
		atributosMotorista.setCarteira(carteira);
		atributosMotorista.setEmail(email);
		return atributosMotorista;
	}

	public Setor criarSetor(
			String nome) {
		Setor setor = new Setor();
		setor.setNome(nome);
		return setor;
	}
}