//package com.javali.CtrlA.hateoas;
//
//import java.util.List;
//
//import org.springframework.hateoas.Link;
//import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
//import org.springframework.stereotype.Component;
//
//import com.javali.CtrlA.endpoint.ManutencaoControle;
//import com.javali.CtrlA.entidades.Manutencao;
//
//@Component
//public class ManutencaoHateoas implements Hateoas<Manutencao> {
//
//    @Override
//    public void adicionarLink(List<Manutencao> lista) {
//        for (Manutencao usuario : lista) {
//            long id = usuario.getId();
//            Link linkProprio = WebMvcLinkBuilder
//                    .linkTo(WebMvcLinkBuilder
//                            .methodOn(ManutencaoControle.class)
//                            .obterManutencao(id))
//                    .withSelfRel();
//            usuario.add(linkProprio);
//        }
//    }
//
//    @Override
//    public void adicionarLink(Manutencao objeto) {
//        Link linkProprio = WebMvcLinkBuilder
//                .linkTo(WebMvcLinkBuilder
//                        .methodOn(ManutencaoControle.class)
//                        .obterManutencoes())
//                .withRel("manutencoes");
//        objeto.add(linkProprio);
//    }
//}