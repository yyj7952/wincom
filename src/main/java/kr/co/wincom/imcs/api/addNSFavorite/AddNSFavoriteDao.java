package kr.co.wincom.imcs.api.addNSFavorite;

import org.springframework.stereotype.Repository;

@Repository
public interface AddNSFavoriteDao
{
	// 찜목록 중복 체크
	public Integer getFavoriteDupChk(AddNSFavoriteRequestVO paramVO);

	// 찜목록 인덱스 조회
	public Integer getFavoriteIndex(String saId);

	// 찜목록 갯수 조회
	public Integer getFavoriteCount(String saId);

	// 찜목록 등록
	public Integer addNSFavoriteInfo(AddNSFavoriteRequestVO paramVO);
}
