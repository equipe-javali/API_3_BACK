package com.javali.CtrlA.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javali.CtrlA.entidades.Usuario;
import com.javali.CtrlA.repositorios.UsuarioRepositorio;

@Service
public class UsuarioServico {
	
	@Autowired
	private UsuarioRepositorio repo;
	
	public List<Usuario> pegarPorEmail(String email){
		return repo.findByEmail(email);
	}
}
