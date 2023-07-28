package br.com.transportes.apitransportes.configuration;

import java.util.Map;

import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Configuration
@EnableCaching
@RequiredArgsConstructor
@ConfigurationProperties("transportes-api.cache")
@EnableConfigurationProperties(CustomCacheConfiguration.class)
public class CustomCacheConfiguration {

	/**
	 * Map of cache names to {@link com.github.benmanes.caffeine.cache.CaffeineSpec}.
	 */
	@Setter
	private Map<String, String> specs;

	@Bean
	public CacheManagerCustomizer<CaffeineCacheManager> cacheManagerCustomizer() {
		return cacheManager -> specs.forEach((cacheName, spec) -> cacheManager.registerCustomCache(cacheName,
				Caffeine.from(spec).build()));
	}
}
