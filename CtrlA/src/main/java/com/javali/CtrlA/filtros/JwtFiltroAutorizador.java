package com.javali.CtrlA.filtros;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.javali.CtrlA.jwt.JsonWebTotenGerador;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFiltroAutorizador extends BasicAuthenticationFilter {

	private JsonWebTotenGerador jwtTokenGerador;
	private UserDetailsService servico;

	public JwtFiltroAutorizador(AuthenticationManager gerenciadorAutenticacao, JsonWebTotenGerador jwtTokenGerador,
			UserDetailsService servico) {
		super(gerenciadorAutenticacao);
		this.jwtTokenGerador = jwtTokenGerador;
		this.servico = servico;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String cabecalho = request.getHeader("Authorization");
		if (cabecalho != null && cabecalho.startsWith("Bearer ")) {
			String[] partes = cabecalho.split(" ");
			String jwtToken = partes[1];
			UsernamePasswordAuthenticationToken tokenAutenticacao = obterAutenticacao(jwtToken);
			if (tokenAutenticacao != null) {
				SecurityContextHolder.getContext().setAuthentication(tokenAutenticacao);
			}
		}
		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken obterAutenticacao(String jwtToken) {
		if (jwtTokenGerador.validarToken(jwtToken)) {
			String email = jwtTokenGerador.obterEmail(jwtToken);
			UserDetails usuarioSpring = servico.loadUserByUsername(email);
			return new UsernamePasswordAuthenticationToken(usuarioSpring, usuarioSpring.getPassword(), usuarioSpring.getAuthorities());
		}
		return null;
	}
}
