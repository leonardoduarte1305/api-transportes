package br.com.transportes.apitransportes.entity.keycloak;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Role {

	public String id;
	public String name;
	public String description;
	public boolean composite;
	public boolean clientRole;
	public String containerId;
}
