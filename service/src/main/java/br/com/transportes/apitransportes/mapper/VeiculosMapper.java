package br.com.transportes.apitransportes.mapper;

import br.com.transportes.apitransportes.entity.Veiculo;
import br.com.transportes.server.model.UpsertVeiculo;
import org.mapstruct.Mapper;

@Mapper
public interface VeiculosMapper {

    @ToEntity
    Veiculo toVeiculoEntity(UpsertVeiculo veiculo);

    br.com.transportes.server.model.Veiculo toVeiculoDto(Veiculo veiculo);
}
