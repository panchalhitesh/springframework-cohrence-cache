/**
 * 
 */
package org.cachemangements.springframework.test.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.cachemangements.springframework.test.service.CacheableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Hitech
 *
 */
@Service
public class CacheableServiceImpl implements CacheableService {
	
	private Map<Integer,String> dataStore;
	
	protected final Logger slf4jLogger;
	
	
	/**
	 * Default contructor
	 */
	public CacheableServiceImpl() {
		super();
		this.slf4jLogger = LoggerFactory.getLogger(this.getClass());
		this.dataStore = new HashMap<Integer, String>(10);
	}

	/* (non-Javadoc)
	 * @see org.gridmangements.util.springframework.service.CacheableService#createData(int, java.lang.String)
	 */
	public void createData(int id, String data) {
		this.slf4jLogger.debug("Data stored to underlying storage {} ",id);
		this.dataStore.put(id,data);
	}

	/* (non-Javadoc)
	 * @see org.gridmangements.util.springframework.service.CacheableService#getData(int)
	 */
	public String getData(int id) {
		this.slf4jLogger.debug("Data read from underlying storage {} ",id);
		return this.dataStore.get(id);
	}

	/* (non-Javadoc)
	 * @see org.gridmangements.util.springframework.service.CacheableService#removeData(int)
	 */
	public void removeData(int id) {
		this.slf4jLogger.debug("Data removed from underlying storage {} ",id);
		this.dataStore.remove(id);
	}

}
