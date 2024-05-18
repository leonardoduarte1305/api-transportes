package br.com.transportes.apitransportes.mapper;

import br.com.transportes.apitransportes.entity.Destino;
import br.com.transportes.apitransportes.entity.Motorista;
import br.com.transportes.apitransportes.entity.Veiculo;
import br.com.transportes.apitransportes.entity.Viagem;
import br.com.transportes.server.model.UpsertViagem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(uses = {VeiculosMapper.class, MotoristasMapper.class, DestinosMapper.class})
public interface ViagensMapper {

    @ToEntity
    @Mapping(target = "status", ignore = true)
    default Viagem toViagemEntity(UpsertViagem viagem, Motorista motorista, Veiculo veiculo, List<Destino> destinos) {
        if (viagem == null) {
            return null;
        }

        Viagem entidade = new Viagem();

        entidade.setDatetimeSaida(viagem.getDatetimeSaida());
        entidade.setDatetimeVolta(viagem.getDatetimeVolta());
        entidade.setMotorista(motorista);
        entidade.setVeiculo(veiculo);
        entidade.setDestinos(destinos);
        entidade.setSede(viagem.getSede());

        return entidade;
    }

    // https://www.tutorialspoint.com/mapstruct/mapstruct_using_expression.htm
    @Mapping(target = "motoristaId", source = "motorista.id")
    @Mapping(target = "veiculoId", source = "veiculo.id")
    @Mapping(target = "encerrado", expression = "java(viagem.isEncerrado())")
    br.com.transportes.server.model.Viagem toViagemDto(Viagem viagem);

    default List<Integer> map(List<Destino> destinos) {
        return destinos.stream().map(Destino::getId).toList();
    }
}
