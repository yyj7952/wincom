package kr.co.wincom.imcs.api.rmNSCHFavor;

import org.springframework.stereotype.Repository;

@Repository
public interface RmNSCHFavorDao {
	
	// 선호채널 순번 Update
	public Integer updateFavIdx(RmNSCHFavorRequestVO paramVO);
	
	// 선호채널 삭제
	public Integer deleteCHFavor(RmNSCHFavorRequestVO paramVO);
		
}
