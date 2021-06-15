package kr.co.wincom.imcs.api.getNSHighLight;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.wincom.imcs.common.vo.StillImageVO;

@Repository
public interface GetNSHighLightDao {

	GetNSHighLightAlbumVO getHighlightAlbumInfo (GetNSHitSumResultVO paramVO);
	// 스틸이미지명 조회
	public List<StillImageVO> getStillImage(String album_id);
}
