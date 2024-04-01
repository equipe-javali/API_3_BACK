package com.javali.CtrlA.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "historicoativointangivel")
public class HistoricoAtivoIntangivel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"id_ativo_Intangivel\"")
    private AtivoIntangivel idAtivoIntangivel;

    @Column(name = "data_alteracao")
    private OffsetDateTime dataAlteracao;

    @Column(name = "nome", length = Integer.MAX_VALUE)
    private String nome;

    @Column(name = "custo_aquisicao")
    private Long custoAquisicao;

    @Column(name = "data_aquisicao")
    private OffsetDateTime dataAquisicao;

    @Column(name = "data_expiracao")
    private OffsetDateTime dataExpiracao;

    @Column(name = "numero_identificacao")
    private Long numeroIdentificacao;

    @Column(name = "tipo", length = Integer.MAX_VALUE)
    private String tipo;

    @Column(name = "tag", length = Integer.MAX_VALUE)
    private String tag;

    @Column(name = "grau_importancia")
    private Long grauImportancia;

    @Column(name = "status", length = Integer.MAX_VALUE)
    private String status;

    @Column(name = "ultima_atualizacao")
    private OffsetDateTime ultimaAtualizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_responsavel")
    private Usuario idResponsavel;

    @Column(name = "descricao")
    private Long descricao;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nota_fiscal")
    private NotaFiscal idNotaFiscal;
}