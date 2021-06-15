package kr.co.wincom.imcs.api.getFXFavorGenre;

import java.util.List;



public interface GetFXFavorGenreService {
	// 관심장르 리스트 조회 API
	public GetFXFavorGenreResultVO getFXFavorGenre(GetFXFavorGenreRequestVO requestVO) throws Exception;
	
	// 관심장르 정보 조회
	public List<GetFXFavorGenreResponseVO> getFavorGenre(GetFXFavorGenreRequestVO paramVO) throws Exception;
}
