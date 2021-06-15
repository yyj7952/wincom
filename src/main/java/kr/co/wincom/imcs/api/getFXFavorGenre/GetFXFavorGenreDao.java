package kr.co.wincom.imcs.api.getFXFavorGenre;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetFXFavorGenreDao {

	// 관심장르 정보 조회
	public List<GetFXFavorGenreResponseVO> getFavorGenre(GetFXFavorGenreRequestVO requestVO);
	
	
}
