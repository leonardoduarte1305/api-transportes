package br.com.transportes.apitransportes.mapper;

import br.com.transportes.apitransportes.entity.Setor;
import org.mapstruct.Mapper;

@Mapper
public interface SetoresMapper {

    @ToEntity
    Setor toSetorEntity(br.com.transportes.server.model.Setor setor);

    @ToDto
    br.com.transportes.server.model.Setor toSetorDto(Setor setor);
}
