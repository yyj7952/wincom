package kr.co.wincom2.imcs.api.rmNSFavorite;

import org.springframework.stereotype.Repository;

import kr.co.wincom.imcs.api.rmNSFavorite.RmNSFavoriteRequestVO;

@Repository
public interface RmNSFavoriteDao2 {
	
	// 찜 목록 삭제
	public int rmNSFavoriteInfo(RmNSFavoriteRequestVO requestVO);

}
