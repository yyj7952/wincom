package kr.co.wincom.imcs.api.rmFXAllWatchHis;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface RmFXAllWatchHisService {
	
	// 시청목록 삭제 API
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public RmFXAllWatchHisResultVO rmFXAllWatchHis(RmFXAllWatchHisRequestVO paramVO) throws Exception;
	 	 
	// 시청목록 삭제 (DEL_YN 업데이트)
	public Integer rmFXAllWatchHisUpdate(RmFXAllWatchHisRequestVO paramVO) throws Exception;
	
	// 시청목록 삭제 (DEL_YN 업데이트) / DB분리 DAO2 사용
	public Integer rmFXAllWatchHisUpdate2(RmFXAllWatchHisRequestVO paramVO) throws Exception;
	 	 	 
}
