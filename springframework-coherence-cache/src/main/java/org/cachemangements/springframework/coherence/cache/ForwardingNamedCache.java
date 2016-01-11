/**
 * 
 */
package org.cachemangements.springframework.coherence.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import com.tangosol.net.NamedCache;

/**
 * @author Hitech
 *
 */
public class ForwardingNamedCache<K,V> implements Cache {
	/**
	 * native Coherence Cache
	 */
	@SuppressWarnings("rawtypes")
	private NamedCache delegate;
	
	

	/**
	 * @param delegate
	 */
	@SuppressWarnings("rawtypes")
	public ForwardingNamedCache(NamedCache delegate) {
		super();
		this.delegate = delegate;
	}

	/* (non-Javadoc)
	 * @see org.springframework.cache.Cache#clear()
	 */
	public void clear() {
		delegate.clear();
	}

	/* (non-Javadoc)
	 * @see org.springframework.cache.Cache#evict(java.lang.Object)
	 */
	public void evict(Object key) {
		delegate.remove(key);
	}

	/* (non-Javadoc)
	 * @see org.springframework.cache.Cache#get(java.lang.Object)
	 */
	public ValueWrapper get(Object key) {
		Object value = this.delegate.get(key);
		return (value != null ? new SimpleValueWrapper(value) : null);
	}

	/* (non-Javadoc)
	 * @see org.springframework.cache.Cache#get(java.lang.Object, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(Object key, Class<T> classType) {
		Object value = this.delegate.get(key);
		if (classType != null && !classType.isInstance(value)) {
			throw new IllegalStateException(
					"Cached value is not of required type [" + classType.getName()
							+ "]: " + value);
		}
		return (T) value;
	}

	/* (non-Javadoc)
	 * @see org.springframework.cache.Cache#getName()
	 */
	public String getName() {
		return delegate.getCacheName();
	}

	/* (non-Javadoc)
	 * @see org.springframework.cache.Cache#getNativeCache()
	 */
	public Object getNativeCache() {
		return delegate;
	}

	/* (non-Javadoc)
	 * @see org.springframework.cache.Cache#put(java.lang.Object, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public void put(Object key, Object value) {
		delegate.put(key, value);
	}

	/* (non-Javadoc)
	 * @see org.springframework.cache.Cache#putIfAbsent(java.lang.Object, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public ValueWrapper putIfAbsent(Object key, Object value) {
		Object returnValue = null;
		if(!delegate.containsKey(key))
			returnValue = this.delegate.put(key,value);
		
		return (returnValue != null ? new SimpleValueWrapper(returnValue) : null);
	}


}
