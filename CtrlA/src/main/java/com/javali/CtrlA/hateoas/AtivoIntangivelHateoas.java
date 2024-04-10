package com.javali.CtrlA.hateoas;

import com.javali.CtrlA.endpoint.AtivoIntangivelControle;
import com.javali.CtrlA.entidades.AtivoIntangivel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AtivoIntangivelHateoas implements Hateoas<AtivoIntangivel> {

    @Override
    public void adicionarLink(List<AtivoIntangivel> lista) {
        for (AtivoIntangivel ativoIntangivel : lista) {
            long id = ativoIntangivel.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(AtivoIntangivelControle.class)
                            .obterAtivoIntangivel(id))
                    .withSelfRel();
            ativoIntangivel.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(AtivoIntangivel objeto) {
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(AtivoIntangivelControle.class)
                        .obterAtivoIntangivels())
                .withRel("ativointangivels");
        objeto.add(linkProprio);
    }
}