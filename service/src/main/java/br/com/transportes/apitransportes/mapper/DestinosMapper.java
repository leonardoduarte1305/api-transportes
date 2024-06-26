package br.com.transportes.apitransportes.mapper;

import br.com.transportes.apitransportes.entity.Destino;
import br.com.transportes.apitransportes.entity.Sede;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(uses = {SedesMapper.class, MaterialQuantidadeSetorMapper.class})
public interface DestinosMapper {

    @ToEntity
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
    br.com.transportes.server.model.Destino toDestinoDto(Destino destino);
}
