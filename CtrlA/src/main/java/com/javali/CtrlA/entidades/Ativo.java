package com.javali.CtrlA.entidades;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@Entity
@Table(name = "ativo")
public class Ativo extends RepresentationModel<Ativo> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_responsavel", nullable = true)
    private Usuario idResponsavel;
    
    @OneToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "id_nota_fiscal", nullable = true)
    private NotaFiscal idNotaFiscal;

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
    
    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @Column(name = "campos_personalizados", length = Integer.MAX_VALUE)
    private String camposPersonalizados;

    @Column(name = "valor_residual", precision = 10, scale = 2)
    private BigDecimal valorResidual;
}