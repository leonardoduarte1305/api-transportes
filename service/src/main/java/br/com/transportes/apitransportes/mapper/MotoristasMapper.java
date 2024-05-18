package br.com.transportes.apitransportes.mapper;

import br.com.transportes.apitransportes.entity.Motorista;
import br.com.transportes.server.model.AtributosMotorista;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface MotoristasMapper {

    @Mapping(target = "id", ignore = true)
    Motorista toMotoristaEntity(AtributosMotorista veiculo);

    br.com.transportes.server.model.Motorista toMotoristaDto(Motorista veiculo);
}
