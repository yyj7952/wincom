package kr.co.wincom2.imcs.api.rmFXWatchHis;

import org.springframework.stereotype.Repository;

import kr.co.wincom.imcs.api.rmFXWatchHis.RmFXWatchHisRequestVO;

@Repository
public interface RmFXWatchHisDao2 {
	
	// 시청목록 삭제 (DEL_YN 업데이트)
	public Integer rmFXWatchHisUpdate(RmFXWatchHisRequestVO paramVO);
	
}
