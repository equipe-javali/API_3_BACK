package com.javali.CtrlA.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "historicoativotangivel")
public class HistoricoAtivoTangivel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ativo_tangivel")
    private AtivoTangivel idAtivoTangivel;

    @Column(name = "data_alteracao")
    private OffsetDateTime dataAlteracao;

    @Column(name = "nome", length = Integer.MAX_VALUE)
    private String nome;

    @Column(name = "marca", length = Integer.MAX_VALUE)
    private String marca;

    @Column(name = "custo_aquisicao")
    private Long custoAquisicao;

    @Column(name = "garantia")
    private OffsetDateTime garantia;

    @Column(name = "data_aquisicao")
    private OffsetDateTime dataAquisicao;

    @Column(name = "numero_identificador", length = Integer.MAX_VALUE)
    private String numeroIdentificador;

    @Column(name = "ultima_atualizacao")
    private OffsetDateTime ultimaAtualizacao;

    @Column(name = "tipo", length = Integer.MAX_VALUE)
    private String tipo;

    @Column(name = "tag", length = Integer.MAX_VALUE)
    private String tag;

    @Column(name = "grau_importancia")
    private Long grauImportancia;

    @Column(name = "status", length = Integer.MAX_VALUE)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_responsavel")
    private Usuario idResponsavel;

    @Column(name = "descricao", length = Integer.MAX_VALUE)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nota_fiscal")
    private NotaFiscal idNotaFiscal;

}