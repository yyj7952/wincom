package kr.co.wincom.imcs.api.getNSVODRank;

import java.util.List;


public interface GetNSVODRankService {
	
	// 추천VOD 목록조회 (Best VOD) API 
	public GetNSVODRankResultVO getNSVODRank(GetNSVODRankRequestVO paramVO);
	
	// 검수 STB 여부 조회
	public void getTestSbc(GetNSVODRankRequestVO paramVO) throws Exception;
	
	// 추천VOD목록(Best VOD) 조회 
	public List<GetNSVODRankResponseVO> getNSVODRankList(GetNSVODRankRequestVO paramVO) throws Exception;
	
	// 시리즈 카테고리명 조회
	public String getCategoryId(GetNSVODRankRequestVO paramVO) throws Exception;
	
}
