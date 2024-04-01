package com.javali.CtrlA.repositorios;

import com.javali.CtrlA.entidades.Ativo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtivoRepositorio extends JpaRepository<Ativo, Long> {
}