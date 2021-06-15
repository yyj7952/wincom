package kr.co.wincom.imcs.api.moveNSFavorIdx;

import org.springframework.stereotype.Repository;

@Repository
public interface MoveNSFavorIdxDao
{
	// 찜목록  사이 인덱스 변경 이전 인덱스가 이후 인덱스 보다 클 때
	public Integer updatePNIndex(MoveNSFavorIdxRequestVO paramVO);

	// 찜목록  사이 인덱스 변경 이전 인덱스가 이후 인덱스 보다 작을 때
	public Integer updateNPIndex(MoveNSFavorIdxRequestVO paramVO);

	// 찜목록 순서 변경
	public Integer updateIndex(MoveNSFavorIdxRequestVO paramVO);

}
