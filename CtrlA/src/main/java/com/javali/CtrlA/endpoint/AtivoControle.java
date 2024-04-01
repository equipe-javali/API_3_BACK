package com.javali.CtrlA.endpoint;

import com.javali.CtrlA.entidades.Ativo;
import com.javali.CtrlA.hateoas.AtivoHateoas;
import com.javali.CtrlA.repositorios.AtivoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class AtivoControle {

    @Autowired
    private AtivoRepositorio repositorio;

    @Autowired
    private AtivoHateoas hateoas;

    @PostMapping("/ativo")
    public ResponseEntity<Ativo> criarAtivo(@RequestBody Ativo novoAtivo) {
        Ativo ativo = repositorio.save(novoAtivo);
        hateoas.adicionarLink(ativo);
        return new ResponseEntity<>(ativo, HttpStatus.CREATED);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<Ativo>> obterAtivos() {
        List<Ativo> ativos = repositorio.findAll();
        if (ativos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            hateoas.adicionarLink(ativos);
            return new ResponseEntity<>(ativos, HttpStatus.OK);
        }
    }

    @GetMapping("/ativo/{id}")
    public ResponseEntity<Ativo> obterAtivo(@PathVariable long id) {
        Optional<Ativo> ativoOptional = repositorio.findById(id);
        return ativoOptional.map(ativo -> {
            hateoas.adicionarLink(ativo);
            return new ResponseEntity<>(ativo, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/ativo/{id}")
    public ResponseEntity<Ativo> atualizarAtivo(@PathVariable long id, @RequestBody Ativo ativoAtualizado) {
        return repositorio.findById(id)
                .map(ativo -> {
                    // Assuming Ativo has setters for its fields
                    // Replace with actual field setters
                    // ativo.setField(ativoAtualizado.getField());
                    repositorio.save(ativo);
                    hateoas.adicionarLink(ativo);
                    return new ResponseEntity<>(ativo, HttpStatus.OK);
                }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/ativo/{id}")
    public ResponseEntity<Void> deletarAtivo(@PathVariable long id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}