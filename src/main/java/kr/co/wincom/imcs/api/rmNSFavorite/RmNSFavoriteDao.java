package kr.co.wincom.imcs.api.rmNSFavorite;

import kr.co.wincom.imcs.api.rmNSFavorite.RmNSFavoriteRequestVO;

import org.springframework.stereotype.Repository;

@Repository
public interface RmNSFavoriteDao
{
	// 찜목록 INDEX 일괄 수정
	public Integer uptFavoriteIndex(RmNSFavoriteRequestVO paramVO);

	// 찜목록 삭제
	public Integer rmNSFavoriteInfo(RmNSFavoriteRequestVO paramVO);
	public Integer rmNSFavoriteInfo2(RmNSFavoriteRequestVO paramVO);
	
}
