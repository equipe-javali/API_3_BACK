package com.javali.CtrlA.endpoint;

import com.javali.CtrlA.adaptadores.UsuarioCadastrarAdaptador;
import com.javali.CtrlA.componentes.UsuarioSelecionadorEmail;
import com.javali.CtrlA.entidades.Usuario;
import com.javali.CtrlA.entidades.UsuarioLogin;
import com.javali.CtrlA.hateoas.UsuarioHateoas;
import com.javali.CtrlA.modelo.Perfil;
import com.javali.CtrlA.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.apache.commons.beanutils.BeanUtilsBean;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/usuario")
public class UsuarioControle {

    @Autowired
    private UsuarioRepositorio repositorio;

    @Autowired
    private UsuarioHateoas hateoas;

    @PostMapping("/cadastro")
    public ResponseEntity<Usuario> criarUsuario(@RequestBody UsuarioCadastrarAdaptador novoUsuario) {
        String email = novoUsuario.getUsuario().getEmail();
    	String cpf = novoUsuario.getUsuario().getCpf();
    	    	
    	List<Usuario> usuarios = repositorio.findAll();
    	for (Usuario u : usuarios) {
    		if (u.getEmail().equals(email) || u.getCpf().equals(cpf)) {
    			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    		}
    	}

    	if (novoUsuario.getUsuario().getUsuariologin() != null) {
    		Usuario usuario = novoUsuario.adaptar();
    		usuario.setPerfil(Perfil.ADM);
    		repositorio.save(usuario);
    		hateoas.adicionarLink(usuario);
    		return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    	} else {
            Usuario usuario = novoUsuario.getUsuario();
            usuario.setPerfil(Perfil.DESTINATARIO);
            repositorio.save(usuario);
            hateoas.adicionarLink(usuario);
            return new ResponseEntity<>(usuario, HttpStatus.CREATED);
        }
    }

    @GetMapping("/listagemTodos")
    @PreAuthorize("hasAnyAuthority('ADM')")
    public ResponseEntity<List<Usuario>> obterUsuarios() {
        List<Usuario> usuarios = repositorio.findAll();
        if (usuarios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
        	for(Usuario u : usuarios) {
        		u.setUsuariologin(null);
        	}
            hateoas.adicionarLink(usuarios);
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }
    }

    @GetMapping("/listagemTodosAdm")
    @PreAuthorize("hasAnyAuthority('ADM')")
    public ResponseEntity<List<Usuario>> obterUsuariosAdm() {
        List<Usuario> todosUsuarios = repositorio.findAll();
        List<Usuario> usuarios = new ArrayList<Usuario>();
        for (Usuario u : todosUsuarios) {
            if (u.getUsuariologin() != null) {
                UsuarioLogin login = u.getUsuariologin();
                login.setSenha("SenhaSuperSecreta");
                u.setUsuariologin(login);
                usuarios.add(u);
            }
        }
        if (usuarios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            hateoas.adicionarLink(usuarios);
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }
    }

    @GetMapping("/listagem/{id}")
    @PreAuthorize("hasAnyAuthority('ADM')")
    public ResponseEntity<Usuario> obterUsuario(@PathVariable long id) {
        Optional<Usuario> usuarioOptional = repositorio.findById(id);
        return usuarioOptional.map(usuario -> {
        	usuario.setUsuariologin(null);
        	hateoas.adicionarLink(usuario);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/atualizacao/{id}")
    @PreAuthorize("hasAnyAuthority('ADM')")
    public ResponseEntity<?> atualizarUsuario(@PathVariable long id, @RequestBody Usuario usuarioAtualizado) {
        return repositorio.findById(id)
                .map(usuario -> {
                    BeanUtilsBean notNull = new BeanUtilsBean() {
                        @Override
                        public void copyProperty(Object dest, String name, Object value)
                                throws IllegalAccessException, InvocationTargetException {
                            if (value != null) {
                                super.copyProperty(dest, name, value);
                            }
                        }
                    };
                    try {
                        notNull.copyProperties(usuario, usuarioAtualizado);
                    } catch (Exception e) {
                        System.out.println("Exception while updating user: " + e.getMessage());
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    Usuario updatedUsuario = repositorio.save(usuario);
                    hateoas.adicionarLink(updatedUsuario);
                    return new ResponseEntity<>(updatedUsuario, HttpStatus.OK);
                }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/exclusao/{id}")
    @PreAuthorize("hasAnyAuthority('ADM')")
    public ResponseEntity<Void> deletarUsuario(@PathVariable long id) {
        Optional<Usuario> usuarioOptional = repositorio.findById(id);
        if (!usuarioOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Usuario usuario = usuarioOptional.get();
        if (usuario.getUsuariologin() != null) {
            usuario.getUsuariologin().setUsuario(null);
            usuario.setUsuariologin(null);
            repositorio.save(usuario);
        }

        try {
            repositorio.deleteById(id);
            if (repositorio.existsById(id)) {
                // The user still exists in the repository after the delete operation
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            System.out.println("Exception while deleting user: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}