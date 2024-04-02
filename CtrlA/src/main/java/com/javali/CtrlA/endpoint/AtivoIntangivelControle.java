package com.javali.CtrlA.endpoint;

import com.javali.CtrlA.entidades.AtivoIntangivel;
import com.javali.CtrlA.repositorios.AtivoIntangivelRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class AtivoIntangivelControle {

    @Autowired
    private AtivoIntangivelRepositorio repositorio;

    @PostMapping("/ativointangivel")
    public ResponseEntity<AtivoIntangivel> criarAtivoIntangivel(@RequestBody AtivoIntangivel novoAtivoIntangivel) {
        AtivoIntangivel ativoIntangivel = repositorio.save(novoAtivoIntangivel);
        return new ResponseEntity<>(ativoIntangivel, HttpStatus.CREATED);
    }

    @GetMapping("/ativointangivels")
    public ResponseEntity<List<AtivoIntangivel>> obterAtivoIntangivels() {
        List<AtivoIntangivel> ativoIntangivels = repositorio.findAll();
        if (ativoIntangivels.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(ativoIntangivels, HttpStatus.OK);
        }
    }

    @GetMapping("/ativointangivel/{id}")
    public ResponseEntity<AtivoIntangivel> obterAtivoIntangivel(@PathVariable long id) {
        Optional<AtivoIntangivel> ativoIntangivelOptional = repositorio.findById(id);
        return ativoIntangivelOptional.map(ativoIntangivel -> new ResponseEntity<>(ativoIntangivel, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/ativointangivel/{id}")
    public ResponseEntity<AtivoIntangivel> atualizarAtivoIntangivel(@PathVariable long id, @RequestBody AtivoIntangivel ativoIntangivelAtualizado) {
        return repositorio.findById(id)
                .map(ativoIntangivel -> {
                    repositorio.save(ativoIntangivel);
                    return new ResponseEntity<>(ativoIntangivel, HttpStatus.OK);
                }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/ativointangivel/{id}")
    public ResponseEntity<Void> deletarAtivoIntangivel(@PathVariable long id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}