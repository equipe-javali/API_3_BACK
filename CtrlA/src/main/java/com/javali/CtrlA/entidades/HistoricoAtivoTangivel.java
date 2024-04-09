package com.javali.CtrlA.entidades;

import com.javali.CtrlA.entidades.Ativo;
import com.javali.CtrlA.entidades.AtivoTangivel;
import com.javali.CtrlA.entidades.NotaFiscal;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "historicoativotangivel")
public class HistoricoAtivoTangivel extends RepresentationModel<HistoricoAtivoTangivel> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ativo_tangivel")
    private AtivoTangivel idAtivoTangivel;

    @Column(name = "data_alteracao")
    private LocalDate dataAlteracao;

    @Size(max = 100)
    @Column(name = "nome", length = 100)
    private String nome;

    @Size(max = 100)
    @Column(name = "marca", length = 100)
    private String marca;

    @Column(name = "custo_aquisicao", precision = 2, scale = 10)
    private BigDecimal custoAquisicao;

    @Column(name = "garantia")
    private LocalDate garantia;

    @Column(name = "data_aquisicao")
    private LocalDate dataAquisicao;

    @Size(max = 50)
    @Column(name = "numero_identificacao", length = 50)
    private String numeroIdentificacao;

    @Column(name = "ultima_atualizacao")
    private LocalDate ultimaAtualizacao;

    @Size(max = 50)
    @Column(name = "tipo", length = 50)
    private String tipo;

    @Size(max = 50)
    @Column(name = "tag", length = 50)
    private String tag;

    @Column(name = "grau_importancia")
    private Long grauImportancia;

    @Size(max = 50)
    @Column(name = "status", length = 50)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_responsavel")
    private Ativo idResponsavel;

    @Size(max = 500)
    @Column(name = "descricao", length = 500)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nota_fiscal")
    private NotaFiscal idNotaFiscal;

}