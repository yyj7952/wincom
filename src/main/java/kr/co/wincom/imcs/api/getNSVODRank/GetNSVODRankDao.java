package kr.co.wincom.imcs.api.getNSVODRank;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSVODRankDao {
	
	// 검수 STB 여부 조회
	public List<String> getTestSbc(GetNSVODRankRequestVO paramVO);
	
	public List<GetNSVODRankResponseVO> getNSVODRankList(GetNSVODRankRequestVO paramVO);
	
	public List<String> getCategoryId(GetNSVODRankRequestVO paramVO);	
		
}
