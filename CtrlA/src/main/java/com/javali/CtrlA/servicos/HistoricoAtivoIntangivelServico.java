package com.javali.CtrlA.servicos;

import com.javali.CtrlA.entidades.*;
import com.javali.CtrlA.repositorios.*;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

@Service
public class HistoricoAtivoIntangivelServico {

    private static final Logger logger = LoggerFactory.getLogger(HistoricoAtivoIntangivelServico.class);

    @Autowired
    private HistoricoAtivoIntangivelRepositorio historicoRepositorio;

    @Autowired
    private AtivointangivelRepositorio ativoIntangivelRepositorio;

    public HistoricoAtivoIntangivel createHistorico(AtivoIntangivel ativoIntangivel) {
        // Create a new HistoricoAtivoIntangivel and copy the properties from AtivoIntangivel
        HistoricoAtivoIntangivel historico = new HistoricoAtivoIntangivel();
        try {
            BeanUtils.copyProperties(historico, ativoIntangivel);
            BeanUtils.copyProperties(historico, ativoIntangivel.getAtivo());
            BeanUtils.copyProperties(historico, ativoIntangivel.getAtivo().getIdResponsavel());
            BeanUtils.copyProperties(historico, ativoIntangivel.getAtivo().getIdNotaFiscal());

            // Set the data_alteracao field to the current local date
            historico.setDataAlteracao(LocalDate.now());

        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("Error copying properties", e);
            throw new RuntimeException("Error copying properties", e);
        }

        // Save the new HistoricoAtivoIntangivel
        return historicoRepositorio.save(historico);
    }
}