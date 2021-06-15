package kr.co.wincom.imcs.api.getNSVSI;


import java.util.List;

import kr.co.wincom.imcs.common.vo.ComCdVO;
import kr.co.wincom.imcs.common.vo.ComNodeVO;
import kr.co.wincom.imcs.common.vo.ComProdInfoVO;
import kr.co.wincom.imcs.common.vo.M3u8ProfileVO;

public interface GetNSMakeVSIService {

	// GetNSMakeLists API
	public GetNSVSIResultVO getNSMakeVSIs(GetNSVSIRequestVO requestVO) throws Exception;
	
	// GetNSLists에서도 사용하기 위하여 모듈화 
	public GetNSVSIResultVO getNSMakeVSI(GetNSVSIRequestVO paramVO, String apiInfo) throws Exception;
	
	// 검수 STB 여부 조회
//	public void getTestSbc(GetNSVSIRequestVO paramVO) throws Exception;
	
	// 가입 상품 조회
//	public void getmProdId(GetNSVSIRequestVO paramVO) throws Exception;
	
	// 실시간 서버 조회
	public List<ComCdVO> getLiveTimeServer(GetNSVSIRequestVO paramVO) throws Exception;
	
	// 노드 정보 조회
	public List<ComNodeVO> getNode(GetNSVSIRequestVO paramVO) throws Exception;
	
	// 동 여부 조회
	public String getDongYn(GetNSVSIRequestVO paramVO) throws Exception;
	
	// 장르명 조회
	public String getComName(GetNSVSIRequestVO paramVO) throws Exception;
	
	// 앨범이 속한 상품 조회
	public List<ComProdInfoVO> getProdInfo(GetNSVSIRequestVO paramVO) throws Exception;
	
	// 선호채널 여부 조회
	public String getFavorYn(GetNSVSIRequestVO paramVO) throws Exception;
	
	// 최소 SAVE_TIME 조회
	public String getSaveTime(GetNSVSIRequestVO paramVO) throws Exception;
	
	// m3u8 신규 테이블 조회
	public List<M3u8ProfileVO> Chnl_m3u8_search(GetNSVSIResponseVO resultVO, GetNSVSIRequestVO paramVO) throws Exception;
}
