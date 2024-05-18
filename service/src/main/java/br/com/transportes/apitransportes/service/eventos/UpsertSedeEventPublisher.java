package br.com.transportes.apitransportes.service.eventos;

import br.com.transportes.server.model.UpsertSede;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpsertSedeEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void emiteEventoParaSalvarSede(final UpsertSede message) {
        long delay = Math.round(Math.random() * 2000);
        try {
            log.info("Publicando o evento com o delay de: {}s", delay);
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        applicationEventPublisher.publishEvent(new UpsertSedeEvent(message));
    }
}
