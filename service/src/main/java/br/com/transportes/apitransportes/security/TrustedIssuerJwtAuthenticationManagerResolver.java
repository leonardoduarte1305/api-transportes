package br.com.transportes.apitransportes.security;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.With;
import lombok.extern.slf4j.Slf4j;

//@Slf4j
//@RequiredArgsConstructor
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TrustedIssuerJwtAuthenticationManagerResolver { // implements AuthenticationManagerResolver<String> {
//	private final List<UriTemplate> templates;
//
//	private final Map<String, AuthenticationManager> managers = new ConcurrentHashMap<>();
//
//	@With
//	@Setter
//	private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter;
//
//	@Override
//	public AuthenticationManager resolve(final String issuer) {
//		final AuthenticationManager manager = this.managers.get(issuer);
//		if (manager == null) {
//			for (final UriTemplate template : this.templates) {
//				if (template.matches(issuer)) {
//					return this.managers.computeIfAbsent(issuer,
//							this::newAuthenticationManager);
//				}
//			}
//		}
//
//		return manager;
//	}
//
//	protected AuthenticationManager newAuthenticationManager(final String issuer) {
//		return this.newJwtAuthenticationProvider(
//				JwtDecoders.fromIssuerLocation(issuer))::authenticate;
//	}
//
//	protected JwtAuthenticationProvider newJwtAuthenticationProvider(final JwtDecoder decoder) {
//		final JwtAuthenticationProvider provider = new JwtAuthenticationProvider(decoder);
//		provider.setJwtAuthenticationConverter(this.jwtAuthenticationConverter);
//		return provider;
//	}
}
