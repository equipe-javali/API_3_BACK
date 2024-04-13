package com.javali.CtrlA.endpoint;

import com.javali.CtrlA.entidades.Ativo;
import com.javali.CtrlA.entidades.AtivoIntangivel;
import com.javali.CtrlA.repositorios.AtivoRepositorio;
import com.javali.CtrlA.repositorios.AtivointangivelRepositorio;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ativoIntangivel")
public class AtivoIntangivelControle {

    @Autowired
    private AtivointangivelRepositorio repositorio;

    @Autowired
    private AtivoRepositorio ativoRepositorio;

    @PostMapping("/cadastro")
    public ResponseEntity<AtivoIntangivel> criarAtivoIntangivel(@RequestBody AtivoIntangivel novoAtivoIntangivel) {
        AtivoIntangivel ativoIntangivel = repositorio.save(novoAtivoIntangivel);
        return new ResponseEntity<>(ativoIntangivel, HttpStatus.CREATED);
    }

    @GetMapping("/listagemTodos")
    public ResponseEntity<List<AtivoIntangivel>> obterAtivoIntangivels() {
        List<AtivoIntangivel> ativoIntangivels = repositorio.findAll();
        if (ativoIntangivels.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(ativoIntangivels, HttpStatus.OK);
        }
    }

    @GetMapping("/listagem/{id}")
    public ResponseEntity<AtivoIntangivel> obterAtivoIntangivel(@PathVariable long id) {
        Optional<AtivoIntangivel> ativoIntangivelOptional = repositorio.findById(id);
        return ativoIntangivelOptional.map(ativoIntangivel -> new ResponseEntity<>(ativoIntangivel, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/atualizacao/{id}")
    public ResponseEntity<?> atualizarAtivoIntangivel(@PathVariable long id, @RequestBody AtivoIntangivel ativoIntangivelAtualizado) {
        return repositorio.findById(id)
                .map(ativoIntangivel -> {
                    Ativo ativo = ativoRepositorio.findById(ativoIntangivelAtualizado.getAtivo().getId())
                            .orElseThrow(() -> new RuntimeException("Ativo not found with id " + ativoIntangivelAtualizado.getAtivo().getId()));
                    ativoIntangivelAtualizado.setAtivo(ativo);

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
                        notNull.copyProperties(ativoIntangivel, ativoIntangivelAtualizado);
                    } catch (Exception e) {
                        System.out.println("Exception while updating intangible asset: " + e.getMessage());
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    AtivoIntangivel updatedAtivoIntangivel = repositorio.save(ativoIntangivel);
                    return new ResponseEntity<>(updatedAtivoIntangivel, HttpStatus.OK);
                }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/exclusao/{id}")
    public ResponseEntity<Void> deletarAtivoIntangivel(@PathVariable long id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}