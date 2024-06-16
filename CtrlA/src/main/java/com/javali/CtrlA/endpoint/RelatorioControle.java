package com.javali.CtrlA.endpoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.javali.CtrlA.entidades.HistoricoAtivoIntangivel;
import com.javali.CtrlA.entidades.HistoricoAtivoTangivel;
import com.javali.CtrlA.entidades.Manutencao;
import com.javali.CtrlA.entidades.Usuario;
import com.javali.CtrlA.modelo.FiltroRelatorioAtivo;
import com.javali.CtrlA.modelo.FiltroRelatorioCombinado;
import com.javali.CtrlA.modelo.FiltroRelatorioManutencao;
import com.javali.CtrlA.modelo.RelatorioAtivo;
import com.javali.CtrlA.modelo.RelatorioManutencao;
import com.javali.CtrlA.modelo.RetornoArquivo;
import com.javali.CtrlA.modelo.TipoRelatorioAtivo;
import com.javali.CtrlA.modelo.TipoRelatorioManutencao;
import com.javali.CtrlA.repositorios.AtivoRepositorio;
import com.javali.CtrlA.repositorios.AtivointangivelRepositorio;
import com.javali.CtrlA.repositorios.AtivotangivelRepositorio;
import com.javali.CtrlA.repositorios.HistoricoAtivoIntangivelRepositorio;
import com.javali.CtrlA.repositorios.HistoricoAtivoTangivelRepositorio;
import com.javali.CtrlA.repositorios.ManutencaoRepositorio;

@RestController
@RequestMapping("/relatorio")
@PreAuthorize("hasAnyAuthority('ADM')")
public class RelatorioControle {
	private String[] valoresCabecalhoTangivel = {
		"ID Ativo",
		"Nome",
		"Custo de Aquisicao",
		"Tipo",
		"Tag",
		"Grau de Importancia",
		"Status",
		"Responsavel",
		"Tipo Nota fiscal",
		"Descricao",
		"Identificacao",
		"Marca",
		"Data de Aquisicao",
		"Data de Cadastro",
		"Valor Residual",
		"Garantia",
		"Vida Útil",
		"Periodo de Depreciacao"
	};
	
	private String[] valoresCabecalhoIntangivel = {
			"ID Ativo",
			"Nome",
			"Custo de Aquisicao",
			"Tipo",
			"Tag",
			"Grau de Importancia",
			"Status",
			"Responsavel",
			"Tipo Nota fiscal",
			"Descricao",
			"Identificacao",
			"Marca",
			"Data de Aquisicao",
			"Data de Cadastro",
			"Valor Residual",
			"Data de Expiração",
			"Vida Útil",
			"Periodo de Depreciacao"
		};
	
    @Autowired
    private AtivointangivelRepositorio intangivelRepositorio;
    
    @Autowired
    private HistoricoAtivoIntangivelRepositorio historicoIntangivelRepositorio;
    
    @Autowired
    private AtivotangivelRepositorio tangivelRepositorio;
    
    @Autowired
    private HistoricoAtivoTangivelRepositorio historicoTangivelRepositorio;
    
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
			if (manutencaoFim == null) {
				manutencaoFim = LocalDate.parse("9999-12-31");
			}
    		
    		if (manutencaoInicio == null) {
    			continue;
    		}
    		
