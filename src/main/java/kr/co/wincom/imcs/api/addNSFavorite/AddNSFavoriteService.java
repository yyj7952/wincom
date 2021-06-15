package kr.co.wincom.imcs.api.addNSFavorite;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public interface AddNSFavoriteService
{
	// 찜목록 등록 API
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public AddNSFavoriteResultVO addNSFavorite(AddNSFavoriteRequestVO requestVO) throws Exception;
	
	// 찜목록 등록
	public Integer addNSFavoriteInfo(AddNSFavoriteRequestVO paramVO) throws Exception;
}
