package com.javali.CtrlA.endpoint;

import com.javali.CtrlA.entidades.Ativo;
import com.javali.CtrlA.entidades.AtivoIntangivel;
import com.javali.CtrlA.entidades.HistoricoAtivoIntangivel;
import com.javali.CtrlA.entidades.NotaFiscal;
import com.javali.CtrlA.entidades.Usuario;
import com.javali.CtrlA.repositorios.AtivoRepositorio;
import com.javali.CtrlA.repositorios.AtivointangivelRepositorio;
import com.javali.CtrlA.repositorios.HistoricoAtivoIntangivelRepositorio;
import com.javali.CtrlA.repositorios.NotaFiscalRepositorio;
import com.javali.CtrlA.repositorios.UsuarioRepositorio;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ativoIntangivel")
@PreAuthorize("hasAnyAuthority('ADM')")
public class AtivoIntangivelControle {

    @Autowired
    private HistoricoAtivoIntangivelRepositorio historicoRepositorio;

    @Autowired
    private AtivointangivelRepositorio repositorio;

    @Autowired
    private AtivoRepositorio ativoRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private NotaFiscalRepositorio notaFiscalRepositorio;

    @PostMapping("/cadastro")
    public ResponseEntity<AtivoIntangivel> criarAtivoIntangivel(@RequestBody AtivoIntangivel novoAtivoIntangivel) {
        AtivoIntangivel ativoIntangivel = repositorio.save(novoAtivoIntangivel);
        return new ResponseEntity<>(ativoIntangivel, HttpStatus.CREATED);
    }

    @GetMapping("/listagemTodos")
    public ResponseEntity<List<AtivoIntangivel>> obterAtivoIntangivels() {
        List<AtivoIntangivel> ativoIntangivels = repositorio.findAll();
        if (ativoIntangivels.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(ativoIntangivels, HttpStatus.OK);
        }
    }

