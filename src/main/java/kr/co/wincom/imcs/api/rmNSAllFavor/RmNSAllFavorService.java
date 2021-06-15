package kr.co.wincom.imcs.api.rmNSAllFavor;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public interface RmNSAllFavorService
{
	// 찜목록 전체 삭제 API
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public RmNSAllFavorResultVO rmNSAllFavor(RmNSAllFavorRequestVO requestVO) throws Exception;
	
	// 찜목록 전체 삭제
	public Integer rmAllFavor(RmNSAllFavorRequestVO paramVO) throws Exception;
}
