
package com.javali.CtrlA.endpoint;

import com.javali.CtrlA.entidades.Ativo;
import com.javali.CtrlA.entidades.AtivoTangivel;
import com.javali.CtrlA.entidades.HistoricoAtivoTangivel;
import com.javali.CtrlA.entidades.NotaFiscal;
import com.javali.CtrlA.entidades.Usuario;
import com.javali.CtrlA.repositorios.AtivoRepositorio;
import com.javali.CtrlA.repositorios.AtivotangivelRepositorio;
import com.javali.CtrlA.repositorios.HistoricoAtivoTangivelRepositorio;
import com.javali.CtrlA.repositorios.NotaFiscalRepositorio;
import com.javali.CtrlA.repositorios.UsuarioRepositorio;
import lombok.val;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ativoTangivel")
@PreAuthorize("hasAnyAuthority('ADM')")
public class AtivoTangivelControle {
    @Autowired
    private HistoricoAtivoTangivelRepositorio historicoRepositorio;

    @Autowired
    private AtivotangivelRepositorio repositorio;

    @Autowired
    private AtivoRepositorio ativoRepositorio;
    @Autowired
    private NotaFiscalRepositorio notaFiscalRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @PostMapping(value = "/cadastro", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AtivoTangivel> criarAtivoTangivel(@RequestBody AtivoTangivel novoAtivoTangivel) {
        AtivoTangivel ativoTangivel = repositorio.save(novoAtivoTangivel);
        return new ResponseEntity<>(ativoTangivel, HttpStatus.CREATED);
    }

    @GetMapping("/listagemTodos")
    public ResponseEntity<List<AtivoTangivel>> obterAtivoTangivels() {
        List<AtivoTangivel> ativoTangivels = repositorio.findAll();
        if (ativoTangivels.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(ativoTangivels, HttpStatus.OK);
        }
    }

    @GetMapping("/listagem/{id}")
    public ResponseEntity<AtivoTangivel> obterAtivoTangivel(@PathVariable long id) {
        Optional<AtivoTangivel> ativoTangivelOptional = repositorio.findById(id);
        return ativoTangivelOptional.map(ativoTangivel -> new ResponseEntity<>(ativoTangivel, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/atualizacao/{id}")
    public ResponseEntity<?> atualizarAtivoTangivel(@PathVariable long id, @RequestBody AtivoTangivel ativoTangivelAtualizado) {
        return repositorio.findById(id)
                .map(ativoTangivel -> {
                    Ativo ativo = ativoRepositorio.findById(ativoTangivel.getAtivo().getId())
                            .orElseThrow(() -> new RuntimeException("Ativo not found with id " + ativoTangivel.getAtivo().getId()));
                    LocalDate dataAtualizacao = LocalDate.now();
                    ativo.setUltimaAtualizacao(dataAtualizacao);
                    
                    HistoricoAtivoTangivel historicoTangivel = new HistoricoAtivoTangivel();
                    historicoTangivel.setIdAtivoTangivel(ativoTangivel.getId());
                    historicoTangivel.setDataAlteracao(dataAtualizacao);
                    historicoTangivel.setIdAtivo(ativo.getId());
                    historicoTangivel.setNomeAtivo(ativo.getNome());
                    historicoTangivel.setMarcaAtivo(ativo.getMarca());
                    historicoTangivel.setCustoAquisicaoAtivo(ativo.getCustoAquisicao());
                    historicoTangivel.setDataAquisicaoAtivo(ativo.getDataAquisicao());
                    historicoTangivel.setGarantiaAtivoTangivel(ativoTangivel.getGarantia());
                    historicoTangivel.setNumeroIdentificacaoAtivo(ativo.getNumeroIdentificacao());
                    historicoTangivel.setTipoAtivo(ativo.getTipo());
                    historicoTangivel.setTagAtivo(ativo.getTag());
                    historicoTangivel.setGrauImportanciaAtivo(ativo.getGrauImportancia());
                    historicoTangivel.setStatusAtivo(ativo.getStatus());
                    historicoTangivel.setDescricaoAtivo(ativo.getDescricao());
                    historicoTangivel.setTaxaDepreciacaoAtivoTangivel(ativoTangivel.getTaxaDepreciacao());
                    historicoTangivel.setPeriodoDepreciacaoAtivoTangivel(ativoTangivel.getPeriodoDepreciacao());
                    historicoTangivel.setUltimaAtualizacaoAtivo(ativo.getUltimaAtualizacao());
                    historicoTangivel.setCamposPersonalizadosAtivo(ativo.getCamposPersonalizados());
                    
                    Usuario responsavel = ativo.getIdResponsavel();
                    if (responsavel != null) {
                    	historicoTangivel.setIdUsuario(responsavel.getId());
                        historicoTangivel.setNomeUsuario(responsavel.getNome());
                        historicoTangivel.setCpfUsuario(responsavel.getCpf());
                        historicoTangivel.setNascimentoUsuario(responsavel.getNascimento());
                        historicoTangivel.setDepartamentoUsuario(responsavel.getDepartamento());
                        historicoTangivel.setTelefoneUsuario(responsavel.getTelefone());
                        historicoTangivel.setEmailUsuario(responsavel.getEmail());
                        historicoTangivel.setStatusUsuario(responsavel.getStatus());	
                    }
                    
                    NotaFiscal notaFiscalHistorico = ativo.getIdNotaFiscal();
                    if (notaFiscalHistorico != null) {
                    	historicoTangivel.setIdNotaFiscal(notaFiscalHistorico.getId());
                    	historicoTangivel.setTipoDocumentoNotaFiscal(notaFiscalHistorico.getTipoDocumento());
                    	historicoTangivel.setDocumentoNotaFiscal(notaFiscalHistorico.getDocumento());
                    }
                    historicoRepositorio.save(historicoTangivel);

                    // Fetch the Usuario and NotaFiscal entities from the database if idResponsavel and idNotaFiscal are not null
                    if (ativoTangivelAtualizado.getAtivo().getIdResponsavel() != null) {
                        Usuario usuario = usuarioRepositorio.findById(ativoTangivelAtualizado.getAtivo().getIdResponsavel().getId())
                                .orElseThrow(() -> new RuntimeException("Usuario not found with id " + ativoTangivelAtualizado.getAtivo().getIdResponsavel().getId()));
                        ativo.setIdResponsavel(usuario);
                    }
                    if (ativoTangivelAtualizado.getAtivo().getIdNotaFiscal() != null) {
                        NotaFiscal notaFiscal = notaFiscalRepositorio.findById(ativoTangivelAtualizado.getAtivo().getIdNotaFiscal().getId())
                                .orElseThrow(() -> new RuntimeException("NotaFiscal not found with id " + ativoTangivelAtualizado.getAtivo().getIdNotaFiscal().getId()));
                        ativo.setIdNotaFiscal(notaFiscal);
                    }

                    // Create a custom BeanUtilsBean that ignores null properties
                    BeanUtilsBean notNull = new BeanUtilsBean() {
                        @Override
                        public void copyProperty(Object dest, String name, Object value)
                                throws IllegalAccessException, InvocationTargetException {
                            if (value == null) return;
                            super.copyProperty(dest, name, value);
                        }
                    };

                    // Copy the properties from ativoTangivelAtualizado.getAtivo() to ativo
                    try {
                        notNull.copyProperties(ativo, ativoTangivelAtualizado.getAtivo());
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException("Error copying properties", e);
                    }

                    // Save the Ativo entity
                    ativo = ativoRepositorio.save(ativo);

                    // Update the AtivoTangivel entity
                    if (ativoTangivelAtualizado.getGarantia() != null) {
                        ativoTangivel.setGarantia(ativoTangivelAtualizado.getGarantia());
                    }
                    if (ativoTangivelAtualizado.getTaxaDepreciacao() != null) {
                        ativoTangivel.setTaxaDepreciacao(ativoTangivelAtualizado.getTaxaDepreciacao());
                    }
                    if (ativoTangivelAtualizado.getPeriodoDepreciacao() != null) {
                        ativoTangivel.setPeriodoDepreciacao(ativoTangivelAtualizado.getPeriodoDepreciacao());
                    }

                    // Save the updated AtivoTangivel entity
                    AtivoTangivel updatedAtivoTangivel = repositorio.save(ativoTangivel);

                    return new ResponseEntity<>(updatedAtivoTangivel, HttpStatus.OK);
                }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/exclusao/{id}")
    public ResponseEntity<Void> deletarAtivoTangivel(@PathVariable long id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}