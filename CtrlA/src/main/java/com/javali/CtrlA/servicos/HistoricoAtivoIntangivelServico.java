package com.javali.CtrlA.servicos;

import com.javali.CtrlA.entidades.*;
import com.javali.CtrlA.repositorios.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

@Service
public class HistoricoAtivoIntangivelServico {

    @Autowired
    private HistoricoAtivoIntangivelRepositorio historicoRepositorio;

    @Autowired
    private AtivointangivelRepositorio ativoIntangivelRepositorio;

    public boolean createHistorico(Long idAtivoIntangivel) {
        AtivoIntangivel ativoIntangivel = ativoIntangivelRepositorio.findById(idAtivoIntangivel).orElse(null);
        if (ativoIntangivel == null) {
            return false;
        }

        // Create a new HistoricoAtivoIntangivel
        HistoricoAtivoIntangivel historicoAtivoIntangivel = new HistoricoAtivoIntangivel();

        // Set the data_alteracao field to the current local date
        historicoAtivoIntangivel.setUltimaAtualizacaoAtivo(LocalDate.now());


        // Campos de AtivoIntangivel
        historicoAtivoIntangivel.setIdAtivoIntangivel(ativoIntangivel.getId());
        historicoAtivoIntangivel.setDataExpiracaoAtivoIntangivel(ativoIntangivel.getDataExpiracao());
        historicoAtivoIntangivel.setTaxaAmortizacaoAtivoIntangivel(ativoIntangivel.getTaxaAmortizacao());
        historicoAtivoIntangivel.setPeriodoAmortizacaoAtivoIntangivel(ativoIntangivel.getPeriodoAmortizacao());


        // Campos de Ativo
        historicoAtivoIntangivel.setIdAtivo(ativoIntangivel.getAtivo().getId());
        historicoAtivoIntangivel.setNomeAtivo(ativoIntangivel.getAtivo().getNome());
        historicoAtivoIntangivel.setCustoAquisicaoAtivo(ativoIntangivel.getAtivo().getCustoAquisicao());
        historicoAtivoIntangivel.setTipoAtivo(ativoIntangivel.getAtivo().getTipo());
        historicoAtivoIntangivel.setTagAtivo(ativoIntangivel.getAtivo().getTag());
        historicoAtivoIntangivel.setGrauImportanciaAtivo(ativoIntangivel.getAtivo().getGrauImportancia());
        historicoAtivoIntangivel.setStatusAtivo(ativoIntangivel.getAtivo().getStatus());
        historicoAtivoIntangivel.setDescricaoAtivo(ativoIntangivel.getAtivo().getDescricao());
        historicoAtivoIntangivel.setNumeroIdentificacaoAtivo(ativoIntangivel.getAtivo().getNumeroIdentificacao());
        historicoAtivoIntangivel.setUltimaAtualizacaoAtivo(ativoIntangivel.getAtivo().getUltimaAtualizacao());
        historicoAtivoIntangivel.setMarcaAtivo(ativoIntangivel.getAtivo().getMarca());
        historicoAtivoIntangivel.setDataAquisicaoAtivo(ativoIntangivel.getAtivo().getDataAquisicao());
        historicoAtivoIntangivel.setDataCadastroAtivo(ativoIntangivel.getAtivo().getDataCadastro());
        historicoAtivoIntangivel.setCamposPersonalizadosAtivo(ativoIntangivel.getAtivo().getCamposPersonalizados());
        historicoAtivoIntangivel.setValorResidual(ativoIntangivel.getAtivo().getValorResidual());
        


        // Campos de Usuario
        if (ativoIntangivel.getAtivo().getIdResponsavel() != null) {
            historicoAtivoIntangivel.setIdUsuario(ativoIntangivel.getAtivo().getIdResponsavel().getId());
            historicoAtivoIntangivel.setNomeUsuario(ativoIntangivel.getAtivo().getIdResponsavel().getNome());
            historicoAtivoIntangivel.setCpfUsuario(ativoIntangivel.getAtivo().getIdResponsavel().getCpf());
            historicoAtivoIntangivel.setNascimentoUsuario(ativoIntangivel.getAtivo().getIdResponsavel().getNascimento());
            historicoAtivoIntangivel.setDepartamentoUsuario(ativoIntangivel.getAtivo().getIdResponsavel().getDepartamento());
            historicoAtivoIntangivel.setTelefoneUsuario(ativoIntangivel.getAtivo().getIdResponsavel().getTelefone());
            historicoAtivoIntangivel.setEmailUsuario(ativoIntangivel.getAtivo().getIdResponsavel().getEmail());
            historicoAtivoIntangivel.setStatusUsuario(ativoIntangivel.getAtivo().getIdResponsavel().getStatus());
        }

        // Campos de NotaFiscal
        if (ativoIntangivel.getAtivo().getIdNotaFiscal() != null) {
            historicoAtivoIntangivel.setIdNotaFiscal(ativoIntangivel.getAtivo().getIdNotaFiscal().getId());
            historicoAtivoIntangivel.setDocumentoNotaFiscal(ativoIntangivel.getAtivo().getIdNotaFiscal().getDocumento());
            historicoAtivoIntangivel.setTipoDocumentoNotaFiscal(ativoIntangivel.getAtivo().getIdNotaFiscal().getTipoDocumento());
        }

        // Salvar o novo HistoricoAtivoIntangivel
        historicoAtivoIntangivel = historicoRepositorio.save(historicoAtivoIntangivel);

        return true;
    }
}