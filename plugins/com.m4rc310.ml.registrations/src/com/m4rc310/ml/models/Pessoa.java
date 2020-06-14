package com.m4rc310.ml.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Pessoa extends MModel{
	
	public static final int FISICA = 0;
	public static final int JURIDICA = 1;
	public static final int NONE = 2;
	
	public static final int SEXO_MASCULINO = 1;
	public static final int SEXO_FEMININO = 0;

	public static final int ESTADO_CIVIL_SOLTEIRO = 0;
	public static final int ESTADO_CIVIL_CASADO = 1;
	public static final int ESTADO_CIVIL_DIVORCIADO = 2;
	public static final int ESTADO_CIVIL_SEPARADO_JUDICIAL = 3;
	public static final int ESTADO_CIVIL_VIUVO = 4;
	public static final int ESTADO_CIVIL_UNIAO_ESTAVEL = 5;
	
	private Long id;
	private String nome;
	private boolean noContainCpfCnpj;
	
}
