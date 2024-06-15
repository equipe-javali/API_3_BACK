package com.javali.CtrlA.repositorios;

import com.javali.CtrlA.entidades.HistoricoAtivoIntangivel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoAtivoIntangivelRepositorio extends JpaRepository<HistoricoAtivoIntangivel, Long> {
	List<HistoricoAtivoIntangivel> findByIdAtivo(Long idAtivo);
}
