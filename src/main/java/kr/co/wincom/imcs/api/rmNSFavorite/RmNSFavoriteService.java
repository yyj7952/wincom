package kr.co.wincom.imcs.api.rmNSFavorite;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public interface RmNSFavoriteService
{
	// 찜목록 삭제 API
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public RmNSFavoriteResultVO rmNSFavorite(RmNSFavoriteRequestVO requestVO) throws Exception;
	
	// 찜목록 삭제
	public Integer rmNSFavoriteInfo(RmNSFavoriteRequestVO paramVO) throws Exception;
	
	// 찜목록 INDEX 수정
	public Integer uptFavoriteIndex(RmNSFavoriteRequestVO paramVO) throws Exception;
}
