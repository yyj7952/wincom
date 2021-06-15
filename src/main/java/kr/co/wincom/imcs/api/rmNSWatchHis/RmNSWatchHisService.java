package kr.co.wincom.imcs.api.rmNSWatchHis;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wincom.imcs.api.setNSPassedTime.SetNSPassedTimeRequestVO;


public interface RmNSWatchHisService {
	
	// 시청목록 삭제(DEL_YN 상태 업데이트)
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public RmNSWatchHisResultVO rmNSWatchHis(RmNSWatchHisRequestVO paramVO);
	
	// 시청목록 삭제(DEL_YN 상태 업데이트)
	public Integer rmNSWatchHisUpdate(RmNSWatchHisRequestVO paramVO) throws Exception;
	
	// 이어보기 시간 갱신
	public int updateSetTime(RmNSWatchHisRequestVO paramVO) throws Exception;
	 
}
