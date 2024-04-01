package com.javali.CtrlA.componentes;

import com.javali.CtrlA.entidades.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioSelecionador implements Selecionador<Usuario, Long> {

    @Override
    public Usuario selecionar(List<Usuario> objetos, Long identificador) {
        Usuario usuario = null;
        for (Usuario objeto : objetos) {
            if (objeto.getId().longValue() == identificador.longValue()) {
                usuario = objeto;
                break;
            }
        }
        return usuario;
    }
}