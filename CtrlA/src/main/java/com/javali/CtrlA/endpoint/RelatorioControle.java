package com.javali.CtrlA.endpoint;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javali.CtrlA.entidades.Ativo;
import com.javali.CtrlA.entidades.AtivoIntangivel;
import com.javali.CtrlA.entidades.AtivoTangivel;
import com.javali.CtrlA.entidades.Manutencao;
import com.javali.CtrlA.entidades.Usuario;
import com.javali.CtrlA.modelo.FiltroRelatorioAtivo;
import com.javali.CtrlA.modelo.FiltroRelatorioManutencao;
import com.javali.CtrlA.modelo.RelatorioAtivo;
import com.javali.CtrlA.modelo.TipoRelatorioAtivo;
import com.javali.CtrlA.repositorios.AtivoRepositorio;
import com.javali.CtrlA.repositorios.AtivointangivelRepositorio;
import com.javali.CtrlA.repositorios.AtivotangivelRepositorio;
import com.javali.CtrlA.repositorios.ManutencaoRepositorio;

@RestController
@RequestMapping("/relatorio")
@PreAuthorize("hasAnyAuthority('ADM')")
public class RelatorioControle {
    @Autowired
    private AtivointangivelRepositorio intangivelRepositorio;
    
    @Autowired
    private AtivotangivelRepositorio tangivelRepositorio;
    
    @Autowired
    private ManutencaoRepositorio manutencaoRepositorio;
    
    @Autowired
    private AtivoRepositorio ativoRepositorio;

    private boolean EmManutencao(long idAtivo) {
    	LocalDate dataAtual = LocalDate.now();
    	for(Manutencao m : manutencaoRepositorio.findAll()) {
    		if (m.getAtivo().getId() != idAtivo) {
    			continue;
    		}
    		
    		LocalDate manutencaoInicio = m.getDataInicio();
    		LocalDate manutencaoFim = m.getDataFim();
    		
    		if (manutencaoInicio == null || manutencaoFim == null) {
    			continue;
    		}
    		
    		if (dataAtual.compareTo(manutencaoInicio) >= 0 && dataAtual.compareTo(manutencaoFim) <= 0) {
    			return true;
    		}
    	}
    	return false;
    }
    
    @PostMapping("/filtroAtivos")
    public ResponseEntity<RelatorioAtivo> criarAtivoIntangivel(@RequestBody FiltroRelatorioAtivo filtro) {
        if (filtro.dataInicio == null) {
            filtro.dataInicio = LocalDate.parse("0001-01-01");
        }
        if (filtro.dataFim == null) {
            filtro.dataFim = LocalDate.parse("9999-12-31");
        }
        
        System.out.println(filtro.tipo);
        
        RelatorioAtivo relatorio = new RelatorioAtivo();
        relatorio.qtdAtivos = 0;
        relatorio.valorTotal = 0;
        relatorio.qtdPorLocal = new HashMap<String, Long>();
        
        float totalNaoAlocado = 0;
    	float totalEmUso = 0;
    	float totalEmManutencao = 0;
        
        if (filtro.tipo == TipoRelatorioAtivo.Tangiveis || filtro.tipo == TipoRelatorioAtivo.DadosGerais) {
        	relatorio.qtdAtivos += tangivelRepositorio.findAll().size();
        	
        	for(AtivoTangivel tangivel : tangivelRepositorio.findAll()) {
        		float vidaUtil = (float)tangivel.getTaxaDepreciacao().intValue();
        		float valorReal = tangivel.getAtivo().getCustoAquisicao().floatValue() - tangivel.getAtivo().getValorResidual().floatValue();
        		
        		LocalDate dataAtual = LocalDate.now();
        		LocalDate dataAquisicao = tangivel.getAtivo().getDataAquisicao();
        		Duration diff = Duration.between(dataAquisicao.atStartOfDay(), dataAtual.atStartOfDay());
        		float difDays = diff.toDays();
        		
        		float valorAtual = (valorReal / vidaUtil) * (difDays / 356f);
        		
        		relatorio.valorTotal += valorAtual;
        	}
        }
        else if (filtro.tipo == TipoRelatorioAtivo.Intangiveis || filtro.tipo == TipoRelatorioAtivo.DadosGerais) {
        	relatorio.qtdAtivos += intangivelRepositorio.findAll().size();
        	
        	for(AtivoIntangivel intangivel : intangivelRepositorio.findAll()) {
        		float vidaUtil = (float)intangivel.getTaxaAmortizacao().intValue();
        		float valorReal = intangivel.getAtivo().getCustoAquisicao().floatValue() - intangivel.getAtivo().getValorResidual().floatValue();
        		
        		LocalDate d1 = intangivel.getAtivo().getDataAquisicao();
        		LocalDate d2 = LocalDate.now();
        		Duration diff = Duration.between(d1.atStartOfDay(), d2.atStartOfDay());
        		float difDays = diff.toDays();
        		
        		float valorAtual = (valorReal / vidaUtil) * (difDays / 356f);
        		
        		relatorio.valorTotal += valorAtual;
        	}
        }
        
        for(Ativo ativo : ativoRepositorio.findAll()) {
        	if (EmManutencao(ativo.getId())) {
    			totalEmManutencao += 1;
    			
    			Long manutencao = relatorio.qtdPorLocal.get("Manutencao");
    			if (manutencao == null) relatorio.qtdPorLocal.put("Manutencao", 0l);
    			else relatorio.qtdPorLocal.put("Manutencao", manutencao + 1);
    		}
    		else if (ativo.getIdResponsavel() != null) {
    			totalEmUso += 1;
    			
    			Usuario usuario = ativo.getIdResponsavel();
    			String local = usuario.getDepartamento();
    			
    			Long manutencao = relatorio.qtdPorLocal.get(local);
    			if (manutencao == null) relatorio.qtdPorLocal.put(local, 0l);
    			else relatorio.qtdPorLocal.put(local, manutencao + 1);
    		}
    		else {
    			totalNaoAlocado += 1;
    		}
        }
        
        float totalTotal = totalNaoAlocado + totalEmUso + totalEmManutencao;
        relatorio.statusNaoAlocado = totalNaoAlocado / totalTotal;
        relatorio.statusEmUso = totalEmUso / totalTotal;
        relatorio.statusEmManutencao = totalEmManutencao / totalTotal;
        
        return new ResponseEntity<>(relatorio, HttpStatus.CREATED);
    }

    @PostMapping("/filtroManutencoes")
    public ResponseEntity<Float> criarAtivoIntangivel(@RequestBody FiltroRelatorioManutencao filtro) {

    	
        
        return new ResponseEntity<>(10.0f, HttpStatus.CREATED);
    }
}
