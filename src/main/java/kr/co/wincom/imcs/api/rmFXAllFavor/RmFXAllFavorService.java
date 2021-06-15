package kr.co.wincom.imcs.api.rmFXAllFavor;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public interface RmFXAllFavorService {
	
	// 찜목록 전체 삭제 API
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public RmFXAllFavorResultVO rmFXAllFavor(RmFXAllFavorRequestVO paramVO);
	
}
