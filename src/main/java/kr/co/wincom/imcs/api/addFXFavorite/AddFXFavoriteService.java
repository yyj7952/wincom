package kr.co.wincom.imcs.api.addFXFavorite;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public interface AddFXFavoriteService
{
	// 찜목록 등록 API
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public AddFXFavoriteResultVO addFXFavorite(AddFXFavoriteRequestVO requestVO) throws Exception;
	
	
	
}
