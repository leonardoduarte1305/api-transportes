package br.com.transportes.apitransportes.security;

import lombok.RequiredArgsConstructor;

//@RequiredArgsConstructor
public class GrantedAuthoritiesFromClientRolesExtractor { //} implements Converter<Jwt, Collection<GrantedAuthority>> {
//	public static final String CLAIM_RESOURCE_ACCESS = "resource_access";
//	public static final String CLAIM_CLIENT_ROLES = "roles";
//	public static final String ROLE_PREFIX = "ROLE_";
//
//	private final String clientId;
//
//	public Collection<GrantedAuthority> convert(@NotNull Jwt jwt) {
//		try {
//			if (!jwt.hasClaim(CLAIM_RESOURCE_ACCESS)) {
//				return NO_AUTHORITIES;
//			}
//
//			LinkedTreeMap<String, LinkedTreeMap<String, Object>> resourceAccess = jwt.getClaim(CLAIM_RESOURCE_ACCESS);
//			if (!resourceAccess.containsKey(clientId)) {
//				return NO_AUTHORITIES;
//			}
//
//			LinkedTreeMap<String, Object> clientAccess = resourceAccess.get(clientId);
//			if (!clientAccess.containsKey(CLAIM_CLIENT_ROLES)) {
//				return NO_AUTHORITIES;
//			}
//			ArrayList<String> roles = (ArrayList) clientAccess.get(CLAIM_CLIENT_ROLES);
//			return roles.stream()
//					.map(String.class::cast)
//					.map(role -> ROLE_PREFIX + role)
//					.map(SimpleGrantedAuthority::new)
//					.map(GrantedAuthority.class::cast)
//					.toList();
//		} catch (Exception e) {
//			return NO_AUTHORITIES;
//		}
//	}
}
