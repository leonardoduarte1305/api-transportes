package br.com.transportes.apitransportes.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import br.com.transportes.apitransportes.entity.Destino;
import br.com.transportes.apitransportes.entity.Sede;
import br.com.transportes.server.model.MaterialQuantidadeSetor;

@Mapper(uses = { SedesMapper.class, MaterialQuantidadeSetorMapper.class })
public interface DestinosMapper {

	DestinosMapper INSTANCE = Mappers.getMapper(DestinosMapper.class);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "status", ignore = true)
	default Destino toDestinoEntity(
			Sede sede,
			List<br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor> listaDeMaterialQuantidadeSetor) {

		Destino entidade = new Destino();
		entidade.setSede(sede);
		entidade.setMateriaisQntdSetor(listaDeMaterialQuantidadeSetor);

		return entidade;
	}

	@Mapping(target = "sedeId", source = "sede.id")
	@Mapping(target = "materiaisQntdSetor", source = "materiaisQntdSetor")
	br.com.transportes.server.model.Destino toDestinoDto(Destino destino);

//	@Mapping(target = "id", ignore = true)
//	br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor toMaterialQuantidadeSetorEntity(
//			MaterialQuantidadeSetor materialQuantidadeSetor);

//	MaterialQuantidadeSetor toMaterialQuantidadeSetorDto(
//			br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor materialQuantidadeSetor);

}
