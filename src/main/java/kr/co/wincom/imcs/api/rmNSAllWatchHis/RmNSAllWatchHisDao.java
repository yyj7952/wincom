package kr.co.wincom.imcs.api.rmNSAllWatchHis;

import org.springframework.stereotype.Repository;

import kr.co.wincom.imcs.api.rmNSWatchHis.RmNSWatchHisRequestVO;

@Repository
public interface RmNSAllWatchHisDao {
	// 시청목록 삭제 (DEL_YN 업데이트)
	public Integer rmNSAllWatchHisUpdate(RmNSAllWatchHisRequestVO paramVO);
	
	public int updateSetTime(RmNSAllWatchHisRequestVO paramVO);
		
}
