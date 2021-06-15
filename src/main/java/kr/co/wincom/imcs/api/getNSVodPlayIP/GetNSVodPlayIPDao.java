package kr.co.wincom.imcs.api.getNSVodPlayIP;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSVodPlayIPDao {
	
	// 검수 STB여부 조회
	public List<String> getTestSbc(GetNSVodPlayIPRequestVO requestVO);
	
	//컨텐츠 정보 가져오기
	public List<GetNSVodPlayIPResponseVO> getNodeLists(GetNSVodPlayIPRequestVO requestVO);
	
}
