package kr.co.wincom2.imcs.api.setFXFavorGenre;

import org.springframework.stereotype.Repository;

import kr.co.wincom.imcs.api.setFXFavorGenre.SetFXFavorGenreRequestVO;

@Repository
public interface SetFXFavorGenreDao2 {
	
	// DB#2 시간 가져오기
	public String getDB2Time();
	
	// 관심장르 삭제
	public Integer deleteFavorGenre(SetFXFavorGenreRequestVO paramVO);

	// 관심장르 등록
	public Integer insertFavorGenre(SetFXFavorGenreRequestVO paramVO);

}
