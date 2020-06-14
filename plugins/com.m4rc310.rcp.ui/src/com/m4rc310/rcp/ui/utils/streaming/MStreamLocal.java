/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.rcp.ui.utils.streaming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Marcelo
 */
public class MStreamLocal {

	private final Map<Object, Map<String, Collection<MListener>>> maps;

	private final Map<Object, Object> mapsObjects;

	private final Map<String, Object[]> cache = new HashMap<>();

	public MStreamLocal() {
		this.maps = new HashMap<>();
		this.mapsObjects = new HashMap<>();
	}

	private Collection<MListener> filter(String p) {
		Collection<MListener> ret = new ArrayList<>();
		maps.entrySet().stream().map((entry) -> entry.getValue()).forEachOrdered((value) -> {
			value.entrySet().forEach((entry1) -> {
				String key = entry1.getKey();
				if (key.equals(p)) {
					ret.addAll(entry1.getValue());
				}
			});
		});
		return ret;
	}

	public void fireListenerInCache(MEvent event) {
		filter(event.getPropertyName()).forEach((listener) -> {
			String key = event.getPropertyName();
			Object[] values = event.getValues();
			if (putValues(key, values)) {
				listener.eventChange(event);
			}
		});
	}

	public void fireListener(MEvent event) {
		filter(event.getPropertyName()).forEach((listener) -> {
			listener.eventChange(event);
		});
	}

	public boolean putValues(String ref, Object... values) {
		if (cache.containsKey(ref)) {
			if (Arrays.equals(cache.get(ref), values)) {
				return false;
			}
		}
		cache.put(ref, values);
		return true;
	}

	public void addListener(Object target, String propertyName, MListener listener) {
		if (!maps.containsKey(target)) {
			Map<String, Collection<MListener>> m = new HashMap<>();
			maps.put(target, m);
		}

		Map<String, Collection<MListener>> mp = maps.get(target);
		if (!mp.containsKey(propertyName)) {
			mp.put(propertyName, new ArrayList<>());
		}

		mp.get(propertyName).add(listener);
	}

	public void addListener(String property, MListener listener) {
		addListener(this, property, listener);
	}

	public void removeListener(String property) {
		try {
			Map<String, Collection<MListener>> mp = maps.get(this);
			mp.get(property).clear();
		} catch (Exception e) {
		}
	}

	public boolean putListener(Object target, String property, MListener listener) {

		if (!maps.containsKey(target)) {
			Map<String, Collection<MListener>> ml = new HashMap<>();
			maps.put(target, ml);
		}

		Map<String, Collection<MListener>> mapListeners = maps.get(target);

		if (mapListeners != null) {
			if (!mapListeners.containsKey(property)) {
				mapListeners.put(property, new ArrayList<>());
				mapListeners.get(property).add(listener);
				return true;
			}
		}
		return false;
	}

	public boolean putListener(String property, MListener listener) {
		return putListener(this, property, listener);
	}

	public boolean removeListenerFromTarget(Object target) {
		if (maps.containsKey(target)) {
			maps.remove(target);
			return true;
		}
		return false;
	}

	public void put(Object key, Object object) {
		mapsObjects.put(key, object);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Object key, Class<T> type) {
		return (T) mapsObjects.get(key);
	}

	public int size() {
		return maps.size();
	}
}
