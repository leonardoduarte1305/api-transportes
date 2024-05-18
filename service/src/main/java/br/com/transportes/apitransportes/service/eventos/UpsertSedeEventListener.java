package br.com.transportes.apitransportes.service.eventos;

import br.com.transportes.apitransportes.service.SedesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpsertSedeEventListener {

    private final SedesService sedesService;

    @EventListener
    public void salvarNovaSede(UpsertSedeEvent upsertSedeEvent) {
        log.error("UpsertSedeEventListener mensagem capturada: {}", upsertSedeEvent.getUpsertSede());

        sedesService.upsertSede(null, upsertSedeEvent.getUpsertSede());
    }
}
