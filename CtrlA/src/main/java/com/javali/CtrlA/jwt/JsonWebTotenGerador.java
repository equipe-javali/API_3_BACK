package com.javali.CtrlA.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JsonWebTotenGerador {

	@Value("${jwt.secret}")
	private String segredo;

	@Value("${jwt.expiration}")
	private Long expiracao;

	public String gerarToken(String email) {
		Date tempoExpiracao = new Date(System.currentTimeMillis() + expiracao);

		return Jwts.builder().setSubject(email).setExpiration(tempoExpiracao)
				.signWith(SignatureAlgorithm.HS512, segredo.getBytes()).compact();
	}

	public boolean validarToken(String jwtToken) {
		Claims reivindicacoes = obterReivindicacoes(jwtToken);
		if (reivindicacoes != null) {
			String nomeUsuario = reivindicacoes.getSubject();
			Date dataExpiracao = reivindicacoes.getExpiration();
			Date agora = new Date(System.currentTimeMillis());
			if (nomeUsuario != null && dataExpiracao != null && agora.before(dataExpiracao)) {
				return true;
			}
		}
		return false;
	}

	private Claims obterReivindicacoes(String jwtToken) {
		try {
			return Jwts.parser().setSigningKey(segredo.getBytes()).parseClaimsJws(jwtToken).getBody();
		} catch (Exception e) {
			return null;
		}
	}

	public String obterEmail(String jwtToken) {
		Claims reivindicacoes = obterReivindicacoes(jwtToken);
		if (reivindicacoes != null) {
			String email = reivindicacoes.getSubject();
			return email;
		}
		return null;
	}
}
