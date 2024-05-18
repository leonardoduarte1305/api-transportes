package br.com.transportes.apitransportes.entity.keycloak;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepresentacaoDeClientDoKeycloak {

    ArrayList<String> redirectUris = new ArrayList<>();
    ArrayList<String> webOrigins = new ArrayList<>();
    ArrayList<Object> defaultClientScopes = new ArrayList<>();
    ArrayList<Object> optionalClientScopes = new ArrayList<>();
    Access accessObject;
    private String id;
    private String clientId;
    private String name;
    private String rootUrl;
    private String baseUrl;
    private boolean surrogateAuthRequired;
    private boolean enabled;
    private boolean alwaysDisplayInConsole;
    private String clientAuthenticatorType;
    private float notBefore;
    private boolean bearerOnly;
    private boolean consentRequired;
    private boolean standardFlowEnabled;
    private boolean implicitFlowEnabled;
    private boolean directAccessGrantsEnabled;
    private boolean serviceAccountsEnabled;
    private boolean publicClient;
    private boolean frontchannelLogout;
    private String protocol;
    private boolean fullScopeAllowed;
    private float nodeReRegistrationTimeout;
}
