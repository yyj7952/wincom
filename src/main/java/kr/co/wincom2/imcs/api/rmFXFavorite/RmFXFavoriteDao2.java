package kr.co.wincom2.imcs.api.rmFXFavorite;

import org.springframework.stereotype.Repository;

import kr.co.wincom.imcs.api.rmFXFavorite.RmFXFavoriteRequestVO;
import kr.co.wincom.imcs.api.setFXFavorGenre.SetFXFavorGenreRequestVO;

@Repository
public interface RmFXFavoriteDao2 {
	
	// 찜 인덱스 수정 
	public int uptFavorIdx(RmFXFavoriteRequestVO paramVO);

	// 찜 목록 삭제
	public int delFavorite(RmFXFavoriteRequestVO requestVO);

}
