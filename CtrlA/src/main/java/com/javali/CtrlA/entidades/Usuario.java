package com.javali.CtrlA.entidades;

import com.fasterxml.jackson.annotation.*;
import com.javali.CtrlA.modelo.Perfil;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@EnableJpaRepositories("com.javali.CtrlA.repositorios")
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

    @Size(max = 100)
    @Column(name = "status", length = 100)
    private String status;

//    @JsonManagedReference
//    @JsonBackReference
//    @OneToMany(mappedBy = "idResponsavel")
//    private Set<Ativo> ativos = new LinkedHashSet<>();

    @JsonManagedReference
    @OneToOne(mappedBy = "usuario", cascade=CascadeType.PERSIST)
    private UsuarioLogin usuariologin;
    
    @Column
    private Perfil perfil;

}
