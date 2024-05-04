package com.javali.CtrlA.configuracao;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class ConfiguracaoSeguranca{
	
	private static final String[] rotasPublicas = {"/**" };
	
	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		return http.csrf(AbstractHttpConfigurer::disable)
		        .cors(AbstractHttpConfigurer::disable)
		        .authorizeHttpRequests(request -> {
		          request.requestMatchers(rotasPublicas).permitAll();
		        }).build();
	}

	@Bean
	UrlBasedCorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET","POST"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}