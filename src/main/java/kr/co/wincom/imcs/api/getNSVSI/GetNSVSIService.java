package kr.co.wincom.imcs.api.getNSVSI;

import java.util.List;

import kr.co.wincom.imcs.common.vo.ComCdVO;
import kr.co.wincom.imcs.common.vo.ComNodeVO;
import kr.co.wincom.imcs.common.vo.ComProdInfoVO;



public interface GetNSVSIService {
	
	// 가상채널 EPG전체 채널정보 가져오기 API 
	public GetNSVSIResultVO getNSVSI(GetNSVSIRequestVO paramVO);
	
	// 검수 STB 여부 조회
	public String getTestSbc(GetNSVSIRequestVO paramVO) throws Exception;
	
	// 가입 상품 조회
	public String getmProdId(GetNSVSIRequestVO paramVO) throws Exception;
	
//	// 실시간 서버 조회
//	public List<ComCdVO> getLiveTimeServer(GetNSVSIRequestVO paramVO) throws Exception;
//	
//	// 노드 정보 조회
//	public List<ComNodeVO> getNode(GetNSVSIRequestVO paramVO) throws Exception;
//	
//	// 동 여부 조회
//	public String getDongYn(GetNSVSIRequestVO paramVO) throws Exception;
//	
//	// 장르명 조회
//	public String getComName(GetNSVSIRequestVO paramVO) throws Exception;
//	
//	// 앨범이 속한 상품 조회
//	public List<ComProdInfoVO> getProdInfo(GetNSVSIRequestVO paramVO) throws Exception;
//	
//	// 선호채널 여부 조회
//	public String getFavorYn(GetNSVSIRequestVO paramVO) throws Exception;
//	
//	// 최소 SAVE_TIME 조회
//	public String getSaveTime(GetNSVSIRequestVO paramVO) throws Exception;
}
