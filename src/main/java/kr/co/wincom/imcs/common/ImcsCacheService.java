package kr.co.wincom.imcs.common;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.common.util.IMCSLog;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@Service
public class ImcsCacheService
{
	private Log imcsLogger = LogFactory.getLog("apiCommonAp");
	
	@Autowired
	private CacheManager ehCacheManager;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ImcsCacheFileVisitor fileVisitor;
	
	public static String IDOL_LIVE_BUY_CACHE = "idolLiveBuyCache";
	public static String LIVE_STAT_DATA_CACHE = "liveStatDataCache";
	
	/**
	 * WAS 실행시 파일 캐시를 읽어서 메모리 캐시에 저장한다.
	 */
	@PostConstruct
	private void init()
	{
		this.buyCacheRefresh();
	}
	
	/**
	 * EhCache 에 값을 put 한다.
	 * imcsCacheService.putEhcacheValue("캐시 이름", "고유키 값", "value")
	 * 
	 * @param cacheName
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean putEhcacheValue(String cacheName, String key, Object value)
	{
		if(StringUtils.isBlank(key))
		{
			return false;
		}
		
		Cache cache = this.ehCacheManager.getCache(cacheName);
		
		cache.put(new Element(key, value));
		
		return true;
	}
	
	/**
	 * Ehcache 에 있는 값 반환
	 * imcsCacheService.getEhcacheValue("캐시 이름", "고유키 값")
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Object getEhcacheValue(String cacheName, String key)
	{
		Cache cache = this.ehCacheManager.getCache(cacheName);
		
		Element element = cache.get(key);
		
		if(element == null)
			return null;
		else
			return element.getObjectValue();
	}
	
	/**
	 * Ehcache 에 값이 있는지 없는지 여부 반환
	 * imcsCacheService.isEhcacheValue("캐시 이름", "고유키 값")
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public boolean isEhcacheValue(String cacheName, String key)
	{
		Cache cache = this.ehCacheManager.getCache(cacheName);
		
		Element element = cache.get(key);
		
		if(element == null)
			return false;
		else
			return true;
	}
	
	/**
	 * 캐시 삭제
	 * 
	 * imcsCacheService.removeCache("캐시 이름", "고유키 값")
	 * 
	 * @param cacheName
	 * @param key
	 */
	public boolean removeCache(String cacheName, String key)
	{
		Cache cache = this.ehCacheManager.getCache(cacheName);
		
		return cache.remove(key);
	}
	
//	/**
//	 * 모든 cache key 반환
//	 * imcsCacheService.getEhcacheKeys("캐시 이름")
//	 * 
//	 * @return
//	 */
//	@SuppressWarnings("rawtypes")
//	public List getEhcacheKeys(String cacheName)
//	{
//		Cache cache = this.ehCacheManager.getCache(cacheName);
//		
//		return cache.getKeys();
//	}
	
	/**
	 * 캐시 사이즈를 반환
	 * @param cacheName
	 * @return
	 */
	public int getCacheSize(String cacheName)
	{
		Cache cache = this.ehCacheManager.getCache(cacheName);
		
		return cache.getSize();
	}
	
	/**
	 * 파일 캐시를 모두 읽어서 캐시 메모리 리프레쉬
	 */
	public void buyCacheRefresh()
	{
		IMCSLog imcsLog	= new IMCSLog(imcsLogger);
		
		int cacheSize = this.getCacheSize(ImcsCacheService.IDOL_LIVE_BUY_CACHE);
		
		imcsLog.serviceLog(String.format("[START][IDOL_LIVE_BUY_CACHE][SIZE][%s]", cacheSize), "", "");
		
		// 아이돌 라이브 캐시 파일 로컬 경로(C:/Imcs/cache/idollive-cache)
		String strLocalRootPath = this.commonService.getCachePath("LOCAL", "idollive-cache", imcsLog);
		File rootPath = new File(strLocalRootPath);
		
		// idollive-cache 폴더가 존재하는지 확인
		if(!rootPath.isDirectory())
		{
			imcsLog.serviceLog(String.format("[%s][%s]", "idollive-cache 폴더가 존재하지 않습니다.", strLocalRootPath), "", "");
			return;
		}
		
		// 파일 처리 갯수를 로그에 남기기 위함
		this.fileVisitor.setCacheCount(0);
		
		File[] arrFiles = rootPath.listFiles();
		Arrays.sort(arrFiles, new ImcsCacheCompare<Object>()); // 오래된 순으로 폴더 정렬
		
		for(File f : arrFiles)
		{
			if(f.isFile()) // 폴더인 경우만 정렬
				continue;
			
			try
			{
				this.buyCachePutRecursion(f);
			}
			catch(NoSuchFileException ex)
			{
				imcsLog.errorLog(ex.getMessage(),
						"이런 경우는 파일을 읽는 도중 상위 폴더가 삭제되거나 했을 때 주로 발생하므로"
						+ " 신경쓰지 않아도 됩니다."
						+ " 캐시 메모리에 저장만 못할 뿐이지 서비스에는 전혀 문제가 없습니다.");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				imcsLog.errorLog(e.getMessage());
			}
		}
		
		cacheSize = this.getCacheSize(ImcsCacheService.IDOL_LIVE_BUY_CACHE);
		imcsLog.serviceLog(String.format("[END  ][IDOL_LIVE_BUY_CACHE][SIZE][%s]", cacheSize), "", "");
		
		arrFiles = null;
	}
	
	/**
	 * 재귀호출로 파일을 모두 읽어서 캐시 메모리에 저장
	 * @param f
	 * @throws Exception
	 */
	private void buyCachePutRecursion(File f) throws Exception
	{		
		Files.walkFileTree(Paths.get(f.getAbsolutePath()), this.fileVisitor);
	}
}














