package br.com.transportes.apitransportes.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum Uf {

    RO("RO"),
    AC("AC"),
    AM("AM"),
    RR("RR"),
    PA("PA"),
    AP("AP"),
    TO("TO"),
    MA("MA"),
    PI("PI"),
    CE("CE"),
    RN("RN"),
    PB("PB"),
    PE("PE"),
    AL("AL"),
    SE("SE"),
    BA("BA"),
    MG("MG"),
    ES("ES"),
    RJ("RJ"),
    SP("SP"),
    PR("PR"),
    SC("SC"),
    RS("RS"),
    MS("MS"),
    MT("MT"),
    GO("GO"),
    DF("DF");

    private String value;

    Uf(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
