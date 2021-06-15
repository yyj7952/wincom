package kr.co.wincom.imcs.api.getNSMakeNodeList;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("rawtypes")
public interface GetNSMakeNodeListDao {
	
	//컨텐츠 정보 가져오기
	public List<GetNSMakeNodeListResponseVO> getNodeLists(GetNSMakeNodeListRequestVO requestVO);
	
	//노드 정보가져오기 1
	public List<GetNSMakeNodeListResponseVO> getNodeLists1(GetNSMakeNodeListRequestVO requestVO);
	
	//노드 정보가져오기 2
	public List<GetNSMakeNodeListResponseVO> getNodeLists2(GetNSMakeNodeListRequestVO requestVO);
}
