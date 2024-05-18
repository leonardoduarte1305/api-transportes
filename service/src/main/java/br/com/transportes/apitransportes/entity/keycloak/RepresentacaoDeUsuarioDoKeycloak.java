package br.com.transportes.apitransportes.entity.keycloak;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RepresentacaoDeUsuarioDoKeycloak {
    private long createdTimestamp;

    private String id;
    private String username;
    private boolean enabled;
    private boolean totp;
    private boolean emailVerified;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> disableableCredentialTypes;
    private List<String> requiredActions;
    private int notBefore;
    private Access access;
    private List<String> realmRoles;
}

