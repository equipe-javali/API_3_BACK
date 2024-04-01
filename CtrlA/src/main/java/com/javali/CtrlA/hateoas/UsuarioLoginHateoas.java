package com.javali.CtrlA.hateoas;

import com.javali.CtrlA.endpoint.UsuarioLoginControle;
import com.javali.CtrlA.entidades.UsuarioLogin;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioLoginHateoas implements Hateoas<UsuarioLogin> {

    @Override
    public void adicionarLink(List<UsuarioLogin> lista) {
        for (UsuarioLogin usuarioLogin : lista) {
            long id = usuarioLogin.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(UsuarioLoginControle.class)
                            .obterUsuariologinPorId(id))
                    .withSelfRel();
            usuarioLogin.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(UsuarioLogin objeto) {
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(UsuarioLoginControle.class)
                        .obterTodosUsuariologin())
                .withRel("usuariologins");
        objeto.add(linkProprio);
    }
}