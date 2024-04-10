package com.javali.CtrlA.hateoas;

import com.javali.CtrlA.endpoint.HistoricoAtivoTangivelControle;
import com.javali.CtrlA.entidades.HistoricoAtivoTangivel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HistoricoAtivoTangivelHateoas implements Hateoas<HistoricoAtivoTangivel> {

    @Override
    public void adicionarLink(List<HistoricoAtivoTangivel> lista) {
        for (HistoricoAtivoTangivel historicoAtivoTangivel : lista) {
            long id = historicoAtivoTangivel.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(HistoricoAtivoTangivelControle.class)
                            .obterHistoricoAtivoTangivel(id))
                    .withSelfRel();
            historicoAtivoTangivel.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(HistoricoAtivoTangivel objeto) {
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(HistoricoAtivoTangivelControle.class)
                        .obterHistoricoAtivoTangivels())
                .withRel("historicoativotangivels");
        objeto.add(linkProprio);
    }
}