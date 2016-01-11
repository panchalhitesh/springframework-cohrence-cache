/**
 * 
 */
package org.cachemanagements.springframework.coherence.test;

import static com.oracle.tools.deferred.DeferredHelper.invoking;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.cachemangements.springframework.coherence.cache.CoherenceFactoryBean;
import org.cachemangements.springframework.test.service.CacheableService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oracle.tools.deferred.Eventually;
import com.oracle.tools.runtime.LocalPlatform;
import com.oracle.tools.runtime.coherence.CoherenceCacheServerSchema;
import com.oracle.tools.runtime.coherence.CoherenceCluster;
import com.oracle.tools.runtime.coherence.CoherenceClusterBuilder;
import com.oracle.tools.runtime.console.SystemApplicationConsole;
import com.oracle.tools.runtime.network.AvailablePortIterator;
import com.oracle.tools.util.Capture;

/**
 * @author Hitech
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/spring-application-context.xml"})
public class SpringCoherenceFactoryTest {

	private static final String DUMMY_REMOTE_CACHE = "DummyRemoteCache";
	protected static Logger slf4jLogger;
	private static String serverCacheConfig = "coherence-test-cache-config.xml";
    private static String clientCacheConfig = "coherence-test-client-cache-config.xml";
    private static String commonPofConfig = "coherence-test-pof-config.xml";
    private static int CLUSTER_SIZE = 1;
    
    private int LOAD_SIZE = 10;
    // acquire the platform on which we'll create the cluster member
    private static LocalPlatform platform = LocalPlatform.getInstance();
    // acquire a set of available ports on the platform
    private static AvailablePortIterator availablePorts;
    private static CoherenceCacheServerSchema nrStorageMembers, nrProxyMembers;
    private static CoherenceClusterBuilder clusterBuilder;
    private static CoherenceCluster cluster;
    
    @Autowired
    CacheableService cacheableService;
    
    @Autowired
    CoherenceFactoryBean coherenceCacheManager;

    @BeforeClass
    public static void initializeTestEnvironment() throws InterruptedException{
    	slf4jLogger = LoggerFactory.getLogger(SpringCoherenceFactoryTest.class);
        slf4jLogger.info("Starting Cluster Test Environment");
        availablePorts = platform.getAvailablePorts();
        Capture<Integer> clusterPort = new Capture<Integer>(availablePorts);
        Capture<Integer> extendProxyNodePort = new Capture<Integer>(availablePorts);
        String hostName = availablePorts.getInetAddress().getHostName();

        nrStorageMembers = new CoherenceCacheServerSchema()
                .setClusterPort(clusterPort)
                .setCacheConfigURI(serverCacheConfig)
                .setPofConfigURI(commonPofConfig)
                .setStorageEnabled(true)
                .setSiteName("LOCAL")
                .setRoleName("STORAGE-1")
                .setSystemProperty("tangosol.coherence.proxy.enabled", true)
                .useLocalHostMode();

        // configure our CoherenceClusterBuilder
        clusterBuilder = new CoherenceClusterBuilder();

        // instruct the builder the schema to use for some cache servers to build
        clusterBuilder.addSchema("CacheStorageServer", nrStorageMembers, CLUSTER_SIZE, platform);
        //clusterBuilder.addSchema("CacheProxyServer", nrProxyMembers, PROXY_SIZE, platform);
        //System Console
        cluster = clusterBuilder.realize(new SystemApplicationConsole());

        // ensure that the expected cluster is created
        Thread.sleep(5000);
        Eventually.assertThat(invoking(cluster).getClusterSize(), is(CLUSTER_SIZE));
        slf4jLogger.info("Coherence Environment Up and Running...");
    }

    @AfterClass
    public static void deInitializeTestEnvironment(){
        slf4jLogger.info("Coherence Environment Shutting...");
        cluster.close();
    }

    @Test
    public void testSpringAbstractCoherenceCacheFactory(){
        assertNotNull(cacheableService);
        Cache dummyRemoteCache = coherenceCacheManager.getCache(DUMMY_REMOTE_CACHE);
        for (int i=0; i < LOAD_SIZE; i++) {
        	cacheableService.createData(i, "Value "+i);
            assertEquals("Value "+i, cacheableService.getData(i));
            assertEquals("Value "+i, cacheableService.getData(i));
            //validated data at cache
            assertEquals("Value "+i,dummyRemoteCache.get(i,String.class));
		}
        
    }

}
