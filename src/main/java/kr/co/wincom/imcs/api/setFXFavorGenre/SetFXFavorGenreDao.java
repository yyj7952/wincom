package kr.co.wincom.imcs.api.setFXFavorGenre;

import org.springframework.stereotype.Repository;

@Repository
public interface SetFXFavorGenreDao {

	// 관심장르 삭제
	public Integer deleteFavorGenre(SetFXFavorGenreRequestVO paramVO);

	// 관심장르 등록
	public Integer insertFavorGenre(SetFXFavorGenreRequestVO paramVO);
	
	
	
		
}
