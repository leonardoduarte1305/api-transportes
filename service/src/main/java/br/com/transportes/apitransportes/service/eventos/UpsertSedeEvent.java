package br.com.transportes.apitransportes.service.eventos;

import br.com.transportes.server.model.UpsertSede;
import org.springframework.context.ApplicationEvent;

public class UpsertSedeEvent extends ApplicationEvent {

    private transient final UpsertSede upsertSede;

    public UpsertSedeEvent(Object upsertSede) {
        super(upsertSede);
        this.upsertSede = (UpsertSede) upsertSede;
    }

    public UpsertSede getUpsertSede() {
        return upsertSede;
    }
}
