
package com.javali.CtrlA.endpoint;

import com.javali.CtrlA.entidades.Ativo;
import com.javali.CtrlA.entidades.AtivoTangivel;
import com.javali.CtrlA.entidades.NotaFiscal;
import com.javali.CtrlA.entidades.Usuario;
import com.javali.CtrlA.repositorios.AtivoRepositorio;
import com.javali.CtrlA.repositorios.AtivotangivelRepositorio;

import com.javali.CtrlA.repositorios.NotaFiscalRepositorio;
import com.javali.CtrlA.repositorios.UsuarioRepositorio;
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
    @Autowired
    private NotaFiscalRepositorio notaFiscalRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

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
                    Ativo ativo = ativoRepositorio.findById(ativoTangivel.getAtivo().getId())
                            .orElseThrow(() -> new RuntimeException("Ativo not found with id " + ativoTangivel.getAtivo().getId()));

                    // Fetch the Usuario and NotaFiscal entities from the database if idResponsavel and idNotaFiscal are not null
                    if (ativoTangivelAtualizado.getAtivo().getIdResponsavel() != null) {
                        Usuario usuario = usuarioRepositorio.findById(ativoTangivelAtualizado.getAtivo().getIdResponsavel().getId())
                                .orElseThrow(() -> new RuntimeException("Usuario not found with id " + ativoTangivelAtualizado.getAtivo().getIdResponsavel().getId()));
                        ativo.setIdResponsavel(usuario);
                    }
                    if (ativoTangivelAtualizado.getAtivo().getIdNotaFiscal() != null) {
                        NotaFiscal notaFiscal = notaFiscalRepositorio.findById(ativoTangivelAtualizado.getAtivo().getIdNotaFiscal().getId())
                                .orElseThrow(() -> new RuntimeException("NotaFiscal not found with id " + ativoTangivelAtualizado.getAtivo().getIdNotaFiscal().getId()));
                        ativo.setIdNotaFiscal(notaFiscal);
                    }

                    // Create a custom BeanUtilsBean that ignores null properties
                    BeanUtilsBean notNull = new BeanUtilsBean() {
                        @Override
                        public void copyProperty(Object dest, String name, Object value)
                                throws IllegalAccessException, InvocationTargetException {
                            if (value == null) return;
                            super.copyProperty(dest, name, value);
                        }
                    };

                    // Copy the properties from ativoTangivelAtualizado.getAtivo() to ativo
                    try {
                        notNull.copyProperties(ativo, ativoTangivelAtualizado.getAtivo());
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException("Error copying properties", e);
                    }

                    // Save the Ativo entity
                    ativo = ativoRepositorio.save(ativo);

                    // Create a new AtivoTangivel object and set the updated Ativo entity to it
                    AtivoTangivel updatedAtivoTangivel = new AtivoTangivel();
                    updatedAtivoTangivel.setAtivo(ativo);

                    // Copy the properties from ativoTangivelAtualizado to updatedAtivoTangivel
                    try {
                        notNull.copyProperties(updatedAtivoTangivel, ativoTangivelAtualizado);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException("Error copying properties", e);
                    }

                    // Save the updated AtivoTangivel entity
                    updatedAtivoTangivel = repositorio.save(updatedAtivoTangivel);

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