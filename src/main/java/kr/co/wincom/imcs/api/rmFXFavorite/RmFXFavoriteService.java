package kr.co.wincom.imcs.api.rmFXFavorite;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public interface RmFXFavoriteService {
	
	// 찜목록 삭제 API
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public RmFXFavoriteResultVO rmFXFavorite(RmFXFavoriteRequestVO paramVO);
	
	// 찜목록 삭제
	public Integer delFavorite(RmFXFavoriteRequestVO paramVO) throws Exception;
}
