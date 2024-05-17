package com.javali.CtrlA.endpoint;

import com.javali.CtrlA.entidades.*;
import com.javali.CtrlA.repositorios.AtivointangivelRepositorio;
import com.javali.CtrlA.repositorios.AtivotangivelRepositorio;
import com.javali.CtrlA.repositorios.HistoricoAtivoIntangivelRepositorio;
import com.javali.CtrlA.repositorios.HistoricoAtivoTangivelRepositorio;
import com.javali.CtrlA.servicos.HistoricoAtivoIntangivelServico;
import com.javali.CtrlA.servicos.HistoricoAtivoTangivelServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/historicoAtivoTangivel")
public class HistoricoAtivoTangivelControle {

    @Autowired
    private HistoricoAtivoTangivelServico historicoServico;

    @Autowired
    private AtivotangivelRepositorio ativoTangivelRepositorio;

    @Autowired
    private HistoricoAtivoTangivelRepositorio repositorio;

    @PostMapping("/cadastro")
    public ResponseEntity<HistoricoAtivoTangivel> criarHistoricoAtivoTangivel(@RequestBody HistoricoAtivoTangivelRequest request) {
        return historicoServico.createHistorico(request);
    }

    @GetMapping("/listagemTodos")
    public ResponseEntity<List<HistoricoAtivoTangivel>> obterHistoricoAtivoTangivels() {
        List<HistoricoAtivoTangivel> historicoAtivoTangivels = repositorio.findAll();
        if (historicoAtivoTangivels.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(historicoAtivoTangivels, HttpStatus.OK);
        }
    }

    @GetMapping("/listagem/{id}")
    public ResponseEntity<HistoricoAtivoTangivel> obterHistoricoAtivoTangivel(@PathVariable long id) {
        Optional<HistoricoAtivoTangivel> historicoAtivoTangivelOptional = repositorio.findById(id);
        return historicoAtivoTangivelOptional.map(historicoAtivoTangivel -> new ResponseEntity<>(historicoAtivoTangivel, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/atualizacao/{id}")
    public ResponseEntity<HistoricoAtivoTangivel> atualizarHistoricoAtivoTangivel(@PathVariable long id, @RequestBody HistoricoAtivoTangivel historicoAtivoTangivelAtualizado) {
        return repositorio.findById(id)
                .map(historicoAtivoTangivel -> {
                    repositorio.save(historicoAtivoTangivel);
                    return new ResponseEntity<>(historicoAtivoTangivel, HttpStatus.OK);
                }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/exclusao/{id}")
    public ResponseEntity<Void> deletarHistoricoAtivoTangivel(@PathVariable long id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}