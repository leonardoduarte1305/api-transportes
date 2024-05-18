package br.com.transportes.apitransportes.service.eventos;

import br.com.transportes.apitransportes.helper.HelperParaRequests;
import br.com.transportes.server.model.Uf;
import br.com.transportes.server.model.UpsertSede;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class UpsertSedeEventListenerTest {

    UpsertSedeEventPublisher eventPublisher;
    @Mock
    ApplicationEventPublisher applicationEventPublisher;

    HelperParaRequests helperParaRequests;

    @BeforeEach
    void setup() {
        eventPublisher = new UpsertSedeEventPublisher(applicationEventPublisher);
        helperParaRequests = new HelperParaRequests();
    }

    @Test
    void salvarNovaSede() {
        UpsertSede sedeRequest = helperParaRequests.criarUpsertSede("Sede Campeche",
                "88063-100", "Florianópolis", "Uma sede na praia", Uf.SC, "Rua do Gramal", 1234);

        eventPublisher.emiteEventoParaSalvarSede(sedeRequest);

        Mockito.verify(applicationEventPublisher).publishEvent(Mockito.argThat((event -> {
            if (event instanceof UpsertSedeEvent upsertSedeEvent) {
                return upsertSedeEvent.getUpsertSede().equals(sedeRequest);
            }
            return false;
        })));
    }
}
