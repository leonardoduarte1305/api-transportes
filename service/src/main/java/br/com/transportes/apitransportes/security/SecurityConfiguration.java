package br.com.transportes.apitransportes.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.util.UriTemplate;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@Profile({ "production", "default" })
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

	private static final String[] SWAGGER_URLS = { "/swagger-ui/**", "/swagger-resources/*", "/v3/api-docs/**" };
	private static final String[] ACTUATOR_URLS = { "/actuator/**" };
	@Value("${spring.security.oauth2.resourceserver.jwt.trusted-issuers:[]}")
	private List<UriTemplate> trustedIssuers;

	// Allowing access to swagger-ui using SpringSecurity
	// https://stackoverflow.com/questions/70906081/springboot-swagger3-failed-to-load-remote-configuration
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
			@Value("${spring.security.oauth2.client-name}") String clientId) throws Exception {
		return http
				.authorizeHttpRequests().requestMatchers(SWAGGER_URLS).permitAll()
				.and()
				.authorizeHttpRequests().requestMatchers(ACTUATOR_URLS).permitAll()
				.anyRequest().authenticated()
				.and()
				.oauth2ResourceServer(
						oAuth2Config -> oAuth2Config.authenticationManagerResolver(
								this.authenticationManagerResolver(clientId)))
				.cors().and()
				.sessionManagement().disable()
				.csrf().disable()
				.headers().disable()
				.formLogin().disable()
				.logout().disable()
				.build();
	}

	public AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver(String clientId) {
		return new JwtIssuerAuthenticationManagerResolver(
				new TrustedIssuerJwtAuthenticationManagerResolver(this.trustedIssuers)
						.withJwtAuthenticationConverter(grantedAuthoritiesExtractor(clientId)));

	}

	Converter<Jwt, AbstractAuthenticationToken> grantedAuthoritiesExtractor(String clientId) {
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
				new GrantedAuthoritiesFromClientRolesExtractor(clientId));
		return jwtAuthenticationConverter;
	}
}
