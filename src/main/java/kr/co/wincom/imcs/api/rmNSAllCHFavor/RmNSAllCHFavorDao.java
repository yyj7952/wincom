package kr.co.wincom.imcs.api.rmNSAllCHFavor;

import org.springframework.stereotype.Repository;

@Repository
public interface RmNSAllCHFavorDao {
	
	// 선호채널 전체 삭제
	public Integer deleteNSAllCHFavor(RmNSAllCHFavorRequestVO paramVO);
		
}
