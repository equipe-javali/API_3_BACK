package com.javali.CtrlA.adaptadores;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.javali.CtrlA.entidades.Usuario;
import com.javali.CtrlA.entidades.UsuarioLogin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioCadastrarAdaptador implements Adaptador<Usuario> {
	private final BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();
	
	private Usuario usuario;
	private String senha;
	@Override
	public Usuario adaptar() {
		UsuarioLogin loginUsuario = new UsuarioLogin();
		loginUsuario.setSenha(codificador.encode(senha));
		usuario.setUsuariologin(loginUsuario);
		return this.usuario;
	}
}