    		if (dataAtual.compareTo(manutencaoInicio) >= 0 && dataAtual.compareTo(manutencaoFim) <= 0) {
    			return true;
    		}
    	}
    	return false;
    }
    
    private RelatorioAtivo calcularRelatorioAtivos(FiltroRelatorioAtivo filtro) {
    	if (filtro.dataInicio == null) {
            filtro.dataInicio = LocalDate.parse("0001-01-01");
        }
        if (filtro.dataFim == null) {
            filtro.dataFim = LocalDate.parse("9999-12-31");
        }
        
        RelatorioAtivo relatorio = new RelatorioAtivo();
        relatorio.qtdAtivos = 0;
        relatorio.valorTotal = 0;
        relatorio.qtdPorLocal = new HashMap<String, Long>();
        
        float totalNaoAlocado = 0;
    	float totalEmUso = 0;
    	float totalEmManutencao = 0;
        
        if (filtro.tipo == TipoRelatorioAtivo.Tangiveis || filtro.tipo == TipoRelatorioAtivo.DadosGerais) {
        	for(AtivoTangivel tangivel : tangivelRepositorio.findAll()) {
        		if (tangivel.getAtivo().getDataAquisicao().compareTo(filtro.dataInicio) < 0 || tangivel.getAtivo().getDataAquisicao().compareTo(filtro.dataFim) > 0) {
        			continue;
        		}
        		relatorio.qtdAtivos += 1;
        		
        		float vidaUtil = (float)tangivel.getTaxaDepreciacao().intValue();
        		float valorReal = tangivel.getAtivo().getCustoAquisicao().floatValue() - tangivel.getAtivo().getValorResidual().floatValue();
        		
        		LocalDate dataAtual = LocalDate.now();
        		LocalDate dataAquisicao = tangivel.getAtivo().getDataAquisicao();
        		Duration diff = Duration.between(dataAquisicao.atStartOfDay(), dataAtual.atStartOfDay());
        		float difDays = diff.toDays();
        		
        		float valorAtual = (valorReal / vidaUtil) * (difDays / 356f);
        		if (vidaUtil == 0) {
        			relatorio.valorTotal += valorReal;
        		}
        		else relatorio.valorTotal += valorAtual;
        		
        		if (EmManutencao(tangivel.getAtivo().getId())) {
        			totalEmManutencao += 1;
        			
        			Long manutencao = relatorio.qtdPorLocal.get("Manutencao");
        			if (manutencao == null) relatorio.qtdPorLocal.put("Manutencao", 1l);
        			else relatorio.qtdPorLocal.put("Manutencao", manutencao + 1);
        		}
        		else if (tangivel.getAtivo().getIdResponsavel() != null) {
        			totalEmUso += 1;
        			
        			Usuario usuario = tangivel.getAtivo().getIdResponsavel();
        			String local = usuario.getDepartamento();
        			
        			Long manutencao = relatorio.qtdPorLocal.get(local);
        			if (manutencao == null) relatorio.qtdPorLocal.put(local, 1l);
        			else relatorio.qtdPorLocal.put(local, manutencao + 1);
        		}
        		else {
        			totalNaoAlocado += 1;
        			
        			Long naoAlocado = relatorio.qtdPorLocal.get("Não Alocado");
        			if (naoAlocado == null) relatorio.qtdPorLocal.put("Não Alocado", 1l);
        			else relatorio.qtdPorLocal.put("Não Alocado", naoAlocado + 1);
        		}
        	}
        }
        if (filtro.tipo == TipoRelatorioAtivo.Intangiveis || filtro.tipo == TipoRelatorioAtivo.DadosGerais) {
        	for(AtivoIntangivel intangivel : intangivelRepositorio.findAll()) {
        		if (intangivel.getAtivo().getDataAquisicao().compareTo(filtro.dataInicio) < 0 || intangivel.getAtivo().getDataAquisicao().compareTo(filtro.dataFim) > 0) {
        			continue;
        		}
        		relatorio.qtdAtivos += 1;
        		
        		float vidaUtil = (float)intangivel.getTaxaAmortizacao().intValue();
        		float valorReal = intangivel.getAtivo().getCustoAquisicao().floatValue() - intangivel.getAtivo().getValorResidual().floatValue();
        		
        		LocalDate d1 = intangivel.getAtivo().getDataAquisicao();
        		LocalDate d2 = LocalDate.now();
        		Duration diff = Duration.between(d1.atStartOfDay(), d2.atStartOfDay());
        		float difDays = diff.toDays();
        		
        		float valorAtual = (valorReal / vidaUtil) * (difDays / 356f);
        		if (vidaUtil == 0) {
        			relatorio.valorTotal += valorReal;
        		}
        		else relatorio.valorTotal += valorAtual;
        		
        		if (EmManutencao(intangivel.getAtivo().getId())) {
        			totalEmManutencao += 1;
        			
        			Long manutencao = relatorio.qtdPorLocal.get("Manutencao");
        			if (manutencao == null) relatorio.qtdPorLocal.put("Manutencao", 1l);
        			else relatorio.qtdPorLocal.put("Manutencao", manutencao + 1);
        		}
        		else if (intangivel.getAtivo().getIdResponsavel() != null) {
        			totalEmUso += 1;
        			
        			Usuario usuario = intangivel.getAtivo().getIdResponsavel();
        			String local = usuario.getDepartamento();
        			
        			Long manutencao = relatorio.qtdPorLocal.get(local);
        			if (manutencao == null) relatorio.qtdPorLocal.put(local, 1l);
        			else relatorio.qtdPorLocal.put(local, manutencao + 1);
        		}
        		else {
        			totalNaoAlocado += 1;
        			
        			Long naoAlocado = relatorio.qtdPorLocal.get("Não Alocado");
        			if (naoAlocado == null) relatorio.qtdPorLocal.put("Não Alocado", 1l);
        			else relatorio.qtdPorLocal.put("Não Alocado", naoAlocado + 1);
        		}
        	}
        }
        
        float totalTotal = totalNaoAlocado + totalEmUso + totalEmManutencao;
        relatorio.statusNaoAlocado = totalNaoAlocado / totalTotal;
        relatorio.statusEmUso = totalEmUso / totalTotal;
        relatorio.statusEmManutencao = totalEmManutencao / totalTotal;
        
        return relatorio;
    }

    private RelatorioManutencao calcularRelatorioManutencoes(FiltroRelatorioManutencao filtro) {
    	if (filtro.dataInicio == null) {
            filtro.dataInicio = LocalDate.parse("0001-01-01");
        }
        if (filtro.dataFim == null) {
            filtro.dataFim = LocalDate.parse("9999-12-31");
        }
        
    	RelatorioManutencao relatorio = new RelatorioManutencao();
    	relatorio.valorTotal = 0;
    	relatorio.mediaTempoPorTipo = new HashMap();
    	relatorio.qtdEnvioPorTipo = new HashMap();
    	
    	Map<Integer, Long> qtdPorTipo = new HashMap();
    	Map<Integer, Long> diasPorTipo = new HashMap();
    	
    	for(Manutencao m : manutencaoRepositorio.findAll()) {
    		if (filtro.idAtivo != null && !filtro.idAtivo.equals(m.getAtivo().getId())) {
    			continue;
        	}
    		if (filtro.tipo != TipoRelatorioManutencao.DadosGerais && filtro.tipo.ordinal() != m.getTipo()) {
    			continue;
    		}

    		LocalDate manutencaoFim = m.getDataFim();
    		if(manutencaoFim == null) manutencaoFim = LocalDate.now();

    		if (m.getDataInicio().compareTo(filtro.dataInicio) < 0 || manutencaoFim.compareTo(filtro.dataFim) > 0) {
    			continue;
    		}
    		
    		if (m.getCusto() != null)
    			relatorio.valorTotal += m.getCusto().floatValue();
			
			Long qtdTipo = qtdPorTipo.get(m.getTipo());
			if (qtdTipo == null) qtdPorTipo.put(m.getTipo(), 1l);
			else qtdPorTipo.put(m.getTipo(), qtdTipo + 1);
			
			long diasManutencao = ChronoUnit.DAYS.between(m.getDataInicio(), manutencaoFim);
			
			Long dias = diasPorTipo.get(m.getTipo());
			if (dias == null) diasPorTipo.put(m.getTipo(), diasManutencao);
			else diasPorTipo.put(m.getTipo(), dias + diasManutencao);
			
			Long qtdEnvio = relatorio.qtdEnvioPorTipo.get(m.getTipo());
			if (qtdEnvio == null) relatorio.qtdEnvioPorTipo.put(m.getTipo(), 1l);
			else relatorio.qtdEnvioPorTipo.put(m.getTipo(), qtdEnvio + 1);
		}

    	for(Integer key : qtdPorTipo.keySet()) {
    		Long media = diasPorTipo.get(key).longValue() / qtdPorTipo.get(key).longValue();
    		relatorio.mediaTempoPorTipo.put(key, media);
    	}
        
        return relatorio;
    }
    
    @PostMapping("/relatorioAtivos")
    public ResponseEntity<RelatorioAtivo> relatorioAtivos(@RequestBody FiltroRelatorioAtivo filtro) {
        return new ResponseEntity<RelatorioAtivo>(calcularRelatorioAtivos(filtro), HttpStatus.OK);
    }

    @PostMapping("/relatorioManutencoes")
    public ResponseEntity<RelatorioManutencao> RelatorioManutencoes(@RequestBody FiltroRelatorioManutencao filtro) {
        return new ResponseEntity<RelatorioManutencao>(calcularRelatorioManutencoes(filtro), HttpStatus.OK);
    }
    
    private void PopularHistoricoTangivel(Sheet sheet) {
    	// Cabeçalho
        Row headerTangivel = sheet.createRow(0);
        for (int i = 0; i < valoresCabecalhoTangivel.length; i++) {
        	Cell cell1 = headerTangivel.createCell(i);
            cell1.setCellValue(valoresCabecalhoTangivel[i]);
        }
        
        // Linhas
        int contadorLinha = 1;
        for (AtivoTangivel tangivel : tangivelRepositorio.findAll()) {
        	Row linha = sheet.createRow(contadorLinha);
        	Cell cell = linha.createCell(0);
            cell.setCellValue(tangivel.getId());
            
        	cell = linha.createCell(1);
            cell.setCellValue(tangivel.getAtivo().getNome());
            
            cell = linha.createCell(2);
            cell.setCellValue(tangivel.getAtivo().getCustoAquisicao().doubleValue());
            
            cell = linha.createCell(3);
            cell.setCellValue(tangivel.getAtivo().getTipo());
            
            cell = linha.createCell(4);
            cell.setCellValue(tangivel.getAtivo().getTag());
            
            cell = linha.createCell(5);
            cell.setCellValue(tangivel.getAtivo().getGrauImportancia());
            
            cell = linha.createCell(6);
            cell.setCellValue(tangivel.getAtivo().getStatus());
            
            if (tangivel.getAtivo().getIdResponsavel() != null) {
	            cell = linha.createCell(7);
	            cell.setCellValue(tangivel.getAtivo().getIdResponsavel().getNome());
            }
            
            if (tangivel.getAtivo().getIdNotaFiscal() != null) {
	            cell = linha.createCell(8);
	            cell.setCellValue(tangivel.getAtivo().getIdNotaFiscal().getTipoDocumento());
            }
           
            cell = linha.createCell(9);
            cell.setCellValue(tangivel.getAtivo().getDescricao());
            
            cell = linha.createCell(10);
            cell.setCellValue(tangivel.getAtivo().getNumeroIdentificacao());
            
            cell = linha.createCell(11);
            cell.setCellValue(tangivel.getAtivo().getMarca());
            
            if(tangivel.getAtivo().getDataAquisicao() != null) {
            	cell = linha.createCell(12);
                cell.setCellValue(tangivel.getAtivo().getDataAquisicao().toString());	
            }
            
            if(tangivel.getAtivo().getDataCadastro() != null) {
            	cell = linha.createCell(13);
                cell.setCellValue(tangivel.getAtivo().getDataCadastro().toString());
            }
            
            cell = linha.createCell(14);
            cell.setCellValue(tangivel.getAtivo().getValorResidual().doubleValue());
            
            if(tangivel.getGarantia() != null) {
                cell = linha.createCell(15);
                cell.setCellValue(tangivel.getGarantia().toString());
            }
            
            cell = linha.createCell(16);
            cell.setCellValue(tangivel.getTaxaDepreciacao().intValue());
            
            cell = linha.createCell(17);
            cell.setCellValue(tangivel.getPeriodoDepreciacao());
        	
        	contadorLinha += 1;
        	List<HistoricoAtivoTangivel> historico = historicoTangivelRepositorio.findByIdAtivo(tangivel.getId());
        	for (int i = 0; i < historico.size(); i++) {
        		linha = sheet.createRow(contadorLinha);
        		HistoricoAtivoTangivel h = historico.get(i);
        		
        		cell = linha.createCell(1);
                cell.setCellValue(h.getNomeAtivo());
                
                cell = linha.createCell(2);
                cell.setCellValue(h.getCustoAquisicaoAtivo().doubleValue());
                
                cell = linha.createCell(3);
                cell.setCellValue(h.getTipoAtivo());
                
                cell = linha.createCell(4);
                cell.setCellValue(h.getTagAtivo());
                
                cell = linha.createCell(5);
                cell.setCellValue(h.getGrauImportanciaAtivo());
                
                cell = linha.createCell(6);
                cell.setCellValue(h.getStatusAtivo());
                
                cell = linha.createCell(7);
                cell.setCellValue(h.getNomeUsuario());
                
                cell = linha.createCell(8);
                cell.setCellValue(h.getTipoDocumentoNotaFiscal());
               
                cell = linha.createCell(9);
                cell.setCellValue(h.getDescricaoAtivo());
               
                cell = linha.createCell(10);
                cell.setCellValue(h.getNumeroIdentificacaoAtivo());
                
                cell = linha.createCell(11);
                cell.setCellValue(h.getMarcaAtivo());
                
                if(h.getDataAquisicaoAtivo() != null) {
                	cell = linha.createCell(12);
                    cell.setCellValue(h.getDataAquisicaoAtivo().toString());
                }
                
                if(h.getDataCadastroAtivo() != null) {
                	cell = linha.createCell(13);
                    cell.setCellValue(h.getDataCadastroAtivo().toString());
                }
                
                cell = linha.createCell(14);
                cell.setCellValue(h.getValorResidual().doubleValue());
                
                if(h.getGarantiaAtivoTangivel() != null) {
                	cell = linha.createCell(15);
                    cell.setCellValue(h.getGarantiaAtivoTangivel());
                }
                
                cell = linha.createCell(16);
                cell.setCellValue(h.getTaxaDepreciacaoAtivoTangivel().intValue());
                
                cell = linha.createCell(17);
                cell.setCellValue(h.getPeriodoDepreciacaoAtivoTangivel());
                
        		contadorLinha += 1;
        	}
        	contadorLinha += 1;
        }
    }
    
    private void PopularHistoricoIntangivel(Sheet sheet) {
    	// Cabeçalho
        Row headerIntangivel = sheet.createRow(0);
        for (int i = 0; i < valoresCabecalhoIntangivel.length; i++) {
        	Cell cell1 = headerIntangivel.createCell(i);
            cell1.setCellValue(valoresCabecalhoIntangivel[i]);
        }
        
        // Linhas
        int contadorLinha = 1;
        for (AtivoIntangivel intangivel : intangivelRepositorio.findAll()) {
        	Row linha = sheet.createRow(contadorLinha);
        	Cell cell = linha.createCell(0);
            cell.setCellValue(intangivel.getId());
            
        	cell = linha.createCell(1);
            cell.setCellValue(intangivel.getAtivo().getNome());
            
            cell = linha.createCell(2);
            cell.setCellValue(intangivel.getAtivo().getCustoAquisicao().doubleValue());
            
            cell = linha.createCell(3);
            cell.setCellValue(intangivel.getAtivo().getTipo());
            
            cell = linha.createCell(4);
            cell.setCellValue(intangivel.getAtivo().getTag());
            
            cell = linha.createCell(5);
            cell.setCellValue(intangivel.getAtivo().getGrauImportancia());
            
            cell = linha.createCell(6);
            cell.setCellValue(intangivel.getAtivo().getStatus());
            
            if (intangivel.getAtivo().getIdResponsavel() != null) {
	            cell = linha.createCell(7);
	            cell.setCellValue(intangivel.getAtivo().getIdResponsavel().getNome());
            }
            
            if (intangivel.getAtivo().getIdNotaFiscal() != null) {
	            cell = linha.createCell(8);
	            cell.setCellValue(intangivel.getAtivo().getIdNotaFiscal().getTipoDocumento());
            }
           
            cell = linha.createCell(9);
            cell.setCellValue(intangivel.getAtivo().getDescricao());
            
            cell = linha.createCell(10);
            cell.setCellValue(intangivel.getAtivo().getNumeroIdentificacao());
            
            cell = linha.createCell(11);
            cell.setCellValue(intangivel.getAtivo().getMarca());
            
            if(intangivel.getAtivo().getDataAquisicao() != null) {
            	cell = linha.createCell(12);
                cell.setCellValue(intangivel.getAtivo().getDataAquisicao().toString());	
            }
            
            if(intangivel.getAtivo().getDataCadastro() != null) {
            	cell = linha.createCell(13);
                cell.setCellValue(intangivel.getAtivo().getDataCadastro().toString());
            }
            
            cell = linha.createCell(14);
            cell.setCellValue(intangivel.getAtivo().getValorResidual().doubleValue());
            
            if(intangivel.getDataExpiracao() != null) {
                cell = linha.createCell(15);
                cell.setCellValue(intangivel.getDataExpiracao().toString());
            }
            
            cell = linha.createCell(16);
            cell.setCellValue(intangivel.getTaxaAmortizacao().intValue());
            
            cell = linha.createCell(17);
            cell.setCellValue(intangivel.getPeriodoAmortizacao());
        	
        	contadorLinha += 1;
        	List<HistoricoAtivoIntangivel> historico = historicoIntangivelRepositorio.findByIdAtivo(intangivel.getId());
        	for (int i = 0; i < historico.size(); i++) {
        		linha = sheet.createRow(contadorLinha);
        		HistoricoAtivoIntangivel h = historico.get(i);
        		
        		cell = linha.createCell(1);
                cell.setCellValue(h.getNomeAtivo());
                
                cell = linha.createCell(2);
                cell.setCellValue(h.getCustoAquisicaoAtivo().doubleValue());
                
                cell = linha.createCell(3);
                cell.setCellValue(h.getTipoAtivo());
                
                cell = linha.createCell(4);
                cell.setCellValue(h.getTagAtivo());
                
                cell = linha.createCell(5);
                cell.setCellValue(h.getGrauImportanciaAtivo());
                
                cell = linha.createCell(6);
                cell.setCellValue(h.getStatusAtivo());
                
                cell = linha.createCell(7);
                cell.setCellValue(h.getNomeUsuario());
                
                cell = linha.createCell(8);
                cell.setCellValue(h.getTipoDocumentoNotaFiscal());
               
                cell = linha.createCell(9);
                cell.setCellValue(h.getDescricaoAtivo());
               
                cell = linha.createCell(10);
                cell.setCellValue(h.getNumeroIdentificacaoAtivo());
                
                cell = linha.createCell(11);
                cell.setCellValue(h.getMarcaAtivo());
                
                if(h.getDataAquisicaoAtivo() != null) {
                	cell = linha.createCell(12);
                    cell.setCellValue(h.getDataAquisicaoAtivo().toString());
                }
                
                if(h.getDataCadastroAtivo() != null) {
                	cell = linha.createCell(13);
                    cell.setCellValue(h.getDataCadastroAtivo().toString());
                }
                
                cell = linha.createCell(14);
                cell.setCellValue(h.getValorResidual().doubleValue());
                
                if(h.getDataExpiracaoAtivoIntangivel() != null) {
                	cell = linha.createCell(15);
                    cell.setCellValue(h.getDataExpiracaoAtivoIntangivel());
                }
                
                cell = linha.createCell(16);
                cell.setCellValue(h.getTaxaAmortizacaoAtivoIntangivel().intValue());
                
                cell = linha.createCell(17);
                cell.setCellValue(h.getPeriodoAmortizacaoAtivoIntangivel());
                
        		contadorLinha += 1;
        	}
        	contadorLinha += 1;
        }
    }
    
    private void PopularRelatorioAtivos(Workbook workbook, FiltroRelatorioAtivo Filtro) {
    	RelatorioAtivo relatorio = calcularRelatorioAtivos(Filtro); 
    	
    	Sheet sheet = workbook.createSheet("Relatório Ativos");
    	
    	Row headerTangivel = sheet.createRow(0);
    	headerTangivel.createCell(0).setCellValue("Total de Ativos");
    	headerTangivel.createCell(1).setCellValue("Valor Total");
    	
    	Row linhaTotais = sheet.createRow(1);
    	linhaTotais.createCell(0).setCellValue(relatorio.qtdAtivos);
    	linhaTotais.createCell(1).setCellValue(relatorio.valorTotal);
    	
    	Row headerStatus = sheet.createRow(3);
    	headerStatus.createCell(1).setCellValue("Status dos Ativos");
    	
    	CellStyle estiloPorcentagem = workbook.createCellStyle();
    	estiloPorcentagem.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
    	
    	Row linhaEmUso = sheet.createRow(4);
    	linhaEmUso.createCell(0).setCellValue("Em Uso");
    	Cell emUso = linhaEmUso.createCell(1);
    	emUso.setCellStyle(estiloPorcentagem);
    	emUso.setCellValue(relatorio.statusEmUso);
    	
    	Row linhaEmManutencao = sheet.createRow(5);
    	linhaEmManutencao.createCell(0).setCellValue("Em Manutenção");
    	Cell emManutencao = linhaEmManutencao.createCell(1);
    	emManutencao.setCellStyle(estiloPorcentagem);
    	emManutencao.setCellValue(relatorio.statusEmManutencao);
    	
    	Row linhaNaoAlocado = sheet.createRow(6);
    	linhaNaoAlocado.createCell(0).setCellValue("Não Alocado");
    	Cell naoAlocado = linhaNaoAlocado.createCell(1);
    	naoAlocado.setCellStyle(estiloPorcentagem);
    	naoAlocado.setCellValue(relatorio.statusNaoAlocado);
    	
    	sheet.createRow(8).createCell(1).setCellValue(("Ativos X Local"));
    	int keysN = relatorio.qtdPorLocal.size();
    	String[] locais = new String[keysN];
    	System.arraycopy(relatorio.qtdPorLocal.keySet().toArray(), 0, locais, 0, keysN);
    	
    	int contadorLinha = 9;
    	for (int i = 0; i < locais.length; i++) {
    		Row linhaLocal = sheet.createRow(contadorLinha++);
    		linhaLocal.createCell(0).setCellValue(locais[i]);
    		linhaLocal.createCell(1).setCellValue(relatorio.qtdPorLocal.get(locais[i]));
    	}
    }

    private void PopularRelatorioManutencoes(Workbook workbook, FiltroRelatorioManutencao filtro) {
    	RelatorioManutencao relatorio = calcularRelatorioManutencoes(filtro); 
    	
    	Sheet sheet = workbook.createSheet("Relatório Manutenções");
    	
    	Row identificacao = sheet.createRow(0);
    	identificacao.createCell(0).setCellValue("Ativo");
    	if (filtro.idAtivo == null) {
    		identificacao.createCell(1).setCellValue("Todos");
    	}
    	else {
    		String nome = ativoRepositorio.findById(filtro.idAtivo).get().getNome();
    		identificacao.createCell(1).setCellValue(nome);
    	}
    	
    	Row linhaTotal = sheet.createRow(2);
    	linhaTotal.createCell(0).setCellValue("Valor Total");
    	linhaTotal.createCell(1).setCellValue(relatorio.valorTotal);
    	
    	sheet.createRow(4).createCell(1).setCellValue(("Tempo X Tipo"));
    	int keysN = relatorio.mediaTempoPorTipo.size();
    	Integer[] tempoTipo = new Integer[keysN];
    	System.arraycopy(relatorio.mediaTempoPorTipo.keySet().toArray(), 0, tempoTipo, 0, keysN);
    	
    	int contadorLinha = 5;
    	for (int i = 0; i < tempoTipo.length; i++) {
    		Row linhaLocal = sheet.createRow(contadorLinha++);
    		String nome = "ERRO";
    		if(tempoTipo[i] == 0) nome = "Preventiva";
    		else if(tempoTipo[i] == 1) nome = "Corretiva";
    		else if(tempoTipo[i] == 2) nome = "Preditiva";
    		linhaLocal.createCell(0).setCellValue(nome);
    		linhaLocal.createCell(1).setCellValue(relatorio.mediaTempoPorTipo.get(tempoTipo[i]));
    	}
    	
    	sheet.createRow(++contadorLinha).createCell(1).setCellValue(("Envios X Tipo"));
    	keysN = relatorio.qtdEnvioPorTipo.size();
    	Integer[] envioTipo = new Integer[keysN];
    	System.arraycopy(relatorio.mediaTempoPorTipo.keySet().toArray(), 0, envioTipo, 0, keysN);
    	
    	contadorLinha += 1;
    	for (int i = 0; i < envioTipo.length; i++) {
    		Row linhaLocal = sheet.createRow(contadorLinha++);
    		String nome = "ERRO";
    		if(envioTipo[i] == 0) nome = "Preventiva";
    		else if(envioTipo[i] == 1) nome = "Corretiva";
    		else if(envioTipo[i] == 2) nome = "Preditiva";
    		linhaLocal.createCell(0).setCellValue(nome);
    		linhaLocal.createCell(1).setCellValue(relatorio.mediaTempoPorTipo.get(tempoTipo[i]));
    	}
    }
    
    @PostMapping("/exportRelatorio")
    public ResponseEntity<RetornoArquivo> exportRelatorioAtivos(@RequestBody FiltroRelatorioCombinado filtro){
    	Workbook workbook = new XSSFWorkbook();
    	 
    	Sheet sheetHistoricoTangivel = workbook.createSheet("Histórico Tangível");
        PopularHistoricoTangivel(sheetHistoricoTangivel);
        
        Sheet sheetHistoricoIntangivel = workbook.createSheet("Histórico Intangível");
        PopularHistoricoIntangivel(sheetHistoricoIntangivel);
        
        FiltroRelatorioAtivo filtroAtivo = new FiltroRelatorioAtivo();
        filtroAtivo.dataInicio = filtro.dataInicio;
        filtroAtivo.dataInicio = filtro.dataFim;
        filtroAtivo.tipo = filtro.tipoAtivo;
        PopularRelatorioAtivos(workbook, filtroAtivo);
    	
        FiltroRelatorioManutencao filtroManutencao = new FiltroRelatorioManutencao();
        filtroManutencao.dataInicio = filtro.dataInicio;
        filtroManutencao.dataInicio = filtro.dataFim;
        filtroManutencao.tipo = filtro.tipoManutencao;
        filtroManutencao.idAtivo = filtro.idAtivo;
        PopularRelatorioManutencoes(workbook, filtroManutencao);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
        	workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	RetornoArquivo arquivo = new RetornoArquivo();
    	arquivo.nome = "Relatorio.xlsx";
    	arquivo.tipoDocumento = "document/xlsx";
    	arquivo.documento = outputStream.toByteArray();
    	
    	return new ResponseEntity<RetornoArquivo>(arquivo, HttpStatus.OK);
    }
}
