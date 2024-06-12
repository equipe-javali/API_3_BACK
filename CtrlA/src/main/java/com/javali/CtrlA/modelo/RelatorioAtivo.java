package com.javali.CtrlA.modelo;

import java.util.Map;

public class RelatorioAtivo {
	public long qtdAtivos;
	public float valorTotal;
	public float statusNaoAlocado;
	public float statusEmUso;
	public float statusEmManutencao;
	public Map<String, Long> qtdPorLocal;
}
