package com.m4rc310.rcp.graphql;

import java.util.Date;
import java.util.List;

public class Test extends A{
	private String profissao;
	private Date dataNascimento;
	
	private List<B> list;

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getProfissao() {
		return profissao;
	}

	public void setProfissao(String profissao) {
		this.profissao = profissao;
	}

	public List<B> getList() {
		return list;
	}

	public void setList(List<B> list) {
		this.list = list;
	}
	
	
	
}
