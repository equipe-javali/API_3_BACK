package com.javali.CtrlA.endpoint.autenticacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.javali.CtrlA.entidades.Login;
import com.javali.CtrlA.entidades.Usuario;
import com.javali.CtrlA.repositorios.UsuarioRepositorio;
import com.javali.CtrlA.servicos.AuthenticationService;


@CrossOrigin
@RestController
public class AutenticacaoControle {

	@Autowired
	private AuthenticationService authenticatorService;
	
	@Autowired
	private UsuarioRepositorio repositorio;

	@PostMapping("/login/signin")
	public ResponseEntity<?> authenticate(@RequestBody Login login) {
		List<Usuario> usuarios = repositorio.findAll();
		Usuario usuario = null;
		for(Usuario u : usuarios) {
			if(u.getEmail().equals(login.getEmail())) {
				usuario = u;
			}
		}
		return this.authenticatorService.authenticate(usuario);
	}
}
