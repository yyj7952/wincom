package kr.co.wincom.imcs.api.getNSCHRatings;

import java.util.List;

import kr.co.wincom.imcs.common.vo.ComNodeVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSCHRatingsDao {
	
	// 검수 STB 여부 조회
	public List<String> testSbc(GetNSCHRatingsRequestVO paramVO);
	
	// 채널 정보 조회
	public List<GetNSCHRatingsResponseVO> getNSChnlList(GetNSCHRatingsRequestVO paramVO);
	
	// 기지국 정보 조회
	public List<ComNodeVO> getNode(GetNSCHRatingsRequestVO paramVO);
	
	// 프로그램명 조회
	public List<String> getProName(GetNSCHRatingsRequestVO paramVO);
		
}
