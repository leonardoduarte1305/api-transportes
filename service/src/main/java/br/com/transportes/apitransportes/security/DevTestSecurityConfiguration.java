package br.com.transportes.apitransportes.security;

//@Slf4j
//@Configuration
//@EnableWebSecurity
//@Profile({ "dev", "test" })
public class DevTestSecurityConfiguration {
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
//						.requestMatchers("/**").permitAll()
//				)
//				.cors().and()
//				.sessionManagement().disable()
//				.csrf().disable()
//				.headers().disable()
//				.formLogin().disable()
//				.logout().disable()
//				.build();
//	}
}
