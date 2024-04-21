package com.javali.CtrlA.endpoint;

import com.javali.CtrlA.entidades.Ativo;
import com.javali.CtrlA.entidades.Usuario;
import com.javali.CtrlA.hateoas.AtivoHateoas;
import com.javali.CtrlA.repositorios.AtivoRepositorio;
import com.javali.CtrlA.repositorios.UsuarioRepositorio;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ativo")
public class AtivoControle {

    @Autowired
    private AtivoRepositorio repositorio;

    @Autowired UsuarioRepositorio repoUsu;

    @Autowired
    private AtivoHateoas hateoas;

    @PostMapping("/cadastro")
    public ResponseEntity<Ativo> criarAtivo(@RequestBody Ativo novoAtivo) {
        Ativo ativo = repositorio.save(novoAtivo);
        hateoas.adicionarLink(ativo);
        return new ResponseEntity<>(ativo, HttpStatus.CREATED);
    }

    @GetMapping("/listagemTodos")
    public ResponseEntity<List<Ativo>> obterAtivos() {
        List<Ativo> ativos = repositorio.findAll();
        if (ativos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            hateoas.adicionarLink(ativos);
            return new ResponseEntity<>(ativos, HttpStatus.OK);
        }
    }

    @GetMapping("/listagem/{id}")
    public ResponseEntity<Ativo> obterAtivo(@PathVariable long id) {
        Optional<Ativo> ativoOptional = repositorio.findById(id);
        return ativoOptional.map(ativo -> {
            hateoas.adicionarLink(ativo);
            return new ResponseEntity<>(ativo, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/atualizacao/{id}")
    public ResponseEntity<?> atualizarAtivo(@PathVariable long id, @RequestBody Ativo ativoAtualizado) {
        return repositorio.findById(id)
                .map(ativo -> {
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
                        notNull.copyProperties(ativo, ativoAtualizado);
                    } catch (Exception e) {
                        System.out.println("Exception while updating asset: " + e.getMessage());
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    Ativo updatedAtivo = repositorio.save(ativo);
                    hateoas.adicionarLink(updatedAtivo);
                    return new ResponseEntity<>(updatedAtivo, HttpStatus.OK);
                }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/exclusao/{id}")
    public ResponseEntity<Void> deletarAtivo(@PathVariable long id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("associarAtivo/{id}")
    public ResponseEntity<?> associarAtivoUsuario(@PathVariable Long id, @RequestBody Long usuarioId){
        if (repositorio.existsById(id)) {
            Ativo ativo = repositorio.findById(id).get();
            if (repoUsu.existsById(usuarioId)) {
                ativo.setIdResponsavel(repoUsu.findById(usuarioId).get());
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            repositorio.save(ativo);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }


}