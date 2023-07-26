package br.com.transportes.apitransportes.service.eventos;

import org.springframework.context.ApplicationEvent;

import br.com.transportes.server.model.UpsertSede;

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
