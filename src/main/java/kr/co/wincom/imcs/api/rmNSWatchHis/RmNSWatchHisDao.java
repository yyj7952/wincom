package kr.co.wincom.imcs.api.rmNSWatchHis;

import org.springframework.stereotype.Repository;

@Repository
public interface RmNSWatchHisDao {
	
	// 시청목록 삭제 (DEL_YN 업데이트)
	public Integer rmNSWatchHisUpdate(RmNSWatchHisRequestVO paramVO);
	
	public int updateSetTime(RmNSWatchHisRequestVO paramVO);
	
}
