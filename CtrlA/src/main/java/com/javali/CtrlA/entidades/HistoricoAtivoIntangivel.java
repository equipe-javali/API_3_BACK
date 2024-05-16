package com.javali.CtrlA.entidades;

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
    private Integer id;

    @Column(name = "data_alteracao")
    private LocalDate dataAlteracao;

    // Fields from AtivoIntangivel
    @Column(name = "id_ativo_intangivel")
    private Long idAtivoIntangivel;

    @Column(name = "data_expiracao")
    private LocalDate dataExpiracao;

    @Column(name = "taxa_amortizacao", precision = 10, scale = 2)
    private BigDecimal taxaAmortizacao;

    @Size(max = 30)
    @Column(name = "periodo_amortizacao", length = 30)
    private String periodoAmortizacao;

    // Fields from Ativo
    @Size(max = 100)
    @Column(name = "nome", length = 100)
    private String nome;

    @Column(name = "custo_aquisicao", precision = 10, scale = 2)
    private BigDecimal custoAquisicao;

    @Size(max = 20)
    @Column(name = "tipo", length = 20)
    private String tipo;

    @Size(max = 20)
    @Column(name = "tag", length = 20)
    private String tag;

    @Column(name = "grau_importancia")
    private Long grauImportancia;

    @Size(max = 50)
    @Column(name = "status", length = 50)
    private String status;

    @Size(max = 500)
    @Column(name = "descricao", length = 500)
    private String descricao;

    @Size(max = 50)
    @Column(name = "numero_identificacao", length = 50)
    private String numeroIdentificacao;

    @Column(name = "ultima_atualizacao")
    private LocalDate ultimaAtualizacao;

    @Size(max = 100)
    @Column(name = "marca", length = 100)
    private String marca;

    @Column(name = "data_aquisicao")
    private LocalDate dataAquisicao;

    @Column(name = "campos_personalizados", length = Integer.MAX_VALUE)
    private String camposPersonalizados;

    // Fields from Usuario
    @Size(max = 100)
    @Column(name = "nome_responsavel", length = 100)
    private String nomeResponsavel;

    @Size(max = 11)
    @Column(name = "cpf_responsavel", length = 11)
    private String cpfResponsavel;

    @Column(name = "nascimento_responsavel")
    private LocalDate nascimentoResponsavel;

    @Size(max = 20)
    @Column(name = "departamento_responsavel", length = 20)
    private String departamentoResponsavel;

    @Size(max = 20)
    @Column(name = "telefone_responsavel", length = 20)
    private String telefoneResponsavel;

    @Size(max = 100)
    @Column(name = "email_responsavel", length = 100)
    private String emailResponsavel;

    @Size(max = 100)
    @Column(name = "status_responsavel", length = 100)
    private String statusResponsavel;

    @Column(name = "documento", length = Integer.MAX_VALUE)
    private String documento;

    @Size(max = 30)
    @Column(name = "tipo_documento", length = 30)
    private String tipoDocumento;


}