package br.com.transportes.apitransportes.security;

import java.util.List;

//@Slf4j
//@Configuration
//@EnableWebSecurity
//@Profile({ "production", "default" })
//@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {
//
//	@Value("${spring.security.oauth2.resourceserver.jwt.trusted-issuers:[]}")
//	private List<UriTemplate> trustedIssuers;
//
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http,
//			@Value("${spring.security.oauth2.client-name}") String clientId) throws Exception {
//		return http
//				.authorizeHttpRequests(request -> request
//						.requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
//						.requestMatchers("/swagger-ui*/**").permitAll()
//						.anyRequest().authenticated()
//				)
//				.oauth2ResourceServer(
//						oAuth2Config -> oAuth2Config.authenticationManagerResolver(
//								this.authenticationManagerResolver(clientId)))
//				.cors().and()
//				.sessionManagement().disable()
//				.csrf().disable()
//				.headers().disable()
//				.formLogin().disable()
//				.logout().disable()
//				.build();
//	}
//
//	public AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver(String clientId) {
//		return new JwtIssuerAuthenticationManagerResolver(
//				new TrustedIssuerJwtAuthenticationManagerResolver(this.trustedIssuers)
//						.withJwtAuthenticationConverter(grantedAuthoritiesExtractor(clientId)));
//
//	}
//
//	Converter<Jwt, AbstractAuthenticationToken> grantedAuthoritiesExtractor(String clientId) {
//		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//
//		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
//				new GrantedAuthoritiesFromClientRolesExtractor(clientId));
//		return jwtAuthenticationConverter;
//	}
}
