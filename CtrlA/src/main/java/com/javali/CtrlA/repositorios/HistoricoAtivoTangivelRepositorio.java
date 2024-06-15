package com.javali.CtrlA.repositorios;

import com.javali.CtrlA.entidades.HistoricoAtivoTangivel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoAtivoTangivelRepositorio extends JpaRepository<HistoricoAtivoTangivel, Long> {
	List<HistoricoAtivoTangivel> findByIdAtivo(Long idAtivo);
}
