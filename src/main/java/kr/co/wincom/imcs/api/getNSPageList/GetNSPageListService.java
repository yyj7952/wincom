package kr.co.wincom.imcs.api.getNSPageList;

import java.util.List;


public interface GetNSPageListService
{
	// 컨텐츠 리스트 조회 API
	public GetNSPageListResultVO getNSPageList(GetNSPageListRequestVO requestVO) throws Exception;

	// 썸네일 이미지명 조회
	public String getThumbnailImage(GetNSPageListRequestVO paramVO) throws Exception;

	// 컨텐츠 상세정보 조회(앨범정보 조회)
	public GetNSPageListResponseVO getAlbumInfo(GetNSPageListRequestVO paramVO) throws Exception;
	
	// 컨텐츠 리스트 조회 
	public List<GetNSPageListResponseVO> getContList(GetNSPageListRequestVO paramVO) throws Exception; 

	// 검수 STB 여부 조회
    public String testSbc(GetNSPageListRequestVO paramVO) throws Exception;

}
