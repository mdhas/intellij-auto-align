package aligner.utils;

public class Pair<K, V> {
	private final K key;
	private final V val;

	Pair(K key, V val) {
		this.key = key;
		this.val = val;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return val;
	}
}
