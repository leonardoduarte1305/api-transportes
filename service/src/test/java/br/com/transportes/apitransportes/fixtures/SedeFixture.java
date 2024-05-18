package br.com.transportes.apitransportes.fixtures;

import br.com.transportes.apitransportes.entity.Sede;
import br.com.transportes.apitransportes.entity.Uf;

public class SedeFixture {

    private Sede sede;

    private SedeFixture() {
        sede = new Sede();
        sede.setId(22L);
        sede.setCep("88040-600");
        sede.setRua("Privet Drive");
        sede.setNumero(4);
        sede.setCidade("Surrey");
        sede.setNome("Duddley's House");
        sede.setObservacao("Muggle family");
        sede.setUf(Uf.SC);
    }

    public static SedeFixture builder() {
        return new SedeFixture();
    }

    public Sede build() {
        return sede;
    }

    public SedeFixture setId(long id) {
        sede.setId(id);
        return this;
    }

    public SedeFixture setCep(String cep) {
        sede.setCep(cep);
        return this;
    }

    public SedeFixture setRua(String rua) {
        sede.setRua(rua);
        return this;
    }

    public SedeFixture setNumero(Integer numero) {
        sede.setNumero(numero);
        return this;
    }

    public SedeFixture setCidade(String cidade) {
        sede.setCidade(cidade);
        return this;
    }

    public SedeFixture setNome(String nome) {
        sede.setNome(nome);
        return this;
    }

    public SedeFixture setObservacao(String observacao) {
        sede.setObservacao(observacao);
        return this;
    }

    public SedeFixture setUf(Uf uf) {
        sede.setUf(uf);
        return this;
    }

}
