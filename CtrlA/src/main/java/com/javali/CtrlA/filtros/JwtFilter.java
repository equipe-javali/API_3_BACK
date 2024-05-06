package com.javali.CtrlA.filtros;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.javali.CtrlA.servicos.HeaderService;
import com.javali.CtrlA.servicos.JwtService;
import com.javali.CtrlA.servicos.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	private final String secret = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private HeaderService headerService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header = headerService.extractHeader(request, "Authorization");
		String jwtToken = headerService.extractJwtToken(header);

		if (jwtToken != null) {
			String email = jwtService.extractUsername(jwtToken, secret);
			if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(email);
				if (jwtService.validateToken(jwtToken, secret, email)) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}

		filterChain.doFilter(request, response);
	}
}
