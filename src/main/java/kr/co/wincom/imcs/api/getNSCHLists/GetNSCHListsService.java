package kr.co.wincom.imcs.api.getNSCHLists;

import java.util.List;

import kr.co.wincom.imcs.common.vo.ComNodeVO;

public interface GetNSCHListsService {
	
	// 메인페이지 채널 정보 조회 API
	public GetNSCHListsResultVO getNSCHLists(GetNSCHListsRequestVO paramVO);
	
	// 노드 정보 조회
	public List<ComNodeVO> getNode(GetNSCHListsRequestVO paramVO) throws Exception;

	// 검수 STB 여부 조회
	public void testSbc(GetNSCHListsRequestVO paramVO) throws Exception;

	// 행정동 코드 조회
	public void setNodeHjd(GetNSCHListsRequestVO paramVO) throws Exception;
	
	// 메인페이지 채널 정보 조회
	public List<GetNSCHListsResponseVO> getNSChnlLists(GetNSCHListsRequestVO paramVO) throws Exception;
	 
}
