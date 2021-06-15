package kr.co.wincom.imcs.api.getNSNodeList;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("rawtypes")
public interface GetNSNodeListDao {
	
	// 검수 STB여부 조회
	public List<String> getTestSbc(GetNSNodeListRequestVO requestVO);
	
	//컨텐츠 정보 가져오기
	public List<GetNSNodeListResponseVO> getNodeLists(GetNSNodeListRequestVO requestVO);
	
}
