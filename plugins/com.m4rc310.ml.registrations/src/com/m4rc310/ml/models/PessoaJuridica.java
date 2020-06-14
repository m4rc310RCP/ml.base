package com.m4rc310.ml.models;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PessoaJuridica extends Pessoa{
	private String cnpj;
	private String fantasia;
	private String abertura;
	private String situacao;
	private String naturezaJuridica;
	private String tipo;
	private String logradouro;
	private String numero;
	private String cep;
	private String bairro;
	private String municipio;
	private String uf;
	
	private Vinculo vinculo;
	
//	logradouro numero bairro municipio uf

	private BigDecimal capitalSocial;
	private String scapitalSocial;
	
	private String satividadePrincipal;
	private List<Atividade> atividadePrincipal;
	private List<Atividade> atividadesSecundarias;

	private List<Socio> qsa;
	private Map<String, QualGrupo> mapSocios;
	
}
