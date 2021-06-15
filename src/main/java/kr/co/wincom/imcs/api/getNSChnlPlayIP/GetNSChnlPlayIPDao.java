package kr.co.wincom.imcs.api.getNSChnlPlayIP;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSChnlPlayIPDao {
	
	// 검수 STB여부 조회
	public List<String> getTestSbc(GetNSChnlPlayIPRequestVO requestVO);
	
	//컨텐츠 정보 가져오기
	public List<GetNSChnlPlayIPResponseVO> getNodeLists(GetNSChnlPlayIPRequestVO requestVO);
	
}
