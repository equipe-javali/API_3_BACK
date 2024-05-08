package com.javali.CtrlA.modelos;

import com.javali.CtrlA.entidades.Usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ModeloAutenticacao {
	private String token;
	private Usuario usuario;
}
