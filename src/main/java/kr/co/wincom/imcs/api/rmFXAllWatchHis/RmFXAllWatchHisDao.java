package kr.co.wincom.imcs.api.rmFXAllWatchHis;

import org.springframework.stereotype.Repository;

@Repository
public interface RmFXAllWatchHisDao {
	// 시청목록 삭제 (DEL_YN 업데이트)
	public Integer rmFXAllWatchHisUpdate(RmFXAllWatchHisRequestVO paramVO);
		
}
