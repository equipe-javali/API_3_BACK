package com.javali.CtrlA.hateoas;

import com.javali.CtrlA.endpoint.NotaFiscalControle;
import com.javali.CtrlA.entidades.NotaFiscal;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotaFiscalHateoas implements Hateoas<NotaFiscal> {

    @Override
    public void adicionarLink(List<NotaFiscal> lista) {
        for (NotaFiscal notaFiscal : lista) {
            long id = notaFiscal.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(NotaFiscalControle.class)
                            .obterNotaFiscal(id))
                    .withSelfRel();
            notaFiscal.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(NotaFiscal objeto) {
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(NotaFiscalControle.class)
                        .obterNotasFiscais())
                .withRel("notasfiscais");
        objeto.add(linkProprio);
    }
}