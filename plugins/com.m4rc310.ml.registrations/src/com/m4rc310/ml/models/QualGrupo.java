package com.m4rc310.ml.models;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class QualGrupo extends MModel{
	private String codigo;
	private String descricao;
	
	private List<Socio> socios;
}
