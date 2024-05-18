package br.com.transportes.apitransportes.mapper;

import br.com.transportes.apitransportes.entity.Material;
import br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor;
import br.com.transportes.apitransportes.entity.Setor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {MateriaisMapper.class, SetoresMapper.class})
public interface MaterialQuantidadeSetorMapper {

    @Mapping(target = "id", ignore = true)
    default MaterialQuantidadeSetor toMaterialQuantidadeSetorEntity(
            br.com.transportes.server.model.MaterialQuantidadeSetor materialQuantidadeSetor,
            Material material,
            Setor setor) {

        if (materialQuantidadeSetor == null) {
            return null;
        }

        MaterialQuantidadeSetor entidade = new MaterialQuantidadeSetor();
        entidade.setQuantidade(materialQuantidadeSetor.getQuantidade());
        entidade.setMaterial(material);
        entidade.setSetorDestino(setor);
        return entidade;
    }

    @Mapping(target = "materialId", source = "material.id")
    @Mapping(target = "setorDestino", source = "setorDestino.id")
    @Mapping(target = "quantidade", source = "quantidade")
    br.com.transportes.server.model.MaterialQuantidadeSetor toMaterialQuantidadeSetorDto(
            MaterialQuantidadeSetor materialQuantidadeSetor);
}
