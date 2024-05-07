package com.javali.CtrlA.servicos;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.javali.CtrlA.adaptadores.UserDetailsImpl;
import com.javali.CtrlA.entidades.Usuario;
import com.javali.CtrlA.entidades.UsuarioLogin;
import com.javali.CtrlA.repositorios.UsuarioRepositorio;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepositorio repositorio;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario = repositorio.findByEmail(email);
		if (usuario == null) {
			throw new UsernameNotFoundException(email);
		}
		UsuarioLogin loginUsuario = usuario.getUsuariologin();
		return new UserDetailsImpl(usuario.getEmail(), loginUsuario.getSenha(), Arrays.asList(usuario.getPerfil()));
	}
}
