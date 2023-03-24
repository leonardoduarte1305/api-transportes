package br.com.transportes.apitransportes.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import br.com.transportes.apitransportes.entity.Destino;
import br.com.transportes.apitransportes.entity.Motorista;
import br.com.transportes.apitransportes.entity.Veiculo;
import br.com.transportes.apitransportes.entity.Viagem;
import br.com.transportes.server.model.UpsertViagem;

@Mapper(uses = { VeiculosMapper.class, MotoristasMapper.class, DestinosMapper.class })
public interface ViagensMapper {

	ViagensMapper INSTANCE = Mappers.getMapper(ViagensMapper.class);

	@Mapping(target = "id", ignore = true)
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

		return entidade;
	}

	@Mapping(target = "motoristaId", source = "motorista.id")
	@Mapping(target = "veiculoId", source = "veiculo.id")
	br.com.transportes.server.model.Viagem toViagemDto(Viagem viagem);

	default List<Integer> map(List<Destino> destinos) {
		return destinos.stream().map(Destino::getId).toList();
	}
}