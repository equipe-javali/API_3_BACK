package com.javali.CtrlA.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "notafiscal")
public class NotaFiscal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "documento", length = Integer.MAX_VALUE)
    private String documento;

    @OneToOne(mappedBy = "idNotaFiscal")
    private HistoricoAtivoIntangivel historicoAtivoIntangivels;

    @OneToOne(mappedBy = "idNotaFiscal")
    private HistoricoAtivoTangivel historicoAtivoTangivels;

    @OneToOne(mappedBy = "idNotaFiscal")
    private Ativo ativos;
}