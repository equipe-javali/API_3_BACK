package com.javali.CtrlA.entidades;

import com.fasterxml.jackson.annotation.*;
import com.javali.CtrlA.entidades.Ativo;
import com.javali.CtrlA.entidades.UsuarioLogin;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario extends RepresentationModel<Usuario> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 100)
    @Column(name = "nome", length = 100)
    private String nome;

    @Size(max = 11)
    @Column(name = "cpf", length = 11)
    private String cpf;

    @Column(name = "nascimento")
    private LocalDate nascimento;

    @Size(max = 20)
    @Column(name = "departamento", length = 20)
    private String departamento;

    @Size(max = 20)
    @Column(name = "telefone", length = 20)
    private String telefone;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

//    @JsonManagedReference
    @JsonBackReference
    @OneToMany(mappedBy = "idResponsavel")
    private Set<Ativo> ativos = new LinkedHashSet<>();

    @JsonManagedReference
    @OneToOne(mappedBy = "usuario")
    private UsuarioLogin usuariologin;

}