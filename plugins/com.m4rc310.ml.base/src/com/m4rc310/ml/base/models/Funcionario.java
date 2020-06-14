package com.m4rc310.ml.base.models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Funcionario extends MModel {
	private String matricula;
	private String nomeCompleto;
	
	private Cargo cargo;
	
	
	public String getFormatoMatricula() {
		if(matricula==null) {
			return "";
		}
		
		String res = String.format("%07d", Long.parseLong(matricula)); 
		
		Pattern pattern = Pattern.compile("(\\d{0,3})(\\d{3})(\\d{1})");
		
		Matcher matcher = pattern.matcher(res);
		if(matcher.matches()) {
				res = matcher.replaceAll("$1.$2-$3");
		}
		
		res = String.format("%7s", res);
		
		return res;
	}
	
	
	
	
}
