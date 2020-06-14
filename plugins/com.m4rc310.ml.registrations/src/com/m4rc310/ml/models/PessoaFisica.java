package com.m4rc310.ml.models;

import java.io.Serializable;
import java.util.Date;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PessoaFisica extends MModel implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	private String cpf;
	private String nome;
	private Date nascimento;
	private Integer sexo;
	private Integer estadoCivil;
}

