package kr.co.wincom.imcs.api.rmNSAllCHFavor;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



public interface RmNSAllCHFavorService {
	
	// 선호채널 전체 삭제 API
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public RmNSAllCHFavorResultVO rmNSAllCHFavor(RmNSAllCHFavorRequestVO paramVO);
	 	 	 
}
