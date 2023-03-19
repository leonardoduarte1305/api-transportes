package br.com.transportes.apitransportes.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import br.com.transportes.apitransportes.entity.Setor;

@Mapper
public interface SetoresMapper {

	SetoresMapper INSTANCE = Mappers.getMapper(SetoresMapper.class);

	@Mapping(target = "id", ignore = true)
	Setor toSetorEntity(br.com.transportes.server.model.Setor setor);

	br.com.transportes.server.model.Setor toSetorDto(Setor setor);
}
