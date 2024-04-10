package com.javali.CtrlA.hateoas;

import com.javali.CtrlA.endpoint.AtivoTangivelControle;
import com.javali.CtrlA.entidades.AtivoTangivel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AtivoTangivelHateoas implements Hateoas<AtivoTangivel> {

    @Override
    public void adicionarLink(List<AtivoTangivel> lista) {
        for (AtivoTangivel ativoTangivel : lista) {
            long id = ativoTangivel.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(AtivoTangivelControle.class)
                            .obterAtivoTangivel(id))
                    .withSelfRel();
            ativoTangivel.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(AtivoTangivel objeto) {
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(AtivoTangivelControle.class)
                        .obterAtivoTangivels())
                .withRel("ativotangivels");
        objeto.add(linkProprio);
    }
}