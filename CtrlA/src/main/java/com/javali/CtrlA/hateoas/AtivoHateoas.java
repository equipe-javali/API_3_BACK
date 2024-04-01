package com.javali.CtrlA.hateoas;

import com.javali.CtrlA.endpoint.AtivoControle;
import com.javali.CtrlA.entidades.Ativo;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AtivoHateoas implements Hateoas<Ativo> {

    @Override
    public void adicionarLink(List<Ativo> lista) {
        for (Ativo ativo : lista) {
            long id = ativo.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(AtivoControle.class)
                            .obterAtivo(id))
                    .withSelfRel();
            ativo.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(Ativo objeto) {
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(AtivoControle.class)
                        .obterAtivos())
                .withRel("ativos");
        objeto.add(linkProprio);
    }
}