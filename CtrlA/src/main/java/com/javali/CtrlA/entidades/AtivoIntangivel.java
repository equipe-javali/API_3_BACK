package com.javali.CtrlA.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "ativointangivel")
public class AtivoIntangivel {
    @Id
    @Column(name = "id_ativo", nullable = false)
    private Long id;

    @Column(name = "data_aquisicao")
    private LocalDate dataAquisicao;

    @Column(name = "data_expiracao")
    private LocalDate dataExpiracao;

}