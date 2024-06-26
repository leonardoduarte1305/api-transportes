package br.com.transportes.apitransportes.mapper;

import br.com.transportes.apitransportes.entity.Material;
import br.com.transportes.server.model.UpsertMaterial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface MateriaisMapper {

    @Mapping(target = "id", ignore = true)
    Material toMaterialEntity(UpsertMaterial upsertMaterial);

    br.com.transportes.server.model.Material toMaterialDto(Material material);
}
