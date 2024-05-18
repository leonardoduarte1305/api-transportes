package br.com.transportes.apitransportes.mapper;

import br.com.transportes.apitransportes.entity.Sede;
import br.com.transportes.server.model.UpsertSede;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SedesMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "inscritos", ignore = true)
    Sede toSedeEntity(UpsertSede sede);

    br.com.transportes.server.model.Sede toSedeDto(Sede sede);
}
