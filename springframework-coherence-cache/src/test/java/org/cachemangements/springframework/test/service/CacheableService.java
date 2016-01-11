/**
 * 
 */
package org.cachemangements.springframework.test.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author Hitech
 *
 */
public interface CacheableService {

	@CachePut(value="DummyRemoteCache", key="#id")
	public void createData(int id, String data);
	
	@Cacheable(cacheNames="DummyRemoteCache", key="#id")
	public String getData(int id);
	
	@CacheEvict(cacheNames="DummyRemoteCache")
	public void removeData(int id);
	
	
}
