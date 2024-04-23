package com.javali.CtrlA.endpoint;

import com.javali.CtrlA.entidades.HistoricoAtivoIntangivel;
import com.javali.CtrlA.repositorios.HistoricoAtivoIntangivelRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/historicoAtivoIntangivel")
public class HistoricoAtivoIntangivelControle {

    @Autowired
    private HistoricoAtivoIntangivelRepositorio repositorio;

    @PostMapping("/cadastro")
    public ResponseEntity<HistoricoAtivoIntangivel> criarHistoricoAtivoIntangivel(@RequestBody HistoricoAtivoIntangivel novoHistoricoAtivoIntangivel) {
        HistoricoAtivoIntangivel historicoAtivoIntangivel = repositorio.save(novoHistoricoAtivoIntangivel);
        return new ResponseEntity<>(historicoAtivoIntangivel, HttpStatus.CREATED);
    }

    @GetMapping("/listagemTodos")
    public ResponseEntity<List<HistoricoAtivoIntangivel>> obterHistoricoAtivoIntangivels() {
        List<HistoricoAtivoIntangivel> historicoAtivoIntangivels = repositorio.findAll();
        if (historicoAtivoIntangivels.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(historicoAtivoIntangivels, HttpStatus.OK);
        }
    }

    @GetMapping("/listagem/{id}")
    public ResponseEntity<HistoricoAtivoIntangivel> obterHistoricoAtivoIntangivel(@PathVariable long id) {
        Optional<HistoricoAtivoIntangivel> historicoAtivoIntangivelOptional = repositorio.findById(id);
        return historicoAtivoIntangivelOptional.map(historicoAtivoIntangivel -> new ResponseEntity<>(historicoAtivoIntangivel, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/atualizacao/{id}")
    public ResponseEntity<HistoricoAtivoIntangivel> atualizarHistoricoAtivoIntangivel(@PathVariable long id, @RequestBody HistoricoAtivoIntangivel historicoAtivoIntangivelAtualizado) {
        return repositorio.findById(id)
                .map(historicoAtivoIntangivel -> {
                    repositorio.save(historicoAtivoIntangivel);
                    return new ResponseEntity<>(historicoAtivoIntangivel, HttpStatus.OK);
                }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/exclusao/{id}")
    public ResponseEntity<Void> deletarHistoricoAtivoIntangivel(@PathVariable long id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}