package com.javali.CtrlA.entidades;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;
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

    @Column(name = "data_cadastro")
    private Instant dataCadastro;

    @Column(name = "data_nascimento")
    private Instant dataNascimento;

    @Size(max = 255)
    @Column(name = "nome_social")
    private String nomeSocial;

    @JsonManagedReference
    @OneToMany(mappedBy = "idResponsavel")
    private Set<Ativo> ativos = new LinkedHashSet<>();

    @JsonManagedReference
    @OneToOne(mappedBy = "usuario")
    private UsuarioLogin usuariologin;

}