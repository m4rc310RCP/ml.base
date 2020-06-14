package com.m4rc310.rcp.ui.utils;

import com.m4rc310.rcp.ui.utils.streaming.MEvent;
import com.m4rc310.rcp.ui.utils.streaming.MListener;
import com.m4rc310.rcp.ui.utils.streaming.MStreamLocal;

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
	
}
