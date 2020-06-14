package com.m4rc310.ml.base.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Data {
//	@JsonProperty("solicitarAcesso")
	public Acesso acesso;
	
	@JsonProperty("acesso")
	public Acesso acesso2;
	
	
	@JsonProperty("testeConeccao")
	public String testeConeccao;
}
