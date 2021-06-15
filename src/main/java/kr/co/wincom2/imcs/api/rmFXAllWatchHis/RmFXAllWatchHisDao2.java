package kr.co.wincom2.imcs.api.rmFXAllWatchHis;

import org.springframework.stereotype.Repository;

import kr.co.wincom.imcs.api.rmFXAllWatchHis.RmFXAllWatchHisRequestVO;

@Repository
public interface RmFXAllWatchHisDao2 {
	// 시청목록 삭제 (DEL_YN 업데이트)
	public Integer rmFXAllWatchHisUpdate(RmFXAllWatchHisRequestVO paramVO);
		
}
