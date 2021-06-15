package kr.co.wincom.imcs.api.getNSSI;

import java.util.List;

import kr.co.wincom.imcs.common.vo.ComNodeVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSSIDao {
	
	// 테스트 가입자 여부 조회
	public List<String> getTestSbc(GetNSSIRequestVO paramVO);
	
	// 노드 조회
	public List<ComNodeVO> getNode(GetNSSIRequestVO paramVO);
	
	// 동 여부 조회
	public List<String> getDongYn(GetNSSIRequestVO paramVO);
	
	// 인기순으로  Nscreen 가상채널 EPG전체 채널정보 조회
	public List<GetNSSIResponseVO> getNSSIListH(GetNSSIRequestVO paramVO);
	
	// 기본으로  Nscreen 가상채널 EPG전체 채널정보 조회
	public List<GetNSSIResponseVO> getNSSIListN(GetNSSIRequestVO paramVO);
	
}
