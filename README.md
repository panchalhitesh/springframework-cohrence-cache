## Spring Framework - Coherence Cache Support 
An extension module for the [Spring's Framework Abstract Cache], integration for Oracle Coherence

## License
This library is released under version 2.0 of the [Apache License][].

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
[Spring Framework]: https://github.com/spring-projects/spring-framework

## Pre-requisite libraries
[Spring Framework Cache {4.2.4.RELEASE}](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/cache.html)

[Oracle Coherence minimum supported version {12.2.x.x}](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/cache.html)    

##Usage
1) Spring cache abstraction bean declaration for Coherence Cache Manager, using system configured coherence cache and pof configuration files.

```
<!-- Coherence Cache Manager -->
	<bean id="cacheManager"
		class="org.cachemangements.springframework.coherence.cache.CoherenceFactoryBean">
	</bean>
	<!-- Spring Cache annotation to use Coherence cache manager -->
	<cache:annotation-driven/>
```
**Note**: This implementation will invoked coherence default cache factory with system configured coherence cache and pof configuration file.([Ref:](https://docs.oracle.com/cd/E15357_01/coh.360/e15723/gs_config.htm#COHDG5014) Configuring Coherence cache configuration using system arguments "tangosol.coherence.cacheconfig" , "tangosol.pof.config")

2) Spring cache abstraction bean delaration for Coherence Cache Manager, using specified coherence cache configuration file.

```
<!-- Coherence Cache Manager -->
	<bean id="cacheManager"
		class="org.cachemangements.springframework.coherence.cache.CoherenceFactoryBean">
		<constructor-arg value="classpath:coherence-test-client-cache-config.xml" />
	</bean>
	<!-- Spring Cache annotation to use Coherence cache manager -->
	<cache:annotation-driven/>
```
**Note:** This implementation confiugres coherence cache factory using specified configuration file from classpath, In-case of using pof follow practise to specified at cache configuration file using ["serializer"](https://docs.oracle.com/cd/E24290_01/coh.371/e22837/appendix_cacheconfig.htm#COHDG5259) xml element.

####Supported Runtime Environments
1) Java Version [1.7.x,+)

2) Oracle Coherence Version[12.2.x,+)

**Note:**
	A small changes to pom for different versions of above runtime libraries can facilitate working of library for Coherence Version[3.6,+) and Java Version[1.5.x,).

