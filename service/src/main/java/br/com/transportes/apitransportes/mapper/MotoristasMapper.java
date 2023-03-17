package br.com.transportes.apitransportes.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import br.com.transportes.apitransportes.entity.Motorista;
import br.com.transportes.server.model.AtributosMotorista;

@Mapper
public interface MotoristasMapper {

	MotoristasMapper INSTANCE = Mappers.getMapper(MotoristasMapper.class);

	@Mapping(target = "id", ignore = true)
	Motorista toMotoristaEntity(AtributosMotorista veiculo);

	br.com.transportes.server.model.Motorista toMotoristaDto(Motorista veiculo);
}
