package com.javali.CtrlA.repositorios;

import com.javali.CtrlA.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
	Usuario findByEmail(String email);
}