    @GetMapping("/listagem/{id}")
    public ResponseEntity<AtivoIntangivel> obterAtivoIntangivel(@PathVariable long id) {
        Optional<AtivoIntangivel> ativoIntangivelOptional = repositorio.findById(id);
        return ativoIntangivelOptional.map(ativoIntangivel -> new ResponseEntity<>(ativoIntangivel, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/atualizacao/{id}")
    public ResponseEntity<?> atualizarAtivoIntangivel(@PathVariable long id, @RequestBody AtivoIntangivel ativoIntangivelAtualizado) {
        return repositorio.findById(id)
                .map(ativoIntangivel -> {
                    Ativo ativo = ativoRepositorio.findById(ativoIntangivel.getAtivo().getId())
                            .orElseThrow(() -> new RuntimeException("Ativo not found with id " + ativoIntangivel.getAtivo().getId()));
                    ativo.setUltimaAtualizacao(LocalDate.now());
                    
                    HistoricoAtivoIntangivel historicoIntangivel = new HistoricoAtivoIntangivel();
                    historicoIntangivel.setIdAtivo(ativo.getId());
                    historicoIntangivel.setIdAtivoIntangivel(ativoIntangivel.getId());
                    historicoIntangivel.setDataAlteracao(ativoIntangivel.getDataExpiracao());
                    historicoIntangivel.setNomeAtivo(ativo.getNome());
                    historicoIntangivel.setMarcaAtivo(ativo.getMarca());
                    historicoIntangivel.setCustoAquisicaoAtivo(ativo.getCustoAquisicao());
                    historicoIntangivel.setDataAquisicaoAtivo(ativo.getDataAquisicao());
                    historicoIntangivel.setDataExpiracaoAtivoIntangivel(ativoIntangivel.getDataExpiracao());
                    historicoIntangivel.setNumeroIdentificacaoAtivo(ativo.getNumeroIdentificacao());
                    historicoIntangivel.setTipoAtivo(ativo.getTipo());
                    historicoIntangivel.setTagAtivo(ativo.getTag());
                    historicoIntangivel.setGrauImportanciaAtivo(ativo.getGrauImportancia());
                    historicoIntangivel.setStatusAtivo(ativo.getStatus());
                    historicoIntangivel.setDescricaoAtivo(ativo.getDescricao());
                    historicoIntangivel.setTaxaAmortizacaoAtivoIntangivel(ativoIntangivel.getTaxaAmortizacao());
                    historicoIntangivel.setPeriodoAmortizacaoAtivoIntangivel(ativoIntangivel.getPeriodoAmortizacao());
                    historicoIntangivel.setUltimaAtualizacaoAtivo(ativo.getUltimaAtualizacao());
                    
                    Usuario responsavel = ativo.getIdResponsavel();
                    if (responsavel != null) {
                    	historicoIntangivel.setIdUsuario(responsavel.getId());
                        historicoIntangivel.setNomeUsuario(responsavel.getNome());
                        historicoIntangivel.setCpfUsuario(responsavel.getCpf());
                        historicoIntangivel.setNascimentoUsuario(responsavel.getNascimento());
                        historicoIntangivel.setDepartamentoUsuario(responsavel.getDepartamento());
                        historicoIntangivel.setTelefoneUsuario(responsavel.getTelefone());
                        historicoIntangivel.setEmailUsuario(responsavel.getEmail());
                        historicoIntangivel.setStatusUsuario(responsavel.getStatus());	
                    }
                    
                    NotaFiscal notaFiscalHistorico = ativo.getIdNotaFiscal();
                    if (notaFiscalHistorico != null) {
                    	historicoIntangivel.setIdNotaFiscal(notaFiscalHistorico.getId());
                    	historicoIntangivel.setTipoDocumentoNotaFiscal(notaFiscalHistorico.getTipoDocumento());
                    	historicoIntangivel.setDocumentoNotaFiscal(notaFiscalHistorico.getDocumento());
                    }
                    historicoRepositorio.save(historicoIntangivel);

                    // Fetch the Usuario and NotaFiscal entities from the database if idResponsavel and idNotaFiscal are not null
                    if (ativoIntangivelAtualizado.getAtivo().getIdResponsavel() != null) {
                        Usuario usuario = usuarioRepositorio.findById(ativoIntangivelAtualizado.getAtivo().getIdResponsavel().getId())
                                .orElseThrow(() -> new RuntimeException("Usuario not found with id " + ativoIntangivelAtualizado.getAtivo().getIdResponsavel().getId()));
                        ativo.setIdResponsavel(usuario);
                    }
                    if (ativoIntangivelAtualizado.getAtivo().getIdNotaFiscal() != null) {
                        NotaFiscal notaFiscal = notaFiscalRepositorio.findById(ativoIntangivelAtualizado.getAtivo().getIdNotaFiscal().getId())
                                .orElseThrow(() -> new RuntimeException("NotaFiscal not found with id " + ativoIntangivelAtualizado.getAtivo().getIdNotaFiscal().getId()));
                        ativo.setIdNotaFiscal(notaFiscal);
                    }

                    // Copy the properties from ativoIntangivelAtualizado.getAtivo() to ativo
                    BeanUtilsBean notNull = new BeanUtilsBean() {
                        @Override
                        public void copyProperty(Object dest, String name, Object value)
                                throws IllegalAccessException, InvocationTargetException {
                            if (value == null) return;
                            super.copyProperty(dest, name, value);
                        }
                    };

                    try {
                        notNull.copyProperties(ativo, ativoIntangivelAtualizado.getAtivo());
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException("Error copying properties", e);
                    }

                    // Save the Ativo entity
                    ativo = ativoRepositorio.save(ativo);

                    // Update the AtivoIntangivel entity
                    if (ativoIntangivelAtualizado.getDataExpiracao() != null) {
                        ativoIntangivel.setDataExpiracao(ativoIntangivelAtualizado.getDataExpiracao());
                    }
                    if (ativoIntangivelAtualizado.getTaxaAmortizacao() != null) {
                        ativoIntangivel.setTaxaAmortizacao(ativoIntangivelAtualizado.getTaxaAmortizacao());
                    }
                    if (ativoIntangivelAtualizado.getPeriodoAmortizacao() != null) {
                        ativoIntangivel.setPeriodoAmortizacao(ativoIntangivelAtualizado.getPeriodoAmortizacao());
                    }

                    AtivoIntangivel updatedAtivoIntangivel = repositorio.save(ativoIntangivel);
                    
                    return new ResponseEntity<>(updatedAtivoIntangivel, HttpStatus.OK);
                }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/exclusao/{id}")
    public ResponseEntity<Void> deletarAtivoIntangivel(@PathVariable long id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}