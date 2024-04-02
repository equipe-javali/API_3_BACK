package com.javali.CtrlA.endpoint;

import com.javali.CtrlA.entidades.HistoricoAtivoTangivel;
import com.javali.CtrlA.repositorios.HistoricoAtivoTangivelRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class HistoricoAtivoTangivelControle {

    @Autowired
    private HistoricoAtivoTangivelRepositorio repositorio;

    @PostMapping("/historicoativotangivel")
    public ResponseEntity<HistoricoAtivoTangivel> criarHistoricoAtivoTangivel(@RequestBody HistoricoAtivoTangivel novoHistoricoAtivoTangivel) {
        HistoricoAtivoTangivel historicoAtivoTangivel = repositorio.save(novoHistoricoAtivoTangivel);
        return new ResponseEntity<>(historicoAtivoTangivel, HttpStatus.CREATED);
    }

    @GetMapping("/historicoativotangivels")
    public ResponseEntity<List<HistoricoAtivoTangivel>> obterHistoricoAtivoTangivels() {
        List<HistoricoAtivoTangivel> historicoAtivoTangivels = repositorio.findAll();
        if (historicoAtivoTangivels.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(historicoAtivoTangivels, HttpStatus.OK);
        }
    }

    @GetMapping("/historicoativotangivel/{id}")
    public ResponseEntity<HistoricoAtivoTangivel> obterHistoricoAtivoTangivel(@PathVariable long id) {
        Optional<HistoricoAtivoTangivel> historicoAtivoTangivelOptional = repositorio.findById(id);
        return historicoAtivoTangivelOptional.map(historicoAtivoTangivel -> new ResponseEntity<>(historicoAtivoTangivel, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/historicoativotangivel/{id}")
    public ResponseEntity<HistoricoAtivoTangivel> atualizarHistoricoAtivoTangivel(@PathVariable long id, @RequestBody HistoricoAtivoTangivel historicoAtivoTangivelAtualizado) {
        return repositorio.findById(id)
                .map(historicoAtivoTangivel -> {
                    repositorio.save(historicoAtivoTangivel);
                    return new ResponseEntity<>(historicoAtivoTangivel, HttpStatus.OK);
                }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/historicoativotangivel/{id}")
    public ResponseEntity<Void> deletarHistoricoAtivoTangivel(@PathVariable long id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}