package kr.co.wincom.imcs.api.getNSCHRank;

import java.util.List;

import kr.co.wincom.imcs.common.vo.ComNodeVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSCHRankDao {

	// 노드 정보 조회
	public List<ComNodeVO> getNode(GetNSCHRankRequestVO praramVO);
	
	// 동정보 조회
	public List<String> getDongYn(GetNSCHRankRequestVO praramVO);
	
	// 가입자 정보 조회
	public List<String> testSbc(GetNSCHRankRequestVO praramVO);
	
	// 실시간 인기채널 프로그램 정보 조회
	public List<GetNSCHRankResponseVO> getNSCHRankList(GetNSCHRankRequestVO praramVO);
			 
		
}
