package br.com.transportes.apitransportes.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import br.com.transportes.apitransportes.entity.Veiculo;
import br.com.transportes.server.model.UpsertVeiculo;

@Mapper
public interface VeiculosMapper {

	VeiculosMapper INSTANCE = Mappers.getMapper(VeiculosMapper.class);

	@Mapping(target = "id", ignore = true)
	Veiculo toVeiculoEntity(UpsertVeiculo veiculo);

	br.com.transportes.server.model.Veiculo toVeiculoDto(Veiculo veiculo);
}
