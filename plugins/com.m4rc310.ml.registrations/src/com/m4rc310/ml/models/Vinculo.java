package com.m4rc310.ml.models;

import java.util.Date;

//@Getter @Setter
public class Vinculo extends MModel {
	private Long id;
	private Date data;
	private String matricula;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	
	
	
	
}
