package com.javali.CtrlA.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "manutencao")
public class Manutencao extends RepresentationModel<Manutencao> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "id_ativo", nullable = false)
    private Ativo ativo;
    
    @Column(name = "data_inicio")
    private LocalDate dataInicio;
    
    @Column(name = "data_fim")
    private LocalDate dataFim;
    
    @Column(name = "custo", precision = 10, scale = 2)
    private BigDecimal custo;
    
    @Column(name = "tipo")
    private Integer tipo;

    @Size(max = 200)
    @Column(name = "descricao")
    private String descricao;


    @Size(max = 100)
    @Column(name = "localizacao")
    private String localizacao;
    
    @OneToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "id_nota_fiscal", nullable = false)
    private NotaFiscal idNotaFiscal;
}