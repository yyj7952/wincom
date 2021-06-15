package kr.co.wincom.imcs.api.getCopyCache;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetCopyCacheDao {
	
	public String getCacheChk(GetCopyCacheRequestVO vo);	
	
	public int insertCacheVersion(GetCopyCacheRequestVO vo);	
	
	public int updateCacheVersion(GetCopyCacheRequestVO vo);	
	
}
