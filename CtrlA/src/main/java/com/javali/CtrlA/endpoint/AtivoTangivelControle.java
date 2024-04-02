package com.javali.CtrlA.endpoint;

import com.javali.CtrlA.entidades.AtivoTangivel;
import com.javali.CtrlA.repositorios.AtivoTangivelRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class AtivoTangivelControle {

    @Autowired
    private AtivoTangivelRepositorio repositorio;

    @PostMapping("/ativotangivel")
    public ResponseEntity<AtivoTangivel> criarAtivoTangivel(@RequestBody AtivoTangivel novoAtivoTangivel) {
        AtivoTangivel ativoTangivel = repositorio.save(novoAtivoTangivel);
        return new ResponseEntity<>(ativoTangivel, HttpStatus.CREATED);
    }

    @GetMapping("/ativotangivels")
    public ResponseEntity<List<AtivoTangivel>> obterAtivoTangivels() {
        List<AtivoTangivel> ativoTangivels = repositorio.findAll();
        if (ativoTangivels.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(ativoTangivels, HttpStatus.OK);
        }
    }

    @GetMapping("/ativotangivel/{id}")
    public ResponseEntity<AtivoTangivel> obterAtivoTangivel(@PathVariable long id) {
        Optional<AtivoTangivel> ativoTangivelOptional = repositorio.findById(id);
        return ativoTangivelOptional.map(ativoTangivel -> new ResponseEntity<>(ativoTangivel, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/ativotangivel/{id}")
    public ResponseEntity<AtivoTangivel> atualizarAtivoTangivel(@PathVariable long id, @RequestBody AtivoTangivel ativoTangivelAtualizado) {
        return repositorio.findById(id)
                .map(ativoTangivel -> {
                    repositorio.save(ativoTangivel);
                    return new ResponseEntity<>(ativoTangivel, HttpStatus.OK);
                }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/ativotangivel/{id}")
    public ResponseEntity<Void> deletarAtivoTangivel(@PathVariable long id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}