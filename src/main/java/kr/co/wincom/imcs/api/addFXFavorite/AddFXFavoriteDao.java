package kr.co.wincom.imcs.api.addFXFavorite;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface AddFXFavoriteDao {

	// 찜목록 중복 체크
	List<Integer> getFavorDupChk(AddFXFavoriteRequestVO requestVO);

	// 찜목록 인덱스 조회
	List<Integer> getFavorIdx(AddFXFavoriteRequestVO requestVO);

	// 찜목록 개수 조회
	List<Integer> getFavorCnt(AddFXFavoriteRequestVO requestVO);

	// 찜목록 등록
	int addFavorInfo(AddFXFavoriteRequestVO param);

}
