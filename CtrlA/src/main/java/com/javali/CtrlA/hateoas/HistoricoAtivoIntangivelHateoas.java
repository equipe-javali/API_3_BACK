package com.javali.CtrlA.hateoas;

import com.javali.CtrlA.endpoint.HistoricoAtivoIntangivelControle;
import com.javali.CtrlA.entidades.HistoricoAtivoIntangivel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HistoricoAtivoIntangivelHateoas implements Hateoas<HistoricoAtivoIntangivel> {

    @Override
    public void adicionarLink(List<HistoricoAtivoIntangivel> lista) {
        for (HistoricoAtivoIntangivel historicoAtivoIntangivel : lista) {
            long id = historicoAtivoIntangivel.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(HistoricoAtivoIntangivelControle.class)
                            .obterHistoricoAtivoIntangivel(id))
                    .withSelfRel();
            historicoAtivoIntangivel.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(HistoricoAtivoIntangivel objeto) {
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(HistoricoAtivoIntangivelControle.class)
                        .obterHistoricoAtivoIntangivels())
                .withRel("historicoativointangivels");
        objeto.add(linkProprio);
    }
}