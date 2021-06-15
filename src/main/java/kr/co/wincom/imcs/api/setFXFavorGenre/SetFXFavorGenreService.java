package kr.co.wincom.imcs.api.setFXFavorGenre;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface SetFXFavorGenreService {

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public SetFXFavorGenreResultVO setFxFavorGenre(SetFXFavorGenreRequestVO paramVO);
	
	// 장르정보 전체 삭제
	public Integer deleteFavorGenre(SetFXFavorGenreRequestVO paramVO) throws Exception;
	
	// 관심장르 등록
	public Integer insertFavorGenre(SetFXFavorGenreRequestVO paramVO) throws Exception;
	
	//DB분리에 따른 tpacall 호출 로직 추가 (2019.03.07)
	public Integer setFxFavorGenre2(SetFXFavorGenreRequestVO paramVO) throws Exception;
    
}
