/**
 * 
 */
package org.cachemanagements.springframework.coherence.test.cachefactory.test;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.cachemanagements.springframework.coherence.cachefactory.CohereneConfigurableCacheFactoryBean;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.tangosol.net.NamedCache;

/**
 * @author Hitech
 *
 */
public class CohereneConfigurableCacheFactoryBeanTest {


	private static final String DUMMY_CACHE = "DummyCache";

	@Test
	public void testConfigurableCacheFactoryWithConfig() throws IOException {
		Resource cacheConfigXMLResource  = new ClassPathResource("cache-config.xml");
		CohereneConfigurableCacheFactoryBean cohereneConfigurableCacheFactoryBean 
		= new CohereneConfigurableCacheFactoryBean(cacheConfigXMLResource.getURI().toString(),
				Thread.currentThread().getContextClassLoader());
		assertNotNull(cohereneConfigurableCacheFactoryBean);
		assertNotNull(cohereneConfigurableCacheFactoryBean.getCacheNames());
		cohereneConfigurableCacheFactoryBean.shutDown();
	}
	
	@Test
	public void testConfigurableCacheFactory(){
		CohereneConfigurableCacheFactoryBean cohereneConfigurableCacheFactoryBean 
		= new CohereneConfigurableCacheFactoryBean();
		assertNotNull(cohereneConfigurableCacheFactoryBean);
		assertNotNull(cohereneConfigurableCacheFactoryBean.getCacheNames());
		cohereneConfigurableCacheFactoryBean.shutDown();
	}

}
