package com.javali.CtrlA.dto;

import com.javali.CtrlA.entidades.Usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class esqueciSenhaDTO {
	private Usuario usuario;
	private String novaSenha;
	private String ConfNovaSenha;
}
