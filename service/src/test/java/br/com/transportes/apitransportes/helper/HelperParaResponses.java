package br.com.transportes.apitransportes.helper;

import br.com.transportes.server.model.Material;
import br.com.transportes.server.model.Motorista;
import br.com.transportes.server.model.Sede;
import br.com.transportes.server.model.Setor;
import br.com.transportes.server.model.Uf;

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
		br.com.transportes.server.model.Sede sede = new Sede();
		sede.setId(id);
		sede.setUf(uf);
		sede.setRua(rua);
		sede.setCep(cep);
		sede.setNome(nome);
		sede.setNumero(numero);
		sede.setCidade(cidade);
		sede.setObservacao(observacao);
		return sede;
	}

	public Material criarMaterialResponse(
			Integer id,
			String nome,
			String descricao) {
		br.com.transportes.server.model.Material material = new Material();
		material.setId(id);
		material.setNome(nome);
		material.setDescricao(descricao);
		return material;
	}

	public Motorista criarMotoristaResponse(
			Integer id,
			String nome,
			String carteira,
			String email) {
		Motorista motorista = new Motorista();
		motorista.setId(id);
		motorista.setNome(nome);
		motorista.setCarteira(carteira);
		motorista.setEmail(email);
		return motorista;
	}

	public Setor criarSetorResponse(
			Integer id,
			String nome) {
		Setor setor = new Setor();
		setor.setNome(nome);
		setor.setId(id);
		return setor;
	}
}
