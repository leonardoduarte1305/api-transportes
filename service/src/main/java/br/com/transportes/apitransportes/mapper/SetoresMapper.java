package br.com.transportes.apitransportes.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import br.com.transportes.apitransportes.entity.Setor;

@Mapper
public interface SetoresMapper {

	@ToEntity
	Setor toSetorEntity(br.com.transportes.server.model.Setor setor);

	@ToDto
	br.com.transportes.server.model.Setor toSetorDto(Setor setor);
}
