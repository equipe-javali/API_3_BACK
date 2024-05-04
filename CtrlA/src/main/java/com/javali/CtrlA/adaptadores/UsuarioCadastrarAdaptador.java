package com.javali.CtrlA.adaptadores;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.javali.CtrlA.entidades.Usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioCadastrarAdaptador implements Adaptador<Usuario> {
	private final BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();
	
	private Usuario usuario;
	@Override
	public Usuario adaptar() {
		this.usuario.getUsuariologin().setSenha(codificador.encode(this.usuario.getUsuariologin().getSenha()));
		return this.usuario;
	}
}
