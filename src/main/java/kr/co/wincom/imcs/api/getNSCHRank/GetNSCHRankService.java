package kr.co.wincom.imcs.api.getNSCHRank;

import java.util.List;

import kr.co.wincom.imcs.common.vo.ComNodeVO;

public interface GetNSCHRankService {
	
	// 실시간 인기채널 프로그램 API
	public GetNSCHRankResultVO getNSCHRank(GetNSCHRankRequestVO paramVO);
	 
	// 노드 정보 조회
	public List<ComNodeVO> getNode(GetNSCHRankRequestVO paramVO) throws Exception;
	 
	// 동정보 조회
	public void getDongYn(GetNSCHRankRequestVO paramVO) throws Exception;
	 
	// 가입자 정보 조회
	public void testSbc(GetNSCHRankRequestVO paramVO) throws Exception;
	 
	// 실시간 인기채널 프로그램 정보 조회
	public List<GetNSCHRankResponseVO> getNSCHRankList(GetNSCHRankRequestVO paramVO) throws Exception;
	 
}
