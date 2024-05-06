package com.javali.CtrlA.componentes;

import com.javali.CtrlA.entidades.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioSelecionadorEmail implements Selecionador<Usuario, String> {

    @Override
    public Usuario selecionar(List<Usuario> objetos, String identificador) {
        Usuario usuario = null;
        for (Usuario objeto : objetos) {
            if (objeto.getEmail().equals(identificador)) {
                usuario = objeto;
                break;
            }
        }
        return usuario;
    }
}