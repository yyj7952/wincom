package kr.co.wincom.imcs.common;

import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * 재귀호출로 파일을 모두 읽어서 캐시 메모리에 저장
 * 
 * SimpleFileVisitor 클래스를 상속받지 않고 그대로 사용하면
 * ...$1.class 같은 익명클래스가 생성됨.
 * 혹시나 운영서버에 소스 반영할 때 실수할 수 있을 것 같아서 ImcsCacheFileVisitor 클래스를 따로 만들었음.
 *
 */
@Component
public class ImcsCacheFileVisitor extends SimpleFileVisitor<Path>
{
	private Log imcsLogger = LogFactory.getLog("apiCommonAp");
	
	@Autowired
	private ImcsCacheService imcsCacheService;
	
	private long cacheCount = 0;
	
	@Override
	public FileVisitResult visitFile(Path filePath, BasicFileAttributes attributes)
	{
		String cacheFileName = filePath.getFileName().toString();
		
		// 압축 파일이 아닌 경우만
		if (!cacheFileName.endsWith(".tar") && !cacheFileName.endsWith(".gz")
				&& !cacheFileName.endsWith(".gzip") && !cacheFileName.endsWith(".zip"))
		{
			// 파일명을 키값으로 해서 캐시 메모리에 저장한다.
			if(cacheFileName.indexOf(".") > -1)
			{				
				try {
					List<String> list = Files.readAllLines(filePath, Charset.forName("UTF-8"));
					String strCont = "";
					
					for(int i = 0; list != null && i < list.size(); i++)
					{
						strCont = String.format("%s%s", strCont, list.get(i));
					}
					
					if(list != null)
						list.clear();
					
					String cacheKey = cacheFileName.substring(0, cacheFileName.lastIndexOf("."));
					
					this.imcsCacheService.putEhcacheValue(ImcsCacheService.IDOL_LIVE_BUY_CACHE, cacheKey, strCont);
					
					int cacheSize = this.imcsCacheService.getCacheSize(ImcsCacheService.IDOL_LIVE_BUY_CACHE);
					this.cacheCount++;
					
					// 로그 남기기
					if(this.cacheCount % 10000 == 0)
					{
						this.imcsLogger.info(
								String.format("[IDOL_LIVE_BUY_CACHE][파일 처리 갯수][%s][CACHE_SIZE][%s]",
										this.cacheCount, cacheSize));
					}
					
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		
		return FileVisitResult.CONTINUE;
	}

	public void setCacheCount(long cacheCount) {
		this.cacheCount = cacheCount;
	}
}



















