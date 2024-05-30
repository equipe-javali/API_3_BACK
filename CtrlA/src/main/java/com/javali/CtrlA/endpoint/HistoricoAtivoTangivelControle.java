package com.javali.CtrlA.endpoint;

import com.javali.CtrlA.entidades.*;
import com.javali.CtrlA.repositorios.AtivotangivelRepositorio;
import com.javali.CtrlA.repositorios.HistoricoAtivoTangivelRepositorio;
import com.javali.CtrlA.servicos.HistoricoAtivoTangivelServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/historicoAtivoTangivel")
public class HistoricoAtivoTangivelControle {
    @Autowired
    private AtivotangivelRepositorio ativoTangivelRepositorio;

    @Autowired
    private HistoricoAtivoTangivelRepositorio repositorio;

    @GetMapping("/listagemTodos")
    public ResponseEntity<List<HistoricoAtivoTangivel>> obterHistoricoAtivoTangivels() {
        List<HistoricoAtivoTangivel> historicoAtivoTangivels = repositorio.findAll();
        if (historicoAtivoTangivels.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(historicoAtivoTangivels, HttpStatus.OK);
        }
    }

    @GetMapping("/listagem/{id}")
    public ResponseEntity<HistoricoAtivoTangivel> obterHistoricoAtivoTangivel(@PathVariable long id) {
        Optional<HistoricoAtivoTangivel> historicoAtivoTangivelOptional = repositorio.findById(id);
        return historicoAtivoTangivelOptional.map(historicoAtivoTangivel -> new ResponseEntity<>(historicoAtivoTangivel, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/listagemAtivo/{id}")
    public ResponseEntity<List<HistoricoAtivoTangivel>> obterHistoricosAtivoTangivel(@PathVariable long id) {
        List<HistoricoAtivoTangivel> todosHistoricos = repositorio.findAll();

        List<HistoricoAtivoTangivel> historicos = new ArrayList<HistoricoAtivoTangivel>();
        for (HistoricoAtivoTangivel historico : todosHistoricos) {
            if (historico.getIdAtivoTangivel() == id) {
                historicos.add(historico);
            }
        }
        
        Optional<AtivoTangivel> opAtivoTangivelAtual = ativoTangivelRepositorio.findById(id);
        if (opAtivoTangivelAtual.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        AtivoTangivel ativoTangivelAtual = opAtivoTangivelAtual.get();
        HistoricoAtivoTangivel atual = new HistoricoAtivoTangivel();
        atual.setUltimaAtualizacaoAtivo(LocalDate.now());
        atual.setIdAtivoTangivel(ativoTangivelAtual.getId());
        atual.setGarantiaAtivoTangivel(ativoTangivelAtual.getGarantia());
        atual.setTaxaDepreciacaoAtivoTangivel(ativoTangivelAtual.getTaxaDepreciacao());
        atual.setPeriodoDepreciacaoAtivoTangivel(ativoTangivelAtual.getPeriodoDepreciacao());
        atual.setIdAtivo(ativoTangivelAtual.getAtivo().getId());
        atual.setNomeAtivo(ativoTangivelAtual.getAtivo().getNome());
        atual.setCustoAquisicaoAtivo(ativoTangivelAtual.getAtivo().getCustoAquisicao());
        atual.setTipoAtivo(ativoTangivelAtual.getAtivo().getTipo());
        atual.setTagAtivo(ativoTangivelAtual.getAtivo().getTag());
        atual.setGrauImportanciaAtivo(ativoTangivelAtual.getAtivo().getGrauImportancia());
        atual.setStatusAtivo(ativoTangivelAtual.getAtivo().getStatus());
        atual.setDescricaoAtivo(ativoTangivelAtual.getAtivo().getDescricao());
        atual.setNumeroIdentificacaoAtivo(ativoTangivelAtual.getAtivo().getNumeroIdentificacao());
        atual.setUltimaAtualizacaoAtivo(ativoTangivelAtual.getAtivo().getUltimaAtualizacao());
        atual.setMarcaAtivo(ativoTangivelAtual.getAtivo().getMarca());
        atual.setDataAquisicaoAtivo(ativoTangivelAtual.getAtivo().getDataAquisicao());
        atual.setDataCadastroAtivo(ativoTangivelAtual.getAtivo().getDataCadastro());
        atual.setCamposPersonalizadosAtivo(ativoTangivelAtual.getAtivo().getCamposPersonalizados());
        if (ativoTangivelAtual.getAtivo().getIdResponsavel() != null) {
        	atual.setIdUsuario(ativoTangivelAtual.getAtivo().getIdResponsavel().getId());
        	atual.setNomeUsuario(ativoTangivelAtual.getAtivo().getIdResponsavel().getNome());
        	atual.setCpfUsuario(ativoTangivelAtual.getAtivo().getIdResponsavel().getCpf());
        	atual.setNascimentoUsuario(ativoTangivelAtual.getAtivo().getIdResponsavel().getNascimento());
            atual.setDepartamentoUsuario(ativoTangivelAtual.getAtivo().getIdResponsavel().getDepartamento());
            atual.setTelefoneUsuario(ativoTangivelAtual.getAtivo().getIdResponsavel().getTelefone());
            atual.setEmailUsuario(ativoTangivelAtual.getAtivo().getIdResponsavel().getEmail());
            atual.setStatusUsuario(ativoTangivelAtual.getAtivo().getIdResponsavel().getStatus());
        }
        if (ativoTangivelAtual.getAtivo().getIdNotaFiscal() != null) {
        	atual.setIdNotaFiscal(ativoTangivelAtual.getAtivo().getIdNotaFiscal().getId());
        	atual.setDocumentoNotaFiscal(ativoTangivelAtual.getAtivo().getIdNotaFiscal().getDocumento());
        	atual.setTipoDocumentoNotaFiscal(ativoTangivelAtual.getAtivo().getIdNotaFiscal().getTipoDocumento());
        }
        historicos.add(atual);

        if (historicos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(historicos, HttpStatus.OK);
    }

    @DeleteMapping("/exclusao/{id}")
    public ResponseEntity<Void> deletarHistoricoAtivoTangivel(@PathVariable long id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}