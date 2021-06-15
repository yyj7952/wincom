package kr.co.wincom.imcs.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.wincom.imcs.common.util.IMCSLog;

@Controller
public class ImcsCacheController
{
	private Log imcsLogger = LogFactory.getLog("apiCommonAp");
	
	@Autowired
	private ImcsCacheService imcsCacheService;
	
	/**
	 * 유료콘서트 파일 캐시를 전부 메모리 캐시에 저장한다
	 * [cacheRefresh][BEFORE][25000][NOW][24999] 형식으로 리턴
	 */
	@RequestMapping(value="/cacheRefresh.do")
	@ResponseBody
	public String cacheRefresh()
	{
		int beforeSize = 0;
		int nowSize = 0;
		
		beforeSize = this.imcsCacheService.getCacheSize(ImcsCacheService.IDOL_LIVE_BUY_CACHE);
		
		this.imcsCacheService.buyCacheRefresh();
		
		nowSize = this.imcsCacheService.getCacheSize(ImcsCacheService.IDOL_LIVE_BUY_CACHE);
		
		return String.format("[cacheRefresh][BEFORE][%s][NOW][%s]\n", beforeSize, nowSize);
	}
	
	/**
	 * 유료콘서트 특정 캐시를 삭제한다.
	 * [removeCache][BEFORE][25000][NOW][24999] 형식으로 리턴
	 * 
	 * @param key
	 */
	@RequestMapping(value="/removeCache.do")
	@ResponseBody
	public String removeCache(@RequestParam("key") String key)
	{
		IMCSLog imcsLog	= new IMCSLog(imcsLogger);
		
		int beforeSize = 0;
		int nowSize = 0;
		
		beforeSize = this.imcsCacheService.getCacheSize(ImcsCacheService.IDOL_LIVE_BUY_CACHE);
		
		// 캐시 삭제
		this.imcsCacheService.removeCache(ImcsCacheService.IDOL_LIVE_BUY_CACHE, key);
		
		nowSize = this.imcsCacheService.getCacheSize(ImcsCacheService.IDOL_LIVE_BUY_CACHE);
		
		imcsLog.serviceLog(String.format("[START][IDOL_LIVE_BUY_CACHE][SIZE][%s]", beforeSize), "", "");
		imcsLog.serviceLog(String.format("[END  ][IDOL_LIVE_BUY_CACHE][SIZE][%s]", nowSize), "", "");
		
		return String.format("[removeCache][BEFORE][%s][NOW][%s]\n", beforeSize, nowSize);
	}
}


























