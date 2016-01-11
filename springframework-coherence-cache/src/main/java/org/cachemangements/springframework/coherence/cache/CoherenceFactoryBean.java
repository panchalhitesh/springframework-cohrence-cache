/**
 * 
 */
package org.cachemangements.springframework.coherence.cache;

import java.util.Collection;

import org.cachemanagements.springframework.coherence.cachefactory.CohereneConfigurableCacheFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.Resource;

import com.google.common.base.Preconditions;

/**
 * @author Hitech
 *
 */
public class CoherenceFactoryBean implements CacheManager, InitializingBean, DisposableBean {
	
	protected final Logger slf4jLogger;
	
	private Resource cacheConfig;
	
	private CohereneConfigurableCacheFactoryBean cohereneConfigurableCacheFactoryBean;
	
	

	/**
	 * Default Constructor
	 */
	public CoherenceFactoryBean() {
		super();
		this.slf4jLogger = LoggerFactory.getLogger(this.getClass());
	}



	/**
	 * @param cacheConfig Coherence cache configuration location
	 */
	public CoherenceFactoryBean(Resource cacheConfig) {
		super();
		this.slf4jLogger = LoggerFactory.getLogger(this.getClass());
		this.cacheConfig = cacheConfig;
	}
	
	

	/**
	 * @param cacheConfig the cacheConfig to set
	 */
	public void setCacheConfig(Resource cacheConfig) {
		this.cacheConfig = cacheConfig;
	}



	/**
	 * @return Coherence cache configuration location
	 */
	public Resource getCacheConfig() {
		return cacheConfig;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	public void destroy() throws Exception {
		slf4jLogger.info("Destroying Coherence Cache Factory");
		Preconditions.checkNotNull(cohereneConfigurableCacheFactoryBean, "Unable to destroyed inactive configured cache factory");
		cohereneConfigurableCacheFactoryBean.shutDown();
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		//initialize Coherence cache Factory
		if(cacheConfig == null)
			cohereneConfigurableCacheFactoryBean = new CohereneConfigurableCacheFactoryBean();
		else{
			//initialize cache factory for provided cache configuration location
			cohereneConfigurableCacheFactoryBean = 
					new CohereneConfigurableCacheFactoryBean(this.cacheConfig.getURI().toString(),this.getClass().getClassLoader());
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.cache.CacheManager#getCache(java.lang.String)
	 */
	public Cache getCache(String name){
		return new ForwardingNamedCache<Object, Object>(cohereneConfigurableCacheFactoryBean.getNamedCache(name));
	}

	/* (non-Javadoc)
	 * @see org.springframework.cache.CacheManager#getCacheNames()
	 */
	public Collection<String> getCacheNames() {
		return cohereneConfigurableCacheFactoryBean.getCacheNames();
	}

}
