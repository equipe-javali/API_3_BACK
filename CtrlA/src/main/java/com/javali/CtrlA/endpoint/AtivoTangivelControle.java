
package com.javali.CtrlA.endpoint;

import com.javali.CtrlA.entidades.Ativo;
import com.javali.CtrlA.entidades.AtivoTangivel;
import com.javali.CtrlA.repositorios.AtivoRepositorio;
import com.javali.CtrlA.repositorios.AtivotangivelRepositorio;

import lombok.val;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ativoTangivel")
public class AtivoTangivelControle {

    @Autowired
    private AtivotangivelRepositorio repositorio;

    @Autowired
    private AtivoRepositorio ativoRepositorio;

    @PostMapping(value = "/cadastro", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AtivoTangivel> criarAtivoTangivel(@RequestBody AtivoTangivel novoAtivoTangivel) {
        AtivoTangivel ativoTangivel = repositorio.save(novoAtivoTangivel);
        return new ResponseEntity<>(ativoTangivel, HttpStatus.CREATED);
    }

    @GetMapping("/listagemTodos")
    public ResponseEntity<List<AtivoTangivel>> obterAtivoTangivels() {
        List<AtivoTangivel> ativoTangivels = repositorio.findAll();
        if (ativoTangivels.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(ativoTangivels, HttpStatus.OK);
        }
    }

    @GetMapping("/listagem/{id}")
    public ResponseEntity<AtivoTangivel> obterAtivoTangivel(@PathVariable long id) {
        Optional<AtivoTangivel> ativoTangivelOptional = repositorio.findById(id);
        return ativoTangivelOptional.map(ativoTangivel -> new ResponseEntity<>(ativoTangivel, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/atualizacao/{id}")
    public ResponseEntity<?> atualizarAtivoTangivel(@PathVariable long id, @RequestBody AtivoTangivel ativoTangivelAtualizado) {
        return repositorio.findById(id)
                .map(ativoTangivel -> {
                    Ativo ativo = ativoRepositorio.findById(ativoTangivelAtualizado.getAtivo().getId())
                            .orElseThrow(() -> new RuntimeException("Ativo not found with id " + ativoTangivelAtualizado.getAtivo().getId()));
                    ativoTangivelAtualizado.setAtivo(ativo);

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
                        notNull.copyProperties(ativoTangivel, ativoTangivelAtualizado);
                    } catch (Exception e) {
                        System.out.println("Exception while updating tangible asset: " + e.getMessage());
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    AtivoTangivel updatedAtivoTangivel = repositorio.save(ativoTangivel);
                    return new ResponseEntity<>(updatedAtivoTangivel, HttpStatus.OK);
                }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/exclusao/{id}")
    public ResponseEntity<Void> deletarAtivoTangivel(@PathVariable long id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}