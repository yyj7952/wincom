package kr.co.wincom.imcs.api.rmFXWatchHis;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public interface RmFXWatchHisService {
	
	// 시청목록 삭제(DEL_YN 상태 업데이트)
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public RmFXWatchHisResultVO rmFXWatchHis(RmFXWatchHisRequestVO paramVO);
	
	// 시청목록 삭제(DEL_YN 상태 업데이트)
	public Integer rmFXWatchHisUpdate(RmFXWatchHisRequestVO paramVO) throws Exception;
	
	// 시청목록 삭제(DEL_YN 상태 업데이트) DB분리
	public Integer rmFXWatchHisUpdate2(RmFXWatchHisRequestVO paramVO) throws Exception;
	 
}
