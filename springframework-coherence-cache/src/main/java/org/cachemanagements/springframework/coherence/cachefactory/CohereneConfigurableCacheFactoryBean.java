/**
 * 
 */
package org.cachemanagements.springframework.coherence.cachefactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.tangosol.net.ConfigurableCacheFactory;
import com.tangosol.net.DefaultConfigurableCacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.run.xml.XmlDocument;
import com.tangosol.run.xml.XmlElement;
import com.tangosol.run.xml.XmlHelper;

/**
 * @author Hitech
 *
 */
/**
 * Loads Coherence configurable cache factory and maintains's its state
 * 
 * @author Hitech
 *
 */
@SuppressWarnings("deprecation")
public class CohereneConfigurableCacheFactoryBean {

	private static final String CACHE_NAME = "cache-name";

	protected final Logger slf4jLogger;

	private Map<String, NamedCache<Object,Object>> loadedCoherenceCaches;

	private XmlDocument cacheConfigXML;

	private ConfigurableCacheFactory configurableCacheFactory;
	
	private List<String> cacheNames;
	
	private ClassLoader clazzLoader;
	
	private String cacheConfigurationLocation;

	/**
	 * Load coherence cache configuration based on system properties, or load primary server node coherence
	 */
	public CohereneConfigurableCacheFactoryBean() {
		super();
		this.slf4jLogger = LoggerFactory.getLogger(this.getClass());
		init();
	}

	/**
	 * Create Configurable Cache Factory
	 * @param cacheConfiguration Cache configuration xml
	 * @param clazzLoader	Class Loader to be used.
	 */
	public CohereneConfigurableCacheFactoryBean(String cacheConfiguration,ClassLoader clazzLoader) {
		super();
		this.slf4jLogger = LoggerFactory.getLogger(this.getClass());
		Preconditions.checkNotNull(cacheConfiguration, "Coherence cache configuratino file must not be null");
		Preconditions.checkNotNull(clazzLoader, "class loader to be used must not be null");
		this.cacheConfigurationLocation = cacheConfiguration;
		init();
	}

	/**
	 * Initialize coherence cache factory
	 */
	private void init() {
		this.loadedCoherenceCaches = new HashMap<String, NamedCache<Object,Object>>(5);
		this.cacheNames = new ArrayList<String>(10);
		createCofigurableCacheFactory();
	}

	/**
	 *creates coherence configurable cache factory
	 */
	private void createCofigurableCacheFactory() {
		long start1 = System.currentTimeMillis();
		//Using deprecated Api, as it is the best way for now to load multiple configuration factory
		if(this.cacheConfigurationLocation != null && !this.cacheConfigurationLocation.isEmpty()){
			cacheConfigXML = XmlHelper.loadFileOrResource(cacheConfigurationLocation, "Client");		
		}else{
			//load default
			cacheConfigXML = (XmlDocument)new DefaultConfigurableCacheFactory().getConfig();
		}
		configurableCacheFactory = new DefaultConfigurableCacheFactory(this.cacheConfigXML,this.clazzLoader);
		configurableCacheFactory.activate();
		//Load all Caches for the configured Cache factory
		lookUpAllCaches();
		slf4jLogger.debug("Total Time take to create cache factory & load caches:{} Caches:{}",(System.currentTimeMillis()-start1),cacheNames);
	}

	/**
	 * Loads all caches declared at cache configuration file.
	 */
	private void loadCacheNames() {
		@SuppressWarnings("unchecked")
		List<XmlElement> elements = cacheConfigXML.findElement("caching-scheme-mapping").getElementList();
		for (XmlElement xmlElement : elements) {
			XmlElement cacheNameElement = xmlElement.findElement(CACHE_NAME);
			if(cacheNameElement.getName().equals(CACHE_NAME))
				cacheNames.add(cacheNameElement.getString());
		}
	}
	
	/**
	 * Loads all caches declared at cache configuration file, and ensures caches services.
	 */
	private void lookUpAllCaches(){
		Preconditions.checkNotNull(configurableCacheFactory, "Unabled to configured cache factory");
		loadCacheNames();
		for (String cacheName : cacheNames) {
			NamedCache<Object,Object> namedCache = configurableCacheFactory.ensureCache(cacheName, this.clazzLoader);
			loadedCoherenceCaches.put(cacheName, namedCache);
		}
	}


	/**
	 * Caches loaded through cache factory
	 * @return List<String>
	 */
	public List<String> getCacheNames(){
		return this.cacheNames;
	}
	/**
	 * Lookups and returns named cache instance.
	 * @param cacheName cache named to lookup from coherence
	 * @return {@linkplain: NamedCache} Coherence Named Cache instance
	 */
	public NamedCache<Object,Object> getNamedCache(String cacheName){
		Preconditions.checkNotNull(cacheName, "cache named must not be null or empty");
		if(!loadedCoherenceCaches.containsKey(cacheName)){
			//load from underlying cachefactory
			NamedCache<Object,Object> namedCache = configurableCacheFactory.ensureCache(cacheName, this.clazzLoader);
			Preconditions.checkNotNull(namedCache, "unable to find cache at cohere");
			loadedCoherenceCaches.put(cacheName, namedCache);
		}
		//return cache
		return loadedCoherenceCaches.get(cacheName);
	}
	
	/**
	 * Shutdown configurable cache factory
	 */
	public void  shutDown(){
		Preconditions.checkNotNull(configurableCacheFactory, "Unabled to configured cache factory");
		configurableCacheFactory.dispose();
	}
}
