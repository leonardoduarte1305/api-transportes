package br.com.transportes.apitransportes.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import br.com.transportes.apitransportes.entity.Destino;
import br.com.transportes.server.model.MaterialQuantidadeSetor;
import br.com.transportes.server.model.UpsertDestino;

@Mapper
public interface DestinosMapper {

	DestinosMapper INSTANCE = Mappers.getMapper(DestinosMapper.class);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "status", ignore = true)
	Destino toDestinoEntity(UpsertDestino destino);

	br.com.transportes.server.model.Destino toDestinoDto(Destino destino);

	@Mapping(target = "id", ignore = true)
	br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor toMaterialQuantidadeSetorEntity(
			MaterialQuantidadeSetor materiaisQntdSetor);

	MaterialQuantidadeSetor toMaterialQuantidadeSetorDto(
			br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor materiaisQntdSetor);
}
