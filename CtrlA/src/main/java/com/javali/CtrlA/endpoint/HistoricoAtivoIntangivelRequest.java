package com.javali.CtrlA.endpoint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoricoAtivoIntangivelRequest {
    private Long idAtivo;
    private Long idAtivoIntangivel;
    private Long idNotaFiscal;
    private Long idUsuario;
}