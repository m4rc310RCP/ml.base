package com.m4rc310.ml.base.models;

import java.io.Serializable;
import java.util.Calendar;


public class Acesso extends MModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String serial;
	
	private Calendar dataPrimeiroAcesso;
	private Calendar dataFimAcesso;
	
	private boolean avaliacao;
	
	private boolean expirado;
	private boolean bloqueado;
	
	private Usuario usuario;
	
	
	private int diasAcesso;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public Calendar getDataPrimeiroAcesso() {
		return dataPrimeiroAcesso;
	}

	public void setDataPrimeiroAcesso(Calendar dataPrimeiroAcesso) {
		this.dataPrimeiroAcesso = dataPrimeiroAcesso;
	}

	public boolean isAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(boolean avaliacao) {
		this.avaliacao = avaliacao;
	}

	public int getDiasAcesso() {
		return diasAcesso;
	}

	public void setDiasAcesso(int diasAcesso) {
		this.diasAcesso = diasAcesso;
	}

	public boolean isExpirado() {
		return expirado;
	}

	public void setExpirado(boolean expirado) {
		this.expirado = expirado;
	}

	public Calendar getDataFimAcesso() {
		return dataFimAcesso;
	}

	public void setDataFimAcesso(Calendar dataFimAcesso) {
		this.dataFimAcesso = dataFimAcesso;
	}

	public boolean isBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
	
	
	
}
