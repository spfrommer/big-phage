package engine.commons.utils;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class BiMap<K extends Object, V extends Object> {
	private Map<K, V> forward = new Hashtable<K, V>();
	private Map<V, K> backward = new Hashtable<V, K>();

	public synchronized void put(K key, V value) {
		forward.put(key, value);
		backward.put(value, key);
	}

	public synchronized void putAll(BiMap<K, V> map) {
		for (K key : map.getKeys()) {
			V value = map.getForward(key);
			forward.put(key, value);
			backward.put(value, key);
		}
	}

	public synchronized V getForward(K key) {
		return forward.get(key);
	}

	public synchronized K getBackward(V key) {
		return backward.get(key);
	}

	public synchronized Set<K> getKeys() {
		return forward.keySet();
	}

	public synchronized Set<V> getValues() {
		return backward.keySet();
	}

	public synchronized void removeForward(K key) {
		backward.remove(forward.get(key));
		forward.remove(key);
	}

	public synchronized void removeBackward(V key) {
		forward.remove(backward.get(key));
		backward.remove(key);
	}

	public synchronized boolean containsKey(K key) {
		return forward.containsKey(key);
	}

	public synchronized boolean containsValue(V key) {
		return backward.containsKey(key);
	}
}
