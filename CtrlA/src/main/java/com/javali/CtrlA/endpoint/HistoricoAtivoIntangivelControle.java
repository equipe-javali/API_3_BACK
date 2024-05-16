package com.javali.CtrlA.endpoint;

import com.javali.CtrlA.entidades.AtivoIntangivel;
import com.javali.CtrlA.entidades.HistoricoAtivoIntangivel;
import com.javali.CtrlA.entidades.NotaFiscal;
import com.javali.CtrlA.entidades.Usuario;
import com.javali.CtrlA.repositorios.AtivointangivelRepositorio;
import com.javali.CtrlA.repositorios.HistoricoAtivoIntangivelRepositorio;
import com.javali.CtrlA.servicos.HistoricoAtivoIntangivelServico;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/historicoAtivoIntangivel")
public class HistoricoAtivoIntangivelControle {

    @Autowired
    private HistoricoAtivoIntangivelServico historicoServico;

    @Autowired
    private AtivointangivelRepositorio ativoIntangivelRepositorio;

    @Autowired
    private HistoricoAtivoIntangivelRepositorio repositorio;

    @PostMapping("/cadastro")
    public ResponseEntity<HistoricoAtivoIntangivel> criarHistoricoAtivoIntangivel(@RequestBody HistoricoAtivoIntangivelRequest request) throws InvocationTargetException, IllegalAccessException {
        AtivoIntangivel ativoIntangivel = ativoIntangivelRepositorio.findById(request.getIdAtivoIntangivel()).orElse(null);
        if (ativoIntangivel == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Create and save the HistoricoAtivoIntangivel
        HistoricoAtivoIntangivel historicoAtivoIntangivel = historicoServico.createHistorico(ativoIntangivel);

        // Set the idAtivoIntangivel field
        historicoAtivoIntangivel.setIdAtivoIntangivel(ativoIntangivel.getId());

        // Set the fields from AtivoIntangivel or Ativo
        historicoAtivoIntangivel.setNomeResponsavel(ativoIntangivel.getAtivo().getIdResponsavel().getNome());
        historicoAtivoIntangivel.setCpfResponsavel(ativoIntangivel.getAtivo().getIdResponsavel().getCpf());
        historicoAtivoIntangivel.setNascimentoResponsavel(ativoIntangivel.getAtivo().getIdResponsavel().getNascimento());
        historicoAtivoIntangivel.setDepartamentoResponsavel(ativoIntangivel.getAtivo().getIdResponsavel().getDepartamento());
        historicoAtivoIntangivel.setTelefoneResponsavel(ativoIntangivel.getAtivo().getIdResponsavel().getTelefone());
        historicoAtivoIntangivel.setEmailResponsavel(ativoIntangivel.getAtivo().getIdResponsavel().getEmail());
        historicoAtivoIntangivel.setStatusResponsavel(ativoIntangivel.getAtivo().getIdResponsavel().getStatus());
        historicoAtivoIntangivel.setDocumento(ativoIntangivel.getAtivo().getIdNotaFiscal().getDocumento());
        historicoAtivoIntangivel.setTipoDocumento(ativoIntangivel.getAtivo().getIdNotaFiscal().getTipoDocumento());

        // Save the HistoricoAtivoIntangivel with the set idAtivoIntangivel
        historicoAtivoIntangivel = repositorio.save(historicoAtivoIntangivel);

        return new ResponseEntity<>(historicoAtivoIntangivel, HttpStatus.CREATED);
    }

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

    @PutMapping("/atualizacao/{id}")
    public ResponseEntity<HistoricoAtivoIntangivel> atualizarHistoricoAtivoIntangivel(@PathVariable long id, @RequestBody HistoricoAtivoIntangivel historicoAtivoIntangivelAtualizado) {
        return repositorio.findById(id)
                .map(historicoAtivoIntangivel -> {
                    repositorio.save(historicoAtivoIntangivel);
                    return new ResponseEntity<>(historicoAtivoIntangivel, HttpStatus.OK);
                }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
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