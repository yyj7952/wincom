package kr.co.wincom.imcs.api.rmNSAllWatchHis;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wincom.imcs.api.rmNSWatchHis.RmNSWatchHisRequestVO;

public interface RmNSAllWatchHisService {
	
	// 시청목록 삭제 API
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public RmNSAllWatchHisResultVO rmNSAllWatchHis(RmNSAllWatchHisRequestVO paramVO) throws Exception;
	 	 
	// 시청목록 삭제 (DEL_YN 업데이트)
	public Integer rmNSAllWatchHisUpdate(RmNSAllWatchHisRequestVO paramVO) throws Exception;
	
	// 이어보기 시간 갱신
	public int updateSetTime(RmNSAllWatchHisRequestVO paramVO) throws Exception;
	 	 	 
}
