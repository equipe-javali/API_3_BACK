package com.javali.CtrlA.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "ativotangivel")
public class AtivoTangivel {
    @Id
    @Column(name = "id_ativo", nullable = false)
    private Long id;

    @Size(max = 100)
    @Column(name = "marca", length = 100)
    private String marca;

    @Column(name = "garantia")
    private LocalDate garantia;

    @Column(name = "data_aquisicao")
    private LocalDate dataAquisicao;

    @OneToMany(mappedBy = "idAtivoTangivel")
    private Set<HistoricoAtivoTangivel> historicoAtivoTangivels = new LinkedHashSet<>();

}