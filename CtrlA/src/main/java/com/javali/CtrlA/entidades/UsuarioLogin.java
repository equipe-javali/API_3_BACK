package com.javali.CtrlA.entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.javali.CtrlA.entidades.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@Entity
@Table(name = "usuariologin")
public class UsuarioLogin extends RepresentationModel<UsuarioLogin> {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id", nullable = false)
    @JsonBackReference
    private Usuario usuario;

    @Size(max = 60)
    @NotNull
    @Column(name = "senha", nullable = false, length = 60)
    private String senha;

}