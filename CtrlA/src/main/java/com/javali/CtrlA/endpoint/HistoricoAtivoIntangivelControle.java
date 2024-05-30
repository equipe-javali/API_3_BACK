package com.javali.CtrlA.endpoint;

import com.javali.CtrlA.entidades.AtivoIntangivel;
import com.javali.CtrlA.entidades.HistoricoAtivoIntangivel;
import com.javali.CtrlA.repositorios.AtivointangivelRepositorio;
import com.javali.CtrlA.repositorios.HistoricoAtivoIntangivelRepositorio;
import com.javali.CtrlA.servicos.HistoricoAtivoIntangivelServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/historicoAtivoIntangivel")
public class HistoricoAtivoIntangivelControle {

    @Autowired
    private HistoricoAtivoIntangivelServico historicoServico;

    @Autowired
    private HistoricoAtivoIntangivelRepositorio repositorio;
    
    @Autowired
    private AtivointangivelRepositorio repoAtivoIntangivel;

    @GetMapping("/listagemTodos")
    public ResponseEntity<List<HistoricoAtivoIntangivel>> obterHistoricoAtivoIntangivels() {
        List<HistoricoAtivoIntangivel> historicoAtivoIntangivels = repositorio.findAll();
        if (historicoAtivoIntangivels.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(historicoAtivoIntangivels, HttpStatus.OK);
        }
    }

    @GetMapping("/listagem/{id}")
    public ResponseEntity<HistoricoAtivoIntangivel> obterHistoricoAtivoIntangivel(@PathVariable long id) {
        Optional<HistoricoAtivoIntangivel> historicoAtivoIntangivelOptional = repositorio.findById(id);
        return historicoAtivoIntangivelOptional.map(historicoAtivoIntangivel -> new ResponseEntity<>(historicoAtivoIntangivel, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/listagemAtivo/{id}")
    public ResponseEntity<List<HistoricoAtivoIntangivel>> obterHistoricosAtivoIntangivel(@PathVariable long id) {
        List<HistoricoAtivoIntangivel> todosHistoricos = repositorio.findAll();

        List<HistoricoAtivoIntangivel> historicos = new ArrayList<HistoricoAtivoIntangivel>();
        for (HistoricoAtivoIntangivel historico : todosHistoricos) {
            if (historico.getIdAtivoIntangivel() == id) {
                historicos.add(historico);
            }
        }
        
        Optional<AtivoIntangivel> opAtivoIntangivelAtual = repoAtivoIntangivel.findById(id);
        if (opAtivoIntangivelAtual.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        AtivoIntangivel ativoIntangivelAtual = opAtivoIntangivelAtual.get();
        HistoricoAtivoIntangivel atual = new HistoricoAtivoIntangivel();
        atual.setUltimaAtualizacaoAtivo(LocalDate.now());
        atual.setIdAtivoIntangivel(ativoIntangivelAtual.getId());
        atual.setDataExpiracaoAtivoIntangivel(ativoIntangivelAtual.getDataExpiracao());
        atual.setTaxaAmortizacaoAtivoIntangivel(ativoIntangivelAtual.getTaxaAmortizacao());
        atual.setPeriodoAmortizacaoAtivoIntangivel(ativoIntangivelAtual.getPeriodoAmortizacao());
        atual.setIdAtivo(ativoIntangivelAtual.getAtivo().getId());
        atual.setNomeAtivo(ativoIntangivelAtual.getAtivo().getNome());
        atual.setCustoAquisicaoAtivo(ativoIntangivelAtual.getAtivo().getCustoAquisicao());
        atual.setTipoAtivo(ativoIntangivelAtual.getAtivo().getTipo());
        atual.setTagAtivo(ativoIntangivelAtual.getAtivo().getTag());
        atual.setGrauImportanciaAtivo(ativoIntangivelAtual.getAtivo().getGrauImportancia());
        atual.setStatusAtivo(ativoIntangivelAtual.getAtivo().getStatus());
        atual.setDescricaoAtivo(ativoIntangivelAtual.getAtivo().getDescricao());
        atual.setNumeroIdentificacaoAtivo(ativoIntangivelAtual.getAtivo().getNumeroIdentificacao());
        atual.setUltimaAtualizacaoAtivo(ativoIntangivelAtual.getAtivo().getUltimaAtualizacao());
        atual.setMarcaAtivo(ativoIntangivelAtual.getAtivo().getMarca());
        atual.setDataAquisicaoAtivo(ativoIntangivelAtual.getAtivo().getDataAquisicao());
        atual.setDataCadastroAtivo(ativoIntangivelAtual.getAtivo().getDataCadastro());
        atual.setCamposPersonalizadosAtivo(ativoIntangivelAtual.getAtivo().getCamposPersonalizados());
        if (ativoIntangivelAtual.getAtivo().getIdResponsavel() != null) {
        	atual.setIdUsuario(ativoIntangivelAtual.getAtivo().getIdResponsavel().getId());
        	atual.setNomeUsuario(ativoIntangivelAtual.getAtivo().getIdResponsavel().getNome());
        	atual.setCpfUsuario(ativoIntangivelAtual.getAtivo().getIdResponsavel().getCpf());
        	atual.setNascimentoUsuario(ativoIntangivelAtual.getAtivo().getIdResponsavel().getNascimento());
            atual.setDepartamentoUsuario(ativoIntangivelAtual.getAtivo().getIdResponsavel().getDepartamento());
            atual.setTelefoneUsuario(ativoIntangivelAtual.getAtivo().getIdResponsavel().getTelefone());
            atual.setEmailUsuario(ativoIntangivelAtual.getAtivo().getIdResponsavel().getEmail());
            atual.setStatusUsuario(ativoIntangivelAtual.getAtivo().getIdResponsavel().getStatus());
        }
        if (ativoIntangivelAtual.getAtivo().getIdNotaFiscal() != null) {
        	atual.setIdNotaFiscal(ativoIntangivelAtual.getAtivo().getIdNotaFiscal().getId());
        	atual.setDocumentoNotaFiscal(ativoIntangivelAtual.getAtivo().getIdNotaFiscal().getDocumento());
        	atual.setTipoDocumentoNotaFiscal(ativoIntangivelAtual.getAtivo().getIdNotaFiscal().getTipoDocumento());
        }
        historicos.add(atual);

        if (historicos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(historicos, HttpStatus.OK);
    }

    @DeleteMapping("/exclusao/{id}")
    public ResponseEntity<Void> deletarHistoricoAtivoIntangivel(@PathVariable long id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}