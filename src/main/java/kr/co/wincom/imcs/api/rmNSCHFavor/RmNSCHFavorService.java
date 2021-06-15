package kr.co.wincom.imcs.api.rmNSCHFavor;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface RmNSCHFavorService {
	
	// 선호채널 삭제 API
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public RmNSCHFavorResultVO rmNSCHFavor(RmNSCHFavorRequestVO paramVO);
		 	 	 
}
