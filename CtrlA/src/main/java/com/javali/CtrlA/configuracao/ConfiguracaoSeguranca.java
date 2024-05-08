package com.javali.CtrlA.configuracao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.javali.CtrlA.filtros.JwtFilter;
import com.javali.CtrlA.servicos.UserDetailsServiceImpl;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ConfiguracaoSeguranca {

	@Autowired
	private JwtFilter filter;

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		List<String> origins = new ArrayList<>();
		List<String> methods = new ArrayList<>();
		List<String> headers = new ArrayList<>();

		origins.add("*");
		methods.add("GET");
		methods.add("POST");
		methods.add("PUT");
		methods.add("DELETE");
		headers.add("Authorization");
		headers.add("Cache-Control");
		headers.add("Content-Type");

		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOrigins(origins);
		configuration.setAllowedMethods(methods);
		configuration.setAllowedHeaders(headers);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/login/**", configuration);
		return source;
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(this.userDetailsService());
		authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
		return authenticationProvider;
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(AbstractHttpConfigurer::disable).csrf(AbstractHttpConfigurer::disable);

		http.authorizeHttpRequests(request -> {
			request.requestMatchers("/**").permitAll().anyRequest().authenticated();
		});


		http.authenticationProvider(this.authenticationProvider());
		http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}