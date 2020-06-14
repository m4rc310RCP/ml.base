package com.m4rc310.ml.base.models;

import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Processo {
	private Long id;
	private Calendar dataInicio;
	private Calendar dataFim;
	private String device;
	private boolean open;

}
