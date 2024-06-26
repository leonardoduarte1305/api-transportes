package br.com.transportes.apitransportes.helper;

import br.com.transportes.server.model.AtributosMotorista;
import br.com.transportes.server.model.MaterialQuantidadeSetor;
import br.com.transportes.server.model.Setor;
import br.com.transportes.server.model.Uf;
import br.com.transportes.server.model.UpsertDestino;
import br.com.transportes.server.model.UpsertMaterial;
import br.com.transportes.server.model.UpsertSede;
import br.com.transportes.server.model.UpsertUsuario;
import br.com.transportes.server.model.UpsertVeiculo;
import br.com.transportes.server.model.UpsertViagem;

import java.math.BigDecimal;
import java.util.List;

public class HelperParaRequests {

    public UpsertSede criarUpsertSede(
            String nome,
            String cep,
            String cidade,
            String observacao,
            Uf uf,
            String rua,
            Integer numero) {
        return new UpsertSede()
                .uf(uf)
                .rua(rua)
                .cep(cep)
                .nome(nome)
                .numero(numero)
                .cidade(cidade)
                .observacao(observacao);
    }

    public UpsertMaterial criarUpsertMaterial(
            String nome,
            String descricao) {
        return new UpsertMaterial()
                .nome(nome)
                .descricao(descricao);
    }

    public AtributosMotorista criarAtributosMotorista(
            String nome,
            String carteira,
            String email) {
        return new AtributosMotorista()
                .nome(nome)
                .carteira(carteira)
                .email(email);
    }

    public Setor criarSetor(
            String nome) {
        return new Setor()
                .nome(nome);
    }

    public UpsertUsuario criarUpsertUsuario(
            String username,
            String password,
            String nome,
            String sobrenome,
            String email,
            UpsertUsuario.RoleEnum role) {
        return new UpsertUsuario()
                .nome(nome)
                .sobrenome(sobrenome)
                .username(username)
                .email(email)
                .password(password)
                .role(role);
    }

    public UpsertVeiculo criarUpsertVeiculo(
            String modelo,
            String marca,
            String placa,
            Integer ano,
            BigDecimal renavan,
            String tamanho) {
        return new UpsertVeiculo()
                .modelo(modelo)
                .marca(marca)
                .placa(placa)
                .ano(ano)
                .renavan(renavan)
                .tamanho(tamanho);
    }

    public MaterialQuantidadeSetor criarMaterialQuantidadeSetor(
            Integer materialId,
            Integer quantidade,
            Integer setorDestino) {
        return new MaterialQuantidadeSetor()
                .materialId(materialId)
                .quantidade(quantidade)
                .setorDestino(setorDestino);
    }

    public UpsertDestino criarUpsertDestino(
            Integer sedeId,
            List<MaterialQuantidadeSetor> materialQntSetorLista) {
        return new UpsertDestino()
                .sedeId(sedeId)
                .materiaisQntdSetor(materialQntSetorLista);
    }

    public UpsertViagem criarUpsertViagem(
            Integer motoristaId,
            Integer veiculoId,
            List<Integer> destinos,
            String datetimeSaida,
            String datetimeVolta,
            Integer sedeId) {
        return new UpsertViagem()
                .motoristaId(motoristaId)
                .veiculoId(veiculoId)
                .destinos(destinos)
                .datetimeSaida(datetimeSaida)
                .datetimeVolta(datetimeVolta)
                .sede(sedeId);
    }

    public MaterialQuantidadeSetor criarMaterialQuantidadeSetorCompleto(Integer materialId) {
        return criarMaterialQuantidadeSetor(materialId, 10, 15);
    }
}
