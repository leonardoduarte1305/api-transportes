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
public class Access {

    private boolean manageGroupMembership;
    private boolean view;
    private boolean mapRoles;
    private boolean impersonate;
    private boolean manage;
}
