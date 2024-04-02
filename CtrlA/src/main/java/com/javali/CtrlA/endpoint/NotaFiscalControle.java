package com.javali.CtrlA.endpoint;

import com.javali.CtrlA.entidades.NotaFiscal;
import com.javali.CtrlA.repositorios.NotaFiscalRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class NotaFiscalControle {

    @Autowired
    private NotaFiscalRepositorio repositorio;

    @PostMapping("/notafiscal")
    public ResponseEntity<NotaFiscal> criarNotaFiscal(@RequestBody NotaFiscal novaNotaFiscal) {
        NotaFiscal notaFiscal = repositorio.save(novaNotaFiscal);
        return new ResponseEntity<>(notaFiscal, HttpStatus.CREATED);
    }

    @GetMapping("/notasfiscais")
    public ResponseEntity<List<NotaFiscal>> obterNotasFiscais() {
        List<NotaFiscal> notasFiscais = repositorio.findAll();
        if (notasFiscais.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(notasFiscais, HttpStatus.OK);
        }
    }

    @GetMapping("/notafiscal/{id}")
    public ResponseEntity<NotaFiscal> obterNotaFiscal(@PathVariable long id) {
        Optional<NotaFiscal> notaFiscalOptional = repositorio.findById(id);
        return notaFiscalOptional.map(notaFiscal -> new ResponseEntity<>(notaFiscal, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/notafiscal/{id}")
    public ResponseEntity<NotaFiscal> atualizarNotaFiscal(@PathVariable long id, @RequestBody NotaFiscal notaFiscalAtualizada) {
        return repositorio.findById(id)
                .map(notaFiscal -> {
                    repositorio.save(notaFiscal);
                    return new ResponseEntity<>(notaFiscal, HttpStatus.OK);
                }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/notafiscal/{id}")
    public ResponseEntity<Void> deletarNotaFiscal(@PathVariable long id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}