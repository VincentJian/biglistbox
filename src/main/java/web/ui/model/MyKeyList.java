package web.ui.model;

import java.util.AbstractList;
import java.util.HashMap;
import java.util.Map;

public class MyKeyList<T> extends AbstractList<T> {

	final int _size;
	Map<String, T> _updateCache = new HashMap<String,T> ();
	final Fun<?> _fn;
	final String _key;
	
	public MyKeyList(int size, int key, Fun<?> fn) {
		_size = size;
		_key = key + "_" + size;
		_fn = fn;
	}
	
	@SuppressWarnings("unchecked")
	public T get(int index) {
		// if changed, returns the changed value
		Object val = _updateCache.get(String.valueOf(index));
		if (val != null)
			return (T) val;
		T t = null;
		try {
			t = (T) _fn.apply(index);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	public int size() {
		return _size;
	}
	
	public boolean isEmpty() {
		return _size == 0;
	}
	
	public T set(int index, T element) {
		_updateCache.put(String.valueOf(index), element);
		return element;
	}
	
	public int hashCode() {
		return _key.hashCode();
	}
	
	@SuppressWarnings("rawtypes")
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof MyKeyList) {
			return _key.equals(((MyKeyList)(obj))._key);
		}
		return false;
	}
	
	public String toString() {
		return _key;
	}
}
