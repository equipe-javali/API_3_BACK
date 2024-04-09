package com.javali.CtrlA.endpoint;

import com.javali.CtrlA.entidades.Usuario;
import com.javali.CtrlA.entidades.UsuarioLogin;
import com.javali.CtrlA.repositorios.UsuarioLoginRepositorio;
import com.javali.CtrlA.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class UsuarioLoginControle {

    @Autowired
    private UsuarioLoginRepositorio repositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @PostMapping("/usuariologin")
    public ResponseEntity<UsuarioLogin> criarUsuariologin(@RequestBody UsuarioLogin novoUsuarioLogin) {
        Usuario usuario = usuarioRepositorio.findById(novoUsuarioLogin.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuario not found with id " + novoUsuarioLogin.getUsuario().getId()));
        novoUsuarioLogin.setUsuario(usuario);
        UsuarioLogin usuariologin = repositorio.save(novoUsuarioLogin);
        return new ResponseEntity<>(usuariologin, HttpStatus.CREATED);
    }

    @GetMapping("/usuarioslogins")
    public ResponseEntity<List<UsuarioLogin>> obterTodosUsuariologin() {
        List<UsuarioLogin> usuarioLogins = repositorio.findAll();
        if (usuarioLogins.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(usuarioLogins, HttpStatus.OK);
        }
    }

    @GetMapping("/usuariologin/{id}")
    public ResponseEntity<UsuarioLogin> obterUsuariologinPorId(@PathVariable Long id) {
        Optional<UsuarioLogin> usuariologinOptional = repositorio.findById(id);
        return usuariologinOptional.map(usuarioLogin -> new ResponseEntity<>(usuarioLogin, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/usuariologin/{id}")
    public ResponseEntity<UsuarioLogin> atualizarUsuariologin(@PathVariable Long id, @RequestBody UsuarioLogin usuarioLoginAtualizado) {
        return repositorio.findById(id)
                .map(usuarioLogin -> {
                    usuarioLogin.setSenha(usuarioLoginAtualizado.getSenha());

                    Usuario usuario = usuarioRepositorio.findById(usuarioLoginAtualizado.getUsuario().getId())
                            .orElseThrow(() -> new RuntimeException("Usuario not found with id " + usuarioLoginAtualizado.getUsuario().getId()));

                    usuarioLogin.setUsuario(usuario);
                    repositorio.save(usuarioLogin);
                    return new ResponseEntity<>(usuarioLogin, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/usuariologin/{id}")
    public ResponseEntity<Void> deletarUsuariologin(@PathVariable Long id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}