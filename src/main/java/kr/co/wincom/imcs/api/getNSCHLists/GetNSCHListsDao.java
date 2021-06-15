package kr.co.wincom.imcs.api.getNSCHLists;

import java.util.List;

import kr.co.wincom.imcs.common.vo.ComNodeVO;
import kr.co.wincom.imcs.common.vo.ComTestSbcVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSCHListsDao {
	
	// 노드 정보 조회
	public List<ComNodeVO> getNode(GetNSCHListsRequestVO paramVO);
	
	// 검수 STB 여부 조회
	public List<ComTestSbcVO> testSbc(GetNSCHListsRequestVO paramVO);
	
	// 행정동 코드 조회
	public List<String> getNodeHjd(GetNSCHListsRequestVO paramVO);
	
	// 메인페이지 채널 정보 조회
	public List<GetNSCHListsResponseVO> getNSChnlLists(GetNSCHListsRequestVO paramVO);
		
}
