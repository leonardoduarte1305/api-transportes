package br.com.transportes.apitransportes.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.util.UriTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@Profile({ "dev", "test" })
public class DevTestSecurityConfiguration {

	@Value("${spring.security.oauth2.resourceserver.jwt.trusted-issuers:[]}")
	private List<UriTemplate> trustedIssuers;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
			@Value("${spring.security.oauth2.client-name}") String clientId) throws Exception {
		return http
				.authorizeHttpRequests(request -> request
						.requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
						.requestMatchers("/**").permitAll()
				)
				.cors().and()
				.sessionManagement().disable()
				.csrf().disable()
				.headers().disable()
				.formLogin().disable()
				.logout().disable()
				.build();
	}
}
