package com.javali.CtrlA.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@Entity
@Table(name = "notafiscal")
public class NotaFiscal extends RepresentationModel<NotaFiscal> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 50)
    @Column(name = "nome", length = 50)
    private String nome;
    
    @Size(max = 30)
    @Column(name = "tipo_documento", length = 30)
    private String tipoDocumento;
    
    @Column(name = "documento", length = Integer.MAX_VALUE)
    private byte[] documento;
}