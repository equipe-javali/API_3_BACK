package com.javali.CtrlA.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "idAtivoIntangivel")
    private Set<HistoricoAtivoIntangivel> historicoAtivoIntangivels = new LinkedHashSet<>();

}