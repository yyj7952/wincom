package kr.co.wincom.imcs.api.getNSPageList;

import java.util.List;

import kr.co.wincom.imcs.common.vo.StillImageVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSPageListDao
{

	// TEST계정 유무조회
	public List<String> testSbc(GetNSPageListRequestVO requestVO);

	// 컨텐츠 리스트조회 > MAIN
	public List<GetNSPageListResponseVO> getContList(GetNSPageListRequestVO requestVO);

	// 컨텐츠 상세정보조회
	public List<GetNSPageListResponseVO> getContDesc(GetNSPageListRequestVO requestVO);
	
	// 썸네일 이미지명 조회
	public List<StillImageVO> getThumbnailImage(GetNSPageListRequestVO requestVO);

}
