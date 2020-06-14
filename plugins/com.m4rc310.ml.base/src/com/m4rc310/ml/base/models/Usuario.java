package com.m4rc310.ml.base.models;


public class Usuario extends MModel {
	private Long id;
	
	private String email;
	private Acesso acesso;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Acesso getAcesso() {
		return acesso;
	}
	public void setAcesso(Acesso acesso) {
		this.acesso = acesso;
	}
	
}
