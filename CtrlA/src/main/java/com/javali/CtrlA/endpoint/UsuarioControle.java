package com.javali.CtrlA.endpoint;

import com.javali.CtrlA.componentes.UsuarioSelecionador;
import com.javali.CtrlA.entidades.Usuario;
import com.javali.CtrlA.hateoas.UsuarioHateoas;
import com.javali.CtrlA.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class UsuarioControle {

    @Autowired
    private UsuarioRepositorio repositorio;

    @Autowired
    private UsuarioHateoas hateoas;

    @Autowired
    private UsuarioSelecionador selecionador;

    @PostMapping("/usuario")
    public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario novoUsuario) {
        Usuario usuario = repositorio.save(novoUsuario);
        hateoas.adicionarLink(usuario);
        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> obterUsuarios() {
        List<Usuario> usuarios = repositorio.findAll();
        if (usuarios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            hateoas.adicionarLink(usuarios);
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<Usuario> obterUsuario(@PathVariable long id) {
        Optional<Usuario> usuarioOptional = repositorio.findById(id);
        return usuarioOptional.map(usuario -> {
            hateoas.adicionarLink(usuario);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/usuario/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable long id, @RequestBody Usuario usuarioAtualizado) {
        return repositorio.findById(id)
                .map(usuario -> {
                    // Assuming Usuario has setters for its fields
                    // Replace with actual field setters
                    // usuario.setField(usuarioAtualizado.getField());
                    repositorio.save(usuario);
                    hateoas.adicionarLink(usuario);
                    return new ResponseEntity<>(usuario, HttpStatus.OK);
                }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable long id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}