package kr.co.wincom.imcs.api.rmFXWatchHis;

import org.springframework.stereotype.Repository;

@Repository
public interface RmFXWatchHisDao {
	
	// 시청목록 삭제 (DEL_YN 업데이트)
	public Integer rmFXWatchHisUpdate(RmFXWatchHisRequestVO paramVO);
	
}
