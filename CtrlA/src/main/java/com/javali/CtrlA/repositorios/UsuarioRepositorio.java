package com.javali.CtrlA.repositorios;

import com.javali.CtrlA.entidades.Usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
	
//	@Query(
//			value = "SELECT * FROM usuario WHERE email = ?1",
//			nativeQuery = true)
	List<Usuario> findByEmail(String email);
}
