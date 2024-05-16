package com.javali.CtrlA.endpoint;

import com.javali.CtrlA.entidades.HistoricoAtivoIntangivel;
import com.javali.CtrlA.repositorios.HistoricoAtivoIntangivelRepositorio;
import com.javali.CtrlA.servicos.HistoricoAtivoIntangivelServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.annotation.DependsOn;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/historicoAtivoIntangivel")
@DependsOn("historicoAtivoIntangivelServico")
public class HistoricoAtivoIntangivelControle {

    @Autowired
    private HistoricoAtivoIntangivelRepositorio repositorio;

    @Autowired
    private HistoricoAtivoIntangivelServico historicoServico;

    @PostMapping("/cadastro")
    public ResponseEntity<HistoricoAtivoIntangivel> criarHistoricoAtivoIntangivel(@RequestBody HistoricoAtivoIntangivelRequest request) {
        HistoricoAtivoIntangivel historicoAtivoIntangivel = historicoServico.createHistorico(
                request.getIdAtivo(),
                request.getIdAtivoIntangivel(),
                request.getIdNotaFiscal(),
                request.getIdUsuario()
        );
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