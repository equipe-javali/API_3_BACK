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
@Table(name = "historicoativotangivel")
public class HistoricoAtivoTangivel extends RepresentationModel<HistoricoAtivoTangivel> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    // Campos de Ativo
    @Column(name = "id_ativo")
    private Long idAtivo;

    @Size(max = 100)
    @Column(name = "nome_ativo", length = 100)
    private String nomeAtivo;

    @Column(name = "custo_aquisicao_ativo", precision = 10, scale = 2)
    private BigDecimal custoAquisicaoAtivo;

    @Size(max = 20)
    @Column(name = "tipo_ativo", length = 20)
    private String tipoAtivo;

    @Size(max = 20)
    @Column(name = "tag_ativo", length = 20)
    private String tagAtivo;

    @Column(name = "grau_importancia_ativo")
    private Long grauImportanciaAtivo;

    @Size(max = 50)
    @Column(name = "status_ativo", length = 50)
    private String statusAtivo;

    @Size(max = 500)
    @Column(name = "descricao_ativo", length = 500)
    private String descricaoAtivo;

    @Size(max = 50)
    @Column(name = "numero_identificacao_ativo", length = 50)
    private String numeroIdentificacaoAtivo;

    @Column(name = "ultima_atualizacao_ativo")
    private LocalDate ultimaAtualizacaoAtivo;

    @Size(max = 100)
    @Column(name = "marca_ativo", length = 100)
    private String marcaAtivo;

    @Column(name = "data_aquisicao_ativo")
    private LocalDate dataAquisicaoAtivo;
    
    @Column(name = "data_cadastro_ativo")
    private LocalDate dataCadastroAtivo;

    @Column(name = "campos_personalizados_ativo", length = Integer.MAX_VALUE)
    private String camposPersonalizadosAtivo;

    // Campos de AtivoTangivel
    @Column(name = "id_ativo_tangivel")
    private Long idAtivoTangivel;

    @Column(name = "garantia__ativo_tangivel")
    private LocalDate garantiaAtivoTangivel;

    @Column(name = "taxa_depreciacao_ativo_tangivel")
    private BigDecimal taxaDepreciacaoAtivoTangivel;

    @Size(max = 30)
    @Column(name = "periodo_depreciacao_ativo_tangivel", length = 30)
    private String periodoDepreciacaoAtivoTangivel;


    // Campos de Usuario
    @Column(name = "id_usuario")
    private Long idUsuario;

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

    // Campos de NotaFiscal
    @Column(name = "id_nota_fiscal")
    private Long idNotaFiscal;

    @Column(name = "documento_nota_fiscal", length = Integer.MAX_VALUE)
    private byte[] documentoNotaFiscal;

    @Size(max = 30)
    @Column(name = "tipo_documento_nota_fiscal", length = 30)
    private String tipoDocumentoNotaFiscal;

    @Column(name = "valor_residual", precision = 10, scale = 2)
    private BigDecimal valorResidual;
}