package kr.co.wincom.imcs.common;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * 
 * EhCache 설정
 *
 */
@EnableCaching
@Configuration
public class ImcsConfig
{
//	/**
//	 * 비동기 처리시 사용하는 쓰레드풀
//	 * 
//	 * https://www.youtube.com/watch?v=aSTuQiPB4Ns&list=PLv-xDnFD-nnmof-yoZQN8Fs2kVljIuFyC
//	 * 토비님 동영상 강좌 1:13:00 부터 참조.
//	 */
//	@Bean(name="threadPoolTaskExecutor", destroyMethod = "destroy")
//	public ThreadPoolTaskExecutor threadPoolTaskExecutor()
//	{
//		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//		executor.setCorePoolSize(10);
//		executor.setMaxPoolSize(20);
//		executor.setQueueCapacity(100000);
//		executor.setThreadNamePrefix("imcs-async-");
//		executor.initialize();
//		
//		return executor;
//	}
	
	/**
	 * ehcache 설정
	 */
	@Bean
	public CacheManager cacheManager()
	{
		return new EhCacheCacheManager(getEhCacheFactory().getObject());
	}
	
	/**
	 * ehcache 설정
	 */
	@Bean(destroyMethod = "destroy")
	public EhCacheManagerFactoryBean getEhCacheFactory()
	{
		EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
		factoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
		factoryBean.setShared(true); // 다른 캐시 매니저들과 함께 공유

		return factoryBean;
	}
}
