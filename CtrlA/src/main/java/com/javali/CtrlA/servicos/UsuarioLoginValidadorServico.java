package com.javali.CtrlA.servicos;

import org.springframework.stereotype.Service;

import com.javali.CtrlA.entidades.Usuario;


@Service
public class UsuarioLoginValidadorServico {
	public boolean isCredentialValid(Usuario usuario) {
		boolean validation = false;
		if (usuario != null) {
			if ((usuario.getEmail() != null) && (usuario.getUsuariologin().getSenha() != null)) {
				if (!usuario.getEmail().isBlank() && !usuario.getUsuariologin().getSenha().isBlank()) {
					validation = true;
				}
			}
		}
		return validation;
	}
}
