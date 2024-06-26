package com.javali.CtrlA.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "ativotangivel")
public class AtivoTangivel extends RepresentationModel<AtivoTangivel> {
    @Id
    @Column(name = "id_ativo", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ativo", nullable = false)
    private Ativo ativo;

    @Column(name = "garantia")
    private LocalDate garantia;

    @Column(name = "taxa_depreciacao")
    private BigDecimal taxaDepreciacao;

    @Size(max = 30)
    @Column(name = "periodo_depreciacao", length = 30)
    private String periodoDepreciacao;
}