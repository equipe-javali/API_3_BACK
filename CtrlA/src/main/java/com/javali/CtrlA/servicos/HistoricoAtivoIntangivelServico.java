package com.javali.CtrlA.servicos;

import com.javali.CtrlA.entidades.*;
import com.javali.CtrlA.repositorios.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class HistoricoAtivoIntangivelServico {

    private static final Logger logger = LoggerFactory.getLogger(HistoricoAtivoIntangivelServico.class);

    @Autowired
    private HistoricoAtivoIntangivelRepositorio historicoRepositorio;

    @Autowired
    private AtivoRepositorio ativoRepositorio;

    @Autowired
    private AtivointangivelRepositorio ativoIntangivelRepositorio;

    @Autowired
    private NotaFiscalRepositorio notaFiscalRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    public HistoricoAtivoIntangivel createHistorico(Long idAtivo, Long idAtivoIntangivel, Long idNotaFiscal, Long idUsuario) {
        // Fetch the current state of the related entities
        Ativo ativo = ativoRepositorio.findById(idAtivo).orElse(null);
        AtivoIntangivel ativoIntangivel = ativoIntangivelRepositorio.findById(idAtivoIntangivel).orElse(null);
        NotaFiscal notaFiscal = idNotaFiscal != null ? notaFiscalRepositorio.findById(idNotaFiscal).orElse(null) : null;
        Usuario usuario = idUsuario != null ? usuarioRepositorio.findById(idUsuario).orElse(null) : null;

        // Check if ativo and ativoIntangivel are not null
        if (ativo == null || ativoIntangivel == null) {
            throw new IllegalArgumentException("Ativo and AtivoIntangivel cannot be null");
        }

        // Create a new HistoricoAtivoIntangivel and copy the necessary data into it
        HistoricoAtivoIntangivel historico = new HistoricoAtivoIntangivel();

        // Set idAtivo with the idAtivo passed in
        historico.setIdAtivo(idAtivo);

        if (ativo != null) {
            historico.setNome(ativo.getNome());
            historico.setCustoAquisicao(ativo.getCustoAquisicao());
            historico.setTipo(ativo.getTipo());
            historico.setTag(ativo.getTag());
            historico.setGrauImportancia(ativo.getGrauImportancia());
            historico.setStatus(ativo.getStatus());
            historico.setDescricao(ativo.getDescricao());
            historico.setNumeroIdentificacao(ativo.getNumeroIdentificacao());
            historico.setUltimaAtualizacao(ativo.getUltimaAtualizacao());
            historico.setMarca(ativo.getMarca());
            historico.setDataAquisicao(ativo.getDataAquisicao());
            historico.setCamposPersonalizados(ativo.getCamposPersonalizados());
        }

        if (ativoIntangivel != null) {
            historico.setDataExpiracao(ativoIntangivel.getDataExpiracao());
            historico.setTaxaAmortizacao(ativoIntangivel.getTaxaAmortizacao());
            historico.setPeriodoAmortizacao(ativoIntangivel.getPeriodoAmortizacao());
        }

        if (notaFiscal != null) {
            historico.setDocumentoNotaFiscal(notaFiscal.getDocumento());
            historico.setTipoDocumentoNotaFiscal(notaFiscal.getTipoDocumento());
        }

        if (usuario != null) {
            historico.setNomeUsuario(usuario.getNome());
            historico.setCpfUsuario(usuario.getCpf());
            historico.setNascimentoUsuario(usuario.getNascimento());
            historico.setDepartamentoUsuario(usuario.getDepartamento());
            historico.setTelefoneUsuario(usuario.getTelefone());
            historico.setEmailUsuario(usuario.getEmail());
            historico.setStatusUsuario(usuario.getStatus());
        }

        // Save the new HistoricoAtivoIntangivel
        return historicoRepositorio.save(historico);
    }
}