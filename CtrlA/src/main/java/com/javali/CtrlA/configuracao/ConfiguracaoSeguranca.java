package com.javali.CtrlA.configuracao;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfigurationSource;

import com.javali.CtrlA.filtros.JwtFiltroAutenticador;
import com.javali.CtrlA.filtros.JwtFiltroAutorizador;
import com.javali.CtrlA.jwt.JsonWebTotenGerador;
import com.javali.CtrlA.servicos.UserDetailsServiceImpl;


@Configuration
@EnableWebSecurity
public class ConfiguracaoSeguranca extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsServiceImpl servico;
	
	@Autowired
	private JsonWebTotenGerador jwtTokenGerador;
	
	private static final String[] rotasPublicas = {"/**" };
	
	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
		return http.csrf(AbstractHttpConfigurer::disable)
		        .cors(AbstractHttpConfigurer::disable)
		        .authorizeHttpRequests(request -> {
		          request.requestMatchers(rotasPublicas).permitAll();
		        }).addFilter(new JwtFiltroAutenticador(authenticationManager(), jwtTokenGerador))
		        .addFilter(new JwtFiltroAutorizador(authenticationManager(), jwtTokenGerador, servico))
		        .build();
	}
	
	@Bean
	protected void configure(AuthenticationManagerBuilder autenticador) throws Exception {
		autenticador.userDetailsService(servico).passwordEncoder(new BCryptPasswordEncoder());
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