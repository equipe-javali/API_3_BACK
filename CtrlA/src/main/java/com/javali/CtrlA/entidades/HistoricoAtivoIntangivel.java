package com.javali.CtrlA.entidades;

import com.javali.CtrlA.entidades.Ativo;
import com.javali.CtrlA.entidades.AtivoIntangivel;
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
@Table(name = "historicoativointangivel")
public class HistoricoAtivoIntangivel extends RepresentationModel<HistoricoAtivoIntangivel> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"id_ativo_Intangivel\"")
    private AtivoIntangivel idAtivoIntangivel;

    @Column(name = "data_alteracao")
    private LocalDate dataAlteracao;

    @Size(max = 100)
    @Column(name = "nome", length = 100)
    private String nome;

    @Column(name = "custo_aquisicao", precision = 10, scale = 2)
    private BigDecimal custoAquisicao;

    @Column(name = "data_aquisicao")
    private LocalDate dataAquisicao;

    @Column(name = "data_expiracao")
    private LocalDate dataExpiracao;

    @Column(name = "numero_identificacao")
    private Long numeroIdentificacao;

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

    @Column(name = "ultima_atualizacao")
    private LocalDate ultimaAtualizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_responsavel")
    private Ativo idResponsavel;

    @Size(max = 500)
    @Column(name = "descricao", length = 500)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nota_fiscal")
    private NotaFiscal idNotaFiscal;

    @Size(max = 100)
    @Column(name = "marca", length = 100)
    private String marca;

}