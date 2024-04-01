package com.javali.CtrlA.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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

}