package br.com.transportes.apitransportes.configuration;

import org.springframework.stereotype.Component;

import br.com.transportes.apitransportes.repository.DestinosRepository;
import br.com.transportes.apitransportes.repository.MateriaisRepository;
import br.com.transportes.apitransportes.repository.MotoristasRepository;
import br.com.transportes.apitransportes.repository.SedesRepository;
import br.com.transportes.apitransportes.repository.SetoresRepository;
import br.com.transportes.apitransportes.repository.VeiculosRepository;
import br.com.transportes.apitransportes.repository.ViagensRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ActuatorCustomEndpoints {

	private static final String DESTINOS = "Destinos";
	private static final String MATERIAIS = "Materiais";
	private static final String MOTORISTAS = "Motoristas";
	private static final String SEDES = "Sedes";
	private static final String SETORES = "Setores";
	private static final String VEICULOS = "Veiculos";
	private static final String VIAGENS = "Viagens";
	private final DestinosRepository destinosRepository;
	private final MateriaisRepository materiaisRepository;
	private final MotoristasRepository motoristasRepository;
	private final SedesRepository sedesRepository;
	private final SetoresRepository setoresRepository;
	private final VeiculosRepository veiculosRepository;
	private final ViagensRepository viagensRepository;
	private final MeterRegistry meterRegistry;

	@PostConstruct
	protected void inicializarMetricas() {
		Gauge.builder(DESTINOS, destinosRepository, DestinosRepository::count).register(meterRegistry);
		Gauge.builder(MATERIAIS, materiaisRepository, MateriaisRepository::count).register(meterRegistry);
		Gauge.builder(MOTORISTAS, motoristasRepository, MotoristasRepository::count).register(meterRegistry);
		Gauge.builder(SEDES, sedesRepository, SedesRepository::count).register(meterRegistry);
		Gauge.builder(SETORES, setoresRepository, SetoresRepository::count).register(meterRegistry);
		Gauge.builder(VEICULOS, veiculosRepository, VeiculosRepository::count).register(meterRegistry);
		Gauge.builder(VIAGENS, viagensRepository, ViagensRepository::count).register(meterRegistry);
	}
}
