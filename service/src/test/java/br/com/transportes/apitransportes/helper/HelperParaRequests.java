package br.com.transportes.apitransportes.helper;

import br.com.transportes.server.model.Uf;
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
}
