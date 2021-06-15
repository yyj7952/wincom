package kr.co.wincom.imcs.api.getNSCHRatings;

import java.util.List;

import kr.co.wincom.imcs.common.vo.ComNodeVO;

public interface GetNSCHRatingsService {
	
	// 채널 정보 조회 API
	public GetNSCHRatingsResultVO getNSCHRatings(GetNSCHRatingsRequestVO paramVO);

	// 가입자 정보 조회
	public void testSbc(GetNSCHRatingsRequestVO paramVO) throws Exception;
	
	// 채널 정보 조회
	public List<GetNSCHRatingsResponseVO> getNSChnlList(GetNSCHRatingsRequestVO paramVO) throws Exception;
	
	// 노드 정보 조회
	public List<ComNodeVO> getNode(GetNSCHRatingsRequestVO paramVO) throws Exception;
	
	// 프로그램명 조회
	public String getProName(GetNSCHRatingsRequestVO paramVO) throws Exception;

}
