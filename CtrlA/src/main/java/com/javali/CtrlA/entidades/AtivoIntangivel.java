package com.javali.CtrlA.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.javali.CtrlA.entidades.Ativo;
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
@Table(name = "ativointangivel")
public class AtivoIntangivel extends RepresentationModel<AtivoIntangivel> {
    @Id
    @Column(name = "id_ativo", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ativo", nullable = false)
    private Ativo ativo;

    @Column(name = "data_expiracao")
    private LocalDate dataExpiracao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_responsavel")
    private Ativo idResponsavel;

    @Column(name = "taxa_amortizacao", precision = 10, scale = 2)
    private BigDecimal taxaAmortizacao;

    @Size(max = 30)
    @Column(name = "periodo_amortizacao", length = 30)
    private String periodoAmortizacao;

}