package com.javali.CtrlA.endpoint;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javali.CtrlA.entidades.Manutencao;
//import com.javali.CtrlA.hateoas.ManutencaoHateoas;
import com.javali.CtrlA.repositorios.ManutencaoRepositorio;

@RestController
@RequestMapping("/manutencao")
public class ManutencaoControle {
    @Autowired
    private ManutencaoRepositorio repositorio;

//    @Autowired
//    private ManutencaoHateoas hateoas;

    @PostMapping("/cadastro")
    public ResponseEntity<Manutencao> criarManutencao(@RequestBody Manutencao novaManutencao) {
    	Manutencao manutencao = repositorio.save(novaManutencao);
//        hateoas.adicionarLink(manutencao);
        return new ResponseEntity<>(manutencao, HttpStatus.CREATED);
    }

    @GetMapping("/listagemTodos")
    public ResponseEntity<List<Manutencao>> listarTodasManutencoes() {
        List<Manutencao> todasManutencoes = repositorio.findAll();

        if (todasManutencoes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
//        hateoas.adicionarLink(todasManutencoes);
            return new ResponseEntity<>(todasManutencoes, HttpStatus.OK);
        }
    }


    @GetMapping("/listagem/{id_ativo}")
    public ResponseEntity<List<Manutencao>> obterManutencoes(@PathVariable long id_ativo) {
        List<Manutencao> todasManutencoes = repositorio.findAll();

        List<Manutencao> manutencoes = new ArrayList<Manutencao>();
        for (Manutencao m : todasManutencoes) {
            if (m.getAtivo() != null && m.getAtivo().getId() == id_ativo) {
                manutencoes.add(m);
            }
        }

        if (manutencoes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
//        hateoas.adicionarLink(manutencoes);
            return new ResponseEntity<>(manutencoes, HttpStatus.OK);
        }
    }

    @PutMapping("/atualizacao/{id}")
    public ResponseEntity<?> atualizarManutencao(@PathVariable long id, @RequestBody Manutencao manutencaoAtualizado) {
        return repositorio.findById(id)
                .map(manutencao -> {
                    BeanUtilsBean notNull=new BeanUtilsBean(){
                        @Override
                        public void copyProperty(Object dest, String name, Object value)
                                throws IllegalAccessException, InvocationTargetException {
                            if(value!=null){
                                super.copyProperty(dest, name, value);
                            }
                        }
                    };
                    try {
                        notNull.copyProperties(manutencao, manutencaoAtualizado);
                    } catch (Exception e) {
                        System.out.println("Exception while updating manutencao: " + e.getMessage());
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    Manutencao updatedManutencao = repositorio.save(manutencao);
//                    hateoas.adicionarLink(updatedManutencao);
                    return new ResponseEntity<>(updatedManutencao, HttpStatus.OK);
                }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/exclusao/{id}")
    public ResponseEntity<Void> deletarManutencao(@PathVariable long id) {
        Optional<Manutencao> manutencaoOptional = repositorio.findById(id);
        if (!manutencaoOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

//        Manutencao manutencao = manutencaoOptional.get();
//        if (manutencao.getManutencaologin() != null) {
//            manutencao.getManutencaologin().setManutencao(null);
//            manutencao.setManutencaologin(null);
//            repositorio.save(manutencao);
//        }

        try {
            repositorio.deleteById(id);
            if (repositorio.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            System.out.println("Exception while deleting manutencao: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}