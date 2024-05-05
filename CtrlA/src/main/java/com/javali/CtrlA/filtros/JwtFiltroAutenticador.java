package com.javali.CtrlA.filtros;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javali.CtrlA.dto.CredencialDataTransferObject;
import com.javali.CtrlA.jwt.JsonWebTotenGerador;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFiltroAutenticador extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager gerenciadorAutenticacao;
	private JsonWebTotenGerador jwtTokenGerador;

	public JwtFiltroAutenticador(AuthenticationManager gerenciadorAutenticacao, JsonWebTotenGerador jwtTokenGerador) {
		this.gerenciadorAutenticacao = gerenciadorAutenticacao;
		this.jwtTokenGerador = jwtTokenGerador;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		try {
			CredencialDataTransferObject credencialDto = new ObjectMapper().readValue(request.getInputStream(),
					CredencialDataTransferObject.class);
			UsernamePasswordAuthenticationToken tokenAutenticacao = new UsernamePasswordAuthenticationToken(
					credencialDto.getEmail(), credencialDto.getSenha(), new ArrayList<>());

			Authentication autenticacao = gerenciadorAutenticacao.authenticate(tokenAutenticacao);
			return autenticacao;

		} catch (Exception e) {
			throw new RuntimeException(e.getCause());
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication resultadoAutenticacao) throws IOException, ServletException {
		UserDetails usuarioSpring = (UserDetails) resultadoAutenticacao.getPrincipal();
		String email = usuarioSpring.getUsername();
		String jwtToken = jwtTokenGerador.gerarToken(email);

		response.addHeader("Authorization", "Bearer " + jwtToken);

	}

}
