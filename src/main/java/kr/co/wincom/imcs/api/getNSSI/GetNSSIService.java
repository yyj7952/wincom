package kr.co.wincom.imcs.api.getNSSI;

import java.util.List;

import kr.co.wincom.imcs.common.vo.ComNodeVO;



public interface GetNSSIService {
	
	// 가상채널 EPG전체 채널정보 가져오기 API 
	public GetNSSIResultVO getNSSI(GetNSSIRequestVO paramVO);
	
	// 검수 STB 여부 조회
	public void getTestSbc(GetNSSIRequestVO paramVO) throws Exception;
	
	// 노드 정보 조회
	public List<ComNodeVO> getNode(GetNSSIRequestVO paramVO) throws Exception;
	
	// 동 여부 조회
	public String getDongYn(GetNSSIRequestVO paramVO) throws Exception;

}
