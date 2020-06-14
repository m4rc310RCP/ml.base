package com.m4rc310.ml.ui.actions;

import com.m4rc310.ml.ui.streaming.MEvent;
import com.m4rc310.ml.ui.streaming.MListener;
import com.m4rc310.ml.ui.streaming.MStreamLocal;

public class MAction {
	
	protected final MStreamLocal stream;
	
	public MAction() {
		this.stream = new MStreamLocal();
	}
	
	public MStreamLocal getStream() {
		return stream;
	}
	
	public void removeListeners(Object target) {
		stream.removeListenerFromTarget(target);
	}
	
	public void addListener(String property, MListener listener) {
        addListener(this, property, listener);
    }
	
	public void addListener(Object target,String property, MListener listener) {
		stream.addListener(target, property, listener);
	}
	
	public void putListener(String property, MListener listener) {
		stream.putListener(this, property, listener);
	}
	
	
	public void fireInCache(String ref, Object ... args) {
		stream.fireListenerInCache(MEvent.event(this, ref, args));
	}
	public void fire(String ref, Object ... args) {
//		Runnable runnable = ()->{
			stream.fireListener(MEvent.event(this, ref, args));
//		};
//		runnable.run();
	}
	
	

	public int getMod11(String num){  

		int soma = 0;
		int resto = 0;
		int dv = 0;
	    String[] numeros = new String[num.length()+1];
	    int multiplicador = 2;
	    
	    for (int i = num.length(); i > 0; i--) {  	    	
	    	if(multiplicador > 9){
	    		multiplicador = 2;
	    		numeros[i] = String.valueOf(Integer.valueOf(num.substring(i-1,i))*multiplicador);
	    		multiplicador++;
	    	}else{
	    		numeros[i] = String.valueOf(Integer.valueOf(num.substring(i-1,i))*multiplicador);
	    		multiplicador++;
	    	}
	    }  

	    for(int i = numeros.length; i > 0 ; i--){
	    	if(numeros[i-1] != null){
	    		soma += Integer.valueOf(numeros[i-1]);
	    	}
	    }
	    resto = soma%11;
    	dv = 11 - resto;
	    
	    return dv;
	}
	
	
	public int getMod10(String num){  

		int soma = 0;
		int resto = 0;
		int dv = 0;
	    String[] numeros = new String[num.length()+1];
	    int multiplicador = 2;
	    String aux;
	    String aux2;
	    String aux3;
	    
	    for (int i = num.length(); i > 0; i--) {  	    	
	    	if(multiplicador%2 == 0){
	    		numeros[i] = String.valueOf(Integer.valueOf(num.substring(i-1,i))*2);
	    		multiplicador = 1;
	    	}else{
	    		numeros[i] = String.valueOf(Integer.valueOf(num.substring(i-1,i))*1);
	    		multiplicador = 2;
	    	}
	    }  
	    
	    for(int i = (numeros.length-1); i > 0; i--){
	    	aux = String.valueOf(Integer.valueOf(numeros[i]));
	    	
	    	if(aux.length()>1){
	    		aux2 = aux.substring(0,aux.length()-1);	    		
	    		aux3 = aux.substring(aux.length()-1,aux.length());
	    		numeros[i] = String.valueOf(Integer.valueOf(aux2) + Integer.valueOf(aux3));	    		
	    	}
	    	else{
	    		numeros[i] = aux;    		
	    	}
	    }
	    
	    for(int i = numeros.length; i > 0 ; i--){
	    	if(numeros[i-1] != null){
	    		soma += Integer.valueOf(numeros[i-1]);
	    	}
	    }
	    resto = soma%10;
    	dv = 10 - resto;
    	
    	dv = dv==10?0:dv;
    	
    	
	    return dv;
	}
	
}
