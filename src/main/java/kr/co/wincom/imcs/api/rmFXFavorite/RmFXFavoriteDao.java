package kr.co.wincom.imcs.api.rmFXFavorite;

import org.springframework.stereotype.Repository;

@Repository
public interface RmFXFavoriteDao {
	
	// 찜 인덱스 수정 
	public int uptFavorIdx(RmFXFavoriteRequestVO paramVO);

	// 찜 목록 삭제
	public int delFavorite(RmFXFavoriteRequestVO requestVO);
	
}
