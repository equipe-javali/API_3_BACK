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
    private Long id;

    @Column(name = "id_ativo")
    private Long idAtivo;

    // Fields copied from AtivoIntangivel
    @Column(name = "data_expiracao")
    private LocalDate dataExpiracao;

    @Column(name = "taxa_amortizacao", precision = 10, scale = 2)
    private BigDecimal taxaAmortizacao;

    @Size(max = 30)
    @Column(name = "periodo_amortizacao", length = 30)
    private String periodoAmortizacao;

    // Fields copied from Ativo
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

    // Fields copied from NotaFiscal
    @Column(name = "documento", length = Integer.MAX_VALUE)
    private String documentoNotaFiscal;

    @Size(max = 30)
    @Column(name = "tipo_documento", length = 30)
    private String tipoDocumentoNotaFiscal;

    // Fields copied from Usuario
    @Size(max = 100)
    @Column(name = "nome_usuario", length = 100)
    private String nomeUsuario;

    @Size(max = 11)
    @Column(name = "cpf_usuario", length = 11)
    private String cpfUsuario;

    @Column(name = "nascimento_usuario")
    private LocalDate nascimentoUsuario;

    @Size(max = 20)
    @Column(name = "departamento_usuario", length = 20)
    private String departamentoUsuario;

    @Size(max = 20)
    @Column(name = "telefone_usuario", length = 20)
    private String telefoneUsuario;

    @Size(max = 100)
    @Column(name = "email_usuario", length = 100)
    private String emailUsuario;

    @Size(max = 100)
    @Column(name = "status_usuario", length = 100)
    private String statusUsuario;
}