package com.javali.CtrlA.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javali.CtrlA.entidades.Manutencao;

public interface ManutencaoRepositorio extends JpaRepository<Manutencao, Long> {
}