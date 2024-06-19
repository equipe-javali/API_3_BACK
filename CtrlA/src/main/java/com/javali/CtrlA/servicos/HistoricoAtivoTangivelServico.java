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
public class HistoricoAtivoTangivelServico {

    @Autowired
    private HistoricoAtivoTangivelRepositorio historicoRepositorio;

    @Autowired
    private AtivotangivelRepositorio ativoTangivelRepositorio;

    public boolean createHistorico(Long idAtivoIntangivel) {
        AtivoTangivel ativoTangivel = ativoTangivelRepositorio.findById(idAtivoIntangivel).orElse(null);
        if (ativoTangivel == null) {
            return false;
        }

        // Create a new HistoricoAtivoTangivel
        HistoricoAtivoTangivel historicoAtivoTangivel = new HistoricoAtivoTangivel();

        // Definir campo data_alteracao com data atual local
        historicoAtivoTangivel.setUltimaAtualizacaoAtivo(LocalDate.now());


        // Campos de AtivoTangivel
        historicoAtivoTangivel.setIdAtivoTangivel(ativoTangivel.getId());
        historicoAtivoTangivel.setGarantiaAtivoTangivel(ativoTangivel.getGarantia());
        historicoAtivoTangivel.setTaxaDepreciacaoAtivoTangivel(ativoTangivel.getTaxaDepreciacao());
        historicoAtivoTangivel.setPeriodoDepreciacaoAtivoTangivel(ativoTangivel.getPeriodoDepreciacao());


        // Campos de Ativo
        historicoAtivoTangivel.setIdAtivo(ativoTangivel.getAtivo().getId());
        historicoAtivoTangivel.setNomeAtivo(ativoTangivel.getAtivo().getNome());
        historicoAtivoTangivel.setCustoAquisicaoAtivo(ativoTangivel.getAtivo().getCustoAquisicao());
        historicoAtivoTangivel.setTipoAtivo(ativoTangivel.getAtivo().getTipo());
        historicoAtivoTangivel.setTagAtivo(ativoTangivel.getAtivo().getTag());
        historicoAtivoTangivel.setGrauImportanciaAtivo(ativoTangivel.getAtivo().getGrauImportancia());
        historicoAtivoTangivel.setStatusAtivo(ativoTangivel.getAtivo().getStatus());
        historicoAtivoTangivel.setDescricaoAtivo(ativoTangivel.getAtivo().getDescricao());
        historicoAtivoTangivel.setNumeroIdentificacaoAtivo(ativoTangivel.getAtivo().getNumeroIdentificacao());
        historicoAtivoTangivel.setUltimaAtualizacaoAtivo(ativoTangivel.getAtivo().getUltimaAtualizacao());
        historicoAtivoTangivel.setMarcaAtivo(ativoTangivel.getAtivo().getMarca());
        historicoAtivoTangivel.setDataAquisicaoAtivo(ativoTangivel.getAtivo().getDataAquisicao());
        historicoAtivoTangivel.setDataCadastroAtivo(ativoTangivel.getAtivo().getDataCadastro());
        historicoAtivoTangivel.setCamposPersonalizadosAtivo(ativoTangivel.getAtivo().getCamposPersonalizados());
        historicoAtivoTangivel.setValorResidual(ativoTangivel.getAtivo().getValorResidual());


        // Campos de Usuario
        if (ativoTangivel.getAtivo().getIdResponsavel() != null) {
            historicoAtivoTangivel.setIdUsuario(ativoTangivel.getAtivo().getIdResponsavel().getId());
            historicoAtivoTangivel.setNomeUsuario(ativoTangivel.getAtivo().getIdResponsavel().getNome());
            historicoAtivoTangivel.setCpfUsuario(ativoTangivel.getAtivo().getIdResponsavel().getCpf());
            historicoAtivoTangivel.setNascimentoUsuario(ativoTangivel.getAtivo().getIdResponsavel().getNascimento());
            historicoAtivoTangivel.setDepartamentoUsuario(ativoTangivel.getAtivo().getIdResponsavel().getDepartamento());
            historicoAtivoTangivel.setTelefoneUsuario(ativoTangivel.getAtivo().getIdResponsavel().getTelefone());
            historicoAtivoTangivel.setEmailUsuario(ativoTangivel.getAtivo().getIdResponsavel().getEmail());
            historicoAtivoTangivel.setStatusUsuario(ativoTangivel.getAtivo().getIdResponsavel().getStatus());
        }

        // Campos de NotaFiscal
        if (ativoTangivel.getAtivo().getIdNotaFiscal() != null) {
            historicoAtivoTangivel.setIdNotaFiscal(ativoTangivel.getAtivo().getIdNotaFiscal().getId());
            historicoAtivoTangivel.setDocumentoNotaFiscal(ativoTangivel.getAtivo().getIdNotaFiscal().getDocumento());
            historicoAtivoTangivel.setTipoDocumentoNotaFiscal(ativoTangivel.getAtivo().getIdNotaFiscal().getTipoDocumento());
        }

        // Salvar o novo HistoricoAtivoTangivel
        historicoAtivoTangivel = historicoRepositorio.save(historicoAtivoTangivel);

        return true;
    }
}